
import java.time.Duration
import java.time.LocalTime

class TimeValidator {

    companion object {
        fun validateStartTime(startTime: LocalTime, endTime: LocalTime?): Boolean {
            return endTime == null || !startTime.isAfter(endTime) && isTimeNotLessThan15Minutes(
                startTime,
                endTime
            )
        }

        fun validateEndTime(startTime: LocalTime?, endTime: LocalTime): Boolean {
            return startTime != null && !endTime.isBefore(startTime) && isTimeNotLessThan15Minutes(
                startTime,
                endTime
            )
        }

        private fun isTimeNotLessThan15Minutes(startTime: LocalTime, endTime: LocalTime): Boolean {
            val duration = Duration.between(startTime, endTime)
            val minimumDuration = Duration.ofMinutes(15)

            return duration >= minimumDuration
        }
    }
}