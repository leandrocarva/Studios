package com.example.carvalho.studios

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.os.AsyncTask
import android.widget.Toast
import android.util.Log
import com.example.carvalho.studios.controller.UserService
import com.example.carvalho.studios.database.UserDatabase
import com.example.carvalho.studios.model.User
import com.example.carvalho.studios.util.Util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.carvalho.studios.model.UserPers
import kotlinx.android.synthetic.main.activity_login.*



class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btLogin.setOnClickListener { login_enter() }
        btAdd_User.setOnClickListener { add_User() }

    }

    fun login_enter() {
        if (etEmail.text.isEmpty()) {
            Toast.makeText(this, R.string.write_email, Toast.LENGTH_SHORT).show()
        } else if (etPassword.text.isEmpty()) {
            Toast.makeText(this, R.string.write_password, Toast.LENGTH_SHORT).show()
        } else if (!Util.isNetworkAvailable(this))
        {
            Toast.makeText(this, R.string.connection_accepted, Toast.LENGTH_SHORT).show()
        } else {
            val user: User = User(0, etEmail.text.toString(), etPassword.text.toString(), "")


            UserService.service.veriUser(user).enqueue(object : Callback<User> {

                override fun onResponse(call: Call<User>, response: Response<User>) {

                    Log.d("user", response.body()?.toString())
                    val userResponse = response.body()?.copy()
                    if (userResponse?.id == -2){
                        Toast.makeText(applicationContext, R.string.email_not_found, Toast.LENGTH_LONG).show()
                    } else if (userResponse?.id == -1){
                        Toast.makeText(applicationContext, R.string.incorrect_password, Toast.LENGTH_LONG).show()
                    } else {

                        val userP: UserPers = UserPers(userResponse!!.id , etEmail.text.toString(), cbConnected.isChecked, "")

                        val dao = UserDatabase.getDatabase(applicationContext)

                        DeleteAsyncTask(dao!!).execute()

                        InsertAsyncTask(dao!!).execute(userP)

                        val intent = Intent(this@LoginActivity,
                                MainActivity::class.java)
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        startActivity(intent)
                        this@LoginActivity.finish()
                    }
                }
                override fun onFailure(call: Call<User>?, t: Throwable?) {
                    Toast.makeText(applicationContext, R.string.error_logging, Toast.LENGTH_LONG).show()

                }
            })

        }
    }



    fun add_User() {


        if (etEmail.text.isEmpty()) {
            Toast.makeText(this, R.string.write_email, Toast.LENGTH_SHORT).show()
        } else if (etPassword.text.isEmpty()) {
            Toast.makeText(this, R.string.write_password, Toast.LENGTH_SHORT).show()
        } else if (!Util.isNetworkAvailable(this))
        {
            Toast.makeText(this, R.string.connection_accepted, Toast.LENGTH_SHORT).show()
        } else {
            val user: User = User(0, etEmail.text.toString(), etPassword.text.toString(), "")


            UserService.service.createUser(user).enqueue(object : Callback<User> {

                override fun onResponse(call: Call<User>, response: Response<User>) {
                    val userResponse = response.body()?.copy()
                    if(userResponse?.id == -1) {
                        Toast.makeText(applicationContext, R.string.exist_user, Toast.LENGTH_LONG).show()
                    } else if(userResponse?.id != 0) {

                        val userP: UserPers = UserPers(userResponse!!.id , etEmail.text.toString(), cbConnected.isChecked, "")

                        val dao = UserDatabase.getDatabase(applicationContext)

                        DeleteAsyncTask(dao!!).execute()

                        InsertAsyncTask(dao!!).execute(userP)

                        Toast.makeText(applicationContext, R.string.user_registered, Toast.LENGTH_LONG).show()

                        val intent = Intent(this@LoginActivity,
                                MainActivity::class.java)
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        startActivity(intent)
                        this@LoginActivity.finish()

                    }
                }
                override fun onFailure(call: Call<User>?, t: Throwable?) {
                    Log.d("ERRO", t?.message)

                }
            })

        }
    }

    private inner class InsertAsyncTask internal constructor(appDatabase: UserDatabase) : AsyncTask<UserPers, Void, String>() {
        private val db: UserDatabase = appDatabase

        override fun doInBackground(vararg params: UserPers): String {
            db.userDao().insertUser(params[0])
            return ""
        }
    }

    private inner class DeleteAsyncTask internal constructor(appDatabase: UserDatabase) : AsyncTask<Void, Void, String>() {
        private val db: UserDatabase = appDatabase

        override fun doInBackground(vararg params: Void): String {
            db.userDao().deleteUser()
            return ""
        }
    }

    private inner class GetAsyncTask internal constructor(appDatabase: UserDatabase) : AsyncTask<Void, Void, UserPers>() {
        private val db: UserDatabase = appDatabase

        override fun doInBackground(vararg params: Void): UserPers {
            val user :UserPers = db.userDao().getUser()
            return user
        }
    }


}
