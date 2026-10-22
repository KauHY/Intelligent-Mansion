# ThingsBoard Hybrid Sensor Demo Design

## Goal

Add a new public demo route that displays a ThingsBoard temperature, humidity, and pressure device using a hybrid data strategy:

- API key for REST device info, telemetry key discovery, latest snapshot, and historical series
- JWT for WebSocket realtime telemetry subscription

The existing `tb-sensor-demo` route remains unchanged and continues to represent the API key + REST polling approach.

## Route

- New route: `/#/tb-sensor-hybrid-demo`
- Route remains public and does not depend on backend menu permissions

## Target Device

- Device id: `ec331080-37d4-11f1-bc98-4b23962921cd`
- Device name: `温湿度与气压`
- Valid telemetry keys confirmed from ThingsBoard:
  - `humidity`
  - `temperature_aht30`
  - `temperature_bmp180`
  - `pressure`
  - `altitude`

## Chosen Telemetry Mapping

- Primary temperature: `temperature_aht30`
- Primary humidity: `humidity`
- Primary pressure: `pressure`
- Secondary telemetry:
  - `temperature_bmp180`
  - `altitude`

## Authentication Model

### REST

- Use `X-Authorization: ApiKey <apiKey>`
- Use for:
  - device info
  - telemetry key list
  - latest snapshot
  - historical time series

### WebSocket

- Use JWT token in `authCmd.token`
- Use for:
  - realtime subscription to the primary metrics

### Failure Handling

- If REST fails, page shows configuration and clear error state with empty metrics
- If WebSocket fails, page stays usable with REST snapshot/history and shows degraded realtime status
- WebSocket reconnect should be supported with a controlled retry delay

## Data Flow

### Bootstrap

1. Read config
2. Fetch device info by device id using API key
3. Fetch telemetry key list using API key
4. Fetch latest snapshot for primary and secondary keys using API key
5. Fetch recent historical series for chart rendering using API key
6. Open WebSocket with JWT and subscribe to primary realtime keys

### Realtime Update

- WebSocket messages update:
  - headline metric cards
  - latest timestamp
  - chart tail points for subscribed series

### Fallback Rules

- If telemetry keys endpoint returns empty, fall back to keys detected from latest snapshot or historical series
- If a specific series has no data, only that metric/chart section degrades

## UI Structure

### Hero Status Band

- Device name
- Connection stage
- REST status
- WebSocket realtime status
- Last updated timestamp
- Actions:
  - reconnect realtime
  - refresh history

### Left Metrics Panel

- Large temperature card
- Two supporting cards:
  - humidity
  - pressure
- Secondary information strip:
  - `temperature_bmp180`
  - `altitude`

### Right Charts Panel

- Top chart: temperature + humidity historical trend
- Bottom chart: pressure historical trend

### Bottom Diagnostics

- Device details
- Telemetry key discovery
- Auth summary
- Last error message

## Visual Direction

- Keep the monitoring-dashboard structure from the reference
- Improve refinement with:
  - softer glass cards
  - clearer hierarchy
  - stronger typography contrast
  - restrained but polished gradients
  - cleaner chart framing
- Prefer a light atmospheric industrial dashboard rather than plain white cards

## Component Boundaries

- Extend ThingsBoard client with historical series and WebSocket support
- Add dedicated config for the hybrid page
- Add one new page component for orchestration and rendering
- Keep logic for:
  - snapshot/history fetch
  - realtime websocket lifecycle
  - chart series transformation
  clear and localized to the new page

## Verification

- Route opens directly
- Device info loads from REST
- Key discovery shows confirmed telemetry keys
- Latest values load for temperature, humidity, and pressure
- Historical charts render from REST series
- WebSocket connects with JWT and appends realtime updates
- Reconnect action works after disconnect
- Focused ESLint passes for changed files
