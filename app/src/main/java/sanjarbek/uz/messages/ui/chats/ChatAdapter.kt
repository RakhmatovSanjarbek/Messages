package sanjarbek.uz.messages.ui.chats

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import sanjarbek.uz.messages.MassageApplication
import sanjarbek.uz.messages.core.dto.UserModel
import sanjarbek.uz.messages.databinding.ItemChatsBinding

class ChatAdapter(
    private val onClick: (UserModel) -> Unit
) : ListAdapter<UserModel, ChatAdapter.ChatViewHolder>(diffUtil) {

    inner class ChatViewHolder(
        private val binding: ItemChatsBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(userModel: UserModel) {
            with(binding) {
                tvFullName.text =
                    "${userModel.userSecondName.orEmpty()} ${userModel.userName.orEmpty()}"

                val uri = Firebase.storage.reference.child("images/${userModel.userImage}").downloadUrl

                uri.addOnSuccessListener {
                    imUser.load(it)
                }
                root.setOnClickListener {
                    onClick(userModel)
                }
            }
        }
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<UserModel>() {
            override fun areItemsTheSame(oldItem: UserModel, newItem: UserModel): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: UserModel, newItem: UserModel): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        return ChatViewHolder(
            ItemChatsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}