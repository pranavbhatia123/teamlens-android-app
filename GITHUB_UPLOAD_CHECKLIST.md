# TeamLens Native Android GitHub Checklist

Run these from `mobile-native`.

## Before Push

```powershell
git status --short
git diff --stat
git ls-files | Select-String "\.env|local.properties|app-debug.apk|/build/"
```

Only `.env.example` should be tracked. Real `.env`, `local.properties`, APKs, and build folders should not appear.

If a local secret file is already tracked:

```powershell
git rm --cached .env
git rm --cached local.properties
```

## Build Check

```powershell
.\gradlew.bat --no-daemon :app:assembleDebug
```

## Local Env

Copy the template:

```powershell
Copy-Item .env.example .env
```

Then set:

```env
TEAMLENS_API_BASE_URL=https://api.teamlens.co
TEAMLENS_WEB_BASE_URL=https://test.teamlens.co
```
