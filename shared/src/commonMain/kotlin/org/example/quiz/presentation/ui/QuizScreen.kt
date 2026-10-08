package org.example.quiz.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.example.quiz.presentation.QuizState
import org.example.quiz.presentation.QuizViewModel

@Composable
fun QuizScreen(viewModel: QuizViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    when (val current = state) {
        is QuizState.Idle -> StartScreen(onStartClicked = viewModel::onStartQuiz)
        is QuizState.Loading -> LoadingScreen()
        is QuizState.Playing -> PlayingScreen(
            state = current,
            onAnswerSelected = viewModel::onAnswerSelected
        )
        is QuizState.Finished -> FinishedScreen(
            state = current,
            onRestart = viewModel::onRestart
        )
        is QuizState.Error -> ErrorScreen(              // ← новое
            state = current,
            onRetry = viewModel::onStartQuiz
        )
    }
}
