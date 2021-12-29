package pl.edu.uj.ii.skwarczek.productlist.activities

import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import pl.edu.uj.ii.skwarczek.productlist.R

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val settingsRecyclerView = findViewById<RecyclerView>(R.id.settings_recycler_view)

    }
}