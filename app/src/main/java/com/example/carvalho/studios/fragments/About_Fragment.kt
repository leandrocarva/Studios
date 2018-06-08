package com.example.carvalho.studios.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.carvalho.studios.LoginActivity
import com.example.carvalho.studios.MainActivity
import com.example.carvalho.studios.R
import com.example.carvalho.studios.database.UserDatabase
import kotlinx.android.synthetic.main.fragment_about_.*


class About_Fragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        //return inflater.inflate(R.layout.fragment_about_, container, false)
        val view = inflater!!.inflate(R.layout.fragment_about_, container, false)

        val btOut: Button = view.findViewById(R.id.btLogout)

        btOut.setOnClickListener { log_out() }

        return view

    }

    fun log_out() {


        val dao = UserDatabase.getDatabase(context)
        DeleteAsyncTask(dao!!).execute()
        val intent = Intent(context,  LoginActivity::class.java)
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        startActivity(intent)


    }

    private inner class DeleteAsyncTask internal constructor(appDatabase: UserDatabase) : AsyncTask<Void, Void, String>() {
        private val db: UserDatabase = appDatabase

        override fun doInBackground(vararg params: Void): String {
            db.userDao().deleteUser()
            return ""
        }
    }

}
