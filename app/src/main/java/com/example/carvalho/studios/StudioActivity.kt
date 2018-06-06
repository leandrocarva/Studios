package com.example.carvalho.studios

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.example.carvalho.studios.model.Studio
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_studio.*
import kotlinx.android.synthetic.main.content_studio.*

class StudioActivity : AppCompatActivity() {

    lateinit var studio: Studio

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_studio)
        setSupportActionBar(toolbar)
        btPhone.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
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
        tvDetailComment.text = "${getResources().getString(R.string.add_comments)}: ${studio.obs}"


    }
}
