package com.rakshit.one.ui.prelogin.register

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import com.rakshit.one.R
import com.rakshit.one.ui.prelogin.AuthViewModel
import com.test.papers.kotlin.KotlinBaseFragment
import com.test.papers.utils.extension.hideKeyboard
import com.test.papers.utils.extension.navigateDashboard
import com.test.papers.utils.extension.showToast
import org.koin.android.ext.android.inject

class RegistrationFragment : KotlinBaseFragment(R.layout.fragment_registration) {

    private val viewModel: AuthViewModel by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView(view)

        observeViews()
    }

    private fun initView(view: View) {

        val mName: AppCompatEditText = view.findViewById(R.id.name_input_text)
        val mPassword: AppCompatEditText = view.findViewById(R.id.password_input_text)
        val mEmail: AppCompatEditText = view.findViewById(R.id.email_input_text)
        view.findViewById<Button>(R.id.btn_forgot_password).setOnClickListener {
            val email = mEmail.text.toString()
            val name = mName.text.toString()
            val password = mPassword.text.toString()

            if (name.isBlank()) {
                showToast("Name is compulsory")
            } else if (email.isBlank()) {
                showToast("Email is compulsory")
            } else if (password.isBlank()) {
                showToast("Password is compulsory")
            } else {
                hideKeyboard()
                showLoading()
                viewModel.register(name, email, password, requireActivity())
            }
        }

        view.findViewById<TextView>(R.id.tv_sign_up).setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

    }

    private fun observeViews() {

        viewModel.successRegister.observe(viewLifecycleOwner) {
            hideLoading()
            if (it.isNotEmpty()) {
                showToast(it)
            } else {
                showToast("Registered Successfully!!")

                navigateDashboard()
            }
        }

    }


}