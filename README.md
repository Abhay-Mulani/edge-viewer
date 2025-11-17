# Edge Viewer — Android + OpenCV(C++) + OpenGL + Web Viewer

## Overview
Real-time camera feed processed by OpenCV in native C++ (via JNI) and rendered using OpenGL ES 2.0.
Includes a TypeScript web viewer to display a sample processed frame.

## Tech stack
- Android (Kotlin)
- NDK / JNI (OpenCV C++)
- OpenGL ES 2.0
- TypeScript (simple static viewer)

## Features implemented
- Camera capture via Camera2 -> TextureView
- JNI bridge sending frames to native C++
- Canny edge detection (OpenCV C++)
- Rendering processed frames as OpenGL texture
- Toggle Raw / Edge output + FPS counter
- Simple TS web viewer showing sample processed frame + FPS overlay

## Build / Run
1. Install Android Studio with NDK and CMake.
2. Add OpenCV Android SDK and set `OpenCV_DIR` / include as instructed in `app/CMakeLists.txt`.
3. Open the project in Android Studio and run on device.

## Repo structure
- `/app` — Android Kotlin app
- `/app/src/main/cpp` — native code
- `/gl` — OpenGL renderer classes (Kotlin)
- `/web` — TypeScript viewer

## Screenshots
(Add screenshots/gif here)

## Notes
This project demonstrates modular JNI integration and a tiny web viewer.

