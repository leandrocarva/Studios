package com.example.carvalho.studios.fragments

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.carvalho.studios.R
import com.example.carvalho.studios.StudioActivity
import com.example.carvalho.studios.adapter.StudioAdapter
import com.example.carvalho.studios.controller.StudioService
import com.example.carvalho.studios.model.Studio
import com.example.carvalho.studios.util.Util

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Log
import com.example.carvalho.studios.database.UserDatabase
import com.example.carvalho.studios.model.UserPers
import kotlinx.android.synthetic.main.fragment_list_.*

class List_Fragment : Fragment() {


    lateinit var list: List<Studio>
    var idUser: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dao = UserDatabase.getDatabase(context.applicationContext)

        val user: UserPers? = GetAsyncTask(dao!!).execute().get()

        if(user != null){
            idUser = user.id
        }

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)

        val view: View = inflater!!.inflate(R.layout.fragment_list_, container, false)

        getStudios()

        return view
    }



    fun loadList() {
        val recyclerView: RecyclerView = rvStudios


        recyclerView.adapter = StudioAdapter(context!!, list, {

            val intentDetalhe = Intent(context, StudioActivity::class.java)

            intentDetalhe.putExtra("studio", it)

            startActivity(intentDetalhe)

        }, {
            Toast.makeText(context, "Delete ${it.nome}", Toast.LENGTH_LONG).show()
        })
        recyclerView.layoutManager = LinearLayoutManager(context)

    }

    fun getStudios() {



        if (!Util.isNetworkAvailable(context!!))
        {
            Toast.makeText(context, R.string.connection_accepted, Toast.LENGTH_SHORT).show()
        } else {
            StudioService.service.getStudios().enqueue(object : Callback<List<Studio>> {

                override fun onResponse(call: Call<List<Studio>>, response: Response<List<Studio>>) {
                    val studioResponse = response.body()
                    if(studioResponse != null) {
                        list = response.body()?.toList()!!
                        loadList()
                    }
                }
                override fun onFailure(call: Call<List<Studio>>?, t: Throwable?) {
                    Log.d("ERRO", t?.message)

                }
            })

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
