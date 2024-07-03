package sanjarbek.uz.messages.ui.send_message

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import sanjarbek.uz.messages.MassageApplication
import sanjarbek.uz.messages.R
import sanjarbek.uz.messages.core.dto.ChatModel
import sanjarbek.uz.messages.databinding.ItemSendMessageBinding

class SendMessageAdapter(
    private val context: Context
) : ListAdapter<ChatModel, SendMessageAdapter.SendMessageVH>(diffUtil) {

    inner class SendMessageVH(
        private val binding: ItemSendMessageBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(chatModel: ChatModel) {
            binding.tvMessage.text = chatModel.message
            binding.tvTime.text = chatModel.sendMessageTime

            val myToken = MassageApplication.sharedPrefs.getString(MassageApplication.TOKEN, "")

            val lP = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT
            )

            if (myToken != chatModel.myToken) {
                lP.gravity = Gravity.START
                lP.topMargin = 16
                lP.marginStart = 24
                lP.marginEnd = 200
                binding.cardRoot.setCardBackgroundColor(ContextCompat.getColor(context, R.color.get_message_color))
                binding.cardRoot.layoutParams = lP
            } else {
                lP.gravity = Gravity.END
                lP.topMargin = 16
                lP.marginEnd = 24
                lP.marginStart = 200
                binding.cardRoot.setCardBackgroundColor(ContextCompat.getColor(context, R.color.send_message_color))
                binding.cardRoot.layoutParams = lP
            }
        }
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<ChatModel>() {
            override fun areItemsTheSame(oldItem: ChatModel, newItem: ChatModel): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ChatModel, newItem: ChatModel): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SendMessageVH {
        return SendMessageVH(
            ItemSendMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: SendMessageVH, position: Int) {
        holder.bind(getItem(position))
    }
}