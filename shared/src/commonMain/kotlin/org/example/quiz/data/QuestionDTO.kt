package org.example.quiz.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.example.quiz.domain.Question

// data layer
@Serializable
data class QuestionDto(
    @SerialName("question") val question: String,
    @SerialName("correct_answer") val correctAnswer: String,
    @SerialName("incorrect_answers") val incorrectAnswers: List<String>
)

// data layer — маленькая утилита рядом с мэппером
fun String.unescapeHtml(): String {
    return this
        .replace("&quot;", "\"")
        .replace("&#039;", "'")
        .replace("&lt;", "<")
        .replace("&gt;", ">")
        .replace("&amp;", "&")
}

// data layer (mapper — граница между data и domain)
fun QuestionDto.toDomain(): Question {
    val allAnswers = (incorrectAnswers + correctAnswer)
        .map { it.unescapeHtml() }
        .shuffled()

    val correctIndex = allAnswers.indexOf(correctAnswer.unescapeHtml())

    return Question(
        text = question.unescapeHtml(),
        answers = allAnswers,
        correctAnswerIndex = correctIndex
    )
}