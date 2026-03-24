package com.lostlink.app.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface GitHubModelsApi {
    @POST("chat/completions")
    suspend fun getChatCompletions(
        @Header("Authorization") token: String,
        @Body request: ChatCompletionRequest
    ): ChatCompletionResponse

    companion object {
        private const val BASE_URL = "https://models.inference.ai.azure.com/"

        fun create(): GitHubModelsApi {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GitHubModelsApi::class.java)
        }
    }
}

data class ChatCompletionRequest(
    val messages: List<ChatMessage>,
    val model: String = "gpt-4o",
    val temperature: Double = 0.7,
    val max_tokens: Int = 1000
)

data class ChatMessage(
    val role: String,
    val content: String
)

data class ChatCompletionResponse(
    val choices: List<Choice>,
    val usage: Usage
)

data class Choice(
    val message: ChatMessage,
    val finish_reason: String
)

data class Usage(
    val prompt_tokens: Int,
    val completion_tokens: Int,
    val total_tokens: Int
)
