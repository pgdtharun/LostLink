package com.lostlink.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lostlink.app.data.model.*
import com.lostlink.app.data.repository.FirebaseRepository
import com.lostlink.app.data.repository.MockDataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date

class FirebaseViewModel : ViewModel() {
    private val _items = MutableStateFlow<List<Any>>(emptyList())
    val items = _items.asStateFlow()

    private val _userProfile = MutableStateFlow<User?>(null)
    val userProfile = _userProfile.asStateFlow()

    private val _selectedItem = MutableStateFlow<Any?>(null)
    val selectedItem = _selectedItem.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        fetchItems()
        fetchUserProfile()
    }

    fun fetchItems() {
        viewModelScope.launch {
            _isLoading.value = true
            // Primary: Fetch from Firebase
            val firebaseItems = FirebaseRepository.getAllItems()
            
            // If Firebase is empty, use professional mock data
            if (firebaseItems.isEmpty()) {
                _items.value = MockDataRepository.getLostandFoundItems()
            } else {
                _items.value = firebaseItems
            }
            _isLoading.value = false
        }
    }

    fun fetchUserProfile() {
        viewModelScope.launch {
            val profile = FirebaseRepository.getUserProfile()
            _userProfile.value = profile ?: MockDataRepository.getCurrentUser()
        }
    }

    fun fetchItemById(itemId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val item = FirebaseRepository.getItemById(itemId)
            if (item != null) {
                _selectedItem.value = item
            } else {
                // Check mock data
                _selectedItem.value = MockDataRepository.getLostandFoundItems().find { mockItem ->
                    when (mockItem) {
                        is LostItem -> mockItem.id == itemId
                        is FoundItem -> mockItem.id == itemId
                        else -> false
                    }
                }
            }
            _isLoading.value = false
        }
    }

    fun saveUserProfile(user: User) {
        viewModelScope.launch {
            FirebaseRepository.saveUserProfile(user)
            _userProfile.value = user
        }
    }

    fun reportLostItem(title: String, description: String, location: String, category: ItemCategory, contact: String) {
        viewModelScope.launch {
            val item = LostItem(
                id = "",
                title = title,
                description = description,
                category = category,
                location = location,
                dateReported = Date(),
                images = emptyList(),
                status = ItemStatus.ACTIVE,
                reporterId = "", 
                reporterName = _userProfile.value?.name ?: "Anonymous",
                contactInfo = contact
            )
            FirebaseRepository.reportLostItem(item)
            fetchItems()
        }
    }

    fun reportFoundItem(title: String, description: String, location: String, category: ItemCategory, contact: String) {
        viewModelScope.launch {
            val item = FoundItem(
                id = "",
                title = title,
                description = description,
                category = category,
                location = location,
                dateFound = Date(),
                images = emptyList(),
                status = ItemStatus.ACTIVE,
                finderId = "",
                finderName = _userProfile.value?.name ?: "Anonymous",
                contactInfo = contact
            )
            FirebaseRepository.reportFoundItem(item)
            fetchItems()
        }
    }

    fun updateItemStatus(itemId: String, newStatus: ItemStatus) {
        viewModelScope.launch {
            _isLoading.value = true
            FirebaseRepository.updateItemStatus(itemId, newStatus)
            _isLoading.value = false
            fetchItemById(itemId) // refresh selected item
            fetchItems() // refresh all
        }
    }
}
