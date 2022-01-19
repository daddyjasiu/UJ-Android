package pl.edu.uj.ii.skwarczek.productlist.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.runBlocking
import pl.edu.uj.ii.skwarczek.productlist.R
import pl.edu.uj.ii.skwarczek.productlist.adapters.SignInAdapter
import pl.edu.uj.ii.skwarczek.productlist.models.CustomerRealmModel
import pl.edu.uj.ii.skwarczek.productlist.services.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import pl.edu.uj.ii.skwarczek.productlist.models.CustomerModel
import pl.edu.uj.ii.skwarczek.productlist.utility.Globals
import pl.edu.uj.ii.skwarczek.productlist.utility.RealmHelper
import kotlin.random.Random

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
    }

    fun signUpClicked(view: android.view.View) {
        signUpCurrentUser()
    }

    private fun addCustomerToBackend(customer: CustomerModel) {
        val service = RetrofitService.create()
        val call = service.postCustomerCall(customer)
        call.enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if(response.isSuccessful) {
                    Toast.makeText(applicationContext, "Successfully registered!", Toast.LENGTH_SHORT).show()
                    Log.d("POST CUSTOMER SUCCESS", response.message())
                } else {
                    Toast.makeText(applicationContext, "Error occurred while registration! Given username may be already taken", Toast.LENGTH_SHORT).show()
                    Log.d("POST CUSTOMER FAIL", response.message())
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Toast.makeText(applicationContext, "Error occurred while registration! ${t.message.toString()}", Toast.LENGTH_SHORT).show()
                Log.d("POST CUSTOMER FAIL", t.message.toString())
            }
        })
    }

    private fun signUpCurrentUser() {

        signUpEmailField = findViewById(R.id.sign_up_email)
        signUpPasswordField = findViewById(R.id.sign_up_password)

        if (signUpEmailField.text.toString().isEmpty() || signUpPasswordField.toString().isEmpty())
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
        else {

            val emailText = signUpEmailField.text.toString()
            val passwordText = signUpPasswordField.text.toString()

            val random = Random.nextInt(0, Int.MAX_VALUE)

            val customerModel = CustomerModel(
                random,
                "",
                "",
                emailText,
                passwordText
            )

            val customerRealmModel = CustomerRealmModel(
                random,
                "",
                "",
                emailText,
                passwordText
            )

            addCustomerToBackend(customerModel)

            RealmHelper.getCurrentCustomerByNameAndPasswordFromSQL(customerModel.email, customerModel.password)
//            RealmHelper.addCustomer(customerRealmModel)
            tabLayout.selectTab(tabLayout.getTabAt(0))

            signUpEmailField.setText("")
            signUpPasswordField.setText("")
        }
    }

    private fun signInCurrentUser() {

        signInEmailField = findViewById(R.id.sign_in_email)
        signInPasswordField = findViewById(R.id.sign_in_password)

        val email = signInEmailField.text.toString()
        val password = signInPasswordField.text.toString()

        RealmHelper.getCurrentCustomerByNameAndPasswordFromSQL(email, password)

        val customer = RealmHelper.getCustomerByEmailAndPassword(email, password)

        if (customer != null) {
            RealmHelper.syncRealmWithSQLite(customer.id)
            Toast.makeText(this, "Welcome!", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, ShoppingScreenActivity::class.java)
            startActivity(intent)

        } else {
            Toast.makeText(this, "Login error, please try again", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initView() {

        tabLayout = findViewById(R.id.tab_layout)
        viewPager = findViewById(R.id.view_pager)

        val tabTitles = arrayOf("Sign in", "Sign up")

        tabLayout.addTab(tabLayout.newTab())
        tabLayout.addTab(tabLayout.newTab())
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        viewPager.adapter =
            SignInAdapter(supportFragmentManager, lifecycle, this, tabLayout.tabCount)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }

}