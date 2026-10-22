# ThingsBoard Hybrid Sensor Demo Layout Optimization Design

## Goal

Tighten the layout of `src/views/demo/thingsboard-sensor-hybrid-demo.vue` so the page exposes all critical monitoring content within a typical desktop viewport with less scrolling.

The optimization is layout-focused. It does not change data sources, authentication, telemetry mapping, or page behavior.

## Scope

### In Scope

- Remove non-essential diagnostic sections that consume vertical space
- Replace the current large hero panel with a compact top status bar
- Keep the main monitoring cards and the two charts visible and readable
- Preserve the reconnect and refresh actions
- Keep the device name visible on the page after removing the device detail card
- Reduce spacing, padding, and chart heights to improve first-screen density

### Out of Scope

- No new telemetry keys
- No new charts or tabs
- No authentication flow changes
- No data fetching logic changes unless cleanup is required after UI removal
- No behavioral redesign for mobile beyond preserving acceptable responsive collapse

## Chosen Approach

Use the approved "A" layout:

- A compact top information bar replaces the hero panel
- The dashboard keeps the current two-column information architecture
- The left column keeps the main metric cards and auxiliary metrics
- The right column keeps both charts visible at the same time
- The bottom diagnostic card grid is removed entirely

This approach minimizes structural churn while removing the sections that currently force additional scrolling.

## UI Structure

### Compact Top Bar

Replace `hero-panel` with a shorter header strip that contains:

- Device name
- Last update timestamp
- Phase status tag
- REST status tag
- Realtime socket status tag
- `重新连接` button
- `刷新历史` button

The explanatory hero copy is removed. The top bar should be one row on wide screens and wrap cleanly on narrower widths.

### Main Dashboard Body

Keep the current split layout:

- Left column:
  - primary temperature card
  - humidity card
  - pressure card
  - auxiliary metrics card
- Right column:
  - climate trend chart
  - pressure chart

The body remains recognizable as the current monitoring dashboard, but the cards should use tighter padding and smaller inter-card gaps.

### Removed Sections

Remove these sections entirely:

- top-level `hero-panel`
- `设备详情` card
- `Telemetry Key 探测` card
- `鉴权摘要` card

After removal, the page should no longer render the bottom `detail-grid`.

## Data Presentation Rules

- Device name comes from `resolvedDevice?.name`, then falls back to `config.device.name`, then to the existing default label
- Last update timestamp stays visible in the top bar
- Status tags continue using the existing computed values
- The two action buttons keep their current loading and click behavior
- All metric value formatting remains unchanged
- Both charts remain present simultaneously; no chart switching or tabs are introduced

## Styling Direction

The optimized page should feel denser, not stripped down.

Apply these visual changes:

- reduce outer page padding
- reduce vertical spacing between alerts and dashboard sections
- reduce card padding
- reduce metric card minimum height
- reduce chart card body height
- remove oversized hero-specific backgrounds and shadows

Keep the existing light monitoring palette and card treatment so the page still matches the surrounding demo style.

## Cleanup Expectations

After the removed sections are deleted from the template, remove computed properties and styles that only supported those sections, including any now-unused masking or diagnostics-only values.

Any cleanup must be limited to dead code created by this layout change.

## Verification

### Functional

- Page still boots automatically
- `重新连接` still triggers bootstrap
- `刷新历史` still refreshes history
- Status tags still reflect phase, REST, and realtime state
- Device name is visible without the removed device detail card

### Layout

- On a typical desktop viewport, the key content requires less or no scrolling compared with the previous version
- Both charts remain readable after height reduction
- The page does not leave an empty area where the removed detail grid used to be
- The top bar wraps without overlap at narrower desktop widths

### Code Health

- No stale template bindings remain for removed cards
- No unused computed values remain that only served the removed diagnostics UI
