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
import java.io.File



class GovorActivity : AppCompatActivity() {

    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var textViewResult: TextView
    private lateinit var mediaPlayer: MediaPlayer
    private var mediaRecorder: MediaRecorder? = null
    private var audioPath: String? = null
    private var isRecording = false

    private lateinit var govoriButton: LottieAnimationView
    private lateinit var playButton: LottieAnimationView
    private lateinit var recButton: LottieAnimationView
    private lateinit var listenButton: LottieAnimationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_govor)

        mediaPlayer = MediaPlayer.create(this, R.raw.drvo) // For playButton
        initializeUI()
        setupButtonListeners()
        initializeSpeechRecognizer()
    }

    private fun initializeUI() {
        val lottieBackground: LottieAnimationView = findViewById(R.id.lottieBackground)
        lottieBackground.setAnimationFromUrl("https://lottie.host/553df60d-4abe-47cb-a1ec-cc599f4d37bc/ZY7BGFrDSm.lottie")
        lottieBackground.playAnimation()
        lottieBackground.repeatCount = LottieDrawable.INFINITE

        val lottieTree: LottieAnimationView = findViewById(R.id.lottieTree)
        lottieTree.setAnimation("drvo.lottie")
        lottieTree.playAnimation()

        govoriButton = findViewById(R.id.govorButton)
        playButton = findViewById(R.id.playButton)
        recButton = findViewById(R.id.recButton)
        listenButton = findViewById(R.id.listenButton)
        textViewResult = findViewById(R.id.textViewResult)

    }

    private fun setupButtonListeners() {
        playButton.setOnClickListener {
            playButton.playAnimation()
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()  // Pause the playback
                mediaPlayer.seekTo(0)  // Reset the playback to the start
            } else {
                mediaPlayer.start()  // Start the playback
            }
        }

        govoriButton.setOnClickListener {
            govoriButton.playAnimation()
            startSpeechRecognition()
        }

        recButton.setOnClickListener {
            if (isRecording) {
                stopRecording()
                recButton.playAnimation() // Promeni animaciju u 'stop' stanje
                isRecording = false
                // Omogući listenButton odmah nakon što se snimanje zaustavi
                listenButton.isEnabled = true

            } else {
                startRecording()
                recButton.playAnimation() // Promeni animaciju u 'recording' stanje
                isRecording = true
                listenButton.isEnabled = false  // Onemogući listenButton dok traje snimanje
                // Handler da ponovo omogući listenButton 4.8 sekunde nakon što snimanje započne
                Handler(mainLooper).postDelayed({
                    listenButton.isEnabled = true

                }, 4800)  // Odlaganje od 4.8 sekunde
            }
        }

        listenButton.setOnClickListener {
            if (listenButton.isEnabled) {
                listenButton.playAnimation()
                playRecordedAudio()
            }
        }
    }

    private fun startRecording() {
        audioPath = "${externalCacheDir?.absolutePath}/temp_audio.mp4"  // Change format to .mp4
        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)  // Set format to MPEG_4
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)  // Use AAC encoder
            setOutputFile(audioPath)
            prepare()
            start()
        }
        isRecording = true
        Handler().postDelayed({ stopRecording() }, 5000)  // Stop after 5 seconds
    }

    private fun stopRecording() {
        mediaRecorder?.apply {
            stop()
            release()
        }
        mediaRecorder = null
        isRecording = false
    }

    private fun playRecordedAudio() {
        audioPath?.let {
            MediaPlayer().apply {
                setDataSource(it)
                prepare()
                start()
            }
        }
    }



    private fun initializeSpeechRecognizer() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this).apply {
            setRecognitionListener(object : RecognitionListener {
                override fun onResults(results: Bundle?) {
                    val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    textViewResult.text = matches?.joinToString(separator = "\n") ?: "Nema rezultata"
                }

                override fun onReadyForSpeech(params: Bundle?) {}
                override fun onBeginningOfSpeech() {}
                override fun onRmsChanged(rmsdB: Float) {}
                override fun onBufferReceived(buffer: ByteArray?) {}
                override fun onEndOfSpeech() {}
                override fun onError(error: Int) {
                    textViewResult.text = "Došlo je do greške: $error"
                }

                override fun onPartialResults(partialResults: Bundle?) {}
                override fun onEvent(eventType: Int, params: Bundle?) {}
            })
        }
    }

    private fun startSpeechRecognition() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "sr-RS")
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Govorite sada...")
        }
        speechRecognizer.startListening(intent)
    }



    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        mediaRecorder?.release()
        speechRecognizer.destroy()
        audioPath?.let { File(it).delete() }
    }
}