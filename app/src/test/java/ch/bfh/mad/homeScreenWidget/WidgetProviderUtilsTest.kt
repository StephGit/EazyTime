package ch.bfh.mad.homeScreenWidget

import android.appwidget.AppWidgetManager
import android.os.Bundle
import ch.bfh.mad.eazytime.util.WidgetProviderUtils
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.hamcrest.CoreMatchers.`is` as Is

class WidgetProviderUtilsTest {

    private var utils = WidgetProviderUtils()

    private val mockAppWidgetMgr = mock<AppWidgetManager>()
    private val mockBundle = mock<Bundle>()

    @Test
    fun getAmountOfButtonsToDisplay_1_project_WidgetWith_90() {
        val appWidgetId = 56
        val widgetWith = 90
        val projectCount = 1
        val expectedButtonCount = 1

        given(mockAppWidgetMgr.getAppWidgetOptions(appWidgetId)).willReturn(mockBundle)
        given(mockBundle.containsKey(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)).willReturn(true)
        given(mockBundle.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)).willReturn(widgetWith)

        val amountOfButtonsToDisplay = utils.getAmountOfButtonsToDisplay(projectCount, mockAppWidgetMgr, appWidgetId)
        assertThat(amountOfButtonsToDisplay, Is(expectedButtonCount))
    }

    @Test
    fun getAmountOfButtonsToDisplay_4_project_WidgetWith_90() {
        val appWidgetId = 56
        val widgetWith = 90
        val projectCount = 4
        val expectedButtonCount = 1

        given(mockAppWidgetMgr.getAppWidgetOptions(appWidgetId)).willReturn(mockBundle)
        given(mockBundle.containsKey(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)).willReturn(true)
        given(mockBundle.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)).willReturn(widgetWith)

        val amountOfButtonsToDisplay = utils.getAmountOfButtonsToDisplay(projectCount, mockAppWidgetMgr, appWidgetId)
        assertThat(amountOfButtonsToDisplay, Is(expectedButtonCount))
    }

    @Test
    fun getAmountOfButtonsToDisplay_1_project_WidgetWith_180() {
        val appWidgetId = 56
        val widgetWith = 180
        val projectCount = 1
        val expectedButtonCount = 1

        given(mockAppWidgetMgr.getAppWidgetOptions(appWidgetId)).willReturn(mockBundle)
        given(mockBundle.containsKey(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)).willReturn(true)
        given(mockBundle.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)).willReturn(widgetWith)

        val amountOfButtonsToDisplay = utils.getAmountOfButtonsToDisplay(projectCount, mockAppWidgetMgr, appWidgetId)
        assertThat(amountOfButtonsToDisplay, Is(expectedButtonCount))
    }

    @Test
    fun getAmountOfButtonsToDisplay_4_project_WidgetWith_109() {
        val appWidgetId = 56
        val widgetWith = 109
        val projectCount = 4
        val expectedButtonCount = 1

        given(mockAppWidgetMgr.getAppWidgetOptions(appWidgetId)).willReturn(mockBundle)
        given(mockBundle.containsKey(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)).willReturn(true)
        given(mockBundle.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)).willReturn(widgetWith)

        val amountOfButtonsToDisplay = utils.getAmountOfButtonsToDisplay(projectCount, mockAppWidgetMgr, appWidgetId)
        assertThat(amountOfButtonsToDisplay, Is(expectedButtonCount))
    }

    @Test
    fun getAmountOfButtonsToDisplay_4_project_WidgetWith_110() {
        val appWidgetId = 56
        val widgetWith = 110
        val projectCount = 3
        val expectedButtonCount = 2

        given(mockAppWidgetMgr.getAppWidgetOptions(appWidgetId)).willReturn(mockBundle)
        given(mockBundle.containsKey(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)).willReturn(true)
        given(mockBundle.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)).willReturn(widgetWith)

        val amountOfButtonsToDisplay = utils.getAmountOfButtonsToDisplay(projectCount, mockAppWidgetMgr, appWidgetId)
        assertThat(amountOfButtonsToDisplay, Is(expectedButtonCount))
    }

    @Test
    fun getAmountOfButtonsToDisplay_4_project_WidgetWith_notAvailable() {
        val appWidgetId = 56
        val projectCount = 3
        val expectedButtonCount = 1

        given(mockAppWidgetMgr.getAppWidgetOptions(appWidgetId)).willReturn(mockBundle)
        given(mockBundle.containsKey(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)).willReturn(false)

        val amountOfButtonsToDisplay = utils.getAmountOfButtonsToDisplay(projectCount, mockAppWidgetMgr, appWidgetId)
        assertThat(amountOfButtonsToDisplay, Is(expectedButtonCount))
    }
}
