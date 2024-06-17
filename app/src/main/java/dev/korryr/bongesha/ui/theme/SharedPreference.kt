package dev.korryr.bongesha.ui.theme

import android.content.Context
import android.content.SharedPreferences

fun saveBooleanToSharedPreferences(context: Context, key: String, value: Boolean) {
    val sharedPref: SharedPreferences = context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
    val editor: SharedPreferences.Editor = sharedPref.edit()
    editor.putBoolean(key, value)
    editor.apply()
}

fun getBooleanFromSharedPreferences(context: Context, key: String): Boolean {
    val sharedPref: SharedPreferences = context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
    // The second argument in getBoolean is the default value to return if the key doesn't exist
    return sharedPref.getBoolean(key,false)
}

// To save a boolean value
//val saveBooleanToSharedPreferences(context, "my_boolean_key", true)

// To retrieve a boolean value
//val myBooleanValue = getBooleanFromSharedPreferences(context, "my_boolean_key")