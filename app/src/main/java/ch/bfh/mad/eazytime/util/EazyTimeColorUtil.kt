package ch.bfh.mad.eazytime.util

import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import ch.bfh.mad.eazytime.R
import ch.bfh.mad.eazytime.TAG

/**
 * This seem a bit odd, but te return-value from the colorPicker is the id form the color of the array.
 * This is not the same id as the id of the same color not in the array
 */
class EazyTimeColorUtil constructor(val context: Context) {

    fun getColorString(colorId: Int): String {
        var color = context.getString(R.string.defaul_color_string)
        try {
            val intArray = context.resources.getIntArray(R.array.eazyTime_project_colors)
            val stringArray = context.resources.getStringArray(R.array.eazyTime_project_colors_as_string)
            for (i in 0 until intArray.size) {
                if (colorId == intArray[i]) {
                    color = stringArray[i]
                    Log.i(TAG, "found color by resources: $color")
                    break
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "EazyTimeColorUtil.getColorString failed: ${e.message}", e)
        }
        return color
    }

    fun getColorArrayId(colorString: String): Int {
        var colorArrayId = R.color.eazyTime_default_colorProject
        try {
            val intArray = context.resources.getIntArray(R.array.eazyTime_project_colors)
            val stringArray = context.resources.getStringArray(R.array.eazyTime_project_colors_as_string)
            for (i in 0 until intArray.size) {
                if (colorString == stringArray[i]) {
                    colorArrayId = intArray[i]
                    Log.i(TAG, "found colorid by resources: $colorArrayId")
                    break
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "EazyTimeColorUtil.getColorArrayId failed: ${e.message}", e)
        }
        return colorArrayId
    }


    fun getColorId(colorString: String): Int {
        var colorArrayId = R.color.eazyTime_default_colorProject
        try {
            when (colorString) {
                convertIdToString(R.color.eazyTime_colorProject1) -> return R.color.eazyTime_colorProject1
                convertIdToString(R.color.eazyTime_colorProject2) -> return R.color.eazyTime_colorProject2
                convertIdToString(R.color.eazyTime_colorProject3) -> return R.color.eazyTime_colorProject3
                convertIdToString(R.color.eazyTime_colorProject4) -> return R.color.eazyTime_colorProject4
                convertIdToString(R.color.eazyTime_colorProject5) -> return R.color.eazyTime_colorProject5
                convertIdToString(R.color.eazyTime_colorProject6) -> return R.color.eazyTime_colorProject6
                convertIdToString(R.color.eazyTime_colorProject7) -> return R.color.eazyTime_colorProject7
                convertIdToString(R.color.eazyTime_colorProject8) -> return R.color.eazyTime_colorProject8
                convertIdToString(R.color.eazyTime_colorProject9) -> return R.color.eazyTime_colorProject9
            }
        } catch (e: Exception) {
            Log.e(TAG, "EazyTimeColorUtil.getColorArrayId failed: ${e.message}", e)
        }
        return colorArrayId
    }

    private fun convertIdToString(id: Int): String {
        val color = ContextCompat.getColor(context, id)
        val hexcolor = Integer.toHexString(color).substring(2)
        return "#$hexcolor"
    }


}