package sanjarbek.uz.messages.ui.chats

import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import sanjarbek.uz.messages.R
import sanjarbek.uz.messages.core.base_activity.BaseFragment
import sanjarbek.uz.messages.core.dto.UserModel
import sanjarbek.uz.messages.databinding.FragmentChatsBinding
import sanjarbek.uz.messages.ui.send_message.SendMessageFragment

class ChatsPageFragment : BaseFragment<FragmentChatsBinding>() {

    private val viewModel: ChatsViewModel by viewModels()

    private val chatAdapter by lazy {
        ChatAdapter {
            val bundle = Bundle()
            bundle.putSerializable("friend_model", it)
            println(":::::::: ChatsPageFragment $it")
            navigationToSendMessagePage(bundle)
        }
    }

    private fun navigationToSendMessagePage(bundle:Bundle) {
        findNavController().navigate(R.id.action_navigation_home_to_sendMessageFragment,bundle)
    }

    override fun getMyViewBinding(): FragmentChatsBinding {
        return FragmentChatsBinding.inflate(layoutInflater)
    }

    override fun setup() = with(binding) {
        rv.adapter = chatAdapter
        viewModel.getUsers()

        viewModel.users.observe(this@ChatsPageFragment) {
            when (it) {
                is GetUserEvent.Error -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    lotteAnim.isVisible = false
                }

                is GetUserEvent.Success -> {
                    chatAdapter.submitList(it.userList.toList())
                    lotteAnim.isVisible = false
                }

                is GetUserEvent.Loading -> {
                    lotteAnim.isVisible = true
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.removeListener()
    }
}