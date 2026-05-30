import type { AuthMode, AuthUrlResponse, ErrorResponse, MemberResponse, TokenResponse } from './types'

export const AUTH_TOKEN_STORAGE_KEY = 'catching.auth.tokens'

export class ApiError extends Error {
  readonly status: number
  readonly code?: string

  constructor(
    message: string,
    status: number,
    code?: string,
  ) {
    super(message)
    this.name = 'ApiError'
    this.status = status
    this.code = code
  }
}

async function readError(res: Response, fallbackMessage: string): Promise<ApiError> {
  try {
    const data = (await res.json()) as Partial<ErrorResponse>
    return new ApiError(data.message ?? fallbackMessage, res.status, data.code)
  } catch {
    return new ApiError(fallbackMessage, res.status)
  }
}

export function isExpiredTokenError(error: unknown): boolean {
  return error instanceof ApiError && error.status === 401 && error.code === 'EXPIRED_TOKEN'
}

export async function fetchAuthUrl(mode: AuthMode): Promise<string> {
  const params = new URLSearchParams({
    mode,
  })
  const res = await fetch(`/v1/auth/gateway?${params.toString()}`)
  if (!res.ok) {
    throw new Error(`로그인 URL 요청 실패 (${res.status})`)
  }
  const data = (await res.json()) as AuthUrlResponse
  return data.authUrl
}

export async function exchangeCode(code: string, clientId: string): Promise<TokenResponse> {
  const res = await fetch('/v1/auth/code', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      code,
      clientId,
    }),
  })
  if (!res.ok) {
    throw new Error(`토큰 교환 실패 (${res.status})`)
  }
  return res.json() as Promise<TokenResponse>
}

export async function refreshToken(refreshTokenValue: string): Promise<TokenResponse> {
  const res = await fetch('/v1/auth/token/refresh', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      refreshToken: refreshTokenValue,
    }),
  })
  if (!res.ok) {
    throw new Error(`토큰 갱신 실패 (${res.status})`)
  }
  return res.json() as Promise<TokenResponse>
}

export async function fetchMe(accessToken: string): Promise<MemberResponse> {
  const res = await fetch('/v1/auth/me', {
    headers: {
      Authorization: `Bearer ${accessToken}`,
    },
  })
  if (!res.ok) {
    throw await readError(res, `회원 정보 요청 실패 (${res.status})`)
  }
  return res.json() as Promise<MemberResponse>
}

export function readStoredTokens(): TokenResponse | null {
  const raw = window.localStorage.getItem(AUTH_TOKEN_STORAGE_KEY)
  if (!raw) {
    return null
  }

  try {
    return JSON.parse(raw) as TokenResponse
  } catch {
    window.localStorage.removeItem(AUTH_TOKEN_STORAGE_KEY)
    return null
  }
}

export function storeTokens(tokens: TokenResponse): void {
  window.localStorage.setItem(AUTH_TOKEN_STORAGE_KEY, JSON.stringify(tokens))
}

export function clearTokens(): void {
  window.localStorage.removeItem(AUTH_TOKEN_STORAGE_KEY)
}
