# ♟ Kotlin Multiplatform Chess App

This is a Kotlin Multiplatform project targeting **Android**, **iOS**, and **Web** using **JetBrains
Compose Multiplatform**. It aims to be a simple chess app showcasing shared business logic and UI
across platforms.

## 🧱 Project Structure
The project is structured to separate shared code from platform-specific code. The main components 
are:
```text
kmp-chess/
├── composeApp/          # Shared Kotlin Multiplatform module
│   ├── commonMain/      # Shared code (UI, logic)
│   ├── androidMain/     # Android-specific code
│   ├── iosMain/         # iOS-specific code
│   └── wasmJsMain/      # Web-specific code (WASM)
└── iosApp/              # Native iOS app wrapper (Xcode project)
```

## ▶️ How to Run

### ✅ Android

1. Open the project in **Android Studio**.
2. Select an Android device or emulator.
3. Run the `androidApp` configuration **or** execute:

```bash
./gradlew :composeApp:installDebug
```

### 🍏 iOS

1. Open iosApp/iosApp.xcodeproj in Xcode.
2. Choose a simulator or a physical device.
3. Press Cmd + R to build and run.

### 🌐 Web (WASM)

To run the web (Kotlin/Wasm) version locally:

```bash
./gradlew :composeApp:wasmJsBrowserDevelopmentRun
```
Then open the printed `localhost` URL in your browser.

### Image Attributions

Chess piece and board images are used under
the [Creative Commons Attribution-ShareAlike License (CC BY-SA)](https://creativecommons.org/licenses/by-sa/3.0/).

- **Standard chess pieces** are derived from images
  on [Wikipedia: Chess](https://en.wikipedia.org/wiki/Chess), modified and redistributed by **Uray
  M. János** on the [Green Chess](http://greenchess.sourceforge.net/download.html) project.
- **Boards** are original works by **Uray M. János**.
-
Source: [http://greenchess.sourceforge.net/download.html](http://greenchess.sourceforge.net/download.html)

© Uray M. János 2009–2025
