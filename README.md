# TeamLens Native Android

Fresh Kotlin + Jetpack Compose Android app for TeamLens.

This app is intentionally separate from the Expo/React Native app. It uses:

- Package: `com.teamlens.nativeapp`
- UI: Jetpack Compose
- API: Retrofit + kotlinx serialization
- Session storage: Android DataStore
- Default backend URL for Android emulator: `http://10.0.2.2:5000`

## Run

Start the backend first:

```powershell
cd ..\backend
npm run dev
```

Then build/run the native app:

```powershell
cd ..\mobile-native
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

## Backend URL

For Android emulator, keep:

```kotlin
BuildConfig.API_BASE_URL = "http://10.0.2.2:5000"
```

For a real Android phone, replace it in `app/build.gradle.kts` with your computer LAN IP, for example:

```kotlin
buildConfigField("String", "API_BASE_URL", "\"http://192.168.1.10:5000\"")
```
