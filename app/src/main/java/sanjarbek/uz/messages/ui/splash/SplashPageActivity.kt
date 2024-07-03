package sanjarbek.uz.messages.ui.splash

import android.content.Intent
import android.os.Handler
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import sanjarbek.uz.messages.MassageApplication
import sanjarbek.uz.messages.core.base_activity.BaseActivity
import sanjarbek.uz.messages.databinding.ActivitySplashPageBinding
import sanjarbek.uz.messages.ui.pin.PinPageActivity
import sanjarbek.uz.messages.ui.start.StartPageActivity

@Suppress("DEPRECATION")
class SplashPageActivity : BaseActivity<ActivitySplashPageBinding>() {

    private val SPLASH_TIME_OUT: Long = 3000

    private val token by lazy {
        MassageApplication.sharedPrefs.getString(MassageApplication.TOKEN, "")
    }

    override fun getMyViewBinding(): ActivitySplashPageBinding {
        return ActivitySplashPageBinding.inflate(layoutInflater)
    }

    override fun setup() {
        val currentUser = Firebase.auth.currentUser
        println(":::::::::currentUser-> $currentUser")
        Handler().postDelayed({
            if (currentUser == null) {
                val intent = Intent(
                    this, StartPageActivity::class.java
                )
                intent.addFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                )
                startActivity(intent)
            }else{
                println("::::::::::::::::token-> $token")
                if (token.isNullOrEmpty()){
                    val intent = Intent(
                        this, StartPageActivity::class.java
                    )
                    intent.addFlags(
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    )
                    startActivity(intent)
                }else{
                    val intent = Intent(
                        this, PinPageActivity::class.java
                    )
                    intent.addFlags(
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    )
                    startActivity(intent)
                }
            }

            finish()
        }, SPLASH_TIME_OUT)
    }
}