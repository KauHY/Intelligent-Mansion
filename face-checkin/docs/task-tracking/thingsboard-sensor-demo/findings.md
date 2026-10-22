# Findings

## Project

- Router structure uses public routes from `src/routers/routes/common.ts` and injects authenticated business routes later from backend menu data.
- The existing global axios client is coupled to the park backend base URL and `Authorization` header, so ThingsBoard should use its own client.

## ThingsBoard

- Swagger/OpenAPI is available at `http://183.194.113.230:6080/v3/api-docs`.
- REST auth header supports:
  - `X-Authorization: Bearer <jwt>`
  - `X-Authorization: ApiKey <apiKey>`
- Telemetry latest values endpoint: `GET /api/plugins/telemetry/{entityType}/{entityId}/values/timeseries?keys=...`
- Telemetry keys endpoint: `GET /api/plugins/telemetry/{entityType}/{entityId}/keys/timeseries`
- Entity query endpoint: `POST /api/entitiesQuery/find`
- WebSocket endpoint: `ws(s)://host:port/api/ws`
- WebSocket auth command: `{ "authCmd": { "cmdId": 0, "token": "<jwt>" } }`
- WebSocket telemetry updates use `subscriptionId`, `errorCode`, `errorMsg`, and `data`, where `data` is keyed by telemetry key and values are `[timestamp, value]`.
- Practical validation on `2026-04-15`:
  - JWT can authenticate and receive telemetry over WebSocket.
  - API key can read device info and latest telemetry over REST.
  - API key cannot complete WebSocket subscription; the server closes the connection after the handshake.

## Access Validation

- `sysadmin@thingsboard.org / sysadmin` login works.
- Listing tenants via `/api/tenants` works for sysadmin.
- Listing target-tenant device infos via `/api/tenant/deviceInfos` returns `403`, so the final runtime likely requires tenant-level credentials instead of sysadmin.
