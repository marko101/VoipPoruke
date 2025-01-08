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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_animation, container, false)
        val descriptionTextView = view.findViewById<TextView>(R.id.descriptionTextView)
        val lottieAnimationView = view.findViewById<LottieAnimationView>(R.id.lottieAnimationView)

        val animationFile = arguments?.getString("animationFile") ?: ""
        val audioFile = arguments?.getInt("audioFile") ?: 0
        val description = arguments?.getString("description") ?: ""

        descriptionTextView.text = description


        // Učitavanje Lottie animacije
        lottieAnimationView.setAnimation(animationFile)
        lottieAnimationView.repeatCount = LottieDrawable.INFINITE
        lottieAnimationView.pauseAnimation()

        var isAnimating = false

        lottieAnimationView.setOnClickListener {
            if (isAnimating) {
                lottieAnimationView.pauseAnimation()
                isAnimating = false
            } else {
                lottieAnimationView.playAnimation()
                isAnimating = true
            }
        }

        // Priprema MediaPlayer-a za reprodukciju zvuka
        if (audioFile != 0) {
            mediaPlayer = MediaPlayer.create(context, audioFile)
        }

        return view
    }

    companion object {
        fun newInstance(animationFile: String, audioFile: Int, description: String): AnimationFragment {
            val fragment = AnimationFragment()
            val args = Bundle()
            args.putString("animationFile", animationFile)
            args.putInt("audioFile", audioFile)
            args.putString("description", description)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Oslobađanje MediaPlayer resursa
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
