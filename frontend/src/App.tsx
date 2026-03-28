import { useCallback, useEffect, useState } from 'react'
import { fetchComes } from './api/comes'
import type { FirstComeResponse } from './api/types'
import './App.css'

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

  return (
    <div className="home">
      <header className="home__header">
        <h1 className="home__title">선착순</h1>
        <p className="home__lead">external · GET /v1/comes</p>
        <button type="button" className="home__reload" onClick={() => void load()} disabled={loading}>
          {loading ? '불러오는 중…' : '다시 불러오기'}
        </button>
      </header>

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
