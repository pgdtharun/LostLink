# LostLink - Android App

A modern Android application for a campus lost and found system, built with Kotlin and Jetpack Compose.

## Project Overview

LostLink is a mobile app that helps campus community members:
- **Browse** lost and found items
- **Report** lost or found items  
- **Connect** with others to coordinate item recovery
- **Manage** their profile and activity

## Features

✅ Modern Material 3 UI Design  
✅ Green Theme (matching the web app CSS)  
✅ Jetpack Compose for declarative UI  
✅ Navigation between screens  
✅ Mock data for instant demonstration  
✅ Responsive layouts  
✅ Item browsing and filtering  
✅ User profile management  

## Project Structure

```
com/lostlink/app/
├── ui/
│   ├── screens/
│   │   ├── HomeScreen.kt
│   │   ├── DashboardScreen.kt          # Browse items, search, filter
│   │   ├── ReportAndDetailScreen.kt    # Report & item details
│   │   ├── ProfileScreen.kt
│   │   └── MainActivity.kt             # App entry point
│   ├── components/
│   │   └── Components.kt               # Reusable UI components
│   ├── theme/
│   │   ├── Theme.kt                    # Color scheme & theme
│   │   └── Type.kt                     # Typography settings
│   └── navigation/
│       └── Navigation.kt               # Navigation routes
├── data/
│   ├── model/
│   │   └── Models.kt                   # Data classes
│   └── repository/
│       └── MockDataRepository.kt       # Mock data provider
```

## Color Palette

- **Primary**: #16a34a (Green)
- **Primary Light**: #4ade80
- **Primary Dark**: #15803d
- **Accent**: #059669
- **Success**: #10b981
- **Warning**: #f59e0b
- **Danger**: #ef4444
- **Background**: #f0fdf4 (Light green)

## Screens

### 1. Home Screen
Landing page with app introduction and quick action buttons
- Browse All Items
- Get Started
- Feature highlights

### 2. Dashboard Screen
Browse and filter lost/found items
- Search functionality
- Tab filtering (All, Lost, Found)
- Item cards with details
- Click to view item details

### 3. Report Screen
Report a new lost or found item
- Item name
- Description
- Location
- Category
- Contact information

### 4. Item Detail Screen
View full details of an item
- Item image
- Complete description
- Reporter/Finder information
- Contact option

### 5. Profile Screen
User account management
- Profile information
- Activity statistics
- Settings
- Logout

## Getting Started

### Prerequisites
- Android Studio Arctic Fox or later
- Kotlin 1.9.0+
- Android SDK 28+

### Build & Run

1. **Clone/Open the project** in Android Studio
2. **Sync Gradle dependencies**
   ```bash
   ./gradlew build
   ```
3. **Run on Emulator or Device**
   ```bash
   ./gradlew installDebug
   ```
   Or press `Run` in Android Studio

### Debug in Studio
- Select your emulator/device
- Click "Run" or press Shift + F10

## Dependencies

```gradle
// Compose
- androidx.compose.ui:ui:1.5.0
- androidx.compose.material3:material3:1.0.1
- androidx.navigation:navigation-compose:2.7.0
- androidx.activity:activity-compose:1.7.2

// Core
- androidx.core:core-ktx:1.10.1
- androidx.lifecycle:lifecycle-runtime-ktx:2.6.1
```

## Key Components

### Theme System
The app uses Material 3 with a custom green color scheme defined in `Theme.kt`. All colors, typography, and spacing follow the design tokens from the original CSS.

### Navigation
Uses Jetpack Navigation with Compose integration for seamless screen transitions:
- Home → Dashboard
- Dashboard → Report Item
- Dashboard → Item Detail
- Any Screen → Profile

### Data Management
Currently uses `MockDataRepository` with hardcoded sample data. Ready for backend integration:
- Replace mock data with API calls
- Add Room database for offline support
- Implement Firebase/backend authentication

## Next Steps

To extend this app:

1. **Backend Integration**
   - Replace `MockDataRepository` with Retrofit/HTTP calls
   - Implement API authentication

2. **Image Upload**
   - Add camera/gallery picker for item photos
   - Implement image upload to backend

3. **Real-time Features**
   - Add Firebase Cloud Messaging for notifications
   - Implement real-time messaging between users

4. **Persistence**
   - Add Room database for local caching
   - Implement data sync

5. **Testing**
   - Add unit tests for ViewModels
   - Add UI tests for Compose screens

## Design System

The app follows Material Design 3 principles with a cohesive green color scheme that matches the web app. All components use:
- Rounded corners (8-24dp)
- Smooth transitions (150-400ms)
- Consistent spacing (4dp grid system)
- Clear typography hierarchy

## Support

For issues or questions about the LostLink Android app, please refer to the main project documentation.

## License

This project is part of the LostLink Campus Lost & Found platform.
