package com.example.carvalho.studios.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.example.carvalho.studios.R
import com.example.carvalho.studios.controller.StudioService
import com.example.carvalho.studios.database.UserDatabase
import com.example.carvalho.studios.model.Studio
import com.example.carvalho.studios.model.UserPers
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import android.util.Base64
import com.example.carvalho.studios.MapsActivity
import com.example.carvalho.studios.StudioActivity
import com.example.carvalho.studios.util.ImageUtil
import kotlinx.android.synthetic.main.fragment_add_.*
import kotlinx.android.synthetic.main.fragment_add_.view.*

class Add_Fragment : Fragment() {

    var file: File? = null
    var idUser: Int = 0
    var studio: Studio? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        retainInstance = true

        val dao = UserDatabase.getDatabase(context.applicationContext)

        val user: UserPers? = GetAsyncTask(dao!!).execute().get()


        if(user != null){
            idUser = user.id
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater!!.inflate(R.layout.fragment_add_, container, false)

        val imageView: ImageView = view.findViewById(R.id.ivAddStudio)

        val btSave: Button = view.findViewById(R.id.btAdd_Save)

        val btnMap: Button = view.findViewById(R.id.btMap)

        imageView.setOnClickListener{loadPhoto()}

        btSave.setOnClickListener { saveStudio() }

        btnMap.setOnClickListener { loadMap() }

        if(arguments != null) {
            studio = arguments!!.getParcelable<Studio>("studio")
            btSave.setText(getString(R.string.add_up_studio) )

            view.etAddStudioName.setText(studio?.nome)
            view.etAddZipCode.setText(studio?.cep)
            view.etAddAddress.setText(studio?.endereco)
            view.etAddNumber.setText(studio?.numero.toString())
            view.etAddDistrict.setText(studio?.bairro)
            view.etAddComplement.setText(studio?.complemento)
            view.etAddCity.setText(studio?.cidade)
            view.etAddObs.setText(studio?.obs)
            Picasso.with(context)
                    .load(studio?.path_foto)
                    .resize(100,100)
                    .into(view.ivAddStudio)

        }



        if (file != null) {
            val options = BitmapFactory.Options()
            val bitmap = BitmapFactory.decodeFile(file!!.absolutePath, options)
            imageView.setImageBitmap(bitmap)
        }


        return view
    }

    private fun loadMap() {

        if (etAddZipCode.text.isEmpty()) {
            Toast.makeText(context, R.string.write_zip_code, Toast.LENGTH_SHORT).show()
        } else {

            val intentDetalhe = Intent(context, MapsActivity::class.java)

            intentDetalhe.putExtra("studio", studio)

            startActivity(intentDetalhe)
        }
    }
    private fun loadPhoto() {

        val f = getSdCardFile("studio.jpg")
        file = f;


        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_PICK
        startActivityForResult(Intent.createChooser(intent, "Select File"), 123)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 123) {

                var bitmap: Bitmap =  MediaStore.Images.Media.getBitmap(context.getContentResolver(), data!!.getData());

                val fos: FileOutputStream
                fos = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)


