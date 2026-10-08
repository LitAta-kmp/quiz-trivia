package org.example.quiz.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// data layer
@Serializable
data class TriviaResponseDto(
    @SerialName("response_code") val responseCode: Int,
    @SerialName("results") val results: List<QuestionDto>
)