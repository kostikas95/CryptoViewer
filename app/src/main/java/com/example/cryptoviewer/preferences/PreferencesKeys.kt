package com.example.cryptoviewer.preferences

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey

object PreferencesKeys {
    val FAVOURITE_IDS = stringSetPreferencesKey("favorite_ids")
    val CONVERSION_CURRENCY = stringPreferencesKey("conversion_currency")
    val COMPARISON_TIME = stringPreferencesKey("comparison_time")
    val THEME_MODE = stringPreferencesKey("theme_mode")
    val SORT_BY = stringPreferencesKey("sort_by")
    val SORT_ORDER = booleanPreferencesKey("sort_order")
    val PRICE_ALERTS_ENABLED = booleanPreferencesKey("price_alerts_enabled")
    val DAILY_SUMMARY_NOTIFICATIONS = booleanPreferencesKey("daily_summary_notifications_enabled")
    val DEFAULT_TAB = stringPreferencesKey("default_tab")
    val DISPLAY_MODE = stringPreferencesKey("display_mode")
}