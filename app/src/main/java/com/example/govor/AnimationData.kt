package com.example.govor

data class AnimationData(
    val animationFile: String,
    val audioFile: Int = 0, // Podrazumevani resurs je 0
    val description: String,
    val soundEffectFile: Int = 0 // Podrazumevani efekat je 0

)


