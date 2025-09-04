# USB Camera Preview Application

An Android application that detects and displays live preview from external USB cameras connected to your Android device.

## Features

- **USB Camera Detection**: Automatically detects when USB cameras are connected/disconnected
- **Live Camera Preview**: Shows real-time preview from connected USB cameras
- **Device Management**: Lists all available USB cameras with connection status
- **Permission Handling**: Manages USB permissions for camera access
- **Modern UI**: Built with Jetpack Compose and Material3 design

## How It Works

1. **App Launch** → Scans for connected USB cameras
2. **Camera Detection** → Shows available cameras in the device list
3. **Permission Request** → Requests USB permission when camera is selected
4. **Live Preview** → Displays camera feed in the preview area
5. **Auto-Connection** → Automatically connects to newly detected cameras

## Technical Details

- **UI Framework**: Jetpack Compose with Material3
- **Camera Support**: USB Video Class (UVC) cameras
- **USB Host API**: For device detection and management
- **Architecture**: MVVM with ViewModel and StateFlow
- **Minimum SDK**: 24 (Android 7.0 Nougat)
- **Target SDK**: 35 (Android 15)

## Project Structure

```
app/src/main/java/com/uni/mybackresultapplication/
├── MainActivity.kt              # Main activity with USB handling
├── viewmodel/
│   └── CameraViewModel.kt       # Camera state management
├── ui/
│   ├── CameraPreviewScreen.kt   # Main UI screen
│   └── theme/                   # UI theme and styling
│       ├── Color.kt
│       ├── Theme.kt
│       └── Type.kt
└── res/xml/
    └── device_filter.xml        # USB device filter
```

## Requirements

- **USB OTG Support**: Device must support USB On-The-Go
- **USB Camera**: UVC-compliant USB camera
- **Permissions**: USB and Camera permissions

## How to Use

1. **Connect USB Camera**: Plug your USB camera into the device using USB OTG adapter
2. **Launch App**: Open the application
3. **Grant Permission**: Allow USB access when prompted
4. **View Preview**: Camera feed will appear in the preview area
5. **Device Management**: Use the device list to switch between cameras

## Supported Camera Types

- **UVC Cameras**: Standard USB Video Class cameras
- **Common Brands**: Logitech, Realtek, and other UVC-compliant cameras
- **Webcams**: Most USB webcams that work with computers

## Dependencies

- Jetpack Compose BOM: 2024.02.00
- Activity Compose: 1.8.2
- Lifecycle Runtime Compose: 2.7.0
- Core KTX: 1.12.0

## Build Status

✅ **BUILD SUCCESSFUL** - Ready to run on Android devices with USB OTG support
