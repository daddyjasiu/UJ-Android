package pl.edu.uj.ii.skwarczek.productlist.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import pl.edu.uj.ii.skwarczek.productlist.R
import pl.edu.uj.ii.skwarczek.productlist.adapters.SignInAdapter

class SignInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        val tabTitles = arrayOf("Sign in", "Sign up")

        val tabLayout: TabLayout = findViewById(R.id.tab_layout)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
//        val fb: FloatingActionButton = findViewById(R.id.fab_facebook)
//        val google: FloatingActionButton = findViewById(R.id.fab_google)
//        val twitter: FloatingActionButton = findViewById(R.id.fab_twitter)

        tabLayout.addTab(tabLayout.newTab())
        tabLayout.addTab(tabLayout.newTab())
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        viewPager.adapter = SignInAdapter(supportFragmentManager, lifecycle, this, tabLayout.tabCount)

        TabLayoutMediator(tabLayout, viewPager){ tab, position ->
            tab.text = tabTitles[position]
        }.attach()

    }

    fun signInClicked(view: android.view.View) {
        val intent = Intent(this,ShoppingScreenActivity::class.java)
        startActivity(intent)
    }

    fun signUpClicked(view: android.view.View){
        val intent = Intent(this,ShoppingScreenActivity::class.java)
        startActivity(intent)
    }


}