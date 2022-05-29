package app.inex.inexfilms.core.prefs.utils

import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class StringPreference(
    private val prefs: SharedPreferences,
    private val key: String,
    private val defaultValue: String
): ReadWriteProperty<Any?, String> {

    override fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return prefs.getString(key, defaultValue) ?: defaultValue
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        prefs.edit().run {
            putString(key, value)
            apply()
        }
    }
}