package com.zahid.mathly.data.local

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val editor = prefs.edit()

    companion object {
        private const val PREF_NAME = "mathly_session"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_HAS_COMPLETED_PROFILE = "has_completed_profile"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_LAST_SCREEN = "last_screen"
    }

    var isLoggedIn: Boolean
        get() = prefs.getBoolean(KEY_IS_LOGGED_IN, false)
        set(value) {
            editor.putBoolean(KEY_IS_LOGGED_IN, value)
            editor.apply()
        }

    var hasCompletedProfile: Boolean
        get() = prefs.getBoolean(KEY_HAS_COMPLETED_PROFILE, false)
        set(value) {
            editor.putBoolean(KEY_HAS_COMPLETED_PROFILE, value)
            editor.apply()
        }

    var userId: String?
        get() = prefs.getString(KEY_USER_ID, null)
        set(value) {
            editor.putString(KEY_USER_ID, value)
            editor.apply()
        }

    var lastScreen: String
        get() = prefs.getString(KEY_LAST_SCREEN, "login") ?: "login"
        set(value) {
            editor.putString(KEY_LAST_SCREEN, value)
            editor.apply()
        }

    fun clearSession() {
        editor.clear()
        editor.apply()
    }
}
