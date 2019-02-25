package ch.bfh.mad.eazytime.util

import java.util.concurrent.TimeUnit

class EazyTimeDateUtil{

    companion object {
        fun fromMinutesToHHmm(minutes: Int): String {
            val hours = TimeUnit.MINUTES.toHours(minutes.toLong())
            val remainMinutes = minutes - TimeUnit.HOURS.toMinutes(hours)
            return String.format("%02d:%02d", hours, remainMinutes)
        }

        fun fromSecondsToHHmmSS(seconds: Int): String {
            val hours = TimeUnit.SECONDS.toHours(seconds.toLong())
            var remainSeconds = seconds - TimeUnit.HOURS.toSeconds(hours)
            val minutes = TimeUnit.SECONDS.toMinutes(remainSeconds)
            remainSeconds -= TimeUnit.MINUTES.toSeconds(minutes)
            return String.format("%02d:%02d:%02d", hours, minutes, remainSeconds)
        }
    }
}