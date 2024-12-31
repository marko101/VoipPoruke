package com.example.govor

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import android.widget.BaseAdapter

class LetterAdapter(private val context: Context, private val letters: Array<String>) : BaseAdapter() {
    override fun getCount(): Int = letters.size

    override fun getItem(position: Int): Any = letters[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.letter_item, parent, false)
        val lottieView: LottieAnimationView = view.findViewById(R.id.lottieView)
        val fileName = "${letters[position]}.lottie"
        try {
            lottieView.setAnimation(fileName)
            lottieView.repeatCount = LottieDrawable.INFINITE
            lottieView.playAnimation()
        } catch (e: Exception) {
            Log.e("LetterAdapter", "Error loading Lottie file: $fileName", e)
        }
        return view
    }
}