package pl.edu.uj.ii.skwarczek.productlist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class RegisterScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_screen)
    }

    fun onRegisterClicked(view: android.view.View) {
        startActivity(Intent(this, ShoppingScreen::class.java))
    }
}