package org.example.quiz

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform