package com.rakshit.one.ui.prelogin.login

import android.os.Bundle
import android.sleek.construction.config.Config
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatEditText
import com.rakshit.one.R
import com.rakshit.one.ui.prelogin.AuthViewModel
import com.rakshit.one.ui.prelogin.forgotpassword.ForgotPasswordFragment
import com.rakshit.one.ui.prelogin.register.RegistrationFragment
import com.test.papers.kotlin.KotlinBaseActivity
import com.test.papers.kotlin.replaceFragment
import com.test.papers.utils.extension.navigateDashboard
import com.test.papers.utils.extension.showToast
import org.koin.android.ext.android.inject

class LoginActivity : KotlinBaseActivity(container = android.R.id.content) {

    private val viewModel: AuthViewModel by inject()

    private val mRememberMe: AppCompatCheckBox by lazy { findViewById(R.id.cb_remember_me) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initView()

        observeViews()
    }

    private fun initView() {
        val mPassword: AppCompatEditText = findViewById(R.id.password_input_text)
        val mEmail: AppCompatEditText = findViewById(R.id.email_input_text)

        findViewById<Button>(R.id.btn_to_dashboard).setOnClickListener {

            val password = mPassword.text.toString()
            val email = mEmail.text.toString()

            if (email.isBlank()) {
                showToast("Email is required")
            } else if (password.isBlank()) {
                showToast("Password is required")
            } else {
                viewModel.login(email, password, this)
            }
        }

        findViewById<TextView>(R.id.btn_to_forgot).setOnClickListener {
            replaceFragment<ForgotPasswordFragment>(
                userTag = ForgotPasswordFragment::class.java.name,
                container = android.R.id.content,
                addToBackStack = true,
                animation = true
            )
        }

        findViewById<TextView>(R.id.tv_sign_up).setOnClickListener {
            replaceFragment<RegistrationFragment>(
                userTag = RegistrationFragment::class.java.name,
                container = android.R.id.content,
                addToBackStack = true,
                animation = true
            )
        }
    }

    private fun observeViews() {
        viewModel.successLogin.observe(this) {
            if (it.isEmpty()) {

                if (mRememberMe.isChecked)
                    Config.isLoggedIn = true

                showToast("Logged in successfully!!")
                navigateDashboard()
            } else {
                showToast(it)
            }
        }
    }
}