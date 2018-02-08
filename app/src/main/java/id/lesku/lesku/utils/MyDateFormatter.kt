package id.lesku.lesku.utils

import java.text.SimpleDateFormat
import java.util.*

class MyDateFormatter {
    companion object {
        /* DAY NAME IN BAHASA */
        val SUNDAY = "Sunday"
        val MONDAY = "Monday"
        val TUESDAY = "Tuesday"
        val WEDNESDAY = "Wednesday"
        val THURSDAY = "Thursday"
        val FRIDAY = "Friday"
        val SATURDAY = "Saturday"
        val SUNDAY_BAHASA = "Minggu"
        val MONDAY_BAHASA = "Senin"
        val TUESDAY_BAHASA = "Selasa"
        val WEDNESDAY_BAHASA = "Rabu"
        val THURSDAY_BAHASA = "Kamis"
        val FRIDAY_BAHASA = "Jumat"
        val SATURDAY_BAHASA = "Sabtu"

        /* MONTH NUMBER */
        val JANUARY_NUMBER = "01"
        val FEBRUARY_NUMBER = "02"
        val MARCH_NUMBER = "03"
        val APRIL_NUMBER = "04"
        val MAY_NUMBER = "05"
        val JUNE_NUMBER = "06"
        val JULY_NUMBER = "07"
        val AUGUST_NUMBER = "08"
        val SEPTEMBER_NUMBER = "09"
        val OCTOBER_NUMBER = "10"
        val NOVEMBER_NUMBER = "11"
        val DECEMBER_NUMBER = "12"

        /* MONTH NAME IN BAHASA */
        val JANUARY_BAHASA = "Januari"
        val FEBRUARY_BAHASA = "Februari"
        val MARCH_BAHASA = "Maret"
        val APRIL_BAHASA = "April"
        val MAY_BAHASA = "Mei"
        val JUNE_BAHASA = "Juni"
        val JULY_BAHASA = "Juli"
        val AUGUST_BAHASA = "Agustus"
        val SEPTEMBER_BAHASA = "September"
        val OCTOBER_BAHASA = "Oktober"
        val NOVEMBER_BAHASA = "November"
        val DECEMBER_BAHASA = "Desember"

        //from date format
        fun dateBahasa(date: Date): String {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val nameDayFormat = SimpleDateFormat("EEEE", Locale.US)
            val strDate = dateFormat.format(date)
            val strDay = nameDayFormat.format(date)

            //Day Formatter
            var strDayName = ""
            when (strDay) {
                SUNDAY -> strDayName = SUNDAY_BAHASA
                MONDAY -> strDayName = MONDAY_BAHASA
                TUESDAY -> strDayName = TUESDAY_BAHASA
                WEDNESDAY -> strDayName = WEDNESDAY_BAHASA
                THURSDAY -> strDayName = THURSDAY_BAHASA
                FRIDAY -> strDayName = FRIDAY_BAHASA
                SATURDAY -> strDayName = SATURDAY_BAHASA
            }

            //Month Formatter
            val strYear = (strDate[0].toString() + strDate[1].toString()
                    + strDate[2].toString() + strDate[3].toString())
            val strMonth = strDate[5].toString() + strDate[6].toString()
            val strDayNumber = strDate[8].toString() + strDate[9].toString()

            var strMonthName = ""
            when (strMonth) {
                JANUARY_NUMBER -> strMonthName = JANUARY_BAHASA
                FEBRUARY_NUMBER -> strMonthName = FEBRUARY_BAHASA
                MARCH_NUMBER -> strMonthName = MARCH_BAHASA
                APRIL_NUMBER -> strMonthName = APRIL_BAHASA
                MAY_NUMBER -> strMonthName = MAY_BAHASA
                JUNE_NUMBER -> strMonthName = JUNE_BAHASA
                JULY_NUMBER -> strMonthName = JULY_BAHASA
                AUGUST_NUMBER -> strMonthName = AUGUST_BAHASA
                SEPTEMBER_NUMBER -> strMonthName = SEPTEMBER_BAHASA
                OCTOBER_NUMBER -> strMonthName = OCTOBER_BAHASA
                NOVEMBER_NUMBER -> strMonthName = NOVEMBER_BAHASA
                DECEMBER_NUMBER -> strMonthName = DECEMBER_BAHASA
            }

            return "$strDayName, $strDayNumber $strMonthName $strYear"
        }

        fun setAnotherDateFrom(day: Int, date: Date): Date {
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar.add(Calendar.DAY_OF_YEAR, day)
            val anotherDate = calendar.time
            calendar.clear()

            return anotherDate
        }

        fun getHourFromDate(date: Date): Int {
            val calendar = Calendar.getInstance()
            calendar.time = date
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            calendar.clear()

            return hour
        }

        fun getMinuteFromDate(date: Date): Int {
            val calendar = Calendar.getInstance()
            calendar.time = date
            val minute = calendar.get(Calendar.MINUTE)
            calendar.clear()

            return minute
        }

        fun getDayOfMonthFromDate(date: Date): Int {
            val calendar = Calendar.getInstance()
            calendar.time = date
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            calendar.clear()

            return day
        }

        fun getMonthFromDate(date: Date): Int {
            val calendar = Calendar.getInstance()
            calendar.time = date
            val month = calendar.get(Calendar.MONTH)
            calendar.clear()

            return month
        }

        fun getYearFromDate(date: Date): Int {
            val calendar = Calendar.getInstance()
            calendar.time = date
            val year = calendar.get(Calendar.YEAR)
            calendar.clear()

            return year
        }

        fun getDate(day: Int, month: Int, year: Int): Date {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.DAY_OF_MONTH, day)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.YEAR, year)
            val date = calendar.time
            calendar.clear()

