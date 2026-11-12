package com.paymentalerts

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    companion object {
        // ⚠️ Developer phone number ikkada change chey
        const val DEVELOPER_PHONE = "+918374625736"
        const val DEVELOPER_EMAIL = "madhupatelpudari@gmail.com"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val nameInput = findViewById<EditText>(R.id.nameInput)
        val phoneInput = findViewById<EditText>(R.id.phoneInput)
        val soundSwitch = findViewById<Switch>(R.id.soundSwitch)

        nameInput.setText(UserPrefs.getUserName(this))
        phoneInput.setText(UserPrefs.getPhone(this))
        soundSwitch.isChecked = UserPrefs.isSoundEnabled(this)

        findViewById<Button>(R.id.saveBtn).setOnClickListener {
            UserPrefs.setUserName(this, nameInput.text.toString().ifBlank { "Merchant Name" })
            UserPrefs.setPhone(this, phoneInput.text.toString().ifBlank { "+91 00000 00000" })
            UserPrefs.setSoundEnabled(this, soundSwitch.isChecked)
            Toast.makeText(this, "Saved ✅", Toast.LENGTH_SHORT).show()
            finish()
        }

        findViewById<Button>(R.id.callDevBtn).setOnClickListener {
            startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$DEVELOPER_PHONE")))
        }

        findViewById<Button>(R.id.emailDevBtn).setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(DEVELOPER_EMAIL))
                putExtra(Intent.EXTRA_SUBJECT, "Payment Alerts App Feedback")
            }
            startActivity(intent)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish(); return true
    }
}
