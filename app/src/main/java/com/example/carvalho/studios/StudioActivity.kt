package com.example.carvalho.studios

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.carvalho.studios.model.Studio
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_studio.*
import kotlinx.android.synthetic.main.content_studio.*
import android.support.v7.app.AlertDialog

class StudioActivity : AppCompatActivity() {

    lateinit var studio: Studio

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_studio)
        setSupportActionBar(toolbar)
        btCall.setOnClickListener { view ->

            calling()
        }




        btMaps.setOnClickListener { view ->
            loadMap()

        }
        studio = intent.getParcelableExtra<Studio>("studio")

        toolbar_layout.title = studio.nome

        Picasso.with(this)
                .load(studio.path_foto)
                .resize(100,100)
                .into(ivBanner)

        tvDetailAddress.text = "${getResources().getString(R.string.add_address)}: ${studio.endereco}"
        tvDetailNumber.text = "Nº: ${studio.numero.toString()}"
        tvDetailComplement.text = "${getResources().getString(R.string.add_complement)}: ${studio.complemento}"
        tvDetailDistrict.text = "${getResources().getString(R.string.add_district)}: ${studio.bairro}"
        tvDetailCity.text = "${getResources().getString(R.string.add_city)}: ${studio.cidade}"
        tvDetailZipCode.text = "${getResources().getString(R.string.add_zip_code)}: ${studio.cep}"
        tvDetailPhone.text = "${getResources().getString(R.string.add_phone)}: ${studio.tel}"
        tvDetailComment.text = "${getResources().getString(R.string.add_comments)}: ${studio.obs}"


    }



    private fun loadMap() {

            val intentDetalhe = Intent(this, MapsActivity::class.java)

            intentDetalhe.putExtra("studio", studio)

            startActivity(intentDetalhe)

    }

    fun calling() {
        try {
            if(checkPermission()) {
                //ACTION_DIAL
                //ACTION_CALL
                val uri = Uri.parse("tel:" + studio.tel)

                val intentCall = Intent(Intent.ACTION_CALL, uri);

                startActivity(intentCall);
            }
        } catch(act: ActivityNotFoundException) {
            Log.e(getString(R.string.calling), getString(R.string.fail), act);
        }


    }

    private fun checkPermission() : Boolean {
        var ret: Boolean = false
        val permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.CALL_PHONE)) {

                val builder = AlertDialog.Builder(this)

                builder.setMessage(getString(R.string.required_permission_to_make_calls))
                        .setTitle(getString(R.string.permission_required))

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
