package sanjarbek.uz.messages.ui.auth.sign_in

import android.content.Intent
import android.view.View
import androidx.activity.viewModels
import sanjarbek.uz.messages.MassageApplication
import sanjarbek.uz.messages.core.base_activity.BaseActivity
import sanjarbek.uz.messages.core.extension.toast
import sanjarbek.uz.messages.databinding.ActivitySingInBinding
import sanjarbek.uz.messages.ui.MainActivity
import sanjarbek.uz.messages.ui.auth.sign_up.SignUpActivity

class SingInActivity : BaseActivity<ActivitySingInBinding>() {

    private val viewModel: SignInViewModel by viewModels()

    override fun getMyViewBinding(): ActivitySingInBinding {
        return ActivitySingInBinding.inflate(layoutInflater)
    }

    override fun setup() = with(binding) {
        btnSignIn.setOnClickListener {
            viewModel.signIn(etEmail.text.toString(), etPassword.text.toString())
        }

        btnCreateAccount.setOnClickListener {
            startActivity(Intent(this@SingInActivity,SignUpActivity::class.java))
        }

        viewModel.signInEvent.observe(this@SingInActivity) {
            when (it) {
                is SignInEvent.Failure -> {
                    progressCircular.visibility = View.GONE
                    signIn.visibility = View.VISIBLE
                    btnSignIn.visibility = View.VISIBLE
                    toast(it.message)
                }

                SignInEvent.Loading -> {
                    progressCircular.visibility = View.VISIBLE
                    signIn.visibility = View.GONE
                    btnSignIn.visibility = View.GONE
                }

                is SignInEvent.Success -> {
                    MassageApplication
                        .sharedPrefs
                        .edit()
                        .putString(MassageApplication.TOKEN, it.token)
                        .apply()
                    startActivity(Intent(this@SingInActivity, MainActivity::class.java))
                    toast(it.message)
                    finish()
                }
            }
        }
    }
}