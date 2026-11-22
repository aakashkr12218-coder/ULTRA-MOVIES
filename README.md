# ULTRA MOVIES — Android starter

A simple, ad-free Android video player starter with a Netflix-inspired dark interface.

## Included
- Android app project (Kotlin + Jetpack Compose)
- Select and play videos stored on the device
- Play direct HTTP(S) video URLs that you are authorized to use
- Media3 / ExoPlayer playback controls

## Build the APK
1. Install Android Studio on a computer.
2. Open this folder in Android Studio.
3. Allow Gradle to sync and download dependencies.
4. Select **Build → Build Bundle(s) / APK(s) → Build APK(s)**.
5. Android Studio will show the generated APK location.

## Important
This starter does not unlock, scrape, download, or bypass ads/DRM on Netflix, Prime Video,
Amazon MX Player, or other paid streaming services. Add only media you own or have permission
to stream. This is a starter project, not yet a full streaming catalogue or production app.


## Build in GitHub Actions (without installing Android Studio locally)
1. Create a GitHub repository and upload the contents of this project.
2. Open the repository's **Actions** tab.
3. Run **Build ULTRA MOVIES APK** (or push to the `main` branch).
4. When the workflow finishes, open its run and download the
   `ultra-movies-debug-apk` artifact.
5. Extract the artifact and install `app-debug.apk` on your Android phone.
   You may need to allow installation from your browser or file manager.

The generated debug APK is for testing. A production release should be signed
with your own private signing key before distribution.
