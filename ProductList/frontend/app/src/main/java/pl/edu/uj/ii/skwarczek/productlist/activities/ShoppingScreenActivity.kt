package pl.edu.uj.ii.skwarczek.productlist.activities

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import pl.edu.uj.ii.skwarczek.productlist.R

class ShoppingScreenActivity : AppCompatActivity() {

    private val items: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_screen)

        val listView = findViewById<ListView>(R.id.shopping_cart_list_view)

        listView.setOnItemClickListener { parent, view, position, id ->
            val alert = AlertDialog.Builder(this)
            alert.setTitle("title")
            alert.setMessage("description")
            alert.setPositiveButton("Close") { dialog, which -> }
            alert.show()
        }

    }

    fun refreshCart(){
        val listView = findViewById<ListView>(R.id.shopping_cart_list_view)
        val adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1, items)
        listView.adapter = adapter
    }

    fun wishButtonClicked(view: android.view.View) {

        val wish = findViewById<EditText>(R.id.wish)
        if(!wish.text.toString().isEmpty()){
            items.add(wish.text.toString())
            refreshCart()
        }
    }

    fun settingsClicked(view: android.view.View) {
        val intent = Intent(this,SettingsActivity::class.java)
        startActivity(intent)
    }
}