package com.paymentalerts

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings as AndroidSettings
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    private lateinit var nameText: TextView
    private lateinit var phoneText: TextView
    private lateinit var statusText: TextView
    private lateinit var qrImage: ImageView

    private val pickQr = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) saveQr(uri)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nameText = findViewById(R.id.nameText)
        phoneText = findViewById(R.id.phoneText)
        statusText = findViewById(R.id.statusText)
        qrImage = findViewById(R.id.qrImage)

        findViewById<Button>(R.id.uploadQrBtn).setOnClickListener { pickQr.launch("image/*") }
        findViewById<Button>(R.id.enableBtn).setOnClickListener {
            startActivity(Intent(AndroidSettings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
        }
        findViewById<Button>(R.id.testBtn).setOnClickListener {
            PaymentSoundPlayer.play(this, "Payment received")
        }
        findViewById<Button>(R.id.settingsBtn).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        nameText.text = UserPrefs.getUserName(this)
        phoneText.text = UserPrefs.getPhone(this)
        val enabled = AndroidSettings.Secure.getString(
            contentResolver, "enabled_notification_listeners"
        )?.contains(packageName) == true
        statusText.text = if (enabled) "✅ Notifications Active" else "❌ Tap to Enable Access"
        loadQr()
    }

    private fun loadQr() {
        val path = UserPrefs.getQrPath(this)
        if (path != null && File(path).exists()) {
            qrImage.setImageURI(Uri.fromFile(File(path)))
        } else {
            qrImage.setImageResource(R.drawable.ic_qr_placeholder)
        }
    }

    private fun saveQr(uri: Uri) {
        try {
            val file = File(filesDir, "qr_image.png")
            contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(file).use { output -> input.copyTo(output) }
            }
            UserPrefs.setQrPath(this, file.absolutePath)
            loadQr()
            Toast.makeText(this, "QR uploaded ✅", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Failed: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
