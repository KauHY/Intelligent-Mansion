# ThingsBoard Sensor Demo Task Plan

## Goal

Add a public test route that connects directly from the frontend to ThingsBoard, authenticates with a configurable account, resolves a target sensor device, and shows temperature/humidity latest data with WebSocket-based live updates.

## Phases

| Status | Phase | Notes |
| --- | --- | --- |
| completed | Context and API validation | Confirmed current project routing pattern and ThingsBoard auth/query endpoints. |
| completed | Implement frontend ThingsBoard client | Added REST login, device lookup, latest telemetry fetch, and WebSocket subscription client. |
| completed | Implement sensor demo route/page | Added config-driven demo page and route at `/#/tb-sensor-demo`. |
| in_progress | Verification and handoff docs | ESLint passes for new files; full TypeScript check is blocked by missing `@types/node` in the repository setup. |

## Decisions

- Use a public route under `src/routers/routes/common.ts` so the page does not depend on backend menu permissions.
- Keep ThingsBoard integration fully frontend-side and separate from the existing project axios interceptors.
- Use REST for login/device resolution/latest snapshot and WebSocket for ongoing live telemetry updates.
- Drive runtime setup from a dedicated config file so the user can replace only a few values after creating the device.
- Keep direct access behavior aligned with the existing application router guard; the page is safest to open from within the app or with a valid existing app login session.

## Risks / Notes

- The provided `sysadmin` account can log in but does not have device-list access for the target tenant; final runtime likely needs a tenant-level account.
- Device does not exist yet, so runtime validation must fail gracefully and explain what is missing.
- Embedding account credentials in frontend code is acceptable only for a test/demo page, not for production release.
- Full `npm run ts` verification cannot finish until the repo installs the missing Node type definitions referenced by `tsconfig.json`.
