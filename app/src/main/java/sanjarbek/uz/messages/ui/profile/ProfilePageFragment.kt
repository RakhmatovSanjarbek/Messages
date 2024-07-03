package sanjarbek.uz.messages.ui.profile

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import coil.load
import com.google.android.material.button.MaterialButton
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sanjarbek.uz.messages.MassageApplication
import sanjarbek.uz.messages.R
import sanjarbek.uz.messages.core.base_activity.BaseFragment
import sanjarbek.uz.messages.core.constants.toLoginPage
import sanjarbek.uz.messages.core.dto.UserModel
import sanjarbek.uz.messages.databinding.FragmentProfileBinding

class ProfilePageFragment : BaseFragment<FragmentProfileBinding>() {


    private val viewModel: ProfileViewModel by viewModels()

    private lateinit var user: UserModel

    override fun getMyViewBinding(): FragmentProfileBinding {
        return FragmentProfileBinding.inflate(layoutInflater)
    }

    @SuppressLint("SetTextI18n")
    override fun setup() = with(binding) {
        viewModel.getMyModel()

        logOut.setOnClickListener {
            showAlertDialog()
        }

        viewModel.user.observe(this@ProfilePageFragment) {
            when (it) {
                is GetMyModelEvent.Error -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    lotteAnim.isVisible = true
                    myProfileUi.isVisible = false

                }

                GetMyModelEvent.Loading -> {
                    lotteAnim.isVisible = true
                    myProfileUi.isVisible = false

                }

                is GetMyModelEvent.Success -> {
                    lotteAnim.isVisible = false
                    myProfileUi.isVisible = true
                    user = it.userModel

                    userFullName.text = "${user.userSecondName}  ${user.userName}"

                    Firebase.storage.reference.child("images/${user.userImage}").downloadUrl
                        .addOnSuccessListener { image ->
                            profileImage.load(image)
                        }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showAlertDialog() {
        val inflater = LayoutInflater.from(requireContext())
        val dialogView = inflater.inflate(R.layout.dialog_custom, null)

        val builder = AlertDialog.Builder(requireContext())
        builder.setView(dialogView)

        val dialogTitle = dialogView.findViewById<TextView>(R.id.dialog_title)
        val dialogMessage = dialogView.findViewById<TextView>(R.id.dialog_message)
        val positiveButton = dialogView.findViewById<MaterialButton>(R.id.positive_button)
        val negativeButton = dialogView.findViewById<MaterialButton>(R.id.negative_button)

        dialogTitle.text = "Diqqat!"
        dialogMessage.text = "Siz haqiqatdan ham chiqmoqchimisiz"

        negativeButton.text = "Bekor qilish"
        positiveButton.text = "Ha"

        val dialog = builder.create()

        positiveButton.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                toLoginPage()
                MassageApplication.sharedPrefs.edit().clear().apply()
            }
            dialog.dismiss()
        }

        negativeButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.show()
        dialog.setCancelable(false)
    }

}