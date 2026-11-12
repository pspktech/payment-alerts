package com.paymentalerts

import android.content.Context

object UserPrefs {
    private const val PREFS = "payment_alerts_prefs"
    private const val KEY_NAME = "user_name"
    private const val KEY_PHONE = "user_phone"
    private const val KEY_QR_PATH = "qr_path"
    private const val KEY_SOUND = "sound_enabled"

    private fun prefs(ctx: Context) = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    fun getUserName(ctx: Context): String =
        prefs(ctx).getString(KEY_NAME, "Merchant Name") ?: "Merchant Name"
    fun setUserName(ctx: Context, name: String) =
        prefs(ctx).edit().putString(KEY_NAME, name).apply()

    fun getPhone(ctx: Context): String =
        prefs(ctx).getString(KEY_PHONE, "+91 00000 00000") ?: "+91 00000 00000"
    fun setPhone(ctx: Context, phone: String) =
        prefs(ctx).edit().putString(KEY_PHONE, phone).apply()

    fun getQrPath(ctx: Context): String? = prefs(ctx).getString(KEY_QR_PATH, null)
    fun setQrPath(ctx: Context, path: String?) {
        if (path == null) prefs(ctx).edit().remove(KEY_QR_PATH).apply()
        else prefs(ctx).edit().putString(KEY_QR_PATH, path).apply()
    }

    fun isSoundEnabled(ctx: Context): Boolean = prefs(ctx).getBoolean(KEY_SOUND, true)
    fun setSoundEnabled(ctx: Context, enabled: Boolean) =
        prefs(ctx).edit().putBoolean(KEY_SOUND, enabled).apply()
}
