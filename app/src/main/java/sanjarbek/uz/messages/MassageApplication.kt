package sanjarbek.uz.messages

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MassageApplication : Application() {

    companion object {
        lateinit var sharedPrefs: SharedPreferences
        lateinit var dbReference: DatabaseReference
        lateinit var auth: FirebaseAuth

        const val TOKEN = "token"
    }

    override fun onCreate() {
        super.onCreate()
        auth = Firebase.auth
        dbReference = FirebaseDatabase.getInstance("https://messages-87a34-default-rtdb.firebaseio.com/").reference
        sharedPrefs=getSharedPreferences("chat_app",Context.MODE_PRIVATE)
    }
}