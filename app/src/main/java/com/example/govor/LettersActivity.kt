package com.example.govor

import android.os.Bundle
import android.widget.Button
import android.widget.GridView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


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