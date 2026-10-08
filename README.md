# Quiz

Timed trivia quiz game. Questions come from the Open Trivia Database,
best scores are stored locally.
Built with Kotlin Multiplatform and Compose Multiplatform (Android target).

<p>
  <img src="docs/start.png" width="23%" alt="Start" />
  <img src="docs/playing.png" width="23%" alt="Playing" />
  <img src="docs/finished.png" width="23%" alt="Finished" />
  <img src="docs/error.png" width="23%" alt="Error" />
</p>

## Features

- Questions loaded from the Open Trivia Database (no API key required)
- Four answer options per question and a per-question countdown timer
- Speed-based scoring
- Results screen with the best score
- Loading and error states with a Retry button
- Score history stored locally

## Tech stack

- Kotlin Multiplatform, Compose Multiplatform
- Ktor Client with kotlinx-serialization for networking
- SQLDelight for local score storage
- Koin for dependency injection
- Coroutines and StateFlow
- Unit tests for the reducer

## Architecture

Three layers, dependencies point inward:

- `domain`: models and rules (`Question`, `HighScore`, `QuizRules`)
- `data`: `TriviaApi` (Ktor), DTOs and mappers, `QuizRepository`,
  `HighScoreRepository` (SQLDelight)
- `presentation`: MVI. A sealed `QuizState` (Idle, Loading, Playing, Finished, Error),
  a sealed `QuizIntent`, a pure `QuizReducer` and a `QuizViewModel`.
  A router composable maps each state to a screen.

## Project structure

    shared/src/commonMain/kotlin/org/example/quiz/
    ├── domain/        # models and rules
    ├── data/          # API, repositories, database
    └── presentation/  # state, intents, reducer, ViewModel, UI screens

## Run

Open the project in Android Studio and run the `androidApp` configuration,
or build a debug APK:

    ./gradlew :androidApp:assembleDebug

Run unit tests:

    ./gradlew :shared:testAndroidHostTest

An internet connection is required to load questions.

## Status

- Android: tested on an emulator and a physical device.
- Not handled yet: screen rotation / configuration changes.
- Only Android is set up in this project.

## What I learned

- Building a network layer with Ktor and mapping DTOs to domain models
- Modeling screen phases as a sealed state and reducing intents to new states
- Wiring Koin across common and platform-specific modules
- Writing my first unit tests
- Debugging real build issues: mismatched dependency versions and a missing
  INTERNET permission