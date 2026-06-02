# Music Trigger - 一次性音乐触发器

## 功能
点击图标 → 触发系统播放音乐 → 立即自我关闭（透明无界面）

## 快速开始

### 方法一：Android Studio 一键编译
1. 用 Android Studio 打开此文件夹 (MusicTrigger)
2. 等待 Gradle 同步完成（首次会自动下载 Gradle Wrapper）
3. 点击 **Build → Build Bundle(s) / APK(s) → Build APK(s)**
4. APK 生成在: app/build/outputs/apk/debug/app-debug.apk

### 方法二：运行 build.bat
双击 build.bat 运行自动构建脚本

## 工作原理
1. **MainActivity** - 透明的入口 Activity
2. 请求音频焦点 (Audio Focus)
3. 发送 MEDIA_PLAY 媒体键事件触发系统播放
4. 300ms 后自动 finish() 关闭自身

## 环境要求
- Android Studio Arctic Fox 或更高版本
- Android SDK (API 28+)
- JDK 17 或更高版本

## 注意事项
- 需要系统中有音乐播放器（Spotify、网易云音乐等）
- 应用本身不包含音乐，只是触发系统播放
- 需要 MEDIA_CONTENT_CONTROL 权限