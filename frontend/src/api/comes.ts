import { ApiError } from './auth'
import type {
  AppliedEventResponse,
  AppliedMemberResponse,
  AgreementRequests,
  ErrorResponse,
  FieldResponse,
  FirstComeResponse,
  InformationsRequest,
  TermsAgreementResponses,
  TermsGroupResponse,
} from './types'

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

export async function fetchApplyTerms(): Promise<TermsGroupResponse> {
  const res = await fetch('/v1/terms-groups/APPLY_FOR')
  if (!res.ok) {
    throw await readError(res, `약관 요청 실패 (${res.status})`)
  }
  return res.json() as Promise<TermsGroupResponse>
}

export async function fetchApplyTermsAgreement(
  accessToken: string,
): Promise<TermsAgreementResponses> {
  const res = await fetch('/v1/terms-groups/APPLY_FOR/agreement', {
    headers: {
      Authorization: `Bearer ${accessToken}`,
    },
  })
  if (!res.ok) {
    throw await readError(res, `약관 동의 상태 요청 실패 (${res.status})`)
  }
  return res.json() as Promise<TermsAgreementResponses>
}

export async function submitApplyTermsAgreement(
  accessToken: string,
  payload: AgreementRequests,
): Promise<TermsAgreementResponses> {
  const res = await fetch('/v1/terms-groups/APPLY_FOR/agreement', {
    method: 'POST',
    headers: {
      Authorization: `Bearer ${accessToken}`,
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(payload),
  })
  if (!res.ok) {
    throw await readError(res, `약관 동의 실패 (${res.status})`)
  }
  return res.json() as Promise<TermsAgreementResponses>
}

export async function fetchFields(
  comeId: string,
  accessToken: string,
): Promise<FieldResponse[]> {
  const res = await fetch(`/v1/comes/${encodeURIComponent(comeId)}/fields`, {
    headers: {
      Authorization: `Bearer ${accessToken}`,
    },
  })
  if (!res.ok) {
    throw await readError(res, `정보 입력 항목 요청 실패 (${res.status})`)
  }
  return res.json() as Promise<FieldResponse[]>
}

export async function submitInformation(
  comeId: string,
  accessToken: string,
  payload: InformationsRequest,
): Promise<void> {
  const res = await fetch(`/v1/comes/${encodeURIComponent(comeId)}/information`, {
    method: 'POST',
    headers: {
      Authorization: `Bearer ${accessToken}`,
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(payload),
  })
  if (!res.ok) {
    throw await readError(res, `정보 입력 실패 (${res.status})`)
  }
}

export async function fetchInformation(
  comeId: string,
  accessToken: string,
): Promise<AppliedMemberResponse> {
  const res = await fetch(`/v1/comes/${encodeURIComponent(comeId)}/information`, {
    headers: {
      Authorization: `Bearer ${accessToken}`,
    },
  })
  if (!res.ok) {
    throw await readError(res, `정보 입력 결과 요청 실패 (${res.status})`)
  }
  return res.json() as Promise<AppliedMemberResponse>
}

export async function updateInformation(
  comeId: string,
  accessToken: string,
  payload: InformationsRequest,
): Promise<void> {
  const res = await fetch(`/v1/comes/${encodeURIComponent(comeId)}/information`, {
    method: 'PUT',
    headers: {
      Authorization: `Bearer ${accessToken}`,
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(payload),
  })
  if (!res.ok) {
    throw await readError(res, `정보 수정 실패 (${res.status})`)
  }
}
