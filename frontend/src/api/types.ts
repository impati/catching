export type JoinMethod = 'IMMEDIATELY'
export type WaitType = 'WAITLIST'
export type FirstComeStatus = 'CREATED' | 'READY' | 'ON_GOING' | 'ACTIVE'
export type FieldType =
  | 'NORMAL'
  | 'MOBILE'
  | 'SINGLE_DOMAIN'
  | 'MULTIPLE_DOMAIN'
  | 'DATA_SOURCE'

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
  appliedEventNumber: number
  organizer: string
}

export interface AppliedEventResponse {
  firstComeId: string
  memberId: string
}

export interface DatasourceResponse {
  name: string
  url: string
  createdBy: string
  createdAt: string
}

export interface FieldResponse {
  name: string
  fieldType: FieldType
  required: boolean
  dataSource: DatasourceResponse | null
  domain: string[] | null
}

export interface InformationRequest {
  name: string
  values: string[]
}

export interface InformationsRequest {
  informations: InformationRequest[]
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
