# LostLink Android App - Complete Integration Guide

## 📱 Project Overview

LostLink is a comprehensive campus lost-and-found management system built with **Kotlin + Jetpack Compose** and Material Design 3. It includes 20+ screens supporting the complete workflow for reporting lost items, finding items, claiming items with verification, and admin management.

## 🏗️ Architecture

```
LostLink App
├── UI Layer (Jetpack Compose)
│   ├── Screens (20+ composables)
│   ├── Components (Reusable UI elements)
│   ├── Theme (Material Design 3 + Green color scheme)
│   └── Navigation (20+ routes)
├── Data Layer
│   ├── Models (ExtendedModels.kt with 25+ data classes)
│   ├── Repository (MockDataRepository.kt)
│   └── API (Retrofit - to be implemented)
└── Business Logic
    ├── Services (To be implemented)
    └── ViewModels (To be enhanced)
```

## 🎯 Implemented Features

### ✅ Complete (Currently Implemented)

1. **Authentication System**
   - LoginScreen with email/password validation
   - RegisterScreen with name, email, phone, ID number, password
   - Authentication state management in MainApp

2. **Lost & Found Items**
   - Item reporting (lost/found)
   - Item browsing with filtering
   - Item detail view with contactable owner/finder
   - Search functionality
   - Item categories (Bags, Books, Electronics, ID Cards, Other)

3. **Claim Workflow**
   - Submit claim with proof (text, image, serial number, ID)
   - View claim status (PENDING/APPROVED/REJECTED)
   - Status timeline visualization

4. **QR Verification & Handover**
   - QR code scanner screen
   - Manual QR code entry
   - Verification success confirmation
   - Handover log

5. **Campus Features**
   - Campus map with 6 key locations
   - Location-based item filtering
   - Campus location markers

6. **User Features**
   - Saved items (bookmarks)
   - In-app messaging with conversations
   - Direct chat between users
   - User profile with saved preferences

7. **Admin Dashboard**
   - Statistics overview (total items, pending claims, verified items, users)
   - Claim management
   - User management
   - Reports and audit trails

### 🔄 Ready for Backend Integration

- Retrofit API service layer structure
- Firebase Firestore database models
- FCM notification handling
- Image upload infrastructure
- QR code generation
- AI matching algorithm framework

## 📁 Project Structure

```
src/main/kotlin/com/lostlink/app/
├── MainActivity.kt               # Entry point with NavHost
├── ui/
│   ├── theme/
│   │   ├── Theme.kt             # Material Design 3 + Green colors
│   │   ├── Color.kt             # 14 CSS-derived colors
│   │   └── Type.kt              # Typography system
│   ├── screens/
│   │   ├── auth/
│   │   │   ├── LoginScreen.kt
│   │   │   └── RegisterScreen.kt
│   │   ├── HomeScreen.kt
│   │   ├── DashboardScreen.kt
│   │   ├── ReportScreen.kt
│   │   ├── ItemDetailScreen.kt
│   │   ├── ClaimScreens.kt
│   │   ├── QRAndMapScreens.kt
│   │   ├── ExtendedFeatureScreens.kt
│   │   └── ProfileScreen.kt
│   ├── components/
│   │   ├── Components.kt         # Button, Card, Badge, SearchField
│   │   └── ...
│   └── navigation/
│       └── Navigation.kt         # 20+ routes + navigation events
├── data/
│   ├── model/
│   │   ├── Models.kt            # Basic models
│   │   └── ExtendedModels.kt    # 25+ models for all features
│   └── repository/
│       └── MockDataRepository.kt # Sample data
└── resources/
    ├── colors.xml
    ├── strings.xml
    └── AndroidManifest.xml
```

## 🚀 Getting Started

### Prerequisites

- **Android Studio**: Giraffe or newer
- **Kotlin**: 1.9.0+
- **Gradle**: 8.1.0+
- **Min SDK**: API 28 (Android 9)
- **Target SDK**: API 34 (Android 14)

### Step 1: Project Setup

1. Clone or create the project structure
2. Add required dependencies to `build.gradle`:

