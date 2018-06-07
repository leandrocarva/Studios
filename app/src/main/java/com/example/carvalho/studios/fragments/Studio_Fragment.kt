package com.example.carvalho.studios.fragments

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.example.carvalho.studios.R
import com.example.carvalho.studios.StudioActivity
import com.example.carvalho.studios.adapter.StudioAdapter
import com.example.carvalho.studios.controller.StudioService
import com.example.carvalho.studios.database.UserDatabase


import com.example.carvalho.studios.model.Studio
import com.example.carvalho.studios.model.UserPers
import com.example.carvalho.studios.util.Util
import kotlinx.android.synthetic.main.fragment_list_.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Studio_Fragment : Fragment() {

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_studio_, container, false)
        val view: View = inflater!!.inflate(R.layout.fragment_studio_, container, false)

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
            delStudio(it)
        },{
            val addFragment = Add_Fragment()
            val bundle: Bundle = Bundle()
            bundle.putParcelable("studio", it)

            addFragment.arguments = bundle

            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.content_main, addFragment)
            fragmentTransaction.commit()

        }, {
            share(it)})
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

    fun share(studio: Studio) {

        //fun bindView(studio: Studio, listener: (Studio) -> Unit, listenerDelete: (Studio) -> Unit, listenerEdit: (Studio) -> Unit) = with(itemView) {
            val intentShare = Intent();
            intentShare.action = Intent.ACTION_SEND
            intentShare.putExtra(Intent.EXTRA_SUBJECT, "Studio ${studio.nome}")
            intentShare.putExtra(Intent.EXTRA_TEXT, "Studio: ${studio.nome}, ${getResources().getString(R.string.add_phone)}: ${studio.tel}, ${getResources().getString(R.string.add_zip_code)}: ${studio.cep}")
            intentShare.type = "text/html"
            val bd: Bundle? = Bundle()
            ContextCompat.startActivity(context, Intent.createChooser(intentShare, context.getString(R.string.share)), bd!!);
        //}
    }

    fun delStudio(studioDel: Studio){
        if (!Util.isNetworkAvailable(context)) {
            Toast.makeText(context, R.string.connection_accepted, Toast.LENGTH_SHORT).show()
        } else {
            StudioService.service.delStudios(studioDel.seq_studio).enqueue(object : Callback<Studio> {

                override fun onResponse(call: Call<Studio>, response: Response<Studio>) {

                    Log.d("user", response.body()?.toString())
                    val StudioResponse = response.body()?.copy()
                    if (StudioResponse?.seq_studio == -1){
                        Toast.makeText(context, StudioResponse.nome, Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(context, getString(R.string.list_studio_deleted), Toast.LENGTH_LONG).show()
                        getStudios()

                    }
                }
                override fun onFailure(call: Call<Studio>?, t: Throwable?) {
                    Toast.makeText(context, R.string.error_logging, Toast.LENGTH_LONG).show()

                }
            })
        }
    }


}
