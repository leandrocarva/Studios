package com.example.carvalho.studios

import android.os.Bundle
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.example.carvalho.studios.fragments.About_Fragment
import com.example.carvalho.studios.fragments.Add_Fragment
import com.example.carvalho.studios.fragments.Studio_Fragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener =  OnNavigationItemSelectedListener { item ->

        when (item.itemId) {
            R.id.navigation_home -> {
                changeFragment(Studio_Fragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                changeFragment(Add_Fragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                changeFragment(About_Fragment())
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        var fragment = savedInstanceState?.getString("fragment") ?: "nulo"


        if( fragment == "nulo" ) {
            changeFragment(Studio_Fragment())
        }

    }

    private fun changeFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.content_main, fragment)
        fragmentTransaction.commit()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putString("fragment", "foi")
        super.onSaveInstanceState( outState )
    }

    override fun onResume() {
        super.onResume()
    }
}
