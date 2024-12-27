package com.example.govor

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity



class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnStartGovorActivity = findViewById<Button>(R.id.btnStartGovorActivity)
        btnStartGovorActivity.setOnClickListener {
            val intent = Intent(this, GovorActivity::class.java)
            startActivity(intent)
        }
    }
}