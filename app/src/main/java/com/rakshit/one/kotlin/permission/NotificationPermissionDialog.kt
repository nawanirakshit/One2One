package android.sleek.construction.kotlin.permission

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import com.rakshit.one.R
import com.test.papers.config.FragmentResult

class NotificationPermissionDialog : BasePermissionDialogFragment() {
    override fun getPermissionTitle(): String = getString(R.string.allow_notification)

    override fun getPermissionDesc(): String = getString(R.string.perm_notification_desc)

    override fun getPermissionImage(): String = "lottie_location_perm.json"

    override fun getResultKey(): String = FragmentResult.NOTIFICATION_PERMISSION

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun getPermissions(): ArrayList<String> =
        arrayListOf(Manifest.permission.POST_NOTIFICATIONS)
}