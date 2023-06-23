package com.test.papers.utils.extension

import android.Manifest
import androidx.fragment.app.Fragment
//import com.github.florent37.runtimepermission.kotlin.askPermission

fun androidx.fragment.app.FragmentActivity.checkCameraAndStoragePermission(hasAllPermissions: () -> Unit) {
    askPermissions(
        hasAllPermissions,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )
}

fun androidx.fragment.app.FragmentActivity.checkRecordAndStoragePermission(hasAllPermissions: () -> Unit) {
    askPermissions(
        hasAllPermissions,
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
}

fun Fragment.checkCameraAndStoragePermission(hasAllPermissions: () -> Unit) {
    activity?.checkCameraAndStoragePermission(hasAllPermissions)
}

fun Fragment.checkRecordAndStoragePermission(hasAllPermissions: () -> Unit) {
    activity?.checkRecordAndStoragePermission(hasAllPermissions)
}


fun androidx.fragment.app.FragmentActivity.askPermissions(
    hasAllPermissions: () -> Unit,
    vararg permissons: String
) {
//    askPermission(*permissons) {
//        //all permissions already granted or just granted
//        hasAllPermissions()
//    }.onDeclined { e ->
//        if (e.hasDenied()) {
//
//            AlertDialog.Builder(this)
//                .setMessage("Please accept permissions to continue.")
//                .setPositiveButton("yes") { dialog, which ->
//                    e.askAgain()
//                } //ask again
//                .setNegativeButton("no") { dialog, which ->
//                    dialog.dismiss()
//                }
//                .show()
//
//        }
//
//        if (e.hasForeverDenied()) {
//            e.goToSettings()
//        }
//    }
}