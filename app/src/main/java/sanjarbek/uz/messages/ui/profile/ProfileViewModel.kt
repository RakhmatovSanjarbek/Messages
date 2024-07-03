package sanjarbek.uz.messages.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sanjarbek.uz.messages.MassageApplication
import sanjarbek.uz.messages.core.dto.UserModel

class ProfileViewModel : ViewModel() {

    private val _user = MutableLiveData<GetMyModelEvent>(GetMyModelEvent.Loading)
    val user: LiveData<GetMyModelEvent> = _user

    private var userModel: UserModel? = null

    fun getMyModel() = viewModelScope.launch(Dispatchers.IO) {
        val myToken = MassageApplication.sharedPrefs.getString(MassageApplication.TOKEN, "")
        MassageApplication.dbReference.child("user")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach {
                        it.getValue(UserModel::class.java)?.let { model ->
                            if (myToken.equals(model.userToken)) {
                                userModel = model
                            }
                        }
                        CoroutineScope(Dispatchers.Main).launch {
                            userModel?.let {
                                _user.value = GetMyModelEvent.Success(userModel!!)
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    CoroutineScope(Dispatchers.Main).launch {
                        _user.value=GetMyModelEvent.Error(error.message)
                    }
                }

            })
    }


}

sealed class GetMyModelEvent {
    data class Success(val userModel: UserModel) : GetMyModelEvent()
    data class Error(val message: String) : GetMyModelEvent()
    data object Loading : GetMyModelEvent()
}