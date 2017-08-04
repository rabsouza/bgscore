package br.com.battista.bgscore.util;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

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
    public void testFormatTimeWhenTimeNull() {
        assertThat(DateUtils.formatTime(null), equalTo("00:00"));
    }

    @Test
    public void testFormatTimeWhenTimeZero() {
        assertThat(DateUtils.formatTime(0L), equalTo("00:00"));
    }

    @Test
    public void testFormatTimeWhenTimeValid() {
        assertThat(DateUtils.formatTime(15L), equalTo("00:15"));
        assertThat(DateUtils.formatTime(60L), equalTo("01:00"));
        assertThat(DateUtils.formatTime(90L), equalTo("01:30"));
        assertThat(DateUtils.formatTime(123L), equalTo("02:03"));
        assertThat(DateUtils.formatTime(333L), equalTo("05:33"));
    }

}