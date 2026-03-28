export type JoinMethod = 'IMMEDIATELY'
export type WaitType = 'WAITLIST'
export type FirstComeStatus = 'PENDING'

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
