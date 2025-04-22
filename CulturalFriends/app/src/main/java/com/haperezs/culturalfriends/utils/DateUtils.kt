package com.haperezs.culturalfriends.utils

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun formatTimestamp(timestamp: Timestamp): String {
    val today = Calendar.getInstance()
    val message = Calendar.getInstance().apply {
        time = timestamp.toDate()
    }

    // If it is the same day dispolay only the time, if it is a past day, display the date instead
    val sdf : SimpleDateFormat =
        if (today.get(Calendar.DAY_OF_YEAR) == message.get(Calendar.DAY_OF_YEAR)
            && today.get(Calendar.YEAR) == message.get(Calendar.YEAR)) {
            SimpleDateFormat("HH:mm", Locale.getDefault())
        } else {
            SimpleDateFormat("dd/MM/yy", Locale.getDefault())
        }

    return sdf.format(timestamp.toDate())
}


