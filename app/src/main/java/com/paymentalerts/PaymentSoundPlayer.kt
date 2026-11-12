package com.paymentalerts

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

object PaymentSoundPlayer {
    private var tts: TextToSpeech? = null
    private var pending: String? = null

    fun play(context: Context, message: String) {
        if (tts == null) {
            pending = message
            tts = TextToSpeech(context.applicationContext) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    tts?.language = Locale.US
                    pending?.let { speak(it) }
                    pending = null
                }
            }
        } else speak(message)
    }

    private fun speak(msg: String) {
        tts?.speak(msg, TextToSpeech.QUEUE_ADD, null, "pay_${System.currentTimeMillis()}")
    }
}