```gradle
dependencies {
    // Jetpack Compose
    implementation 'androidx.compose.ui:ui:1.5.0'
    implementation 'androidx.compose.material3:material3:1.0.1'
    implementation 'androidx.compose.animation:animation:1.5.0'
    
    // Navigation
    implementation 'androidx.navigation:navigation-compose:2.7.0'
    
    // ViewModel & Lifecycle
    implementation 'androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1'
    
    // Material Icons
    implementation 'androidx.compose.material:material-icons-extended:1.5.0'
    
    // Retrofit (for API integration)
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
    implementation 'com.squareup.okhttp3:okhttp:4.11.0'
    
    // Firebase (when ready)
    implementation 'com.google.firebase:firebase-firestore-ktx:24.8.1'
    implementation 'com.google.firebase:firebase-messaging-ktx:23.2.1'
    
    // QR Code (for scanning)
    implementation 'com.journeyapps:zxing-android-embedded:4.3.0'
}
```

### Step 2: Run the App

1. Open project in Android Studio
2. Select a target device (emulator or physical device)
3. Run → Run 'app' (or press Shift + F10)
4. App starts at **LoginScreen**

### Step 3: Test the App

**Test Account:**
- Email: `demo@lostlink.com`
- Password: `password123`

**Navigation Flow:**
- Login → Dashboard → Browse items → View details → Claim item → QR verification → Success
- Or: Login → Profile → Edit preferences → Save items → View messages

## 🔌 Backend Integration (Next Steps)

### 1. Create API Service Layer

```kotlin
// data/api/LostLinkApiService.kt
interface LostLinkApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse
    
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse
    
    @GET("items")
    suspend fun getAllItems(@Query("category") category: String?): List<LostItem>
    
    @POST("claims")
    suspend fun submitClaim(@Body claim: Claim): ClaimResponse
    
    @PUT("claims/{claimId}")
    suspend fun updateClaimStatus(
        @Path("claimId") claimId: String,
        @Body update: ClaimStatusUpdate
    ): ClaimResponse
    
    @POST("notifications/{userId}")
    suspend fun getNotifications(@Path("userId") userId: String): List<Notification>
    
    @POST("messages")
    suspend fun sendMessage(@Body message: Message): MessageResponse
}
```

### 2. Implement Repository Pattern

```kotlin
// data/repository/LostItemRepository.kt
class LostItemRepository(private val apiService: LostLinkApiService) {
    suspend fun getItems(category: String? = null): Result<List<LostItem>> = try {
        Result.success(apiService.getAllItems(category))
    } catch (e: Exception) {
        Result.failure(e)
    }
    
    suspend fun getItemById(itemId: String): Result<LostItem> = try {
        // Fetch from API
        Result.success(LostItem(...))
    } catch (e: Exception) {
        Result.failure(e)
    }
}
```

### 3. Add ViewModels

```kotlin
// ui/screens/DashboardViewModel.kt
class DashboardViewModel(private val repository: LostItemRepository) : ViewModel() {
    private val _items = MutableStateFlow<List<LostItem>>(emptyList())
    val items: StateFlow<List<LostItem>> = _items.asStateFlow()
    
    fun loadItems(category: String? = null) {
        viewModelScope.launch {
            repository.getItems(category).onSuccess { itemList ->
                _items.value = itemList
            }
        }
    }
}
```

### 4. Firebase Integration

```kotlin
// Setup in build.gradle
plugins {
    id 'com.google.gms.google-services'
}

dependencies {
    implementation 'com.google.firebase:firebase-firestore-ktx'
    implementation 'com.google.firebase:firebase-messaging-ktx'
}
```

Add `google-services.json` to `app/` folder (from Firebase Console).

### 5. QR Code Integration

```kotlin
// Use ZXing for QR scanning
dependencies {
    implementation 'com.journeyapps:zxing-android-embedded:4.3.0'
}

// In QRVerificationScreen, integrate actual camera
IntentIntegrator(context)
    .setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
    .initiateScan()
```

## 🎨 Theme & Customization

### Colors (from CSS)

```kotlin
// ui/theme/Color.kt
val GreenPrimary = Color(0xFF16a34a)
val GreenAccent = Color(0xFF059669)
val GreenSuccess = Color(0xFF10b981)
val RedDanger = Color(0xFFef4444)
// ... 10 more colors
```

### Typography

- **Headlines**: Plus Jakarta Sans
- **Body**: Inter
- **Sizes**: 12sp to 32sp following Material Design 3 scale

## 📊 Data Models

All models defined in **ExtendedModels.kt**:

- `User` - User profile with role (STUDENT/STAFF/ADMIN)
- `LostItem`, `FoundItem` - Item listings
- `Claim` - Claim with proof documents
- `Message`, `Conversation` - Messaging
- `Notification` - Push notifications
- `CampusLocation` - Map locations
- `AdminAction`, `AuditLog` - Admin features

