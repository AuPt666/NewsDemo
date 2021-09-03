package com.jinbo.newsdemo.ui.person

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.jinbo.newsdemo.R
import com.jinbo.newsdemo.logic.Repository
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
        val historyTextView: Button = requireActivity().findViewById(R.id.person_history_textView)
        historyTextView.setOnClickListener{
            val intent = Intent(context, HistoryActivity::class.java)
            startActivity(intent)
        }

        //跳转编辑个人资料
        val userAvatar: CircleImageView = requireActivity().findViewById(R.id.person_userAvatar_circleImageView)
        val userNameTextView: TextView = requireActivity().findViewById(R.id.person_userName_textView)
        val userIntroductionTextView: TextView = requireActivity().findViewById(R.id.person_userSketch_textView)
        val userGenderTextView: TextView = requireActivity().findViewById(R.id.person_userGender_textView)
        val setUserDataButton : Button = requireActivity().findViewById(R.id.person_setting_textView)
        val listener = View.OnClickListener {
            val intent = Intent(context, UserDataActivity::class.java)
            startActivity(intent)
        }
        userAvatar.setOnClickListener(listener)
        userNameTextView.setOnClickListener(listener)
        userIntroductionTextView.setOnClickListener(listener)
        userGenderTextView.setOnClickListener(listener)
        setUserDataButton.setOnClickListener(listener)
    }

    override fun onResume() {
        super.onResume()
        val userNameTextView: TextView = requireView().findViewById(R.id.person_userName_textView)
        val userIntroductionTextView: TextView = requireView().findViewById(R.id.person_userSketch_textView)
        val userGenderTextView: TextView = requireView().findViewById(R.id.person_userGender_textView)
        userNameTextView.text = Repository.getUserName()
        userIntroductionTextView.text = Repository.getUserIntroduction()
        userGenderTextView.text = Repository.getUserGender()
    }
}