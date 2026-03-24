package com.lostlink.app.data.model

import java.util.Date

// ==================== User & Authentication ====================
data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val avatarUrl: String? = null,
    val role: UserRole = UserRole.STUDENT,
    val idNumber: String = "",
    val joinDate: Date = Date(),
    val isVerified: Boolean = false
)

enum class UserRole {
    STUDENT, STAFF, ADMIN
}

data class LoginRequest(
    val email: String = "",
    val password: String = ""
)

data class RegisterRequest(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val phone: String = "",
    val idNumber: String = "",
    val role: UserRole = UserRole.STUDENT
)

data class AuthResponse(
    val token: String = "",
    val user: User = User(),
    val refreshToken: String = ""
)

// ==================== Lost & Found Items ====================
data class LostItem(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val category: ItemCategory = ItemCategory.OTHER,
    val location: String = "",
    val dateReported: Date = Date(),
    val dateFound: Date? = null,
    val images: List<String> = emptyList(),
    val status: ItemStatus = ItemStatus.ACTIVE,
    val reporterId: String = "",
    val reporterName: String = "",
    val contactInfo: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null,
    val isSavedBy: List<String> = emptyList()
)

data class FoundItem(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val category: ItemCategory = ItemCategory.OTHER,
    val location: String = "",
    val dateFound: Date = Date(),
    val images: List<String> = emptyList(),
    val status: ItemStatus = ItemStatus.ACTIVE,
    val finderId: String = "",
    val finderName: String = "",
    val contactInfo: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null,
    val isSavedBy: List<String> = emptyList()
)

enum class ItemCategory {
    BAGS, BOOKS, ELECTRONICS, ID_CARDS, KEYS, CLOTHING, WALLETS, JEWELRY, DOCUMENTS, OTHER
}

enum class ItemStatus {
    ACTIVE, CLAIMED, VERIFIED_HANDOVER, CLOSED, EXPIRED
}

// ==================== AI Matching ====================
data class MatchSuggestion(
    val id: String = "",
    val lostItemId: String = "",
    val foundItemId: String = "",
    val matchScore: Float = 0f,
    val matchReason: String = "",
    val suggestedAt: Date = Date(),
    val imageComparison: ImageComparison? = null,
    val status: MatchStatus = MatchStatus.PENDING
)

data class ImageComparison(
    val lostImageUrl: String = "",
    val foundImageUrl: String = "",
    val similarity: Float = 0f
)

enum class MatchStatus {
    PENDING, ACCEPTED, REJECTED, VERIFIED
}

// ==================== Claims ====================
data class Claim(
    val id: String = "",
    val claimerId: String = "",
    val claimerName: String = "",
    val itemId: String = "",
    val itemType: String = "", // "LOST" or "FOUND"
    val proofDocuments: List<ProofDocument> = emptyList(),
    val status: ClaimStatus = ClaimStatus.PENDING,
    val submittedAt: Date = Date(),
    val approvedAt: Date? = null,
    val approvedBy: String? = null,
    val rejectionReason: String? = null,
    val verificationDetails: VerificationDetails? = null
)

data class ProofDocument(
    val id: String = "",
    val type: ProofType = ProofType.TEXT_DESCRIPTION,
    val content: String = "", // Text, image URL, or identifier
    val uploadedAt: Date = Date()
)

enum class ProofType {
    TEXT_DESCRIPTION, IMAGE, ID_NUMBER, SERIAL_NUMBER, OTHER_IDENTIFIER
}

enum class ClaimStatus {
    PENDING, APPROVED, REJECTED, VERIFIED_HANDOVER, COMPLETED
}

data class VerificationDetails(
    val qrToken: String = "",
    val qrScannedAt: Date? = null,
    val scannedBy: String? = null,
    val handoverLocation: String? = null,
    val receiverSignature: String? = null
)

// ==================== Notifications ====================
data class Notification(
    val id: String = "",
    val userId: String = "",
    val title: String = "",
    val message: String = "",
    val type: NotificationType = NotificationType.ADMIN_ACTION,
    val relatedItemId: String? = null,
    val relatedClaimId: String? = null,
    val createdAt: Date = Date(),
    val isRead: Boolean = false,
    val actionUrl: String? = null
)

enum class NotificationType {
    MATCH_FOUND, CLAIM_STATUS_CHANGED, ADMIN_ACTION, HANDOVER_READY, MESSAGE_RECEIVED, ITEM_CLAIMED
}

// ==================== Messages & Chat ====================
data class Message(
    val id: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val receiverId: String = "",
    val content: String = "",
    val timestamp: Date = Date(),
    val itemId: String? = null,
    val claimId: String? = null,
    val isRead: Boolean = false,
    val attachments: List<String> = emptyList()
)

data class Conversation(
    val id: String = "",
    val participantIds: List<String> = emptyList(),
    val lastMessage: Message? = null,
    val updatedAt: Date = Date()
)

// ==================== Campus Map ====================
data class CampusLocation(
    val id: String = "",
    val name: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val description: String = "",
    val type: LocationType = LocationType.COMMON_AREA
)

enum class LocationType {
    SECURITY_OFFICE, LOST_AND_FOUND_COUNTER, ADMIN_OFFICE, COMMON_AREA, ACADEMIC_BLOCK, CAFETERIA
}

data class ItemLocation(
    val itemId: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val mapLocationName: String = "",
    val reportedAt: Date = Date()
)

// ==================== Admin ====================
data class AdminAction(
    val id: String = "",
    val adminId: String = "",
    val actionType: AdminActionType = AdminActionType.AUDIT_LOG,
    val targetId: String = "", // user ID, item ID, or claim ID
    val details: String = "",
    val timestamp: Date = Date(),
    val reason: String? = null
)

enum class AdminActionType {
    USER_VERIFIED, USER_SUSPENDED, CLAIM_APPROVED, CLAIM_REJECTED, ITEM_CLOSED, AUDIT_LOG
}

data class AuditLog(
    val id: String = "",
    val actionId: String = "",
    val userId: String = "",
    val action: String = "",
    val timestamp: Date = Date(),
    val details: String = ""
)

// ==================== Reports & Statistics ====================
data class ReportStatistics(
    val totalItems: Int = 0,
    val totalClaims: Int = 0,
    val successfulHandovers: Int = 0,
    val pendingClaims: Int = 0,
    val categoryBreakdown: Map<ItemCategory, Int> = emptyMap(),
    val generatedAt: Date = Date()
)

// ==================== Saved Items ====================
data class SavedItem(
    val id: String = "",
    val userId: String = "",
    val itemId: String = "",
    val itemType: String = "", // "LOST" or "FOUND"
    val savedAt: Date = Date()
)
