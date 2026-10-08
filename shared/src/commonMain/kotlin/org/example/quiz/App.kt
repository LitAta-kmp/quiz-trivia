package org.example.quiz

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import org.example.quiz.presentation.QuizViewModel
import org.example.quiz.presentation.ui.QuizScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {
    MaterialTheme {
        val viewModel: QuizViewModel = koinViewModel()
        QuizScreen(viewModel)
    }
}