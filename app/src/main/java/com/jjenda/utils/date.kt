package com.jjenda.utils

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class ElapsedTime(private val stringDate: String = "03/09/2020 02:40:22", pattern: String = "dd/MM/yyyy HH:mm:ss") {
    private var days: Long

    init {
        val diff =  Calendar.getInstance().time.time - SimpleDateFormat(pattern).parse(stringDate).time
        days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
    }

    fun elapsedTimeString() : String {
        if(elapsedYears() > 0) return "Il y'a ${elapsedYears()} ans"
        if(elapsedMonths() > 0) return "Il y'a ${elapsedMonths()} mois"
        if(elapsedWeeks() > 0) return  "Il y'a ${elapsedWeeks()} semaine${plural(elapsedWeeks())}"
        if(elapsedDays() > 0) return  "Il y'a ${elapsedDays()} jour${plural((elapsedDays()))}"
        return "Aujourd'hui"
    }

    fun plural(number : Long) : String {
        return if(number > 1) "s" else ""
    }

    private fun elapsedDays() : Long { return days }

    private fun elapsedWeeks() : Long { return days / 7 }

    private fun elapsedMonths() : Long { return days / 30 }

    private fun elapsedYears() : Long { return days / 365 }

}
