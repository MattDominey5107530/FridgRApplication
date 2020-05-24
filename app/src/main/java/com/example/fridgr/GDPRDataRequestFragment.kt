package com.example.fridgr

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

class GDPRDataRequestFragment: Fragment() {
    //KFunction to switch between fragments
    private lateinit var switchToFragment: (Fragment, Fragment) -> Unit
    private var myParentFragment: Fragment? = null
    private var requestText = ""

    companion object {
        fun newInstance(
            switchToFragment: (Fragment, Fragment) -> Unit,
            parentFragment: Fragment? = null,
            requestText: String
        ): GDPRDataRequestFragment =
            GDPRDataRequestFragment().apply {
                this.switchToFragment = switchToFragment
                this.myParentFragment = parentFragment
                this.requestText = requestText
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val v: View =
            inflater.inflate(R.layout.fragment_gdpr_data_request, container, false)

        //Setup components
        v.findViewById<ImageButton>(R.id.imbBack)
            .setOnClickListener {
                switchToFragment(this, myParentFragment!!)
            }

        v.findViewById<TextView>(R.id.txvGDPRRequestText)
            .text = requestText

        v.findViewById<Button>(R.id.btnCopyText)
            .setOnClickListener {
                val myClipboardService: ClipboardManager =
                    context!!.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                val myClip = ClipData.newPlainText("text", requestText)
                myClipboardService.setPrimaryClip(myClip)
                Toast.makeText(context, "Text Copied", Toast.LENGTH_SHORT).show()
            }

        return v
    }

}