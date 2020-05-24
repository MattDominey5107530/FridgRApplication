package com.example.fridgr.popups

import android.content.Context
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.fridgr.R
import com.example.fridgr.local_storage.writeUserNickname

class EditNicknamePopup(
    context: Context,
    onDismissListener: (AbstractPopup) -> Unit,
    nickname: String
) :
    AbstractPopup(
        context,
        R.layout.popupwindow_edit_nickname, onDismissListener, 300, 100
    ) {

    private val userNicknameEditText: EditText = view.findViewById<EditText>(R.id.edtUserNickname).apply {
        hint = nickname
    }

    var newNickname = nickname

    init {
        view.findViewById<Button>(R.id.btnChangeNickname)
            .setOnClickListener {
                val newNickname = userNicknameEditText.text.toString()
                if (newNickname != "") {
                    writeUserNickname(context, newNickname)
                    Toast.makeText(context, "Nickname changed!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Nickname cannot be empty.", Toast.LENGTH_SHORT).show()
                }
                popupWindow.dismiss()
            }
    }

    override fun fetchFields() {
        newNickname = userNicknameEditText.text.toString()
    }
}