package ch.bfh.mad.eazytime.geofence

import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.design.widget.Snackbar
import ch.bfh.mad.R


class PermissionHandler {

    private var context: Context
    private var fragment: Fragment



    constructor(context: Context, fragment: Fragment) {
        this.context = context
        this.fragment = fragment

    }


    fun checkForPermissions(requiredPermission: String, permissionRequest: Int) : Boolean {
        if (ContextCompat.checkSelfPermission(context, requiredPermission)
            != PackageManager.PERMISSION_GRANTED) {

            if (fragment.shouldShowRequestPermissionRationale(requiredPermission)) {
                Snackbar.make(fragment.view!!, context.getString(R.string.permission_request_rationale),
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(context.getString(R.string.OK)) {
                        fragment.requestPermissions(arrayOf(requiredPermission), permissionRequest)
                    }
                    .show()
            } else {
                fragment.requestPermissions(arrayOf(requiredPermission),permissionRequest)
            }
            return false
        } else {
            return true
        }
    }

}