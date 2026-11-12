package com.paymentalerts

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import java.util.Locale

class PaymentNotificationListener : NotificationListenerService() {

    private val paymentApps = setOf(
        "com.google.android.apps.nbu.paisa.user", // Google Pay
        "com.phonepe.app",                         // PhonePe
        "net.one97.paytm",                         // Paytm
        "in.org.npci.upiapp",                      // BHIM
        "com.amazon.mShop.android.shopping"        // Amazon Pay
    )

    private val keywords = listOf(
        "received", "credited", "paid you", "sent you",
        "పంపారు", "వచ్చాయి", "प्राप्त", "मिले"
    )

    private val seen = LinkedHashSet<String>()

    override fun onDestroy() {
        PaymentSoundPlayer.shutdown()
        super.onDestroy()
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        if (sbn.packageName !in paymentApps) return
        if (!UserPrefs.isSoundEnabled(applicationContext)) return

        val key = "${sbn.key}_${sbn.postTime}"
        if (!seen.add(key)) return
        if (seen.size > 200) seen.clear()

        val extras = sbn.notification.extras
        val title = extras.getCharSequence(Notification.EXTRA_TITLE)?.toString() ?: ""
        val text  = extras.getCharSequence(Notification.EXTRA_TEXT)?.toString() ?: ""
        val big   = extras.getCharSequence(Notification.EXTRA_BIG_TEXT)?.toString() ?: ""
        val body  = "$title $text $big".lowercase(Locale.ROOT)

        if (keywords.none { body.contains(it) }) return

        val amount = Regex("""(?:₹|rs\.?|inr)\s*([\d,]+(?:\.\d{1,2})?)""")
            .find(body)?.groupValues?.get(1)?.replace(",", "")

        val spoken = if (amount != null) "Payment received. $amount rupees." else "Payment received."
        PaymentSoundPlayer.play(applicationContext, spoken)
    }
}
