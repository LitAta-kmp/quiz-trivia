package org.example.quiz.domain

// domain layer
data class Question(
    val text: String,
    val answers: List<String>,   // уже перемешанные варианты
    val correctAnswerIndex: Int  // индекс правильного ответа в answers
)