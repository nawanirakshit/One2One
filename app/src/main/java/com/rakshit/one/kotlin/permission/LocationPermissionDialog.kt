package android.sleek.construction.kotlin.permission

import android.Manifest
import com.rakshit.one.R
import com.test.papers.config.FragmentResult

class LocationPermissionDialog : BasePermissionDialogFragment() {
    override fun getPermissionTitle(): String = getString(R.string.allow_location)

    override fun getPermissionDesc(): String = getString(R.string.perm_location_desc)

    override fun getPermissionImage(): String = "lottie_location_perm.json"

    override fun getResultKey(): String = FragmentResult.LOCATION_PERMISSION

    override fun getPermissions(): ArrayList<String> =
        arrayListOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
}