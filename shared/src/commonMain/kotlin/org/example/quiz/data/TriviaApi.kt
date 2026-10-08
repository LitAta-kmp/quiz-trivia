package org.example.quiz.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

// data layer
class TriviaApi(private val httpClient: HttpClient) {

    suspend fun fetchQuestions(amount: Int = 10): TriviaResponseDto {
        return httpClient
            .get("https://opentdb.com/api.php") {
                parameter("amount", amount)
                parameter("type", "multiple") // только вопросы с 4 вариантами ответа
            }
            .body()
    }

}