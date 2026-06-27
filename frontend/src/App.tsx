import { useCallback, useEffect, useState } from 'react'
import {
  clearTokens,
  exchangeCode,
  fetchMe,
  fetchAuthUrl,
  isExpiredTokenError,
  readStoredTokens,
  refreshToken,
  storeTokens,
  ApiError,
} from './api/auth'
import {
  applyForCome,
  fetchApplyFor,
  fetchComeDetail,
  fetchComes,
  fetchFields,
  submitInformation,
} from './api/comes'
import type {
  AuthMode,
  FieldResponse,
  FirstComeResponse,
  InformationRequest,
  MemberResponse,
  TokenResponse,
} from './api/types'
import './App.css'

const callbackTokenRequests = new Map<string, Promise<TokenResponse>>()

type ApplyStatus = 'idle' | 'checking' | 'applied' | 'not_applied' | 'unknown'
type InformationValues = Record<string, string[]>

function readComeIdFromUrl() {
  return new URLSearchParams(window.location.search).get('comeId')
}

function formatDt(iso: string) {
  try {
    return new Date(iso).toLocaleString('ko-KR', {
      dateStyle: 'medium',
      timeStyle: 'short',
    })
  } catch {
    return iso
  }
}

function getRemainingCount(item: FirstComeResponse) {
  return Math.max(item.capacity - item.appliedEventNumber, 0)
}

function canApply(item: FirstComeResponse) {
  return item.status === 'ACTIVE' && getRemainingCount(item) > 0
}

function isNotAppliedError(error: unknown) {
  return (
    error instanceof ApiError &&
    (error.status === 404 ||
      error.message.toLowerCase().includes('not found'))
  )
}

function getStatusLabel(status: string) {
  if (status === 'ACTIVE') {
    return '신청 가능'
  }
  return status
}

function getFillPercent(item: FirstComeResponse) {
  if (item.capacity <= 0) {
    return 0
  }
  return Math.min((item.appliedEventNumber / item.capacity) * 100, 100)
}

function createInitialInformationValues(fields: FieldResponse[]): InformationValues {
  return fields.reduce<InformationValues>((acc, field) => {
    acc[field.name] = []
    return acc
  }, {})
}

function getInformationInputType(field: FieldResponse) {
  if (field.fieldType === 'MOBILE') {
    return 'tel'
  }
  return 'text'
}

function toInformationPayload(fields: FieldResponse[], values: InformationValues): InformationRequest[] {
  return fields.map((field) => ({
    name: field.name,
    values: values[field.name]?.map((value) => value.trim()).filter(Boolean) ?? [],
  }))
}

function getRequiredMissingField(fields: FieldResponse[], values: InformationValues) {
  return fields.find((field) => {
    if (!field.required) {
      return false
    }
    return (values[field.name] ?? []).every((value) => value.trim() === '')
  })
}

function ComeCard({
  item,
  onOpen,
}: {
  item: FirstComeResponse
  onOpen: (id: string) => void
}) {
  const remainingCount = getRemainingCount(item)

  return (
    <article
      className="come-card"
      role="button"
      tabIndex={0}
      onClick={() => onOpen(item.id)}
      onKeyDown={(e) => {
        if (e.key === 'Enter' || e.key === ' ') {
          e.preventDefault()
          onOpen(item.id)
        }
      }}
    >
      <div className="come-card__main">
        <span className={`come-status come-status--${item.status.toLowerCase()}`}>
          {getStatusLabel(item.status)}
        </span>
        <div className="come-card__body">
          <h2 className="come-card__title">{item.name}</h2>
          <p className="come-card__sub">
            {item.organizer} · 시작 {formatDt(item.time.startAt)}
          </p>
          <div className="come-meter" aria-hidden="true">
            <span style={{ width: `${getFillPercent(item)}%` }} />
          </div>
        </div>
      </div>
      <dl className="come-card__stats">
        <div>
          <dt>잔여</dt>
          <dd>{remainingCount}명</dd>
        </div>
        <div>
          <dt>신청</dt>
          <dd>
            {item.appliedEventNumber}/{item.capacity}
          </dd>
        </div>
      </dl>
    </article>
  )
}

