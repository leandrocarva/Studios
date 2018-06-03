package com.example.carvalho.studios

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_splash.*
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        changeToLogin()
    }

    fun changeToLogin() {
        val anim = AnimationUtils.loadAnimation(this, R.anim.animation_splash)
        anim.reset()

        ivSplash.clearAnimation()
        ivSplash.startAnimation(anim)

        val intent = Intent(this, LoginActivity::class.java)

        Handler().postDelayed({
            intent.change()
        }, 2000)
    }

    fun Intent.change() {
        startActivity(this)
        finish()
    }
}
