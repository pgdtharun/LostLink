package com.lostlink.app.ui.navigation

sealed class Screen(val route: String) {
    // ==================== Authentication ====================
    object Login : Screen("login")
    object Register : Screen("register")
    
    // ==================== Main Navigation ====================
    object Home : Screen("home")
    object Dashboard : Screen("dashboard")
    object Report : Screen("report")
    
    // ==================== Item Details ====================
    object ItemDetail : Screen("item_detail/{itemId}") {
        fun createRoute(itemId: String) = "item_detail/$itemId"
    }
    
    // ==================== My Items ====================
    object MyLostItems : Screen("my_lost_items")
    object MyFoundItems : Screen("my_found_items")
    
    // ==================== Claims Workflow ====================
    object ClaimSubmission : Screen("claim_submission/{itemId}") {
        fun createRoute(itemId: String) = "claim_submission/$itemId"
    }
    object ClaimStatus : Screen("claim_status/{claimId}") {
        fun createRoute(claimId: String) = "claim_status/$claimId"
    }
    
    // ==================== QR & Verification ====================
    object QRVerification : Screen("qr_verification/{claimId}") {
        fun createRoute(claimId: String) = "qr_verification/$claimId"
    }
    object VerificationSuccess : Screen("verification_success")
    
    // ==================== Features ====================
    object CampusMap : Screen("campus_map")
    object SavedItems : Screen("saved_items")
    object Messaging : Screen("messaging")
    object ChatDetail : Screen("chat_detail/{conversationId}/{itemId}") {
        fun createRoute(conversationId: String, itemId: String = "none") = "chat_detail/$conversationId/$itemId"
    }
    object AIMatches : Screen("ai_matches/{itemId}") {
        fun createRoute(itemId: String) = "ai_matches/$itemId"
    }
    object AIChat : Screen("ai_chat")
    object AISupport : Screen("ai_support")
    
    // ==================== Profile & Admin ====================
    object Profile : Screen("profile")
    object Settings : Screen("settings")
    object AdminDashboard : Screen("admin_dashboard")
    object Policies : Screen("policies")
    object Labs : Screen("labs")
    
    // ==================== Other ====================
    object Messages : Screen("messages")
    object About : Screen("about")
    
    companion object {
        val allRoutes: List<String> = listOf(
            "login", "register", "home", "dashboard", "report",
            "item_detail/{itemId}", "my_lost_items", "my_found_items",
            "claim_submission/{itemId}", "claim_status/{claimId}",
            "qr_verification/{claimId}", "verification_success",
            "campus_map", "saved_items", "messaging", "chat_detail/{conversationId}/{itemId}",
            "profile", "admin_dashboard", "policies", "labs", "messages", "about",
            "ai_matches/{itemId}", "ai_chat", "ai_support"
        )
    }
}

sealed class NavigationEvent {
    // Authentication
    object NavigateToLogin : NavigationEvent()
    object NavigateToRegister : NavigationEvent()
    
    // Main screens
    object NavigateToHome : NavigationEvent()
    object NavigateToDashboard : NavigationEvent()
    object NavigateToReport : NavigationEvent()
    
    // Item management
    data class NavigateToItemDetail(val itemId: String) : NavigationEvent()
    object NavigateToMyLostItems : NavigationEvent()
    object NavigateToMyFoundItems : NavigationEvent()
    
    // Claim workflow
    data class NavigateToClaimSubmission(val itemId: String) : NavigationEvent()
    data class NavigateToClaimStatus(val claimId: String) : NavigationEvent()
    
    // QR verification
    data class NavigateToQRVerification(val claimId: String) : NavigationEvent()
    object NavigateToVerificationSuccess : NavigationEvent()
    
    // Features
    object NavigateToCampusMap : NavigationEvent()
    object NavigateToSavedItems : NavigationEvent()
    object NavigateToMessaging : NavigationEvent()
    data class NavigateToChatDetail(val conversationId: String) : NavigationEvent()
    
    // User
    object NavigateToProfile : NavigationEvent()
    object NavigateToAdminDashboard : NavigationEvent()
    
    // Other
    object NavigateToAbout : NavigationEvent()
    object NavigateBack : NavigationEvent()
    object NavigateUp : NavigationEvent()
}
