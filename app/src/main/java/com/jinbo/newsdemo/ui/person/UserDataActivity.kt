package com.jinbo.newsdemo.ui.person

import android.content.DialogInterface
import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.jinbo.newsdemo.R
import com.jinbo.newsdemo.logic.Repository


/**************用户个人信息编辑界面***************/
class UserDataActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_userdata)

        //设置toolBar相关功能
        val toolbar: Toolbar = findViewById(R.id.userData_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        //设置编辑昵称功能
        val nameTextView: TextView = findViewById(R.id.userData_name_textView)
        nameTextView.setOnClickListener { showChangeNameDialogView() }

        //设置修改性别功能
        val genderTextView: TextView = findViewById(R.id.userData_gender_textView)
        genderTextView.setOnClickListener { showChangeGenderDialogView() }

        //设置编辑界面功能
        val introductionTextView: TextView = findViewById(R.id.userData_introduction_textView)
        introductionTextView.setOnClickListener { showIntroductionDialogView() }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
          if (item.itemId == android.R.id.home){
                finish()
                return true
            }
        return super.onOptionsItemSelected(item)
    }


    //展示修改昵称界面的弹窗
    private fun showChangeNameDialogView(){

        val changeNameDialogView: View = LayoutInflater.from(this).inflate(R.layout.userdata_changename_dialog, null)
        val changeNameEditText: EditText = changeNameDialogView.findViewById(R.id.userDataDialog_changeName_edt)

        val builder = AlertDialog.Builder(this)
        builder.apply {
            setTitle("编辑姓名")
            setView(changeNameDialogView)
            setPositiveButton("确定") { _, _ ->
                val newName = changeNameEditText.text.toString()
                if (newName.isNotEmpty()){
                    //将新修改的昵称保存
                    Toast.makeText(context, "新昵称：$newName", Toast.LENGTH_SHORT).show()
                    Repository.saveUserName(newName)
                }else{
                    Toast.makeText(context, "昵称为空，修改失败", Toast.LENGTH_SHORT).show()
                }
            }
            setNegativeButton("取消"){ _, _ ->
                Toast.makeText(context, "取消修改昵称", Toast.LENGTH_SHORT).show()
            }
            setCancelable(true)
            show()
        }
    }

    //展示更改性别界面的弹窗
    private fun showChangeGenderDialogView(){
        val genderList = arrayOf<CharSequence>("保密", "男", "女")
        val builder = AlertDialog.Builder(this)
        var choice = 0//默认选项是保密
        builder.apply {
            setTitle("更改性别")
            setSingleChoiceItems(genderList, 0){ _, which ->
                choice = which
            }
            setPositiveButton("确定"){ _, _ ->
                Repository.saveUserGender("${genderList[choice]}")
                Toast.makeText(context, "更改性:${genderList[choice]}", Toast.LENGTH_SHORT).show()
            }
            setCancelable(true)
            show()
        }

    }

    //展示编辑简介界面的弹窗
    private fun showIntroductionDialogView(){
        val introductionDialogView: View = LayoutInflater.from(this).inflate(R.layout.userdata_introduction_dialog, null)
        val introductionEditText: EditText = introductionDialogView.findViewById(R.id.userDataDialog_introduction_edt)

        val builder = AlertDialog.Builder(this)
        builder.apply {
            setTitle("编辑简介")
            setView(introductionDialogView)
            setPositiveButton("确定"){ _, _ ->
                val introductionText = introductionEditText.text.toString()
                if (introductionText.isNotEmpty()){
                    Repository.saveUserIntroduction(introductionText)
                }else {
                    Toast.makeText(context, "个人简介不能为空", Toast.LENGTH_SHORT).show()
                }
            }
            setNegativeButton("取消"){ _, _ ->
                Toast.makeText(context, "取消编辑个人简介", Toast.LENGTH_SHORT).show()
            }
            setCancelable(true)
            show()
        }
    }
}