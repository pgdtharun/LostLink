package com.lostlink.app.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.lostlink.app.data.api.GitHubModelsApi
import com.lostlink.app.data.api.ChatCompletionRequest
import com.lostlink.app.data.api.ChatMessage
import com.lostlink.app.data.model.*
import com.lostlink.app.data.repository.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

class AIViewModel(application: Application) : AndroidViewModel(application) {
    private val api = GitHubModelsApi.create()
    private val prefs = application.getSharedPreferences("lostlink_prefs", Context.MODE_PRIVATE)

    private val _githubToken = MutableStateFlow(prefs.getString("github_token", "") ?: "")
    val githubToken: StateFlow<String> = _githubToken

    private val _isMatching = MutableStateFlow(false)
    val isMatching: StateFlow<Boolean> = _isMatching

    private val _matchSuggestions = MutableStateFlow<List<MatchSuggestion>>(emptyList())
    val matchSuggestions: StateFlow<List<MatchSuggestion>> = _matchSuggestions

    private val _suggestedReplies = MutableStateFlow<List<String>>(emptyList())
    val suggestedReplies: StateFlow<List<String>> = _suggestedReplies

    private val _lastSystemResponse = MutableStateFlow<String?>(null)
    val lastSystemResponse: StateFlow<String?> = _lastSystemResponse

    private val _supportChatMessages = MutableStateFlow<List<String>>(listOf("System: Hello! I am your LostLink AI Support assistant. How can I help you today?"))
    val supportChatMessages: StateFlow<List<String>> = _supportChatMessages

    private val _isSupportTyping = MutableStateFlow(false)
    val isSupportTyping: StateFlow<Boolean> = _isSupportTyping

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun saveToken(token: String) {
        prefs.edit().putString("github_token", token).apply()
        _githubToken.value = token
    }

    fun fetchSuggestedReplies(itemId: String) {
        val token = _githubToken.value
        if (token.isBlank()) {
            _suggestedReplies.value = listOf("I found it", "Where to meet?", "Is this yours?")
            return
        }

        viewModelScope.launch {
            try {
                val item = FirebaseRepository.getItemById(itemId) ?: return@launch
                val desc = when (item) {
                    is LostItem -> "LOST: ${item.title}. ${item.description}"
                    is FoundItem -> "FOUND: ${item.title}. ${item.description}"
                    else -> ""
                }

                val response = api.getChatCompletions(
                    "Bearer $token",
                    ChatCompletionRequest(
                        messages = listOf(
                            ChatMessage("system", "You are an AI assistant for a Lost and Found app. Suggest 3 unique, short, and helpful quick reply messages for a user who is inquiring about this item. Return ONLY a JSON array of strings like [\"Reply 1\", \"Reply 2\", \"Reply 3\"]."),
                            ChatMessage("user", "Item details: $desc")
                        ),
                        max_tokens = 100
                    )
                )

                val resultJson = response.choices.firstOrNull()?.message?.content ?: "[]"
                val cleanJson = if (resultJson.contains("[")) {
                    "[" + resultJson.substringAfter("[").substringBeforeLast("]") + "]"
                } else {
                    "[]"
                }
                
                val array = JSONArray(cleanJson)
                val replies = mutableListOf<String>()
                for (i in 0 until array.length()) {
                    replies.add(array.getString(i))
                }
                _suggestedReplies.value = if (replies.isEmpty()) listOf("I found it", "Where to meet?", "Is this yours?") else replies
            } catch (e: Exception) {
                _suggestedReplies.value = listOf("I found it", "Where to meet?", "Is this yours?")
            }
        }
    }

    fun generateSystemResponse(userMessage: String, itemId: String) {
        val token = _githubToken.value
        if (token.isBlank()) {
            _lastSystemResponse.value = "Thank you for the update! We will notify the other user."
            return
        }

        viewModelScope.launch {
            try {
                val item = FirebaseRepository.getItemById(itemId)
                val itemDesc = when (item) {
                    is LostItem -> "LOST: ${item.title}"
                    is FoundItem -> "FOUND: ${item.title}"
                    else -> "an item"
                }

                val response = api.getChatCompletions(
                    "Bearer $token",
                    ChatCompletionRequest(
                        messages = listOf(
                            ChatMessage("system", "You are an automated assistant for a Lost and Found app. A user just sent a message regarding $itemDesc. Provide a very short, unique, helpful, and polite response (max 15 words)."),
                            ChatMessage("user", userMessage)
                        ),
                        max_tokens = 50
                    )
                )
                _lastSystemResponse.value = response.choices.firstOrNull()?.message?.content ?: "Thank you for the update!"
            } catch (e: Exception) {
                _lastSystemResponse.value = "Thank you for the update!"
            }
        }
    }

