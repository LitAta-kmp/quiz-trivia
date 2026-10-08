package org.example.quiz.presentation

import org.example.quiz.domain.Question

sealed interface QuizState {
    data object Idle : QuizState   // ← новое: приветственный экран, ничего не загружено

    data object Loading : QuizState

    data class Playing(
        val currentQuestion: Question,
        val questionNumber: Int,
        val totalQuestions: Int,
        val timeLeftSeconds: Int,
        val score: Int
    ) : QuizState

    data class Finished(
        val finalScore: Int,
        val totalQuestions: Int,
        val bestScore: Int? = null
    ) : QuizState

    data class Error(val message: String) : QuizState
}