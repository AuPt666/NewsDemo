package com.jinbo.newsdemo

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jinbo.newsdemo.logic.Repository
import com.jinbo.newsdemo.ui.home.HomeFragment
import com.jinbo.newsdemo.ui.island.IslandFragment
import com.jinbo.newsdemo.ui.person.PersonFragment
import java.util.ArrayList


/***********主页容器**************/
class MainActivity : AppCompatActivity() {

    private var lastIndex = 0
    private var mFragmentList = ArrayList<Fragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mFragmentList.apply {
            add(HomeFragment())
            add(IslandFragment())
            add(PersonFragment())
        }
        setFragmentPosition(0)
        val mainActivityNavView: BottomNavigationView = findViewById(R.id.main_activity_nav_view)

        mainActivityNavView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId){
                R.id.navigation_home -> setFragmentPosition(0)
                R.id.navigation_island -> setFragmentPosition(1)
                R.id.navigation_person -> setFragmentPosition(2)
            }
            true
        })

        val deleteDbBtn: Button = findViewById(R.id.delete_database)
        deleteDbBtn.setOnClickListener {
            Thread{
                Repository.deleteDataBase(BaseApplication.context)
            }.start()
            Toast.makeText(this, "删除数据库", Toast.LENGTH_SHORT).show()
        }
    }

    //加载对应position的Fragment
    private fun setFragmentPosition(position: Int) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val currentFragment  = mFragmentList[position]
        val lastFragment = mFragmentList[lastIndex]
        lastIndex = position
        fragmentTransaction.hide(lastFragment)
        if (!currentFragment.isAdded) {
            supportFragmentManager.beginTransaction().remove(currentFragment).commit()
            fragmentTransaction.add(R.id.main_activity_ly, currentFragment)
        }
        fragmentTransaction.show(currentFragment)
        fragmentTransaction.commitAllowingStateLoss()
    }
}