import { ApiError } from './auth'
import type { AppliedEventResponse, ErrorResponse, FirstComeResponse } from './types'

async function readError(res: Response, fallbackMessage: string): Promise<ApiError> {
  try {
    const data = (await res.json()) as Partial<ErrorResponse>
    return new ApiError(data.message ?? fallbackMessage, res.status, data.code)
  } catch {
    return new ApiError(fallbackMessage, res.status)
  }
}

export async function fetchComes(): Promise<FirstComeResponse[]> {
  const res = await fetch('/v1/comes')
  if (!res.ok) {
    throw await readError(res, `목록 요청 실패 (${res.status})`)
  }
  return res.json() as Promise<FirstComeResponse[]>
}

export async function fetchComeDetail(comeId: string): Promise<FirstComeResponse> {
  const res = await fetch(`/v1/comes/${encodeURIComponent(comeId)}/active`)
  if (!res.ok) {
    throw await readError(res, `상세 요청 실패 (${res.status})`)
  }
  return res.json() as Promise<FirstComeResponse>
}

export async function applyForCome(comeId: string, accessToken: string): Promise<void> {
  const res = await fetch(`/v1/comes/${encodeURIComponent(comeId)}/apply-for`, {
    method: 'POST',
    headers: {
      Authorization: `Bearer ${accessToken}`,
    },
  })
  if (!res.ok) {
    throw await readError(res, `신청 실패 (${res.status})`)
  }
}

export async function fetchApplyFor(
  comeId: string,
  accessToken: string,
): Promise<AppliedEventResponse> {
  const res = await fetch(`/v1/comes/${encodeURIComponent(comeId)}/apply-for`, {
    headers: {
      Authorization: `Bearer ${accessToken}`,
    },
  })
  if (!res.ok) {
    throw await readError(res, `신청 결과 요청 실패 (${res.status})`)
  }
  return res.json() as Promise<AppliedEventResponse>
}
