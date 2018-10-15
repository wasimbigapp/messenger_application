package app.common.utils

import java.util.concurrent.TimeUnit

class TimeHelper {
    companion object {

        val YEAR: Long = TimeUnit.DAYS.toSeconds(365);
        val MONTH: Long = TimeUnit.DAYS.toSeconds(30);
        val WEEK: Long = TimeUnit.DAYS.toSeconds(7);
        val DAY: Long = TimeUnit.DAYS.toSeconds(1);
        val HOUR = TimeUnit.HOURS.toSeconds(1)
        val MiNUTE = TimeUnit.MINUTES.toSeconds(1)

        fun getTime(earlierTime: Long, olderTime: Long): String {
            val period: Double = Math.abs(earlierTime - olderTime).toDouble()

            var timespan = 1.0
            var format = "seconds"
            when {
                period > YEAR -> {
                    // More than one year
                    format = "years"
                    timespan = Math.floor((period / YEAR))
                }
                period > MONTH -> {
                    // More than one month
                    format = "months"
                    timespan = Math.floor((period / MONTH))
                }
                period > WEEK -> {
                    // More than one week
                    format = "weeks"
                    timespan = Math.floor((period / WEEK))
                }
                period > DAY -> {
                    // More than one day
                    format = "days"
                    timespan = Math.floor((period / DAY))
                }
                period > HOUR -> {
                    // More than one hour
                    format = "hours"
                    timespan = Math.floor((period / HOUR))
                }
                period > MiNUTE -> {
                    // More than one minute
                    format = "minutes"
                    timespan = Math.floor((period / MiNUTE))
                }

            // Remove the s
            }

            // Remove the s
            if (timespan == 1.0) {
                format = format.subSequence(0, format.length - 1).toString()
            }

            return timespan.toInt().toString() + " " + format
        }

    }
}