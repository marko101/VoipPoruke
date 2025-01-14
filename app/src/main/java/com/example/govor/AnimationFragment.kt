package com.example.govor

import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable

class AnimationFragment : Fragment() {
    private lateinit var lottieAnimationView: LottieAnimationView
    private var mediaPlayer: MediaPlayer? = null
    private var soundEffectPlayer: MediaPlayer? = null  // Inicijalizacija MediaPlayer-a na nivou klase

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_animation, container, false)
        val descriptionTextView = view.findViewById<TextView>(R.id.descriptionTextView)
        val lottieAnimationView = view.findViewById<LottieAnimationView>(R.id.lottieAnimationView)

        val animationFile = arguments?.getString("animationFile") ?: ""
        val audioFile = arguments?.getInt("audioFile") ?: 0
        val soundEffectFile = arguments?.getInt("soundEffectFile") ?: 0
        val description = arguments?.getString("description") ?: ""


        descriptionTextView.text = description


        // Učitavanje Lottie animacije
        lottieAnimationView.setAnimation(animationFile)
        lottieAnimationView.repeatCount = LottieDrawable.INFINITE
        lottieAnimationView.pauseAnimation()

        val voicePlayer = MediaPlayer.create(context, audioFile)
        soundEffectPlayer = MediaPlayer.create(context, soundEffectFile)  // Inicijalizacija soundEffectPlayer-a
        soundEffectPlayer?.isLooping = true  // Podesavanje na ponavljanje



        var isAnimating = false

        // Priprema MediaPlayer-a za reprodukciju zvuka
        if (audioFile != 0) {
            mediaPlayer = MediaPlayer.create(context, audioFile)
        }

        if (soundEffectFile != 0) {
            soundEffectPlayer = MediaPlayer.create(context, soundEffectFile)
        }

        lottieAnimationView.setOnClickListener {
            if (isAnimating) {
                // Pauziranje animacije i oba audio playera
                lottieAnimationView.pauseAnimation()
                soundEffectPlayer?.pause()

                isAnimating = false
            } else {
                // Pokretanje animacije i oba audio playera
                lottieAnimationView.playAnimation()
                soundEffectPlayer?.start()

                isAnimating = true
            }
        }



        return view
    }



    companion object {
        fun newInstance(animationFile: String, audioFile: Int, description: String, soundEffectFile: Int): AnimationFragment {
            val fragment = AnimationFragment()
            val args = Bundle().apply {
                putString("animationFile", animationFile)
                putInt("audioFile", audioFile)
                putString("description", description)
                putInt("soundEffectFile", soundEffectFile) // Dodajte sound effect file
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Oslobađanje MediaPlayer resursa
        mediaPlayer?.release()
        mediaPlayer = null
        soundEffectPlayer?.release()
        soundEffectPlayer = null

    }
}
