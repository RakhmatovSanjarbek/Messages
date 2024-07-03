package sanjarbek.uz.messages.core.constants

import android.content.Intent
import sanjarbek.uz.messages.core.base_activity.BaseFragment
import sanjarbek.uz.messages.ui.auth.sign_in.SingInActivity

fun BaseFragment<*>.toLoginPage() {
    startActivity(Intent(requireContext(), SingInActivity::class.java))
    activity?.finish()
}