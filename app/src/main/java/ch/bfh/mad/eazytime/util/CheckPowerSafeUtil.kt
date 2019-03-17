package ch.bfh.mad.eazytime.util

import android.content.Context
import android.os.Build
import android.os.PowerManager
import androidx.fragment.app.FragmentManager
import javax.inject.Inject

class CheckPowerSafeUtil @Inject constructor(private val context: Context) {

    fun checkPowerSaveMode(supportFragmentManager: FragmentManager) {
        val powerManager: PowerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && powerManager.isPowerSaveMode) {
            val newFragment = GeoFenceDialogFragment()
            newFragment.show(supportFragmentManager, "power")
        }
    }
}