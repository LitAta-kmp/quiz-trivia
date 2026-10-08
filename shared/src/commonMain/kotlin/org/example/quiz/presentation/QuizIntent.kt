package org.example.quiz.presentation

import org.example.quiz.domain.Question

sealed interface QuizIntent {
    data object StartQuiz : QuizIntent
    data class QuestionsLoaded(val firstQuestion: Question, val totalQuestions: Int) : QuizIntent

    data class QuestionsLoadFailed(val message: String) : QuizIntent
    data class AnswerSelected(val answerIndex: Int, val nextQuestion: Question?) : QuizIntent
    data object TimerTick : QuizIntent
    data object Restart : QuizIntent
    data class HighScoreLoaded(val bestScore: Int) : QuizIntent   // ← новое
}