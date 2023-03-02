package com.example.mynotes.utils

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

object Helper {

    fun formattingDate(date: String?): String {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
        val inputDate = format.parse(date.toString())
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("in", "ID"))
        return dateFormat.format(inputDate as Date)
    }

}