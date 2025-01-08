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
import android.net.Uri
import android.os.Handler
import java.io.File
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2


class GovorActivity : AppCompatActivity() {

    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var textViewResult: TextView
    private var mediaPlayer: MediaPlayer? = null
    private var mediaRecorder: MediaRecorder? = null
    private var audioPath: String? = null
    private var isRecording = false

    private lateinit var govoriButton: LottieAnimationView
    private lateinit var playButton: LottieAnimationView
    private lateinit var recButton: LottieAnimationView
    private lateinit var listenButton: LottieAnimationView
    private lateinit var viewPager: ViewPager2
    private var currentAudioResource: Int? = null // Čuvanje trenutnog audio resursa

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_govor)


        initializeUI()
        setupViewPager()
        setupButtonListeners()
        initializeSpeechRecognizer()


    }

    private fun initializeUI() {
        val lottieBackground: LottieAnimationView = findViewById(R.id.lottieBackground)
        lottieBackground.setAnimationFromUrl("https://lottie.host/553df60d-4abe-47cb-a1ec-cc599f4d37bc/ZY7BGFrDSm.lottie")
        lottieBackground.playAnimation()
        lottieBackground.repeatCount = LottieDrawable.INFINITE


        govoriButton = findViewById(R.id.govorButton)
        playButton = findViewById(R.id.playButton)
        recButton = findViewById(R.id.recButton)
        listenButton = findViewById(R.id.listenButton)
        textViewResult = findViewById(R.id.textViewResult)

        viewPager = findViewById(R.id.viewPager)

    }

    private fun setupButtonListeners() {
        playButton.setOnClickListener {
            playCurrentAudio()
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

                }, 4900)  // Odlaganje od 4.8 sekunde
            }
        }

        listenButton.setOnClickListener {
            if (listenButton.isEnabled) {
                listenButton.playAnimation()
                playRecordedAudio()
            }
        }
    }

    private fun playCurrentAudio() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer()
        }
        mediaPlayer?.let {
            it.reset()
            it.setDataSource(this, Uri.parse("android.resource://$packageName/${currentAudioResource}"))
            it.prepare()
            it.start()
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

    private fun setupViewPager() {
        val animations = listOf(
            AnimationData("animation1.lottie", R.raw.sound1, "Opis za animaciju 1"),
            AnimationData("animation2.lottie", R.raw.sound2, "Opis za animaciju 2")
        )
        viewPager.adapter = AnimationPagerAdapter(this, animations)
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentAudioResource = animations[position].audioFile // Ažuriranje trenutnog audio resursa
            }
        })
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

    private inner class AnimationPagerAdapter(fa: FragmentActivity, private val animations: List<AnimationData>) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = animations.size

        override fun createFragment(position: Int): Fragment {
            // Uzima trenutnu AnimationData stavku iz liste na osnovu pozicije
            val animationData = animations[position]
            // Prosleđuje sve potrebne podatke newInstance metodi
            return AnimationFragment.newInstance(animationData.animationFile, animationData.audioFile, animationData.description)
        }
    }

    data class AnimationData(val animationFile: String, val audioFile: Int, val description: String)



    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null

        mediaRecorder?.release()
        speechRecognizer.destroy()
        audioPath?.let { File(it).delete() }
    }
}