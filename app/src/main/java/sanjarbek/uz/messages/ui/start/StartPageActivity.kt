package sanjarbek.uz.messages.ui.start

import android.content.Intent
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import sanjarbek.uz.messages.MassageApplication
import sanjarbek.uz.messages.core.base_activity.BaseActivity
import sanjarbek.uz.messages.core.extension.toast
import sanjarbek.uz.messages.databinding.ActivityStartPageBinding
import sanjarbek.uz.messages.ui.MainActivity
import sanjarbek.uz.messages.ui.auth.sign_in.SingInActivity
import sanjarbek.uz.messages.ui.auth.sign_up.SignUpActivity

class StartPageActivity : BaseActivity<ActivityStartPageBinding>() {
    private val token by lazy {
        MassageApplication.sharedPrefs.getString(MassageApplication.TOKEN, "")
    }

    override fun getMyViewBinding(): ActivityStartPageBinding {
        return ActivityStartPageBinding.inflate(layoutInflater)
    }

    override fun setup() = with(binding) {

        val currentUser = Firebase.auth.currentUser
        btnStart.setOnClickListener {
            if (currentUser==null) {
                startActivity(
                    Intent(this@StartPageActivity, SignUpActivity::class.java)
                )
                finish()
            } else {
                if (token.isNullOrEmpty()){
                    startActivity(
                        Intent(this@StartPageActivity, SingInActivity::class.java)
                    )
                    finish()
                }else{
                    startActivity(
                        Intent(this@StartPageActivity, MainActivity::class.java)
                    )
                    finish()
                }
            }
        }
    }


}