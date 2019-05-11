package br.com.battista.bgscore.util;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

public class DateUtilsTest {

    @Test
    public void testParseTimeWhenTimeNull() {
        assertThat(DateUtils.parseTime(null), equalTo(0L));
    }

    @Test
    public void testParseTimeWhenTimeEmpty() {
        assertThat(DateUtils.parseTime(""), equalTo(0L));
    }

    @Test
    public void testParseTimeWhenTimeBlank() {
        assertThat(DateUtils.parseTime("    "), equalTo(0L));
    }

    @Test
    public void testParseTimeWhenTimeZero() {
        assertThat(DateUtils.parseTime("00:00"), equalTo(0L));
    }

    @Test
    public void testParseTimeWhenTimeValid() {
        assertThat(DateUtils.parseTime("01:15"), equalTo(75L));
    }

    @Test
    public void testParseTimeWhenTimeInvalid() {
        assertThat(DateUtils.parseTime("01:"), equalTo(0L));
        assertThat(DateUtils.parseTime(":15"), equalTo(0L));
        assertThat(DateUtils.parseTime("15"), equalTo(0L));
    }

    @Test
    public void testParseDateWhenDateNull() {
        assertThat(DateUtils.parse(null), equalTo(null));
    }

    @Test
    public void testParseDateWhenDateEmpty() {
        assertThat(DateUtils.parse(""), equalTo(null));
    }

    @Test
    public void testParseDateWhenDateValid() {
        Calendar date = Calendar.getInstance();
        date.set(Calendar.YEAR, 2019);
        date.set(Calendar.MONTH, 2);
        date.set(Calendar.DAY_OF_MONTH, 10);
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);

        assertThat(DateUtils.parse("10/03/2019").toString(), equalTo(date.getTime().toString()));
    }

    @Test
    public void testParseDateWhenDateInvalid() {
        assertThat(DateUtils.parse("10/03"), equalTo(null));
        assertThat(DateUtils.parse("03/2019"), equalTo(null));
        assertThat(DateUtils.parse("2019"), equalTo(null));
    }

    @Test
    public void testParseDateWithHoursWhenDateNull() {
        assertThat(DateUtils.parseWithHours(null), equalTo(null));
    }

    @Test
    public void testParseDateWithHoursWhenDateEmpty() {
        assertThat(DateUtils.parseWithHours(""), equalTo(null));
    }

    @Test
    public void testParseDateWithHoursWhenDateValid() {
        Calendar date = Calendar.getInstance();
        date.set(Calendar.YEAR, 2019);
        date.set(Calendar.MONTH, 2);
        date.set(Calendar.DAY_OF_MONTH, 10);
        date.set(Calendar.HOUR_OF_DAY, 23);
        date.set(Calendar.MINUTE, 39);
        date.set(Calendar.SECOND, 0);

        assertThat(DateUtils.parseWithHours("10/03/2019 23:39").toString(), equalTo(date.getTime().toString()));
        assertThat(DateUtils.parseWithHours("10/03/2019 23:39:31").toString(), equalTo(date.getTime().toString()));
    }

    @Test
    public void testParseDateWithHoursWhenDateInvalid() {
        assertThat(DateUtils.parseWithHours("10/03/2019"), equalTo(null));
        assertThat(DateUtils.parseWithHours("23:39:14"), equalTo(null));
    }

    @Test
    public void testFormatTimeWhenTimeNull() {
        assertThat(DateUtils.formatTime(null), equalTo("00:00"));
    }

    @Test
    public void testFormatTimeWhenTimeZero() {
        assertThat(DateUtils.formatTime(0L), equalTo("00:00"));
    }

    @Test
    public void testFormatTimeWhenTimeValid() {
        assertThat(DateUtils.formatTime(60L), equalTo("01:00"));
        assertThat(DateUtils.formatTime(333L), equalTo("05:33"));
    }

    @Test
    public void testFormatHoursWhenDateNull() {
        assertThat(DateUtils.formatHours((Date) null), equalTo("00:00"));
        assertThat(DateUtils.formatHours((Calendar) null), equalTo("00:00"));
    }

    @Test
    public void testFormatHoursWhenDateZero() {
        Calendar date = Calendar.getInstance();
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        assertThat(DateUtils.formatHours(date), equalTo("00:00"));
    }

    @Test
    public void testFormatHoursWhenDateValid() {
        Calendar date = Calendar.getInstance();
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 15);
        assertThat(DateUtils.formatHours(date), equalTo("00:15"));

        date.set(Calendar.HOUR_OF_DAY, 5);
        date.set(Calendar.MINUTE, 33);
        assertThat(DateUtils.formatHours(date.getTime()), equalTo("05:33"));
    }

    @Test
    public void testFormatWhenDateNull() {
        assertThat(DateUtils.format((Date) null), equalTo(""));
        assertThat(DateUtils.format((Calendar) null), equalTo(""));
    }

    @Test
    public void testFormatWhenDateValid() {
        Calendar date = Calendar.getInstance();
        date.set(Calendar.YEAR, 2019);
        date.set(Calendar.MONTH, 2);
        date.set(Calendar.DAY_OF_MONTH, 10);
        assertThat(DateUtils.format(date), equalTo("10/03/2019"));

        date.set(Calendar.YEAR, 2019);
        date.set(Calendar.MONTH, 0);
        date.set(Calendar.DAY_OF_MONTH, 31);
        assertThat(DateUtils.format(date.getTime()), equalTo("31/01/2019"));
    }

    @Test
    public void testFormatWithHoursWhenDateNull() {
        assertThat(DateUtils.formatWithHours((Date) null), equalTo(""));
        assertThat(DateUtils.formatWithHours((Calendar) null), equalTo(""));
    }

    @Test
    public void testFormatWithHoursWhenDateValid() {
        Calendar date = Calendar.getInstance();
        date.set(Calendar.YEAR, 2019);
        date.set(Calendar.MONTH, 2);
        date.set(Calendar.DAY_OF_MONTH, 10);
        date.set(Calendar.HOUR_OF_DAY, 23);
        date.set(Calendar.MINUTE, 39);
        date.set(Calendar.SECOND, 0);
        assertThat(DateUtils.formatWithHours(date), equalTo("10/03/2019 23:39"));

        date.set(Calendar.YEAR, 2019);
        date.set(Calendar.MONTH, 0);
        date.set(Calendar.DAY_OF_MONTH, 31);
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        assertThat(DateUtils.formatWithHours(date.getTime()), equalTo("31/01/2019 00:00"));
    }

}