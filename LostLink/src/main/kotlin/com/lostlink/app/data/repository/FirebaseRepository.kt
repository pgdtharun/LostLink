package com.lostlink.app.data.repository

import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.Firebase
import com.lostlink.app.data.model.*
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.tasks.await
import java.util.Date

object FirebaseRepository {
    private val auth = Firebase.auth
    private val firestore = Firebase.firestore
    private const val TIMEOUT = 10000L // 10 seconds

    // ==================== User Profile ====================
    suspend fun saveUserProfile(user: User) {
        val uid = auth.currentUser?.uid ?: return
        withTimeout(TIMEOUT) {
            firestore.collection("users").document(uid).set(user).await()
        }
    }

    suspend fun getUserProfile(): User? {
        val uid = auth.currentUser?.uid ?: return null
        return try {
            withTimeout(TIMEOUT) {
                firestore.collection("users").document(uid).get().await().toObject(User::class.java)
            }
        } catch (e: Exception) {
            null
        }
    }

    // ==================== Items = Lost / Found ====================
    suspend fun reportLostItem(item: LostItem) {
        val uid = auth.currentUser?.uid ?: return
        val docRef = firestore.collection("lost_items").document()
        val newItem = item.copy(id = docRef.id, reporterId = uid)
        withTimeout(TIMEOUT) {
            docRef.set(newItem).await()
        }
    }

    suspend fun reportFoundItem(item: FoundItem) {
        val uid = auth.currentUser?.uid ?: return
        val docRef = firestore.collection("found_items").document()
        val newItem = item.copy(id = docRef.id, finderId = uid)
        withTimeout(TIMEOUT) {
            docRef.set(newItem).await()
        }
    }

    suspend fun getAllItems(): List<Any> {
        return try {
            val lostItems = withTimeout(TIMEOUT) {
                firestore.collection("lost_items").get().await().toObjects(LostItem::class.java)
            }
            val foundItems = withTimeout(TIMEOUT) {
                firestore.collection("found_items").get().await().toObjects(FoundItem::class.java)
            }
            (lostItems + foundItems).sortedByDescending { 
                when (it) {
                    is LostItem -> it.dateReported
                    is FoundItem -> it.dateFound
                    else -> Date(0)
                }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getItemById(itemId: String): Any? {
        return try {
            val lostDoc = firestore.collection("lost_items").document(itemId).get().await()
            if (lostDoc.exists()) return lostDoc.toObject(LostItem::class.java)
            
            val foundDoc = firestore.collection("found_items").document(itemId).get().await()
            if (foundDoc.exists()) return foundDoc.toObject(FoundItem::class.java)
            
            null
        } catch (e: Exception) {
            null
        }
    }

    // ==================== Claims ====================
    suspend fun submitClaim(claim: Claim) {
        val docRef = firestore.collection("claims").document()
        val newClaim = claim.copy(id = docRef.id)
        docRef.set(newClaim).await()
    }

    suspend fun getClaimsForItem(itemId: String): List<Claim> {
        return try {
            firestore.collection("claims")
                .whereEqualTo("itemId", itemId)
                .get().await()
                .toObjects(Claim::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun updateItemStatus(itemId: String, newStatus: ItemStatus) {
        try {
            val lostDoc = firestore.collection("lost_items").document(itemId).get().await()
            if (lostDoc.exists()) {
                firestore.collection("lost_items").document(itemId).update("status", newStatus).await()
                return
            }
            
            val foundDoc = firestore.collection("found_items").document(itemId).get().await()
            if (foundDoc.exists()) {
                firestore.collection("found_items").document(itemId).update("status", newStatus).await()
                return
            }
        } catch (e: Exception) {
            // Log or handle error
        }
    }
}
