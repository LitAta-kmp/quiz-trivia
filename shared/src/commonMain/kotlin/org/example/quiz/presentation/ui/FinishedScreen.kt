package org.example.quiz.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.example.quiz.presentation.QuizState

@Composable
fun FinishedScreen(
    state: QuizState.Finished,
    onRestart: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Quiz finished!")
        Text("Your score: ${state.finalScore} / ${state.totalQuestions}")

        when (val best = state.bestScore) {
            null -> Text("Loading best score...")
            state.finalScore -> Text("New best score!")   // текущий счёт == рекорд ⇒ мы его и поставили
            else -> Text("Best score: $best")
        }

        Button(onClick = onRestart) {
            Text("Restart")
        }
    }
}