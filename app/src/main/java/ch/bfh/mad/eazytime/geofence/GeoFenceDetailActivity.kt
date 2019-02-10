package ch.bfh.mad.eazytime.geofence

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import ch.bfh.mad.R


class GeoFenceDetailActivity : AppCompatActivity(){

    private val permissionFineLocation= Manifest.permission.ACCESS_FINE_LOCATION
    private var permissionFineLocationGranted:Boolean=false
    private val permissionRequestCode:Int=202

    companion object{
        fun newIntent(ctx: Context)= Intent(ctx,GeoFenceDetailActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_geofence_detail)

        checkForPermissions(permissionFineLocation,permissionRequestCode)


    }

    private fun checkForPermissions(requiredPermission: String, permissionRequest: Int) {
        if (ContextCompat.checkSelfPermission(applicationContext, requiredPermission)
            != PackageManager.PERMISSION_GRANTED) {

//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    requiredPermission)) {
//                // Show an explanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
//            } else {
                ActivityCompat.requestPermissions(this,
                    arrayOf(requiredPermission),
                    permissionRequest)
//            }
        } else {
            permissionFineLocationGranted = true
        }
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == permissionRequestCode) {
            if (permissions.size == 1 &&
                permissions[0] == permissionFineLocation &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                permissionFineLocationGranted = true
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.geofenceDetail, fragment)
            .commit()
    }

}
