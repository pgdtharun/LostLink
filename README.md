# LostLink - Android App

A modern Android application for a campus lost and found system, built with Kotlin and Jetpack Compose. 

---

## 📖 Project Overview
LostLink is a mobile app designed to help campus community members:
* **Browse** lost and found items.
* **Report** newly lost or found items.
* **Connect** with others to coordinate item recovery.
* **Manage** their personal profile and activity.

## ✨ Features

* ✅ **Modern Material 3 UI Design** 
* ✅ **Green Theme** (matches the web application CSS)
* ✅ **Jetpack Compose** for a declarative UI architecture
* ✅ **Seamless Navigation** between application screens
* ✅ **Mock Data Integration** for instant demonstration
* ✅ **Responsive Layouts** for various screen sizes
* ✅ **Item Browsing & Filtering** capabilities
* ✅ **User Profile Management**

---

## 📱 Screens

1. **Home Screen**
   * Landing page with app introduction and quick action buttons.
   * *Actions:* Browse All Items, Get Started, Feature highlights.
2. **Dashboard Screen**
   * Browse and filter lost/found items.
   * *Actions:* Search functionality, Tab filtering (All, Lost, Found), Clickable item cards.
3. **Report Screen**
   * Form to report a new lost or found item.
   * *Fields:* Item name, Description, Location, Category, Contact information.
4. **Item Detail Screen**
   * View the full, detailed breakdown of a specific item.
   * *Details:* Item image, Complete description, Reporter/Finder information, Contact options.
5. **Profile Screen**
   * User account management and settings.
   * *Details:* Profile information, Activity statistics, Settings, Logout.

---

## 🛠 Getting Started

### Prerequisites
* Android Studio Arctic Fox (or later)
* Kotlin 1.9.0+
* Android SDK 28+

### Build & Run
1. Clone or open the project in Android Studio.
2. Sync Gradle dependencies:
   ```bash
   ./gradlew build

Run on an Emulator or Physical Device:
     ```bash
    ./gradlew installDebug

⚙️ Key Components
Theme System
Defined in Theme.kt, utilizing Material 3 components and custom styling to maintain brand consistency across the platform.

Navigation
Uses Jetpack Navigation with Compose for seamless, type-safe screen transitions:

Home → Dashboard

Dashboard → Report Item

Dashboard → Item Detail

Any Screen → Profile

# Next Steps

1.Backend Integration: Replace MockDataRepository with Retrofit HTTP calls and implement API authentication.

2.Image Upload: Add camera/gallery picker functionality for item photos and link to backend storage.

3.Real-time Features: Integrate Firebase Cloud Messaging for push notifications and implement in-app messaging.

4.Persistence: Add a Room database for local caching and offline data synchronization.

5.Testing: Write unit tests for ViewModels and UI tests for Compose screens.


📄 Support & License

 For issues or questions about the LostLink Android app, please refer to the main project documentation.
 This project is part of the LostLink Campus Lost & Found platform and is licensed under the MIT License.
