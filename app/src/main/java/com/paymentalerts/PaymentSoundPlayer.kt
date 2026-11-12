package com.paymentalerts

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

object PaymentSoundPlayer {
    private var tts: TextToSpeech? = null
    private var pending: String? = null
    private var initialized = false

    fun play(context: Context, message: String) {
        if (tts == null) {
            pending = message
            tts = TextToSpeech(context.applicationContext) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    tts?.language = Locale.US
                    initialized = true
                    pending?.let { speak(it) }
                    pending = null
                }
            }
        } else if (initialized) {
            speak(message)
        } else {
            pending = message
        }
    }

    private fun speak(msg: String) {
        tts?.speak(msg, TextToSpeech.QUEUE_ADD, null, "pay_${System.currentTimeMillis()}")
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
        pending = null
        initialized = false
    }
}
