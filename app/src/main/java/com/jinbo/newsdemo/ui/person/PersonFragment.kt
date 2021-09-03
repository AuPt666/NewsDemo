package com.jinbo.newsdemo.ui.person

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.jinbo.newsdemo.R
import de.hdodenhof.circleimageview.CircleImageView

/***********个人信息页面**************/
class PersonFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_person, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //跳转浏览历史
        val historyTextView: TextView = requireActivity().findViewById(R.id.person_history_textView)
        historyTextView.setOnClickListener{
            val intent = Intent(context, HistoryActivity::class.java)
            startActivity(intent)
        }

        //跳转编辑个人资料
        val userAvatar: CircleImageView = requireActivity().findViewById(R.id.person_userAvatar_circleImageView)
        userAvatar.setOnClickListener {
            val intent = Intent(context, UserDataActivity::class.java)
            startActivity(intent)
        }
    }
}