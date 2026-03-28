import type { FirstComeResponse } from './types'

export async function fetchComes(): Promise<FirstComeResponse[]> {
  const res = await fetch('/v1/comes')
  if (!res.ok) {
    throw new Error(`요청 실패 (${res.status})`)
  }
  return res.json() as Promise<FirstComeResponse[]>
}
