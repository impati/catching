# Project Rules

## Review Mode

When the user asks for "코드리뷰", "리뷰", "review", or "검토":

- Treat the task as read-only by default.
- Do not edit, create, delete, format, or regenerate files.
- Do not run commands that mutate source files or build artifacts.
- Provide findings, risks, and suggested patches only.
- Apply changes only when the user explicitly says "반영해줘", "수정해줘", "고쳐줘", or "적용해줘".

## Ambiguous Requests

If a request mixes review and implementation, ask for confirmation before editing files.

Examples:

- "코드리뷰하고 프론트 싱크 맞춰줘" requires confirmation before edits.
- "서버는 리뷰만, 프론트는 반영해줘" allows frontend-only edits.
