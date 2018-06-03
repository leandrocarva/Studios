package com.example.carvalho.studios.adapter

import android.content.Context
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
                    val listenerDelete: (Studio) -> Unit) : RecyclerView.Adapter<StudioAdapter.StudioViewHolder>() {

    override fun onBindViewHolder(holder: StudioViewHolder, position: Int) {
        val studio = studios[position]

        holder?.let {
            holder.bindView(studio, listener, listenerDelete)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudioViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.list_studio, parent, false)
        return StudioViewHolder(itemView)
    }

    override fun getItemCount(): Int {

        return studios.size
    }




    class  StudioViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bindView(studio: Studio, listener: (Studio) -> Unit, listenerDelete: (Studio) -> Unit) = with(itemView) {

            tvNome.text = studio.nome
            tvCidade.text = studio.cidade
            tvObs.text = studio.obs
            Picasso.with(context)
                    .load(studio.path_photo)
                    .resize(100,100)
                    .into(ivPhoto)

            ivDelete.setOnClickListener{
                listenerDelete(studio)
            }

            setOnClickListener { listener(studio) }
        }
    }
}