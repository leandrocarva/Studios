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
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        studio = intent.getParcelableExtra<Studio>("studio")

        toolbar_layout.title = studio.nome

        Picasso.with(this)
                .load(studio.path_foto)
                .resize(100,100)
                .into(ivBanner)

        tvDetailAddress.text = studio.endereco
        tvDetailNumber.text = studio.numero.toString()
        tvDetailComplement.text = studio.complemento
        tvDetailDistrict.text = studio.bairro
        tvDetailCity.text = studio.cidade
        tvDetailZipCode.text = studio.cep
        tvDetailComment.text = studio.obs


    }
}
