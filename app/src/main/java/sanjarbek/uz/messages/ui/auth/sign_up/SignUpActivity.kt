package sanjarbek.uz.messages.ui.auth.sign_up

import android.content.Intent
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.isVisible
import sanjarbek.uz.messages.MassageApplication
import sanjarbek.uz.messages.core.base_activity.BaseActivity
import sanjarbek.uz.messages.core.dto.UserModel
import sanjarbek.uz.messages.core.extension.toast
import sanjarbek.uz.messages.databinding.ActivitySignUpBinding
import sanjarbek.uz.messages.ui.MainActivity
import sanjarbek.uz.messages.ui.auth.sign_in.SingInActivity

class SignUpActivity : BaseActivity<ActivitySignUpBinding>() {

    private val vm: SignUpViewModel by viewModels()

    override fun getMyViewBinding(): ActivitySignUpBinding {
        return ActivitySignUpBinding.inflate(layoutInflater)
    }

    private var uri: String? = null

    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) {
        it?.let {
            binding.btnSelectImage.setImageURI(it)
            uri = it.toString()
            binding.btnSelectImage.visibility = View.VISIBLE
            binding.imgIcon.isVisible = false
        }
    }

    override fun setup() = with(binding) {
        btnSelectImageRoot.setOnClickListener {
            launcher.launch("image/*")
        }

        btnLogInAccount.setOnClickListener {
            startActivity(Intent(this@SignUpActivity, SingInActivity::class.java))
        }

        btnSignUp.setOnClickListener {
            val name = etName.text?.toString()
            val secondName = etSecondName.text?.toString()
            val email = etEmail.text?.toString()
            val password = etPassword.text?.toString()
            val currectPassword = etCurrectPassword.text?.toString()

            if (name.isNullOrEmpty() || secondName.isNullOrEmpty() || email.isNullOrEmpty() || password.isNullOrEmpty()) {
                toast("Barcha Maydonlarni to'ldiring")
                return@setOnClickListener
            } else if (uri == null) {
                toast("Iltimos rasm qo'ying")
                return@setOnClickListener
            } else if (password != currectPassword) {
                toast("Qayta kiritilga parol xato")
                return@setOnClickListener
            }

            vm.signUp(email, password)
        }
        vm.signUpEvent.observe(this@SignUpActivity) {
            when (it) {
                is SignUpEvent.Error -> {
                    toast(it.message)
                    progressCircular.isVisible = false
                    signUp.isVisible = true
                }

                SignUpEvent.Loading -> {
                    progressCircular.isVisible = true
                    signUp.isVisible = false
                }

                is SignUpEvent.Success -> {
                    progressCircular.isVisible = false
                    signUp.isVisible = true
                    vm.saveImage(uri!!, it.token)


                }
            }
        }

        vm.saveUserImage.observe(this@SignUpActivity) {
            when (it) {
                is SaveUserImageEvent.Error -> {
                    progressCircular.isVisible = false
                    signUp.isVisible = true
                    btnSignUp.isVisible = true
                }

                SaveUserImageEvent.Loading -> {
                    progressCircular.isVisible = true
                    signUp.isVisible = false
                    btnSignUp.isVisible = false
                }

                is SaveUserImageEvent.Success -> {
                    progressCircular.isVisible = false
                    signUp.isVisible = true
                    btnSignUp.isVisible = true
                    it.token.let { iToken ->
                        MassageApplication.sharedPrefs.edit()
                            .putString(MassageApplication.TOKEN, iToken).apply()
                        vm.saveUserToDatabase(
                            UserModel(
                                userToken = it.token,
                                userName = etName.text?.toString().orEmpty(),
                                userSecondName = etSecondName.text?.toString().orEmpty(),
                                userImage = it.token
                            )
                        )

                    }
                }
            }
        }

        vm.saveUserEvent.observe(this@SignUpActivity) {
            when (it) {
                is SaveUserEvent.Error -> {
                    progressCircular.isVisible = false
                    signUp.isVisible = true
                    btnSignUp.isVisible = true
                    toast(it.message)
                }

                SaveUserEvent.Loading -> {
                    progressCircular.isVisible = true
                    signUp.isVisible = false
                    btnSignUp.isVisible = false
                }

                is SaveUserEvent.Success -> {
                    progressCircular.isVisible = false
                    signUp.isVisible = true
                    btnSignUp.isVisible = true
                    navigationToMainActivity()
                }
            }
        }
    }

    private fun navigationToMainActivity() {
        startActivity(Intent(this@SignUpActivity, MainActivity::class.java))
        finish()
    }

}