function ComeDetail({
  item,
  isLoggedIn,
  applyStatus,
  applyStatusMessage,
  applyLoading,
  applyMessage,
  fields,
  informationValues,
  informationLoading,
  informationSubmitting,
  informationMessage,
  onInformationChange,
  onSubmitInformation,
  onApply,
  onBack,
  onRefresh,
}: {
  item: FirstComeResponse
  isLoggedIn: boolean
  applyStatus: ApplyStatus
  applyStatusMessage: string | null
  applyLoading: boolean
  applyMessage: string | null
  fields: FieldResponse[] | null
  informationValues: InformationValues
  informationLoading: boolean
  informationSubmitting: boolean
  informationMessage: string | null
  onInformationChange: (name: string, values: string[]) => void
  onSubmitInformation: () => void
  onApply: () => void
  onBack: () => void
  onRefresh: () => void
}) {
  const remainingCount = getRemainingCount(item)
  const isApplicable = canApply(item)
  const alreadyApplied = applyStatus === 'applied'
  const canShowApplyButton =
    isApplicable &&
    !alreadyApplied &&
    (!isLoggedIn || applyStatus === 'not_applied' || applyStatus === 'checking')
  const applyUnavailableMessage =
    isLoggedIn && applyStatus === 'idle'
      ? '신청 여부를 확인하는 중입니다.'
      : isLoggedIn && applyStatus === 'unknown'
      ? '신청 가능 여부를 확인하지 못했습니다.'
      : '현재 신청할 수 없는 선착순입니다.'
  const applyStatusLabel =
    alreadyApplied
      ? '신청 완료'
      : applyStatus === 'checking'
        ? '확인 중'
        : isApplicable
          ? '신청 가능'
          : remainingCount <= 0
            ? '마감'
            : '신청 불가'

  return (
    <article className="come-detail">
      <div className="come-detail__nav">
        <button type="button" className="home__button" onClick={onBack}>
          목록
        </button>
        <button type="button" className="home__button" onClick={onRefresh}>
          새로고침
        </button>
      </div>

      <header className="come-detail__head">
        <div>
          <span className={`come-status come-status--${item.status.toLowerCase()}`}>
            {getStatusLabel(item.status)}
          </span>
          <h2 className="come-detail__title">{item.name}</h2>
          <p className="come-detail__sub">
            {item.organizer} · {item.id}
          </p>
        </div>
        <div className="come-detail__capacity" aria-label="신청 현황">
          <strong>{remainingCount}</strong>
          <span>잔여 / {item.capacity}명</span>
        </div>
      </header>

      <div className="come-detail__progress" aria-hidden="true">
        <span style={{ width: `${getFillPercent(item)}%` }} />
      </div>

      <section className="come-detail__state">
        <div>
          <span>내 신청 상태</span>
          <strong>{isLoggedIn ? applyStatusLabel : '로그인 필요'}</strong>
        </div>
        {canShowApplyButton ? (
          <button
            type="button"
            className="home__button home__button--primary come-detail__apply"
            onClick={onApply}
            disabled={applyLoading || applyStatus === 'checking'}
          >
            {applyLoading
              ? '신청 확인 중...'
              : isLoggedIn && applyStatus === 'checking'
                ? '신청 여부 확인 중...'
                : isLoggedIn
                  ? '신청하기'
                  : '로그인 후 신청'}
          </button>
        ) : alreadyApplied ? (
          <p className="come-detail__notice come-detail__notice--success">신청 완료</p>
        ) : (
          <p className="come-detail__notice">{applyUnavailableMessage}</p>
        )}
      </section>

      {alreadyApplied && (
        <section className="come-detail__information">
          <div className="come-detail__section-head">
            <h3>정보 입력</h3>
            <span>신청 완료자 전용</span>
          </div>
          {informationLoading ? (
            <p className="come-detail__notice">입력 항목을 불러오는 중입니다.</p>
          ) : fields && fields.length > 0 ? (
            <form
              className="come-form"
              onSubmit={(e) => {
                e.preventDefault()
                onSubmitInformation()
              }}
            >
              {fields.map((field) => {
                const currentValues = informationValues[field.name] ?? []

                if (field.fieldType === 'SINGLE_DOMAIN') {
                  return (
                    <label className="come-form__field" key={field.name}>
                      <span>
                        {field.name}
                        {field.required && <strong>필수</strong>}
                      </span>
                      <select
                        value={currentValues[0] ?? ''}
                        required={field.required}
                        onChange={(e) => onInformationChange(field.name, [e.target.value])}
                      >
                        <option value="">선택</option>
                        {(field.domain ?? []).map((value) => (
                          <option key={value} value={value}>
                            {value}
                          </option>
                        ))}
                      </select>
                    </label>
                  )
                }

                if (field.fieldType === 'MULTIPLE_DOMAIN') {
                  return (
                    <fieldset className="come-form__field come-form__field--options" key={field.name}>
                      <legend>
                        {field.name}
                        {field.required && <strong>필수</strong>}
                      </legend>
                      <div>
                        {(field.domain ?? []).map((value) => (
                          <label key={value}>
                            <input
                              type="checkbox"
                              checked={currentValues.includes(value)}
                              onChange={(e) => {
                                const nextValues = e.target.checked
                                  ? [...currentValues, value]
                                  : currentValues.filter((item) => item !== value)
                                onInformationChange(field.name, nextValues)
                              }}
                            />
                            {value}
                          </label>
                        ))}
                      </div>
                    </fieldset>
                  )
                }

                return (
                  <label className="come-form__field" key={field.name}>
                    <span>
                      {field.name}
                      {field.required && <strong>필수</strong>}
                    </span>
                    <input
                      type={getInformationInputType(field)}
                      value={currentValues[0] ?? ''}
                      required={field.required}
                      placeholder={field.dataSource?.name ?? ''}
                      onChange={(e) => onInformationChange(field.name, [e.target.value])}
                    />
                  </label>
                )
              })}
              <div className="come-form__actions">
                <button
                  type="submit"
                  className="home__button home__button--primary"
                  disabled={informationSubmitting}
                >
                  {informationSubmitting ? '저장 중...' : '정보 입력 완료'}
                </button>
                {informationMessage && (
                  <p className="come-detail__notice" role="status">
                    {informationMessage}
                  </p>
                )}
              </div>
            </form>
          ) : (
            <p className="come-detail__notice">입력할 추가 정보가 없습니다.</p>
          )}
        </section>
      )}

      <dl className="come-detail__grid">
        <div>
          <dt>신청 인원</dt>
          <dd>
            {item.appliedEventNumber}/{item.capacity}명
          </dd>
        </div>
        <div>
          <dt>참여 방식</dt>
          <dd>{item.join.method}</dd>
        </div>
        <div>
          <dt>대기 정책</dt>
          <dd>
            {item.waitPolicy.waitType}
            {item.waitPolicy.capacity != null && item.waitPolicy.capacity !== 0
              ? ` · 상한 ${item.waitPolicy.capacity}`
              : ''}
          </dd>
        </div>
        <div>
          <dt>중복 신청</dt>
          <dd>{item.eligibility.duplicable ? '허용' : '불가'}</dd>
        </div>
        <div>
          <dt>자격</dt>
          <dd>{item.eligibility.value || '—'}</dd>
        </div>
      </dl>

      <section className="come-detail__schedule">
        <h3>일정</h3>
        <ol>
          <li>
            <span>노출</span>
            <time dateTime={item.time.displayAt}>{formatDt(item.time.displayAt)}</time>
          </li>
          <li>
            <span>시작</span>
            <time dateTime={item.time.startAt}>{formatDt(item.time.startAt)}</time>
          </li>
          <li>
            <span>종료</span>
            <time dateTime={item.time.endAt}>{formatDt(item.time.endAt)}</time>
          </li>
        </ol>
      </section>

      <footer className="come-detail__actions">
        {applyStatusMessage && <p className="come-detail__notice">{applyStatusMessage}</p>}
        {applyMessage && (
          <p className="come-detail__notice" role="status">
            {applyMessage}
          </p>
        )}
      </footer>
    </article>
  )
}

