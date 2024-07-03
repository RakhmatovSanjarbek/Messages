package sanjarbek.uz.messages.ui.pin

import android.content.Intent
import androidx.core.content.ContextCompat
import sanjarbek.uz.messages.R
import sanjarbek.uz.messages.core.base_activity.BaseActivity
import sanjarbek.uz.messages.core.extension.toast
import sanjarbek.uz.messages.databinding.ActivityPinPageBinding
import sanjarbek.uz.messages.ui.MainActivity

class PinPageActivity : BaseActivity<ActivityPinPageBinding>() {
    private val pinList: MutableList<Int> = mutableListOf()
    override fun getMyViewBinding(): ActivityPinPageBinding {
        return ActivityPinPageBinding.inflate(layoutInflater)
    }

    override fun setup() = with(binding) {
        btn0.setOnClickListener {
            if (listSize(pinList)) {
                pinList.add(0)
                cards(pinList)
                toMainPage(pinList)
            }
        }
        btn1.setOnClickListener {
            if (listSize(pinList)) {
                pinList.add(1)
                cards(pinList)
                toMainPage(pinList)
            }
        }
        btn2.setOnClickListener {
            if (listSize(pinList)) {
                pinList.add(2)
                cards(pinList)
                toMainPage(pinList)
            }
        }
        btn3.setOnClickListener {
            if (listSize(pinList)) {
                pinList.add(3)
                cards(pinList)
                toMainPage(pinList)
            }
        }
        btn4.setOnClickListener {
            if (listSize(pinList)) {
                pinList.add(4)
                cards(pinList)
                toMainPage(pinList)
            }
        }
        btn5.setOnClickListener {
            if (listSize(pinList)) {
                pinList.add(5)
                cards(pinList)
                toMainPage(pinList)
            }
        }
        btn6.setOnClickListener {
            if (listSize(pinList)) {
                pinList.add(6)
                cards(pinList)
                toMainPage(pinList)
            }
        }
        btn6.setOnClickListener {
            if (listSize(pinList)) {
                pinList.add(6)
                cards(pinList)
                toMainPage(pinList)
            }
        }
        btn8.setOnClickListener {
            if (listSize(pinList)) {
                pinList.add(8)
                cards(pinList)
                toMainPage(pinList)
            }
        }
        btn9.setOnClickListener {
            if (listSize(pinList)) {
                pinList.add(9)
                cards(pinList)
                toMainPage(pinList)
            }
        }
    }

    private fun listSize(list: MutableList<Int>): Boolean {
        return list.size < 4
    }

    private fun cards(list: MutableList<Int>) {
        when {
            list.size < 2 -> {
                binding.card1.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white))
                binding.card2.setCardBackgroundColor(ContextCompat.getColor(this, R.color.gray))
                binding.card3.setCardBackgroundColor(ContextCompat.getColor(this, R.color.gray))
                binding.card4.setCardBackgroundColor(ContextCompat.getColor(this, R.color.gray))
            }

            list.size < 3 -> {
                binding.card1.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white))
                binding.card2.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white))
                binding.card3.setCardBackgroundColor(ContextCompat.getColor(this, R.color.gray))
                binding.card4.setCardBackgroundColor(ContextCompat.getColor(this, R.color.gray))
            }

            list.size < 4 -> {
                binding.card1.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white))
                binding.card2.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white))
                binding.card3.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white))
                binding.card4.setCardBackgroundColor(ContextCompat.getColor(this, R.color.gray))
            }

            else -> {
                binding.card1.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white))
                binding.card2.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white))
                binding.card3.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white))
                binding.card4.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white))
            }
        }
    }

    private fun toMainPage(list: MutableList<Int>) {
        if (list.size == 4 && list == listOf(2, 2, 2, 2)) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else if(list.size == 4 && list != listOf(2, 2, 2, 2)){
            toast("Pin kod xato! | [2,2,2,2]")
            list.clear()
            binding.card1.setCardBackgroundColor(ContextCompat.getColor(this, R.color.gray))
            binding.card2.setCardBackgroundColor(ContextCompat.getColor(this, R.color.gray))
            binding.card3.setCardBackgroundColor(ContextCompat.getColor(this, R.color.gray))
            binding.card4.setCardBackgroundColor(ContextCompat.getColor(this, R.color.gray))
        }
    }


}