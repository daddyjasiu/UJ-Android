package pl.edu.uj.ii.skwarczek.productlist.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import pl.edu.uj.ii.skwarczek.productlist.R
import pl.edu.uj.ii.skwarczek.productlist.adapters.SignInAdapter

class SignInActivity : AppCompatActivity() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var signUpEmailField: EditText
    private lateinit var signUpPasswordField: EditText
    private lateinit var signInEmailField: EditText
    private lateinit var signInPasswordField: EditText



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        initView()
    }

    fun signInClicked(view: android.view.View) {
        signInCurrentUser()
        val intent = Intent(this,ShoppingScreenActivity::class.java)
        //startActivity(intent)
    }

    fun signUpClicked(view: android.view.View){
        signUpCurrentUser()
    }

    private fun initView(){

        tabLayout = findViewById(R.id.tab_layout)
        viewPager = findViewById(R.id.view_pager)

        val tabTitles = arrayOf("Sign in", "Sign up")

        tabLayout.addTab(tabLayout.newTab())
        tabLayout.addTab(tabLayout.newTab())
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        viewPager.adapter = SignInAdapter(supportFragmentManager, lifecycle, this, tabLayout.tabCount)

        TabLayoutMediator(tabLayout, viewPager){ tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }

    private fun signUpCurrentUser(){

        signUpEmailField = findViewById(R.id.sign_up_email)
        signUpPasswordField = findViewById(R.id.sign_up_password)

        if(signUpEmailField.text.toString().isEmpty() || signUpPasswordField.toString().isEmpty())
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
        else{

            val emailText = signUpEmailField.text.toString()
            val passwordText = signUpPasswordField.text.toString()

            RealmHelper.addCustomerToDB(emailText, passwordText)
            RealmHelper.syncRealmWithSQLite()
            tabLayout.selectTab(tabLayout.getTabAt(0))

            signUpEmailField.setText("")
            signUpPasswordField.setText("")
        }
    }

    private fun signInCurrentUser(){
        //TODO checking if user credentials exist in SQLite DB

        signInEmailField = findViewById(R.id.sign_in_email)
        signInPasswordField = findViewById(R.id.sign_in_password)

        val customer = RealmHelper.getCustomerByEmailAndPassword(signInEmailField.text.toString(), signInPasswordField.text.toString())

        if (customer != null) {
            Toast.makeText(this, "Customer exists with ID: ${customer.id}", Toast.LENGTH_SHORT)
                .show()
            val intent = Intent(this,ShoppingScreenActivity::class.java)
            startActivity(intent)
        }
        else{
            Toast.makeText(this, "Customer doesn't exist", Toast.LENGTH_SHORT).show()
        }
    }
}