package com.example.govor

import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.GridView
import java.io.File


class LettersActivity : AppCompatActivity() {
    private lateinit var gridView: GridView
    private lateinit var descriptionText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_letters)

        gridView = findViewById(R.id.gridView)
        descriptionText = findViewById(R.id.descriptionText)
        val startButton: Button = findViewById(R.id.startButton)

        // Definisanje niza animacija
        val letters = arrayOf("slovo_ch", "slovo_ci", "slovo_dj", "slovo_s", "slovo_sh", "slovo_z")
        val adapter = LetterAdapter(this, letters)  // Sada prosleđujemo i 'letters' niz
        gridView.adapter = adapter

        gridView.setOnItemClickListener { parent, view, position, id ->
            descriptionText.text = "Vežbajte izgovor slova ${letters[position].split('_')[1]}"
        }

        startButton.setOnClickListener {
            // Implementacija starta SlovaActivity sa izabranim slovom
        }
    }
}