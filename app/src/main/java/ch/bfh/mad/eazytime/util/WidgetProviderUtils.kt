package ch.bfh.mad.eazytime.util

import android.appwidget.AppWidgetManager
import android.util.Log
import ch.bfh.mad.eazytime.TAG

class WidgetProviderUtils  {

    fun getAmountOfButtonsToDisplay(projectCount: Int, appWidgetMgr: AppWidgetManager, appWidgetId: Int): Int {
        val maxDisplayableButtonsByWidgetWidth = getMaxDisplayableButtonsByWidgetWidth(appWidgetMgr, appWidgetId)
        return minOf(projectCount, maxDisplayableButtonsByWidgetWidth)
    }

    private fun getMaxDisplayableButtonsByWidgetWidth(appWidgetMgr: AppWidgetManager, appWidgetId: Int): Int {
        var displayableButtons = 1
        val options = appWidgetMgr.getAppWidgetOptions(appWidgetId)
        if (options.containsKey(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)) {
            val appWidgetMinWidth = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)
            Log.d(TAG, "appWidgetMinWidth: $appWidgetMinWidth")
            // found on https://developer.android.com/guide/practices/ui_guidelines/widget_design#anatomy_determining_size
            displayableButtons = (appWidgetMinWidth + 30) / 70
            Log.d(TAG, "displayableButtons: $displayableButtons")
        } else {
            Log.w(TAG, "OPTION_APPWIDGET_MIN_WIDTH not available, use 1")
        }
        return displayableButtons
    }
}