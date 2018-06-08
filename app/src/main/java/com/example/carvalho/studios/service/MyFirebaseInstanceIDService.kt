package com.example.carvalho.studios.service

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService

class MyFirebaseInstanceIDService : FirebaseInstanceIdService() {
    private val TAG = "firebase"

    // Obtêm o registration token.
    override fun onTokenRefresh() {
        val token = FirebaseInstanceId.getInstance().token
        if(token != null) {
            Log.d(TAG, "Refreshed token: " + token)
            sendRegistrationToServer(token)
        }
    }

    private fun sendRegistrationToServer(token: String) {
        // lógica para enviar o token para o servidor ...
    }
}