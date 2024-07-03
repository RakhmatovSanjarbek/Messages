package sanjarbek.uz.messages.ui.auth.sign_up

import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sanjarbek.uz.messages.MassageApplication
import sanjarbek.uz.messages.core.dto.UserModel

class SignUpViewModel : ViewModel() {

    private val _signUpEvent = MutableLiveData<SignUpEvent>()
    val signUpEvent: LiveData<SignUpEvent> get() = _signUpEvent

    private val _saveUserEvent = MutableLiveData<SaveUserEvent>()
    val saveUserEvent: LiveData<SaveUserEvent> get() = _saveUserEvent

    private val _saveUserImage = MutableLiveData<SaveUserImageEvent>()
    val saveUserImage: LiveData<SaveUserImageEvent> get() = _saveUserImage

    fun signUp(
        email: String,
        password: String,
    ) {
        _signUpEvent.value = SignUpEvent.Loading
        viewModelScope.launch(Dispatchers.IO) {
            MassageApplication.auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    viewModelScope.launch(Dispatchers.Main) {
                        if (it.isSuccessful) {

                            _signUpEvent.value = SignUpEvent.Success(it.result.user?.uid!!)

                        } else {
                            _signUpEvent.value = SignUpEvent.Error(it.exception?.message.toString())
                        }
                    }
                }
                .addOnFailureListener {
                    viewModelScope.launch(Dispatchers.Main) {
                        _signUpEvent.value = SignUpEvent.Error(it.message.toString())
                    }
                }

        }
    }

    fun saveImage(uri: String, name: String) {
        _saveUserImage.value=SaveUserImageEvent.Loading
        viewModelScope.launch(Dispatchers.IO) {
            Firebase.storage.reference.child("images").child(name).putFile(uri.toUri())
                .addOnFailureListener {
                    viewModelScope.launch(Dispatchers.Main) {
                        _saveUserImage.value = SaveUserImageEvent.Error(it.message)
                    }
                }.addOnSuccessListener {
                    viewModelScope.launch(Dispatchers.Main) {
                        _saveUserImage.value = SaveUserImageEvent.Success(name)
                    }
                }
        }
    }

    fun saveUserToDatabase(userModel: UserModel) {
        _saveUserEvent.value = SaveUserEvent.Loading
        viewModelScope.launch(Dispatchers.IO) {
            MassageApplication.dbReference.child("user").push()
                .setValue(
                   userModel
                ).addOnCompleteListener {
                    viewModelScope.launch(Dispatchers.Main) {
                        if (it.isSuccessful) {
                            _saveUserEvent.value =
                                SaveUserEvent.Success(userModel.userToken!!)
                        } else {
                            _saveUserEvent.value =
                                SaveUserEvent.Error(it.exception?.message.toString())
                        }
                    }
                }.addOnFailureListener {
                    viewModelScope.launch(Dispatchers.Main) {
                        _saveUserEvent.value = SaveUserEvent.Error(it.message.toString())
                    }
                }
        }
    }
}

sealed class SaveUserEvent {
    data class Success(val message: String) : SaveUserEvent()
    data class Error(val message: String) : SaveUserEvent()
    data object Loading : SaveUserEvent()
}

sealed class SignUpEvent {
    data class Success(val token: String) : SignUpEvent()
    data class Error(val message: String) : SignUpEvent()
    data object Loading : SignUpEvent()
}

sealed class SaveUserImageEvent {
    data class Success(val token: String) : SaveUserImageEvent()
    data class Error(val message: String?) : SaveUserImageEvent()
    data object Loading : SaveUserImageEvent()
}