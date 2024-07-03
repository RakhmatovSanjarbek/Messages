package sanjarbek.uz.messages.ui.chats

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

class ChatsViewModel : ViewModel() {

    private val _users = MutableLiveData<GetUserEvent>(GetUserEvent.Loading)
    val users: LiveData<GetUserEvent> = _users

    private val userDataList = mutableListOf<UserModel>()

    private var valueListener: ValueEventListener? = null

    fun getUsers() = viewModelScope.launch(Dispatchers.IO) {
        val myToken = MassageApplication.sharedPrefs.getString(MassageApplication.TOKEN, "")
        MassageApplication.dbReference.child("user")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    userDataList.clear()
                    snapshot.children.forEach {
                        it.getValue(UserModel::class.java)?.let { model ->
                            if (!myToken.equals(model.userToken)) {
                                userDataList.add(model)
                            }
                        }
                        CoroutineScope(Dispatchers.Main).launch {
                            _users.value = GetUserEvent.Success(userDataList)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    CoroutineScope(Dispatchers.Main).launch {
                        _users.value = GetUserEvent.Error(error.message)
                    }
                }

            })

    }


    fun removeListener() {
        this.valueListener?.let {
            MassageApplication.dbReference.removeEventListener(it)
        }
    }
}

sealed class GetUserEvent {
    data object Loading : GetUserEvent()
    data class Success(val userList: List<UserModel>) : GetUserEvent()
    data class Error(val message: String) : GetUserEvent()
}