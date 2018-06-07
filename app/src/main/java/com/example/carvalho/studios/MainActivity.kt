package com.example.carvalho.studios

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
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

        checkPermission()

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

    private fun checkPermission() : Boolean {
        var ret: Boolean = false
        val permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.CALL_PHONE)) {

                val builder = AlertDialog.Builder(this)

                builder.setMessage("Necessária a permissao para fazer ligações")
                        .setTitle("Permissao Requerida")

                builder.setPositiveButton("OK") {
                    dialog, id ->
                    requestPermission()
                }

                val dialog = builder.create()
                dialog.show()

            } else {
                requestPermission()
            }
        } else {
            ret = true
        }
        return ret
    }

    protected fun requestPermission() {
        ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CALL_PHONE), 0)
    }

}
