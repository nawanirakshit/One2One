package android.sleek.construction.kotlin.permission

import android.Manifest
import com.rakshit.one.R
import com.test.papers.config.FragmentResult

class StoragePermissionDialog : BasePermissionDialogFragment() {
    override fun getPermissionTitle(): String = getString(R.string.allow_storage)

    override fun getPermissionDesc(): String = getString(R.string.perm_storage_desc)

    override fun getPermissionImage(): String = "lottie_camera_perm.json"

    override fun getResultKey(): String = FragmentResult.STORAGE_PERMISSION

    override fun getPermissions(): ArrayList<String> =
        if (android.os.Build.VERSION.SDK_INT >= 33) arrayListOf(
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.CAMERA
        )
        else arrayListOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        )
}