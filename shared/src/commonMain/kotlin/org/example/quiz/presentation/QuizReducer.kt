package org.example.quiz.presentation

import org.example.quiz.domain.QuizRules

// presentation layer
object QuizReducer {

    fun reduce(state: QuizState, intent: QuizIntent): QuizState = when (intent) {
        is QuizIntent.StartQuiz -> QuizState.Loading
        is QuizIntent.QuestionsLoaded -> handleQuestionsLoaded(intent)
        is QuizIntent.QuestionsLoadFailed -> QuizState.Error(intent.message)
        is QuizIntent.AnswerSelected -> handleAnswerSelected(state, intent)
        is QuizIntent.TimerTick -> handleTimerTick(state)
        is QuizIntent.Restart -> QuizState.Loading
        is QuizIntent.HighScoreLoaded -> handleHighScoreLoaded(state, intent)
    }

    private fun handleQuestionsLoaded(intent: QuizIntent.QuestionsLoaded): QuizState {
        return QuizState.Playing(
            currentQuestion = intent.firstQuestion,
            questionNumber = 1,
            totalQuestions = intent.totalQuestions,
            timeLeftSeconds = QuizRules.SECONDS_PER_QUESTION,
            score = 0
        )
    }

    private fun handleAnswerSelected(
        state: QuizState,
        intent: QuizIntent.AnswerSelected
    ): QuizState {
        if (state !is QuizState.Playing) return state

        val isCorrect = intent.answerIndex == state.currentQuestion.correctAnswerIndex
        val newScore = if (isCorrect) state.score + 1 else state.score

        return if (intent.nextQuestion == null) {
            // nextQuestion == null — сигнал "вопросы кончились", решение об этом
            // принимает reducer, а не ViewModel
            QuizState.Finished(finalScore = newScore, totalQuestions = state.totalQuestions)
        } else {
            QuizState.Playing(
                currentQuestion = intent.nextQuestion,
                questionNumber = state.questionNumber + 1,
                totalQuestions = state.totalQuestions,
                timeLeftSeconds = QuizRules.SECONDS_PER_QUESTION,
                score = newScore
            )
        }
    }

    private fun handleTimerTick(state: QuizState): QuizState {
        if (state !is QuizState.Playing) return state
        return state.copy(timeLeftSeconds = state.timeLeftSeconds - 1)
    }

    private fun handleHighScoreLoaded(state: QuizState, intent: QuizIntent.HighScoreLoaded): QuizState {
        if (state !is QuizState.Finished) return state   // тот же защитный паттерн, что и везде
        return state.copy(bestScore = intent.bestScore)
    }
}