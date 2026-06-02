@echo off
echo ========================================
echo   Music Trigger - Quick Build
echo ========================================
echo.
echo Opening project in Android Studio...
echo.
echo Steps:
echo   1. Wait for Gradle sync (first time: 5-10 min)
echo   2. Click Build ^> Build APK(s)
echo.
echo APK location: app\build\outputs\apk\debug\app-debug.apk
echo.
pause

"C:\Program Files\Android\Android Studio\bin\studio64.exe" "C:\Users\Administrator\.qclaw\workspace\MusicTrigger"