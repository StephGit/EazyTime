package ch.bfh.mad.util

import ch.bfh.mad.eazytime.util.EazyTimeDateUtil
import org.junit.Assert.assertThat
import org.junit.Test
import org.hamcrest.CoreMatchers.`is` as Is


class EazyTimeDateUtilTest {

    @Test
    fun fromMinutesToHHmm_20_Minutes() {
        assertThat(EazyTimeDateUtil.fromMinutesToHHmm(20), Is("00:20"))
    }

    @Test
    fun fromMinutesToHHmm_59_Minutes() {
        assertThat(EazyTimeDateUtil.fromMinutesToHHmm(59), Is("00:59"))
    }

    @Test
    fun fromMinutesToHHmm_60_Minutes() {
        assertThat(EazyTimeDateUtil.fromMinutesToHHmm(60), Is("01:00"))
    }

    @Test
    fun fromMinutesToHHmm_61_Minutes() {
        assertThat(EazyTimeDateUtil.fromMinutesToHHmm(61), Is("01:01"))
    }

    @Test
    fun fromMinutesToHHmm_120_Minutes() {
        assertThat(EazyTimeDateUtil.fromMinutesToHHmm(120), Is("02:00"))
    }

    @Test
    fun fromMinutesToHHmm_125_Minutes() {
        assertThat(EazyTimeDateUtil.fromMinutesToHHmm(125), Is("02:05"))
    }

    @Test
    fun fromMinutesToHHmm_126_Minutes() {
        assertThat(EazyTimeDateUtil.fromMinutesToHHmm(126), Is("02:06"))
    }

    @Test
    fun fromMinutesToHHmm_361_Minutes() {
        assertThat(EazyTimeDateUtil.fromMinutesToHHmm(361), Is("06:01"))
    }

    @Test
    fun fromSecondsToHHmmSS_20_Seconds() {
        assertThat(EazyTimeDateUtil.fromSecondsToHHmmSS(20), Is("00:00:20"))
    }

    @Test
    fun fromSecondsToHHmmSS_0h_0m_0s() {
        val hoursInSec = 0 * 60 * 60
        val minInSec = 0 * 60
        val seconds = 0
        val totalSeconds = hoursInSec + minInSec + seconds
        assertThat(EazyTimeDateUtil.fromSecondsToHHmmSS(totalSeconds), Is("00:00:00"))
    }

    @Test
    fun fromSecondsToHHmmSS_0h_12m_45s() {
        val hoursInSec = 0 * 60 * 60
        val minInSec = 12 * 60
        val seconds = 45
        val totalSeconds = hoursInSec + minInSec + seconds
        assertThat(EazyTimeDateUtil.fromSecondsToHHmmSS(totalSeconds), Is("00:12:45"))
    }

    @Test
    fun fromSecondsToHHmmSS_3h_45m_56s() {
        val hoursInSec = 3 * 60 * 60
        val minInSec = 45 * 60
        val seconds = 56
        val totalSeconds = hoursInSec + minInSec + seconds
        assertThat(EazyTimeDateUtil.fromSecondsToHHmmSS(totalSeconds), Is("03:45:56"))
    }

}