                showImage(file)
            }
        }
    }

    private fun saveStudio() {
        if (etAddStudioName.text.isEmpty()) {
            Toast.makeText(context, R.string.write_studio_name, Toast.LENGTH_SHORT).show()
        } else if (etAddAddress.text.isEmpty()) {
            Toast.makeText(context, R.string.write_address, Toast.LENGTH_SHORT).show()
        } else if (etAddNumber.text.isEmpty()) {
            Toast.makeText(context, R.string.write_number, Toast.LENGTH_SHORT).show()
        } else if (etAddComplement.text.isEmpty()) {
            Toast.makeText(context, R.string.write_complement, Toast.LENGTH_SHORT).show()
        } else if (etAddDistrict.text.isEmpty()) {
            Toast.makeText(context, R.string.write_district, Toast.LENGTH_SHORT).show()
        } else if (etAddCity.text.isEmpty()) {
            Toast.makeText(context, R.string.write_city, Toast.LENGTH_SHORT).show()
        } else if (etAddObs.text.isEmpty()) {
            Toast.makeText(context, R.string.write_comment, Toast.LENGTH_SHORT).show()
        } else if (etAddZipCode.text.isEmpty()) {
            Toast.makeText(context, R.string.write_zip_code, Toast.LENGTH_SHORT).show()
        } else if (file == null) {
            Toast.makeText(context, R.string.choose_picture, Toast.LENGTH_SHORT).show()
        } else {

            if (studio == null) {
                val bytes = file!!.readBytes()
                val imagemBase64 = Base64.encodeToString(bytes, Base64.NO_WRAP)


                val studio: Studio = Studio(0, idUser, etAddStudioName.text.toString(), etAddAddress.text.toString(), Integer.parseInt(etAddNumber.text.toString()), etAddComplement.text.toString(), etAddDistrict.text.toString(), etAddCity.text.toString(), etAddZipCode.text.toString(), etAddObs.text.toString(), imagemBase64, "")


                StudioService.service.postStudios(studio).enqueue(object : Callback<Studio> {

                    override fun onResponse(call: Call<Studio>, response: Response<Studio>) {
                        val userResponse = response.body()?.copy()

                        if (userResponse?.seq_studio != -1) {

                            Toast.makeText(context, getString(R.string.add_register_ok), Toast.LENGTH_LONG).show()

                            val fragmentTransaction = fragmentManager.beginTransaction()
                            fragmentTransaction.replace(R.id.content_main, Studio_Fragment())
                            fragmentTransaction.commit()

                        } else {
                            Toast.makeText(context, getString(R.string.error_reg), Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<Studio>?, t: Throwable?) {
                        Log.d("ERRO", t?.message)

                    }
                })
            } else {
                var imagemBase64: String = ""
                if (file != null) {
                    val bytes = file!!.readBytes()
                    imagemBase64 = Base64.encodeToString(bytes, Base64.NO_WRAP)
                }

                val studio: Studio = Studio(studio!!.seq_studio, studio!!.seq_user, etAddStudioName.text.toString(), etAddAddress.text.toString(), Integer.parseInt(etAddNumber.text.toString()), etAddComplement.text.toString(), etAddDistrict.text.toString(), etAddCity.text.toString(), etAddZipCode.text.toString(), etAddObs.text.toString(), imagemBase64, "")


                StudioService.service.upStudios(studio).enqueue(object : Callback<Studio> {

                    override fun onResponse(call: Call<Studio>, response: Response<Studio>) {
                        val userResponse = response.body()?.copy()

                        if (userResponse?.seq_studio != -1) {

                            Toast.makeText(context, getString(R.string.add_update_ok), Toast.LENGTH_LONG).show()

                            val fragmentTransaction = fragmentManager.beginTransaction()
                            fragmentTransaction.replace(R.id.content_main, Studio_Fragment())
                            fragmentTransaction.commit()

                        }
                    }

                    override fun onFailure(call: Call<Studio>?, t: Throwable?) {
                        Log.d("ERRO", t?.message)

                    }
                })
            }
    }
    }

    private fun showImage(file: File?) {
        if (file != null && file.exists()) {
            val w = ivAddStudio.width
            val h = ivAddStudio.height
            val bitmap = ImageUtil.resize(file, w, h)
            ivAddStudio.setImageBitmap(bitmap)
        }
    }

    fun getSdCardFile(fileName: String): File {
        val sdCardDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        if (!sdCardDir.exists()) {
            sdCardDir.mkdir()
        }
        val file = File(sdCardDir, fileName)
        return file
    }






    @SuppressLint("StaticFieldLeak")
    private inner class GetAsyncTask internal constructor(appDatabase: UserDatabase) : AsyncTask<Void, Void, UserPers>() {
        private val db: UserDatabase = appDatabase

        override fun doInBackground(vararg params: Void): UserPers {
            val user :UserPers = db.userDao().getUser()
            return user
        }
    }

}
