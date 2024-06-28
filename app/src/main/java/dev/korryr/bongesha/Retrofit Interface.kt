package dev.korryr.bongesha

import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface OpenAIApi {
    @Headers("Content-Type: application/json", "Authorization: Bearer YOUR_API_KEY")
    @POST("v1/engines/davinci-codex/completions")
    fun getCompletion(@Body request: OpenAIRequest): Call<OpenAIResponse>
}

data class OpenAIRequest(val prompt: String, val max_tokens: Int)
data class OpenAIResponse(val choices: List<Choice>)
data class Choice(val text: String)

object RetrofitInstance {
    val api: OpenAIApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.openai.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenAIApi::class.java)
    }
}
