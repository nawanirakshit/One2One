package com.rakshit.one.ui.prelogin.forgotpassword

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import com.rakshit.one.R
import com.rakshit.one.ui.prelogin.AuthViewModel
import com.test.papers.kotlin.KotlinBaseFragment
import com.test.papers.utils.extension.showToast
import org.koin.android.ext.android.inject

class ForgotPasswordFragment : KotlinBaseFragment(R.layout.fragment_forgot_password) {

    private val viewModel: AuthViewModel by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView(view)

        observeViews()
    }

    private fun observeViews() {
        viewModel.successForgotPassword.observe(viewLifecycleOwner) {
            if (it)
                requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun initView(view: View) {
        val mEmail: AppCompatEditText = view.findViewById(R.id.email_input_text)
        view.findViewById<Button>(R.id.btn_forgot_password).setOnClickListener {
            val email = mEmail.text.toString()

            if (email.isBlank()) {
                showToast("Email is compulsory")
            } else {
                viewModel.checkForgotPassword(email)
            }
        }

        view.findViewById<TextView>(R.id.tv_sign_up).setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }
}