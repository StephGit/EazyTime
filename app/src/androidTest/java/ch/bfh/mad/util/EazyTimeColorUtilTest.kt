package ch.bfh.mad.util

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
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
    fun getColorId_allProjects() {
        val colorUtil = EazyTimeColorUtil(InstrumentationRegistry.getTargetContext())
        val colorIdArray = InstrumentationRegistry.getTargetContext().resources.getIntArray(R.array.eazyTime_project_colors)
        val colorStringArray = InstrumentationRegistry.getTargetContext().resources.getStringArray(R.array.eazyTime_project_colors_as_string)
        assertThat(colorIdArray.size, Is(colorStringArray.size))
        for (i in 0 until colorStringArray.size) {
            assertThat(colorUtil.getColorArrayId(colorStringArray[i]), Is(colorIdArray[i]))
        }
    }
}