    fun sendSupportMessage(message: String) {
        val token = _githubToken.value
        _supportChatMessages.value = _supportChatMessages.value + "You: $message"
        
        if (token.isBlank()) {
            _supportChatMessages.value = _supportChatMessages.value + "System: Please configure your GitHub Token in Settings to enable AI support chat."
            return
        }

        viewModelScope.launch {
            _isSupportTyping.value = true
            try {
                val response = api.getChatCompletions(
                    "Bearer $token",
                    ChatCompletionRequest(
                        messages = listOf(
                            ChatMessage("system", "You are a helpful support assistant for 'LostLink', a campus lost and found app for NSBM Green University. Answer questions about how to report items, find items, and verify handovers. Keep responses helpful and concise."),
                            ChatMessage("user", message)
                        )
                    )
                )
                val botReply = response.choices.firstOrNull()?.message?.content ?: "I'm sorry, I couldn't process that."
                _supportChatMessages.value = _supportChatMessages.value + "System: $botReply"
            } catch (e: Exception) {
                _supportChatMessages.value = _supportChatMessages.value + "System: Sorry, I'm having trouble connecting to the AI service right now."
            } finally {
                _isSupportTyping.value = false
            }
        }
    }

    fun findMatches(currentItem: Any) {
        val token = _githubToken.value
        if (token.isBlank()) {
            _error.value = "GitHub Token is required for AI features"
            return
        }

        viewModelScope.launch {
            _isMatching.value = true
            _error.value = null
            try {
                val allItems = FirebaseRepository.getAllItems()
                val currentId = when (currentItem) {
                    is LostItem -> currentItem.id
                    is FoundItem -> currentItem.id
                    else -> ""
                }
                
                val candidates = allItems.filter { 
                    when (it) {
                        is LostItem -> currentItem is FoundItem && it.category == currentItem.category && it.id != currentId
                        is FoundItem -> currentItem is LostItem && it.category == currentItem.category && it.id != currentId
                        else -> false
                    }
                }

                if (candidates.isEmpty()) {
                    _matchSuggestions.value = emptyList()
                    _isMatching.value = false
                    return@launch
                }

                val prompt = buildMatchPrompt(currentItem, candidates)
                val response = api.getChatCompletions(
                    "Bearer $token",
                    ChatCompletionRequest(
                        messages = listOf(
                            ChatMessage("system", "You are an AI assistant for a Lost and Found app. Compare the current item with candidates and return a JSON array of matches with score (0-1) and reason. Format: [{\"id\": \"item_id\", \"score\": 0.9, \"reason\": \"matching color and brand\"}]"),
                            ChatMessage("user", prompt)
                        )
                    )
                )

                val resultJson = response.choices.firstOrNull()?.message?.content ?: "[]"
                // Extract JSON if model wrapped it in code blocks
                val cleanJson = resultJson.substringAfter("[").substringBeforeLast("]").let { "[$it]" }
                
                val suggestions = parseSuggestions(cleanJson, currentItem)
                _matchSuggestions.value = suggestions.sortedByDescending { it.matchScore }

            } catch (e: Exception) {
                _error.value = "AI Matching failed: ${e.message}"
            } finally {
                _isMatching.value = false
            }
        }
    }

    private fun buildMatchPrompt(currentItem: Any, candidates: List<Any>): String {
        val currentDesc = when (currentItem) {
            is LostItem -> "LOST: ${currentItem.title}. Description: ${currentItem.description}. Location: ${currentItem.location}"
            is FoundItem -> "FOUND: ${currentItem.title}. Description: ${currentItem.description}. Location: ${currentItem.location}"
            else -> ""
        }

        val candidatesDesc = candidates.joinToString("\n") { 
            when (it) {
                is LostItem -> "- ID: ${it.id}, LOST: ${it.title}, Desc: ${it.description}, Location: ${it.location}"
                is FoundItem -> "- ID: ${it.id}, FOUND: ${it.title}, Desc: ${it.description}, Location: ${it.location}"
                else -> ""
            }
        }

        return "Current Item:\n$currentDesc\n\nCandidates:\n$candidatesDesc"
    }

    private fun parseSuggestions(json: String, currentItem: Any): List<MatchSuggestion> {
        val suggestions = mutableListOf<MatchSuggestion>()
        try {
            val array = JSONArray(json)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                val id = obj.getString("id")
                val score = obj.getDouble("score").toFloat()
                val reason = obj.getString("reason")

                val lostId = if (currentItem is LostItem) currentItem.id else id
                val foundId = if (currentItem is FoundItem) currentItem.id else id

                suggestions.add(MatchSuggestion(
                    id = "match_$i",
                    lostItemId = lostId,
                    foundItemId = foundId,
                    matchScore = score,
                    matchReason = reason
                ))
            }
        } catch (e: Exception) {
            // Log error
        }
        return suggestions
    }
}
