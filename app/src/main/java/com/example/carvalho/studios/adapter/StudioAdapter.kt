package com.example.carvalho.studios.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.carvalho.studios.R
import com.example.carvalho.studios.model.Studio

import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_studio.view.*


class StudioAdapter(val context: Context,
                    val studios: List<Studio>,
                    val listener: (Studio) -> Unit,
                    val listenerDelete: (Studio) -> Unit,
                    val listenerEdit: (Studio) -> Unit): RecyclerView.Adapter<StudioAdapter.StudioViewHolder>() {

    override fun onBindViewHolder(holder: StudioViewHolder, position: Int) {
        val studio = studios[position]

        holder?.let {
            holder.bindView(studio, listener, listenerDelete, listenerEdit)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudioViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.list_studio, parent, false)
        return StudioViewHolder(itemView)
    }

    override fun getItemCount(): Int {

        return studios.size
    }

    fun share(itemView: View) {

        fun bindView(studio: Studio, listener: (Studio) -> Unit, listenerDelete: (Studio) -> Unit, listenerEdit: (Studio) -> Unit) = with(itemView) {
            val intentShare = Intent();
            intentShare.action = Intent.ACTION_SEND
            intentShare.putExtra(Intent.EXTRA_SUBJECT, "Studio ${studio.nome}")
            intentShare.putExtra(Intent.EXTRA_TEXT, "Este pet está perdido, ajude-nos a encontrar o caminha de casa ${studio.path_foto}")
            intentShare.type = "text/html"
            val bd: Bundle? = Bundle()
            startActivity(context,Intent.createChooser(intentShare, "Compartilhar"), bd!!);
        }
    }


    class  StudioViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bindView(studio: Studio, listener: (Studio) -> Unit, listenerDelete: (Studio) -> Unit, listenerEdit: (Studio) -> Unit) = with(itemView) {

            tvListNome.text = studio.nome
            tvCidade.text = studio.cidade
            tvObs.text = studio.obs
            Picasso.with(context)
                    .load(studio.path_foto)
                    .resize(100,100)
                    .into(ivPhoto)

            ivDel.setOnClickListener{
                listenerDelete(studio)
            }

            ivEdit.setOnClickListener {
                listenerEdit(studio)
            }

            ivShare.setOnClickListener {
                //share(studio)
            }

            setOnClickListener { listener(studio) }
        }
    }
}