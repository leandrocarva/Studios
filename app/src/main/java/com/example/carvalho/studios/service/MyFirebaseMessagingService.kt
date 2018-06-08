package com.example.carvalho.studios.service

import android.content.Intent
import android.util.Log
import com.example.carvalho.studios.MainActivity
import com.example.carvalho.studios.R
import com.example.carvalho.studios.util.NotificationUtil
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService : FirebaseMessagingService() {
    val TAG = "firebase"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "onMessageReceived")

        // Verifica se a mensagem é do tipo notificação.
        if (remoteMessage.notification != null) {
            val title = remoteMessage.notification?.title
            val msg = remoteMessage.notification?.body!!
            Log.d(TAG, "Message Notification Title: " + title)
            Log.d(TAG, "Message Notification Body: " + msg)

            showNotification(remoteMessage);
        }
    }


    private fun showNotification(remoteMessage: RemoteMessage) {

        val intent = Intent(this, MainActivity::class.java)



        // Title: O título é opcional...
        val title = remoteMessage.notification?.title ?: getString(R.string.app_name)

        // Mensagem
        val msg = remoteMessage.notification?.body!!

        // Cria uma notificação.
        NotificationUtil.create(this, 1, intent, title, msg)
    }
}