## 🔒 Security Considerations

1. **Authentication**: Store JWT tokens in secure storage (EncryptedSharedPreferences)
2. **API Security**: Use HTTPS with certificate pinning
3. **Data Privacy**: Encrypt sensitive data in transit
4. **Access Control**: Enforce role-based access (STUDENT/STAFF/ADMIN)
5. **Image Handling**: Validate and scan uploaded images for malware

## 📝 Testing

### Unit Tests (Recommended)

```kotlin
// test/kotlin/com/lostlink/app/data/repository/ItemRepositoryTest.kt
@RunWith(RobolectricTestRunner::class)
class ItemRepositoryTest {
    private lateinit var repository: ItemRepository
    
    @Test
    fun testGetItems_Success() = runTest {
        val result = repository.getItems()
        assertTrue(result.isSuccess)
    }
}
```

### UI Tests (Recommended)

```kotlin
// androidTest/kotlin/com/lostlink/app/ui/LoginScreenTest.kt
@RunWith(AndroidJUnit4::class)
class LoginScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()
    
    @Test
    fun testLoginButton_Click() {
        composeTestRule.setContent {
            LoginScreen(onLoginClick = {}, ...)
        }
        composeTestRule.onNodeWithText("Login").performClick()
    }
}
```

## 📚 Screen Details

### Screen 1: Login
- **Path**: `login`
- **Parameters**: None
- **Actions**: Email/password input, Sign up link, Forgot password link

### Screen 2: Dashboard
- **Path**: `dashboard`
- **Parameters**: None
- **Actions**: Browse items, filter by category, view details

### Screen 3: Claim Submission
- **Path**: `claim_submission/{itemId}`
- **Parameters**: itemId
- **Actions**: Select proof type, upload proof, submit claim

### Screen 4: QR Verification
- **Path**: `qr_verification/{claimId}`
- **Parameters**: claimId
- **Actions**: Scan QR, manual entry, verify

### Screen 5: Admin Dashboard
- **Path**: `admin_dashboard`
- **Parameters**: None
- **Actions**: View stats, manage claims, manage users

*See full navigation in Navigation.kt (20+ screens)*

## 🛠️ Troubleshooting

### Issue: Compose compilation errors
**Solution**: Update to latest Compose version (1.5.0+)

### Issue: Navigation not working
**Solution**: Ensure all Screen objects are properly defined in Navigation.kt

### Issue: Mock data not showing
**Solution**: Check MockDataRepository.kt has sample items

### Issue: Colors not applying
**Solution**: Verify Theme.kt is imported and applied to all screens

## 📞 Support & Development

### Next Development Priority

1. **Backend API Integration** - Critical for real data
2. **Firebase Setup** - Critical for notifications and database
3. **QR Code Implementation** - Important for handover
4. **AI Matching** - Enhancement feature
5. **Admin Workflows** - Important for moderation

### Mocking vs. Reality

Currently using MockDataRepository for offline testing. To switch to real backend:

1. Replace MockDataRepository with actual repository using ApiService
2. Add Retrofit ApiService implementation
3. Update ViewModel to call repository instead of mock data
4. Add error handling and loading states

## 📄 File Checklist

- ✅ build.gradle (dependencies)
- ✅ Theme.kt (colors + typography)
- ✅ Models.kt (basic models)
- ✅ ExtendedModels.kt (25+ comprehensive models)
- ✅ MainApp.kt (NavHost + main composable)
- ✅ Navigation.kt (20+ routes + events)
- ✅ AuthScreens.kt (Login & Register)
- ✅ DashboardScreen.kt (item browsing)
- ✅ ClaimScreens.kt (claim workflow)
- ✅ QRAndMapScreens.kt (verification & map)
- ✅ ExtendedFeatureScreens.kt (messaging, saved items, admin)
- ✅ Components.kt (reusable UI elements)
- ✅ Colors.xml, Strings.xml, AndroidManifest.xml
- 📋 ApiService.kt (to be created)
- 📋 Repository.kt (to be created)
- 📋 ViewModels (to be created)

## 🎓 Learning Resources

- [Jetpack Compose Documentation](https://developer.android.com/jetpack/compose/documentation)
- [Material Design 3](https://m3.material.io/)
- [Android Navigation](https://developer.android.com/guide/navigation)
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)

---

**App Version**: 1.0.0  
**Last Updated**: $(date)  
**Status**: ✅ UI Complete | 🔄 Backend Integration Ready
