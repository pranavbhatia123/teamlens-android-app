# Coturn / Android Live View Handoff

The native Android app can reach Socket.IO signaling, but the WebRTC media path is failing.

Current Android symptom:

```text
WebRTC connection failed
ICE disconnected | frames 0 | bytes 0
```

Meaning:

- The manager app can request the employee live session.
- The WebRTC peer connection starts.
- Android receives no video frames and no media bytes.
- This points to ICE/TURN/STUN relay config, not normal Kotlin UI rendering.

Backend files involved:

- `teamlense-web-server-2/backend-ws/src/socket.ts`
- `teamlense-web-server-2/backend-ws/src/config/env.ts`
- `teamlense-web-server-2/agent/src/liveScreen.ts`

Android file involved:

- `mobile-native/app/src/main/java/com/teamlens/nativeapp/MainActivity.kt`

Expected production ICE env on the WebSocket backend:

```env
WEBRTC_ICE_SERVERS=[{"urls":["stun:stun.l.google.com:19302","turn:turn.teamlens.co:3478?transport=udp","turn:turn.teamlens.co:3478?transport=tcp","turns:turn.teamlens.co:5349?transport=tcp"],"username":"REPLACE_WITH_COTURN_USERNAME","credential":"REPLACE_WITH_COTURN_PASSWORD"}]
```

Coturn checklist:

- `turn.teamlens.co` DNS points to the Coturn server.
- UDP/TCP `3478` is open.
- TCP `5349` is open if TLS TURN is enabled.
- Coturn relay port range is open in the cloud firewall.
- Username/credential match the deployed `WEBRTC_ICE_SERVERS`.
- `backend-ws` was restarted/redeployed after env changes.

STUN-only is not enough for reliable Android/mobile live view.
