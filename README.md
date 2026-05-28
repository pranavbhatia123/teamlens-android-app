# TeamLens Native Android

Fresh Kotlin + Jetpack Compose Android app for TeamLens.

This app is intentionally separate from the Expo/React Native app. It uses:

- Package: `com.teamlens.nativeapp`
- UI: Jetpack Compose
- API: Retrofit + kotlinx serialization
- Session storage: Android DataStore
- Build-time environment: `.env`, `local.properties`, or shell env vars

## Run

Create local env:

```powershell
Copy-Item .env.example .env
```

For hosted backend, keep:

```env
TEAMLENS_API_BASE_URL=https://api.teamlens.co
TEAMLENS_WEB_BASE_URL=https://test.teamlens.co
```

For Android emulator local backend:

```env
TEAMLENS_API_BASE_URL=http://10.0.2.2:5000
TEAMLENS_WEB_BASE_URL=http://10.0.2.2:3000
```

Then build/run:

```powershell
.\gradlew.bat :app:assembleDebug
```

Open `mobile-native` in Android Studio and run the `app` configuration on an emulator.

## Current Native Coverage

- Login
- Manager signup
- Session restore
- Dashboard analytics cards
- Employees list
- Teams list
- Screenshot count
- Navigation placeholders for Live View, Activities, Attendance, Manual Time, Reports, Recordings, Productivity Rules, and Settings

## Backend URL Priority

The app reads URLs in this order:

1. Shell env vars: `TEAMLENS_API_BASE_URL`, `TEAMLENS_WEB_BASE_URL`
2. `local.properties`
3. `.env`
4. Hosted defaults

Do not commit `.env` or `local.properties`. Commit `.env.example` only.

For the current Android live-view/Coturn issue, share `COTURN_OPENCLAW_HANDOFF.md`.