            return date
        }

        fun getDateAndTime(day: Int, month: Int, year: Int, hour: Int, minute: Int): Date {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.DAY_OF_MONTH, day)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
            val date = calendar.time
            calendar.clear()

            return date
        }

        fun getStartTimeOfTheDay(date: Date): Date {
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            val startTimeOfTheDay = calendar.time
            calendar.clear()

            return startTimeOfTheDay
        }

        fun getEndTimeOfTheDay(date: Date): Date {
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar.set(Calendar.HOUR_OF_DAY, 23)
            calendar.set(Calendar.MINUTE, 59)
            calendar.set(Calendar.SECOND, 59)
            calendar.set(Calendar.MILLISECOND, 59)
            val endTimeOfTheDay = calendar.time
            calendar.clear()

            return endTimeOfTheDay
        }

        fun getClockTime(date: Date): String {
            val formatTime = SimpleDateFormat("HH:mm", Locale.getDefault())
            return formatTime.format(date)
        }

        fun dateAndTimeBahasa(date: Date): String{
            val strDate = dateBahasa(date)
            val strTime = getClockTime(date)

            return ("$strDate  $strTime")
        }

        fun getLastDateOfTheMonth(day: Int, month: Int, year: Int): Int{
            val calendar = Calendar.getInstance()
            calendar.set(year, month, day)
            val intLastDate = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
            calendar.clear()

            return intLastDate
        }

        fun getDateInFormat(date: Date): String{
            val formatTime = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            return formatTime.format(date)
        }

        fun getDateFromStringDate(strDate: String): Date{
            //Month Formatter
            val strYear = (strDate[0].toString() + strDate[1].toString()
                    + strDate[2].toString() + strDate[3].toString())
            val strMonth = strDate[5].toString() + strDate[6].toString()
            val strDayNumber = strDate[8].toString() + strDate[9].toString()

            //get date
            return getDate(strDayNumber.toInt(), strMonth.toInt(), strYear.toInt())
        }
    }
}