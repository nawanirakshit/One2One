package android.sleek.construction.kotlin.permission

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.setFragmentResult
import com.airbnb.lottie.LottieAnimationView
import com.permissionx.guolindev.PermissionX
import com.rakshit.one.R
import com.test.papers.config.IntentKey
import com.test.papers.utils.RoundedBottomSheetDialogFragment

abstract class BasePermissionDialogFragment :
    RoundedBottomSheetDialogFragment(R.layout.dialog_permission) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        setClickListeners()
    }

    private fun setClickListeners() {
        requireView().findViewById<AppCompatButton>(R.id.btn_perm_allow)
        .setOnClickListener {
            PermissionX.init(requireActivity())
                .permissions(getPermissions())
                .onExplainRequestReason { scope, deniedList ->
                    scope.showRequestReasonDialog(
                        deniedList,
                        "Core fundamental are based on these permissions",
                        "OK",
                        "Cancel"
                    )
                }
                .onForwardToSettings { scope, deniedList ->
                    scope.showForwardToSettingsDialog(
                        deniedList,
                        "You need to allow necessary permissions in Settings manually",
                        "OK",
                        "Cancel"
                    )
                }
                .request { allGranted, grantedList, deniedList ->
                    if (allGranted) {
                        setResult(true)
                        Toast.makeText(
                            requireContext(),
                            "Permissions are granted",
                            Toast.LENGTH_LONG
                        ).show()
                        dialog?.dismiss()
                    } else {
                        setResult(false)
                        Toast.makeText(
                            requireContext(),
                            "These permissions are denied: $deniedList",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }
    }

    private fun setResult(granted: Boolean) {
        setFragmentResult(
            getResultKey(),
            Bundle().apply {
                putBoolean(IntentKey.PERM_KEY, granted)
            })
    }

    private fun initViews() {
        requireView().findViewById<TextView>(R.id.tv_perm_title).text = getPermissionTitle()
        requireView().findViewById<TextView>(R.id.tv_perm_desc).text = getPermissionDesc()
        requireView().findViewById<LottieAnimationView>(R.id.iv_perm_image).setAnimation(getPermissionImage())
    }

    abstract fun getPermissionTitle(): String

    abstract fun getPermissionDesc(): String

    abstract fun getPermissionImage(): String

    abstract fun getResultKey(): String

    abstract fun getPermissions(): List<String>


}