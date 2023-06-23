package com.test.papers.kotlin

import android.Manifest
import androidx.fragment.app.Fragment

open class KotlinBasePermissionFragment : Fragment() {

    fun checkCameraAndStoragePermission(hasAllPermissions: () -> Unit) {
        askPermissions(
            hasAllPermissions,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        )
    }

    fun checkRecordAndStoragePermission(hasAllPermissions: () -> Unit) {
        askPermissions(
            hasAllPermissions,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }

    private fun askPermissions(
        hasAllPermissions: () -> Unit,
        vararg permissons: String
    ) {
        /*   askPermission(*permissons) {
               //all permissions already granted or just granted
               hasAllPermissions()
           }.onDeclined { e ->
   //            if (e.hasDenied()) {
   //            }

               if (e.hasForeverDenied()) {

                   this.activity?.let {
                       AlertDialog.Builder(it)
                           .setMessage(getString(R.string.message_permission))
                           .setPositiveButton(getString(R.string.setting)) { dialog, which ->
                               e.goToSettings()
                           } //ask again
                           .setNegativeButton(getString(R.string.later)) { dialog, which ->
                               dialog.dismiss()
                           }
                           .show()
                   }


               }
           }*/
    }
}