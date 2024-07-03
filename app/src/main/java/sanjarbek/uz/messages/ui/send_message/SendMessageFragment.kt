package sanjarbek.uz.messages.ui.send_message

import android.annotation.SuppressLint
import android.os.Build
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import sanjarbek.uz.messages.MassageApplication
import sanjarbek.uz.messages.R
import sanjarbek.uz.messages.core.base_activity.BaseFragment
import sanjarbek.uz.messages.core.dto.UserModel
import sanjarbek.uz.messages.databinding.FragmentSendMessageBinding
import java.text.SimpleDateFormat

class SendMessageFragment : BaseFragment<FragmentSendMessageBinding>() {

    private val myToken by lazy {
        MassageApplication.sharedPrefs.getString(MassageApplication.TOKEN, "")
    }

    private val viewModel: SendMessageViewModel by viewModels()

    private val sendMessageAdapter by lazy {
        SendMessageAdapter(requireContext())
    }

    private var friendToken: String? = null

    private var friendModel: UserModel? = null
    override fun getMyViewBinding(): FragmentSendMessageBinding {
        return FragmentSendMessageBinding.inflate(layoutInflater)
    }

    @SuppressLint("SetTextI18n")
    override fun setup() = with(binding) {

        myToolbar.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_sendMessageFragment_to_navigation_home)
        }

        rvMessage.adapter = sendMessageAdapter

        friendModel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getSerializable("friend_model", UserModel::class.java)
        } else {
            arguments?.getSerializable("friend_model") as UserModel
        }
        friendFullName.text = "${friendModel?.userSecondName} ${friendModel?.userName}"

        Firebase.storage.reference.child("images/${friendModel?.userImage}").downloadUrl
            .addOnSuccessListener {
                friendImage.load(it)
            }

        friendToken = friendModel?.userToken

        viewModel.getMessages(friendToken!!, myToken!!)


        btnSend.setOnClickListener {
            sendMessage()
        }
        observers()
    }

    private fun observers() {
        with(viewModel) {
            getMessageEvent.observe(this@SendMessageFragment) {
                when (it) {
                    is GetMessageEvent.Error -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }

                    GetMessageEvent.Loading -> {}
                    is GetMessageEvent.Success -> {
                        sendMessageAdapter.submitList(it.messageList.toList())
                        val manager =   binding.rvMessage.layoutManager as LinearLayoutManager
                        manager.scrollToPosition(it.messageList.size - 1)
                        binding.rvMessage.layoutManager = manager
                    }
                }
            }
        }
    }

    private fun sendMessage() {
        val message = binding.tvMessage.text?.toString()
        if (!message.isNullOrEmpty()) {
            viewModel.sendMessage(message, getCurrentTime(), friendToken!!, myToken!!)
            binding.tvMessage.setText("")
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun getCurrentTime(): String {

        return SimpleDateFormat("HH:mm").format(System.currentTimeMillis())
    }
}