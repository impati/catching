import { useCallback, useEffect, useState } from 'react'
import {
  clearTokens,
  exchangeCode,
  fetchMe,
  fetchAuthUrl,
  readStoredTokens,
  storeTokens,
} from './api/auth'
import { fetchComes } from './api/comes'
import type { AuthMode, FirstComeResponse, MemberResponse, TokenResponse } from './api/types'
import './App.css'

const callbackTokenRequests = new Map<string, Promise<TokenResponse>>()

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

function ComeCard({ item }: { item: FirstComeResponse }) {
  return (
    <article className="come-card">
      <header className="come-card__head">
        <h2 className="come-card__title">{item.name}</h2>
        <span className={`come-card__status come-card__status--${item.status.toLowerCase()}`}>
          {item.status}
        </span>
      </header>
      <dl className="come-card__meta">
        <div>
          <dt>ID</dt>
          <dd>{item.id}</dd>
        </div>
        <div>
          <dt>정원</dt>
          <dd>{item.capacity}명</dd>
        </div>
        <div>
          <dt>주최</dt>
          <dd>{item.organizer}</dd>
        </div>
        <div>
          <dt>참여</dt>
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
      </dl>
      <section className="come-card__times">
        <h3>일정</h3>
        <ul>
          <li>
            <span>노출</span> {formatDt(item.time.displayAt)}
          </li>
          <li>
            <span>시작</span> {formatDt(item.time.startAt)}
          </li>
          <li>
            <span>종료</span> {formatDt(item.time.endAt)}
          </li>
        </ul>
      </section>
      <footer className="come-card__foot">
        <span>
          자격: {item.eligibility.value || '—'} · 중복{' '}
          {item.eligibility.duplicable ? '허용' : '불가'}
        </span>
      </footer>
    </article>
  )
}

export default function App() {
  const [items, setItems] = useState<FirstComeResponse[] | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [authLoading, setAuthLoading] = useState<AuthMode | 'callback' | null>(null)
  const [authError, setAuthError] = useState<string | null>(null)
  const [tokens, setTokens] = useState<TokenResponse | null>(() => readStoredTokens())
  const [member, setMember] = useState<MemberResponse | null>(null)

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
  }, [])

  const loadMe = useCallback(async (accessToken: string) => {
    try {
      setMember(await fetchMe(accessToken))
    } catch (e) {
      setAuthError(e instanceof Error ? e.message : '회원 정보를 불러오지 못했습니다.')
      setMember(null)
    }
  }, [])

  useEffect(() => {
    if (!tokens) {
      setMember(null)
      return
    }

    void loadMe(tokens.accessToken)
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
        await loadMe(nextTokens.accessToken)
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
  }, [])

  return (
    <div className="home">
      <header className="home__header">
        <div>
          <h1 className="home__title">선착순</h1>
          <p className="home__lead">external · 회원 시스템 연동</p>
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
                <ComeCard item={item} />
              </li>
            ))}
          </ul>
        )}
      </main>
    </div>
  )
}
