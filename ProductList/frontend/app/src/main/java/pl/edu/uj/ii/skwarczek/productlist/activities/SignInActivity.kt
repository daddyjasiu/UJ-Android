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
import pl.edu.uj.ii.skwarczek.productlist.R
import pl.edu.uj.ii.skwarczek.productlist.adapters.SignInAdapter
import pl.edu.uj.ii.skwarczek.productlist.models.CustomerRealmModel
import pl.edu.uj.ii.skwarczek.productlist.services.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import org.json.JSONObject
import pl.edu.uj.ii.skwarczek.productlist.utility.Globals
import pl.edu.uj.ii.skwarczek.productlist.utility.RealmHelper


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

    private fun addCustomerToBackend(customer: CustomerRealmModel) {
        val service = RetrofitService.create()

        val paramObject = JSONObject()
        paramObject.put("id", customer.id)
        paramObject.put("firstName", customer.firstName)
        paramObject.put("lastName", customer.lastName)
        paramObject.put("email", customer.email)
        paramObject.put("password", customer.password)

        val call = service.createCustomer(paramObject.toString())
        call.enqueue(object : Callback<CustomerRealmModel> {
            override fun onResponse(
                call: Call<CustomerRealmModel>,
                response: Response<CustomerRealmModel>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        applicationContext,
                        "Successfully registered!",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d("POST CUSTOMER SUCCESS", response.message())
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Error occurred while registration!",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d("POST CUSTOMER FAIL", response.message())
                }
            }

            override fun onFailure(call: Call<CustomerRealmModel>, t: Throwable) {
                Toast.makeText(
                    applicationContext,
                    "Error occurred while registration!",
                    Toast.LENGTH_SHORT
                ).show()
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

            val customer = CustomerRealmModel(
                0,
                "",
                "",
                emailText,
                passwordText
            )

            //addCustomerToBackend(customer)
            RealmHelper.addCustomer(customer)

            RealmHelper.syncRealmWithSQLite()

            Globals.setCurrentUser(customer)
            tabLayout.selectTab(tabLayout.getTabAt(0))

            signUpEmailField.setText("")
            signUpPasswordField.setText("")
        }
    }

    private fun signInCurrentUser() {
        //TODO checking if user credentials exist in SQLite DB

        signInEmailField = findViewById(R.id.sign_in_email)
        signInPasswordField = findViewById(R.id.sign_in_password)

        val customer = RealmHelper.getCustomerByEmailAndPassword(
            signInEmailField.text.toString(),
            signInPasswordField.text.toString()
        )

        if (customer != null) {
            Toast.makeText(this, "Customer exists with ID: ${customer.id}", Toast.LENGTH_SHORT)
                .show()

            val currentUser = CustomerRealmModel()
            currentUser.id = customer.id
            currentUser.firstName = customer.firstName
            currentUser.lastName = customer.lastName
            currentUser.email = customer.email
            currentUser.password = customer.password
            Globals.setCurrentUser(currentUser)

            val intent = Intent(this, ShoppingScreenActivity::class.java)

//            intent.putExtra("currentUserId", currentUser.id)
//            intent.putExtra("currentUserFirstName", currentUser.firstName)
//            intent.putExtra("currentUserLastName", currentUser.lastName)
//            intent.putExtra("currentUserEmail", currentUser.email)
//            intent.putExtra("currentUserPassword", currentUser.password)

            startActivity(intent)

        } else {
            Toast.makeText(this, "Customer doesn't exist", Toast.LENGTH_SHORT).show()
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