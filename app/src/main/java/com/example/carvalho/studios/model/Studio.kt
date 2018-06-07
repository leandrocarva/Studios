package com.example.carvalho.studios.model

import android.os.Parcel
import android.os.Parcelable



data class Studio (val seq_studio: Int,
                val seq_user: Int,
                val nome: String,
                val endereco: String,
                val numero: Int,
                val complemento: String,
                val bairro: String,
                val cidade: String,
                val cep: String,
                val obs: String,
                val path_foto: String,
                val chave_push: String,
                val tel: String) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(seq_studio)
        parcel.writeInt(seq_user)
        parcel.writeString(nome)
        parcel.writeString(endereco)
        parcel.writeInt(numero)
        parcel.writeString(complemento)
        parcel.writeString(bairro)
        parcel.writeString(cidade)
        parcel.writeString(cep)
        parcel.writeString(obs)
        parcel.writeString(path_foto)
        parcel.writeString(chave_push)
        parcel.writeString(tel)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Studio> {
        override fun createFromParcel(parcel: Parcel): Studio {
            return Studio(parcel)
        }

        override fun newArray(size: Int): Array<Studio?> {
            return arrayOfNulls(size)
        }
    }
}