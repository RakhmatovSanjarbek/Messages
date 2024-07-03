package sanjarbek.uz.messages.ui.send_message

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sanjarbek.uz.messages.MassageApplication
import sanjarbek.uz.messages.core.dto.ChatModel

class SendMessageViewModel : ViewModel() {

    private var vl: ValueEventListener? = null
    private val messageDataList = mutableListOf<ChatModel>()

    private val _sendMessageEvent = MutableLiveData<SendMessageEvent>()
    val sendMessageEvent: LiveData<SendMessageEvent> = _sendMessageEvent

    private val _getMessageEvent = MutableLiveData<GetMessageEvent>()
    val getMessageEvent: LiveData<GetMessageEvent> = _getMessageEvent

    fun getMessages(friendToken: String, myToken: String) {
        _getMessageEvent.value = GetMessageEvent.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val vl = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageDataList.clear()
                    snapshot.children.forEach {
                        val chatModel = it.getValue(ChatModel::class.java)
                        chatModel?.let(messageDataList::add)
                    }
                    _getMessageEvent.value = GetMessageEvent.Success(messageDataList)
                }

                override fun onCancelled(error: DatabaseError) {
                    _getMessageEvent.value = GetMessageEvent.Error(error.message)
                }

            }

            this@SendMessageViewModel.vl = vl
            this@SendMessageViewModel.vl?.let {
                val token = friendToken + myToken
                val chatId = token.toCharArray().sortedArray().joinToString("")
                MassageApplication.dbReference.child("chat").child(chatId).addValueEventListener(it)
            }
        }
    }

    fun sendMessage(
        message: String,
        sendMessageTime: String,
        friendToken: String,
        myToken: String
    ) {
        _sendMessageEvent.value = SendMessageEvent.Loading
        val chatModel = ChatModel(message, sendMessageTime, friendToken, myToken)
        viewModelScope.launch(Dispatchers.IO) {
            val token = friendToken + myToken
            val chatId = token.toCharArray().sortedArray().joinToString("")
            MassageApplication.dbReference.child("chat").child(chatId).push().setValue(chatModel)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        _sendMessageEvent.value = SendMessageEvent.Success
                    } else {
                        _sendMessageEvent.value =
                            SendMessageEvent.Error(it.exception?.message.toString())
                    }
                }.addOnFailureListener {
                    _sendMessageEvent.value = SendMessageEvent.Error(it.message.toString())
                }
        }
    }

    fun removeListener() {
        this.vl?.let {
            MassageApplication.dbReference.removeEventListener(it)
        }
    }
}

sealed class SendMessageEvent {
    data object Success : SendMessageEvent()
    data class Error(val message: String) : SendMessageEvent()
    data object Loading : SendMessageEvent()
}

sealed class GetMessageEvent {
    data class Success(val messageList: List<ChatModel>) : GetMessageEvent()
    data class Error(val message: String) : GetMessageEvent()
    data object Loading : GetMessageEvent()
}