export default function App() {
  const [items, setItems] = useState<FirstComeResponse[] | null>(null)
  const [selectedComeId, setSelectedComeId] = useState<string | null>(() => readComeIdFromUrl())
  const [selectedCome, setSelectedCome] = useState<FirstComeResponse | null>(null)
  const [detailLoading, setDetailLoading] = useState(false)
  const [detailError, setDetailError] = useState<string | null>(null)
  const [applyStatus, setApplyStatus] = useState<ApplyStatus>('idle')
  const [applyStatusMessage, setApplyStatusMessage] = useState<string | null>(null)
  const [applyLoading, setApplyLoading] = useState(false)
  const [applyMessage, setApplyMessage] = useState<string | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [authLoading, setAuthLoading] = useState<AuthMode | 'callback' | null>(null)
  const [authError, setAuthError] = useState<string | null>(null)
  const [tokens, setTokens] = useState<TokenResponse | null>(() => readStoredTokens())
  const [member, setMember] = useState<MemberResponse | null>(null)
  const [fields, setFields] = useState<FieldResponse[] | null>(null)
  const [informationValues, setInformationValues] = useState<InformationValues>({})
  const [informationLoading, setInformationLoading] = useState(false)
  const [informationSubmitting, setInformationSubmitting] = useState(false)
  const [informationMessage, setInformationMessage] = useState<string | null>(null)

  const load = useCallback(async () => {
    setLoading(true)
    setError(null)
    try {
      setItems(await fetchComes())
    } catch (e) {
      setError(e instanceof Error ? e.message : '불러오기에 실패했습니다.')
      setItems(null)
    } finally {
      setLoading(false)
    }
  }, [])

  useEffect(() => {
    void load()
  }, [load])

  useEffect(() => {
    function syncRoute() {
      setSelectedComeId(readComeIdFromUrl())
    }

    window.addEventListener('popstate', syncRoute)
    return () => {
      window.removeEventListener('popstate', syncRoute)
    }
  }, [])

  const openDetail = useCallback((comeId: string) => {
    const params = new URLSearchParams(window.location.search)
    params.set('comeId', comeId)
    window.history.pushState({}, '', `${window.location.pathname}?${params.toString()}`)
    setApplyStatus('idle')
    setApplyStatusMessage(null)
    setApplyMessage(null)
    setFields(null)
    setInformationValues({})
    setInformationMessage(null)
    setSelectedComeId(comeId)
  }, [])

  const closeDetail = useCallback(() => {
    window.history.pushState({}, '', window.location.pathname)
    setSelectedComeId(null)
    setSelectedCome(null)
    setDetailError(null)
    setApplyStatus('idle')
    setApplyStatusMessage(null)
    setApplyMessage(null)
    setFields(null)
    setInformationValues({})
    setInformationMessage(null)
  }, [])

  const loadDetail = useCallback(async (comeId: string) => {
    setDetailLoading(true)
    setDetailError(null)
    setApplyMessage(null)
    try {
      setSelectedCome(await fetchComeDetail(comeId))
    } catch (e) {
      setDetailError(e instanceof Error ? e.message : '상세 정보를 불러오지 못했습니다.')
      setSelectedCome(null)
    } finally {
      setDetailLoading(false)
    }
  }, [])

  useEffect(() => {
    if (!selectedComeId) {
      return
    }

    setApplyStatus(tokens ? 'checking' : 'idle')
    setApplyStatusMessage(null)
    void loadDetail(selectedComeId)
  }, [loadDetail, selectedComeId, tokens])

  const startAuth = useCallback(async (mode: AuthMode) => {
    setAuthLoading(mode)
    setAuthError(null)

    try {
      window.location.href = await fetchAuthUrl(mode)
    } catch (e) {
      setAuthError(e instanceof Error ? e.message : '인증 페이지를 열 수 없습니다.')
      setAuthLoading(null)
    }
  }, [])

  const logout = useCallback(() => {
    clearTokens()
    setTokens(null)
    setMember(null)
    setAuthError(null)
    setApplyStatus('idle')
    setApplyStatusMessage(null)
    setFields(null)
    setInformationValues({})
    setInformationMessage(null)
  }, [])

  const checkApplyStatus = useCallback(
    async (comeId: string, currentTokens: TokenResponse): Promise<boolean> => {
      setApplyStatus('checking')
      setApplyStatusMessage(null)

      async function check(accessToken: string) {
        await fetchApplyFor(comeId, accessToken)
      }

      try {
        await check(currentTokens.accessToken)
        setApplyStatus('applied')
        return true
      } catch (e) {
        if (isNotAppliedError(e)) {
          setApplyStatus('not_applied')
          return false
        }

        if (isExpiredTokenError(e)) {
          try {
            const refreshedTokens = await refreshToken(currentTokens.refreshToken)
            storeTokens(refreshedTokens)
            setTokens(refreshedTokens)
            await check(refreshedTokens.accessToken)
            setApplyStatus('applied')
            return true
          } catch (refreshError) {
            if (isNotAppliedError(refreshError)) {
              setApplyStatus('not_applied')
              return false
            }

            clearTokens()
            setTokens(null)
            setMember(null)
            setApplyStatus('unknown')
            setApplyStatusMessage('로그인 상태를 확인하지 못했습니다.')
            return false
          }
        }

        setApplyStatus('unknown')
        setApplyStatusMessage('신청 완료 여부를 확인하지 못했습니다.')
        return false
      }
    },
    [],
  )

  useEffect(() => {
    if (!selectedComeId) {
      return
    }

    if (!tokens) {
      setApplyStatus('idle')
      setApplyStatusMessage(null)
      setFields(null)
      setInformationValues({})
      setInformationMessage(null)
      return
    }

    void checkApplyStatus(selectedComeId, tokens)
  }, [checkApplyStatus, selectedComeId, tokens])

  const loadInformationFields = useCallback(
    async (comeId: string, currentTokens: TokenResponse) => {
      setInformationLoading(true)
      setInformationMessage(null)

      try {
        const nextFields = await fetchFields(comeId, currentTokens.accessToken)
        setFields(nextFields)
        setInformationValues(createInitialInformationValues(nextFields))
      } catch (e) {
        if (isExpiredTokenError(e)) {
          try {
            const refreshedTokens = await refreshToken(currentTokens.refreshToken)
            storeTokens(refreshedTokens)
            setTokens(refreshedTokens)
            const nextFields = await fetchFields(comeId, refreshedTokens.accessToken)
            setFields(nextFields)
            setInformationValues(createInitialInformationValues(nextFields))
            return
          } catch (refreshError) {
            clearTokens()
            setTokens(null)
            setMember(null)
            setFields(null)
            setInformationValues({})
            setInformationMessage(
              refreshError instanceof Error
                ? refreshError.message
                : '입력 항목을 불러오지 못했습니다.',
            )
            return
          }
        }

        setFields(null)
        setInformationValues({})
        setInformationMessage(e instanceof Error ? e.message : '입력 항목을 불러오지 못했습니다.')
      } finally {
        setInformationLoading(false)
      }
    },
    [],
  )

  useEffect(() => {
    if (!selectedComeId || !tokens || applyStatus !== 'applied') {
      if (applyStatus !== 'applied') {
        setFields(null)
        setInformationValues({})
      }
      return
    }

    void loadInformationFields(selectedComeId, tokens)
  }, [applyStatus, loadInformationFields, selectedComeId, tokens])

  const updateInformationValue = useCallback((name: string, values: string[]) => {
    setInformationValues((current) => ({
      ...current,
      [name]: values,
    }))
  }, [])

  const submitSelectedInformation = useCallback(async () => {
    if (!selectedCome || !tokens || !fields) {
      return
    }

    const missingField = getRequiredMissingField(fields, informationValues)
    if (missingField) {
      setInformationMessage(`${missingField.name} 항목을 입력해주세요.`)
      return
    }

    setInformationSubmitting(true)
    setInformationMessage(null)

    let currentTokens = tokens
    const payload = {
      informations: toInformationPayload(fields, informationValues),
    }

    try {
      await submitInformation(selectedCome.id, currentTokens.accessToken, payload)
      setInformationMessage('정보 입력이 완료되었습니다.')
    } catch (e) {
      if (isExpiredTokenError(e)) {
        try {
          const refreshedTokens = await refreshToken(currentTokens.refreshToken)
          storeTokens(refreshedTokens)
          setTokens(refreshedTokens)
          currentTokens = refreshedTokens
          await submitInformation(selectedCome.id, currentTokens.accessToken, payload)
          setInformationMessage('정보 입력이 완료되었습니다.')
          return
        } catch (refreshError) {
          clearTokens()
          setTokens(null)
          setMember(null)
          setInformationMessage(
            refreshError instanceof Error ? refreshError.message : '정보 입력에 실패했습니다.',
          )
          return
        }
      }

      setInformationMessage(e instanceof Error ? e.message : '정보 입력에 실패했습니다.')
    } finally {
      setInformationSubmitting(false)
    }
  }, [fields, informationValues, selectedCome, tokens])

  const applySelectedCome = useCallback(async () => {
    if (!selectedCome) {
      return
    }

    if (!tokens) {
      await startAuth('login')
      return
    }

    const comeId = selectedCome.id
    setApplyLoading(true)
    setApplyMessage(null)
    setDetailError(null)
    setApplyStatusMessage(null)

    let currentTokens = tokens
    let requestError: unknown = null

    try {
      await applyForCome(comeId, currentTokens.accessToken)
    } catch (e) {
      if (isExpiredTokenError(e)) {
        try {
          const refreshedTokens = await refreshToken(currentTokens.refreshToken)
          storeTokens(refreshedTokens)
          setTokens(refreshedTokens)
          currentTokens = refreshedTokens
          await applyForCome(comeId, refreshedTokens.accessToken)
        } catch (refreshError) {
          requestError = refreshError
        }
      } else {
        requestError = e
      }
    }

    try {
      const applied = await checkApplyStatus(comeId, currentTokens)
      await loadDetail(comeId)
      await load()

      if (applied) {
        setApplyMessage('신청 완료가 확인되었습니다.')
      } else if (requestError) {
        setApplyMessage('신청 요청 후 완료 여부를 확인하지 못했습니다.')
      }
    } finally {
      setApplyLoading(false)
    }
  }, [checkApplyStatus, load, loadDetail, selectedCome, startAuth, tokens])

  const loadMe = useCallback(async (currentTokens: TokenResponse) => {
    try {
      setMember(await fetchMe(currentTokens.accessToken))
    } catch (e) {
      if (isExpiredTokenError(e)) {
        try {
          const refreshedTokens = await refreshToken(currentTokens.refreshToken)
          storeTokens(refreshedTokens)
          setTokens(refreshedTokens)
          setMember(await fetchMe(refreshedTokens.accessToken))
          setAuthError(null)
          return
        } catch (refreshError) {
          clearTokens()
          setTokens(null)
          setAuthError(
            refreshError instanceof Error ? refreshError.message : '토큰 갱신에 실패했습니다.',
          )
          setMember(null)
          return
        }
      }

      setAuthError(e instanceof Error ? e.message : '회원 정보를 불러오지 못했습니다.')
      setMember(null)
    }
  }, [])

  useEffect(() => {
    if (!tokens) {
      setMember(null)
      return
    }

    void loadMe(tokens)
  }, [loadMe, tokens])

  useEffect(() => {
    const params = new URLSearchParams(window.location.search)
    const code = params.get('code')
    const clientId = params.get('clientId')

    if (!code || !clientId) {
      return
    }

    let ignore = false

    async function completeAuth() {
      setAuthLoading('callback')
      setAuthError(null)

      try {
        const requestKey = `${clientId}:${code}`
        let tokenRequest = callbackTokenRequests.get(requestKey)
        if (!tokenRequest) {
          tokenRequest = exchangeCode(code as string, clientId as string)
          callbackTokenRequests.set(requestKey, tokenRequest)
        }

        const nextTokens = await tokenRequest
        if (ignore) {
          return
        }
        storeTokens(nextTokens)
        setTokens(nextTokens)
        await loadMe(nextTokens)
        window.history.replaceState({}, document.title, window.location.pathname)
      } catch (e) {
        if (!ignore) {
          setAuthError(e instanceof Error ? e.message : '로그인 처리에 실패했습니다.')
        }
      } finally {
        if (!ignore) {
          setAuthLoading(null)
        }
      }
    }

    void completeAuth()

    return () => {
      ignore = true
    }
  }, [loadMe])

  return (
    <div className="home">
      <header className="home__header">
        <div>
          <h1 className="home__title">선착순</h1>
          <p className="home__lead">
            {items ? `신청 가능한 이벤트 ${items.length}개` : '이벤트를 불러오는 중입니다'}
          </p>
        </div>
        <div className="home__actions">
          {tokens ? (
            <>
              <span className="home__auth-state">
                {member ? `${member.nickName} · ` : '로그인됨 · '}
                {tokens.expiresIn}초
              </span>
              <button type="button" className="home__button" onClick={logout}>
                로그아웃
              </button>
            </>
          ) : (
            <>
              <button
                type="button"
                className="home__button home__button--primary"
                onClick={() => void startAuth('login')}
                disabled={authLoading !== null}
              >
                {authLoading === 'login' ? '이동 중…' : '로그인'}
              </button>
              <button
                type="button"
                className="home__button"
                onClick={() => void startAuth('signup')}
                disabled={authLoading !== null}
              >
                {authLoading === 'signup' ? '이동 중…' : '회원가입'}
              </button>
            </>
          )}
          <button type="button" className="home__button" onClick={() => void load()} disabled={loading}>
            {loading ? '불러오는 중…' : '새로고침'}
          </button>
        </div>
      </header>

      {authLoading === 'callback' && (
        <div className="home__banner" role="status">
          로그인 결과를 처리하는 중입니다.
        </div>
      )}

      {authError && (
        <div className="home__banner home__banner--error" role="alert">
          {authError}
        </div>
      )}

      {error && (
        <div className="home__banner home__banner--error" role="alert">
          {error}
        </div>
      )}

      <main className="home__main">
        {selectedComeId ? (
          <>
            {detailLoading && !selectedCome && !detailError && (
              <p className="home__placeholder">상세 정보를 불러오는 중입니다…</p>
            )}
            {detailError && (
              <div className="home__banner home__banner--error" role="alert">
                {detailError}
              </div>
            )}
            {selectedCome && (
              <ComeDetail
                item={selectedCome}
                isLoggedIn={tokens !== null}
                applyStatus={applyStatus}
                applyStatusMessage={applyStatusMessage}
                applyLoading={applyLoading}
                applyMessage={applyMessage}
                fields={fields}
                informationValues={informationValues}
                informationLoading={informationLoading}
                informationSubmitting={informationSubmitting}
                informationMessage={informationMessage}
                onInformationChange={updateInformationValue}
                onSubmitInformation={submitSelectedInformation}
                onApply={() => void applySelectedCome()}
                onBack={closeDetail}
                onRefresh={() => void loadDetail(selectedCome.id)}
              />
            )}
          </>
        ) : (
          <>
            {loading && items === null && !error && (
              <p className="home__placeholder">목록을 불러오는 중입니다…</p>
            )}
            {!loading && items && items.length === 0 && (
              <p className="home__placeholder">등록된 선착순이 없습니다.</p>
            )}
            {items && items.length > 0 && (
              <ul className="come-list">
                {items.map((item) => (
                  <li key={item.id}>
                    <ComeCard item={item} onOpen={openDetail} />
                  </li>
                ))}
              </ul>
            )}
          </>
        )}
      </main>
    </div>
  )
}
