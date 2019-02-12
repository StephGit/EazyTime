package ch.bfh.mad.eazytime.projects

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import ch.bfh.mad.R


class PermissionHandler {

    private var activity: Activity? = null
    private var fragment: Fragment? = null
    private var requiredPermission: String? = null
    private var permissionRequestCode: Int = 300
    private var permissionGranted: Boolean = false


    constructor(activity: Activity, requiredPermission: String) {
        this.activity = activity
        this.requiredPermission = requiredPermission
    }

    constructor(fragment: Fragment, requiredPermission: String) {
        this.fragment = fragment
        this.requiredPermission = requiredPermission
    }

    fun checkPermission() : Boolean {
        if (!hasPermission()) {
            activity.let {
                requestPermissionForActivity(it!!)
            }
            fragment.let {
                requestPermissionsForFragment(it!!)
            }
            return false
        }
        return true
    }

    @Suppress("UNCHECKED_CAST")
    fun currentContext(): Context? {
        return if (activity != null) this.activity!!.applicationContext else fragment!!.context
    }


    private fun hasPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(currentContext()!!, requiredPermission!!)
            != PackageManager.PERMISSION_GRANTED
        ) {
            return false
        }
        return true
    }

    private fun requestPermissionForActivity(activity: Activity) {
        ActivityCompat.requestPermissions(activity, arrayOf(requiredPermission), permissionRequestCode)
    }

    private fun requestPermissionsForFragment(fragment: Fragment) {
        if (fragment.shouldShowRequestPermissionRationale(requiredPermission!!)) {
            Snackbar.make(
                fragment.view!!, currentContext()!!.getString(R.string.permission_request_rationale),
                Snackbar.LENGTH_INDEFINITE
            )
                .setAction(currentContext()!!.getString(R.string.OK)) {
                    fragment.requestPermissions(arrayOf(requiredPermission!!), permissionRequestCode)
                }
                .show()
        } else {
            fragment.requestPermissions(arrayOf(requiredPermission), permissionRequestCode)
        }
    }

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == permissionRequestCode) {
            if (permissions.size == 1 &&
                permissions[0] == requiredPermission &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                permissionGranted = true
            }
        }
    }
}