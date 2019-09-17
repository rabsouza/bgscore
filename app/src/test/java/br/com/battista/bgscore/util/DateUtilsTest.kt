package br.com.battista.bgscore.util

import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat

import org.junit.Test

import java.util.Calendar
import java.util.Date

class DateUtilsTest {

    @Test
    fun testParseTimeWhenTimeNull() {
        assertThat(DateUtils.parseTime(null), equalTo(0L))
    }

    @Test
    fun testParseTimeWhenTimeEmpty() {
        assertThat(DateUtils.parseTime(""), equalTo(0L))
    }

    @Test
    fun testParseTimeWhenTimeBlank() {
        assertThat(DateUtils.parseTime("    "), equalTo(0L))
    }

    @Test
    fun testParseTimeWhenTimeZero() {
        assertThat(DateUtils.parseTime("00:00"), equalTo(0L))
    }

    @Test
    fun testParseTimeWhenTimeValid() {
        assertThat(DateUtils.parseTime("01:15"), equalTo(75L))
    }

    @Test
    fun testParseTimeWhenTimeInvalid() {
        assertThat(DateUtils.parseTime("01:"), equalTo(0L))
        assertThat(DateUtils.parseTime(":15"), equalTo(0L))
        assertThat(DateUtils.parseTime("15"), equalTo(0L))
    }

    @Test
    fun testParseDateWhenDateValid() {
        val date = Calendar.getInstance()
        date.set(Calendar.YEAR, 2019)
        date.set(Calendar.MONTH, 2)
        date.set(Calendar.DAY_OF_MONTH, 10)
        date.set(Calendar.HOUR_OF_DAY, 0)
        date.set(Calendar.MINUTE, 0)
        date.set(Calendar.SECOND, 0)

        assertThat(DateUtils.parse("10/03/2019").toString(), equalTo(date.time.toString()))
    }

    @Test
    fun testParseDateWithHoursWhenDateValid() {
        val date = Calendar.getInstance()
        date.set(Calendar.YEAR, 2019)
        date.set(Calendar.MONTH, 2)
        date.set(Calendar.DAY_OF_MONTH, 10)
        date.set(Calendar.HOUR_OF_DAY, 23)
        date.set(Calendar.MINUTE, 39)
        date.set(Calendar.SECOND, 0)

        assertThat(DateUtils.parseWithHours("10/03/2019 23:39").toString(), equalTo(date.time.toString()))
        assertThat(DateUtils.parseWithHours("10/03/2019 23:39:31").toString(), equalTo(date.time.toString()))
    }

    @Test
    fun testParseDateWithHoursWhenDateInvalid() {
        assertThat(DateUtils.parseWithHours("10/03/2019"), equalTo(null as Date?))
        assertThat(DateUtils.parseWithHours("23:39:14"), equalTo(null as Date?))
    }

    @Test
    fun testFormatTimeWhenTimeNull() {
        assertThat(DateUtils.formatTime(null), equalTo("00:00"))
    }

    @Test
    fun testFormatTimeWhenTimeZero() {
        assertThat(DateUtils.formatTime(0L), equalTo("00:00"))
    }

    @Test
    fun testFormatTimeWhenTimeValid() {
        assertThat(DateUtils.formatTime(60L), equalTo("01:00"))
        assertThat(DateUtils.formatTime(333L), equalTo("05:33"))
    }

    @Test
    fun testFormatHoursWhenDateNull() {
        assertThat(DateUtils.formatHours(null as Date?), equalTo("00:00"))
        assertThat(DateUtils.formatHours(null as Calendar?), equalTo("00:00"))
    }

    @Test
    fun testFormatHoursWhenDateZero() {
        val date = Calendar.getInstance()
        date.set(Calendar.HOUR_OF_DAY, 0)
        date.set(Calendar.MINUTE, 0)
        assertThat(DateUtils.formatHours(date), equalTo("00:00"))
    }

    @Test
    fun testFormatHoursWhenDateValid() {
        val date = Calendar.getInstance()
        date.set(Calendar.HOUR_OF_DAY, 0)
        date.set(Calendar.MINUTE, 15)
        assertThat(DateUtils.formatHours(date), equalTo("00:15"))

        date.set(Calendar.HOUR_OF_DAY, 5)
        date.set(Calendar.MINUTE, 33)
        assertThat(DateUtils.formatHours(date.time), equalTo("05:33"))
    }

    @Test
    fun testFormatWhenDateNull() {
        assertThat(DateUtils.format(null as Date?), equalTo(""))
        assertThat(DateUtils.format(null as Calendar?), equalTo(""))
    }

    @Test
    fun testFormatWhenDateValid() {
        val date = Calendar.getInstance()
        date.set(Calendar.YEAR, 2019)
        date.set(Calendar.MONTH, 2)
        date.set(Calendar.DAY_OF_MONTH, 10)
        assertThat(DateUtils.format(date), equalTo("10/03/2019"))

        date.set(Calendar.YEAR, 2019)
        date.set(Calendar.MONTH, 0)
        date.set(Calendar.DAY_OF_MONTH, 31)
        assertThat(DateUtils.format(date.time), equalTo("31/01/2019"))
    }

    @Test
    fun testFormatWithHoursWhenDateNull() {
        assertThat(DateUtils.formatWithHours(null as Date?), equalTo(""))
        assertThat(DateUtils.formatWithHours(null as Calendar?), equalTo(""))
    }

    @Test
    fun testFormatWithHoursWhenDateValid() {
        val date = Calendar.getInstance()
        date.set(Calendar.YEAR, 2019)
        date.set(Calendar.MONTH, 2)
        date.set(Calendar.DAY_OF_MONTH, 10)
        date.set(Calendar.HOUR_OF_DAY, 23)
        date.set(Calendar.MINUTE, 39)
        date.set(Calendar.SECOND, 0)
        assertThat(DateUtils.formatWithHours(date), equalTo("10/03/2019 23:39"))

        date.set(Calendar.YEAR, 2019)
        date.set(Calendar.MONTH, 0)
        date.set(Calendar.DAY_OF_MONTH, 31)
        date.set(Calendar.HOUR_OF_DAY, 0)
        date.set(Calendar.MINUTE, 0)
        date.set(Calendar.SECOND, 0)
        assertThat(DateUtils.formatWithHours(date.time), equalTo("31/01/2019 00:00"))
    }

}