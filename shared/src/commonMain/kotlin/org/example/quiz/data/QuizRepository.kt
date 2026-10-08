package org.example.quiz.data

import org.example.quiz.domain.Question

// data layer
class QuizRepository(private val api: TriviaApi) {

    suspend fun getQuestions(amount: Int = 10): Result<List<Question>> {
        return try {
            val response = api.fetchQuestions(amount)
            val questions = response.results.map { it.toDomain() }
            Result.success(questions)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}