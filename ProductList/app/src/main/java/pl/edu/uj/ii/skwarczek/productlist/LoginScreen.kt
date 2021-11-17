package pl.edu.uj.ii.skwarczek.productlist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent

class LoginScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_screen)
    }

    fun onRegisterClicked(view: android.view.View) {
        print("asdasdasd");
        startActivity(Intent(this, RegisterScreen::class.java))
    }

    fun onLoginClicked(view: android.view.View) {}
}