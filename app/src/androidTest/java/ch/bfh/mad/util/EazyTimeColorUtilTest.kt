package ch.bfh.mad.util

import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import ch.bfh.mad.R
import ch.bfh.mad.eazytime.util.EazyTimeColorUtil
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.hamcrest.CoreMatchers.`is` as Is

private const val DEFAULT_COLOR_STRING = "#ff8787"
private const val DEFAULT_COLOR_ID = R.color.eazyTime_default_colorProject

@RunWith(AndroidJUnit4::class)
class EazyTimeColorUtilTest {

    @Test
    fun getColorForInvalidId_returnsDefaultColor() {
        assertThat(EazyTimeColorUtil(InstrumentationRegistry.getTargetContext()).getColorString(0), Is(DEFAULT_COLOR_STRING))
    }

    @Test
    fun getColorForInvalidSting_returnsDefaultColorId() {
        assertThat(EazyTimeColorUtil(InstrumentationRegistry.getTargetContext()).getColorArrayId(""), Is(DEFAULT_COLOR_ID))
    }

    @Test
    fun getColorSting_allProjects() {
        val colorUtil = EazyTimeColorUtil(InstrumentationRegistry.getTargetContext())
        val colorIdArray = InstrumentationRegistry.getTargetContext().resources.getIntArray(R.array.eazyTime_project_colors)
        val colorStringArray = InstrumentationRegistry.getTargetContext().resources.getStringArray(R.array.eazyTime_project_colors_as_string)
        assertThat(colorIdArray.size, Is(colorStringArray.size))
        for (i in 0 until colorStringArray.size) {
            assertThat(colorUtil.getColorString(colorIdArray[i]), Is(colorStringArray[i]))
        }
    }

    @Test
    fun getColorArrayId_allProjects() {
        val colorUtil = EazyTimeColorUtil(InstrumentationRegistry.getTargetContext())
        val colorIdArray = InstrumentationRegistry.getTargetContext().resources.getIntArray(R.array.eazyTime_project_colors)
        val colorStringArray = InstrumentationRegistry.getTargetContext().resources.getStringArray(R.array.eazyTime_project_colors_as_string)
        assertThat(colorIdArray.size, Is(colorStringArray.size))
        for (i in 0 until colorStringArray.size) {
            assertThat(colorUtil.getColorArrayId(colorStringArray[i]), Is(colorIdArray[i]))
        }
    }

    @Test
    fun getColorId_allProjects() {
        val colorUtil = EazyTimeColorUtil(InstrumentationRegistry.getTargetContext())
        assertThat(colorUtil.getColorId("#ff8787"), Is(R.color.eazyTime_colorProject1))
        assertThat(colorUtil.getColorId("#f783ac"), Is(R.color.eazyTime_colorProject2))
        assertThat(colorUtil.getColorId("#9775fa"), Is(R.color.eazyTime_colorProject3))
        assertThat(colorUtil.getColorId("#3bc9db"), Is(R.color.eazyTime_colorProject4))
        assertThat(colorUtil.getColorId("#38d9a9"), Is(R.color.eazyTime_colorProject5))
        assertThat(colorUtil.getColorId("#69db7c"), Is(R.color.eazyTime_colorProject6))
        assertThat(colorUtil.getColorId("#a9e34b"), Is(R.color.eazyTime_colorProject7))
        assertThat(colorUtil.getColorId("#ffd43b"), Is(R.color.eazyTime_colorProject8))
        assertThat(colorUtil.getColorId("#ffa94d"), Is(R.color.eazyTime_colorProject9))
    }
}