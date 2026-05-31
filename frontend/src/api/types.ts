export type JoinMethod = 'IMMEDIATELY'
export type WaitType = 'WAITLIST'
export type FirstComeStatus = 'CREATED' | 'READY' | 'ON_GOING'

export interface FirstComeTimeResponse {
  startAt: string
  endAt: string
  displayAt: string
}

export interface EligibilityResponse {
  value: string
  duplicable: boolean
}

export interface JoinResponse {
  method: JoinMethod
}

export interface WaitPolicyResponse {
  waitType: WaitType
  capacity: number | null
}

export interface FirstComeResponse {
  id: string
  name: string
  capacity: number
  status: FirstComeStatus
  time: FirstComeTimeResponse
  eligibility: EligibilityResponse
  join: JoinResponse
  waitPolicy: WaitPolicyResponse
  organizer: string
}

export type AuthMode = 'login' | 'signup'

export interface AuthUrlResponse {
  authUrl: string
}

export interface TokenResponse {
  accessToken: string
  expiresIn: number
  refreshToken: string
}

export interface MemberResponse {
  memberId: string
  nickName: string
}

export type ErrorCode = 'EXPIRED_TOKEN'

export interface ErrorResponse {
  message: string
  code: ErrorCode
}
