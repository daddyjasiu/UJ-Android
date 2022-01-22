package pl.edu.uj.ii.skwarczek.productlist.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import pl.edu.uj.ii.skwarczek.productlist.R
import pl.edu.uj.ii.skwarczek.productlist.activities.WishMakingActivity
import pl.edu.uj.ii.skwarczek.productlist.models.CustomerModel
import pl.edu.uj.ii.skwarczek.productlist.services.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpTabFragment : Fragment(){

    private lateinit var auth: FirebaseAuth
    private lateinit var signUpFirstNameField: EditText
    private lateinit var signUpSurnameField: EditText
    private lateinit var signUpEmailField: EditText
    private lateinit var signUpPasswordField: EditText
    private lateinit var signUpButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_signup_tab, container, false)

        initView(view)

        signUpButton.setOnClickListener {
            if(signUpEmailField.text.toString().trim().isNotEmpty() && signUpPasswordField.text.toString().trim().isNotEmpty()){

                val firstName = signUpFirstNameField.text.trim().toString()
                val surname = signUpSurnameField.text.trim().toString()
                val email = signUpEmailField.text.trim().toString()
                val password = signUpPasswordField.text.trim().toString()

                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val firebaseUser: FirebaseUser = task.result!!.user!!

                            Toast.makeText(activity, "Registered successfully!", Toast.LENGTH_SHORT).show()

                            addCustomerToBackend(CustomerModel(firebaseUser.uid, firstName, surname, email, password))

                            val intent = Intent(activity, WishMakingActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        }
                        else{
                            Toast.makeText(activity, "Registration failed.", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
            else{
                Toast.makeText(activity, "Please fill all required fields.", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }

    private fun addCustomerToBackend(customer: CustomerModel) {
        val service = RetrofitService.create()
        val call = service.postCustomerCall(customer)
        call.enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if(response.isSuccessful) {
                    Log.d("POST CUSTOMER SUCCESS", response.message())
                } else {
                    Log.d("POST CUSTOMER FAIL", response.message())
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Log.d("POST CUSTOMER FAIL", t.message.toString())
            }
        })
    }

    private fun initView(view: View){
        auth = Firebase.auth
        signUpButton = view.findViewById(R.id.sign_up_button)
        signUpFirstNameField = view.findViewById(R.id.sign_up_first_name)
        signUpSurnameField = view.findViewById(R.id.sign_up_surname)
        signUpEmailField = view.findViewById(R.id.sign_up_email)
        signUpPasswordField = view.findViewById(R.id.sign_up_password)
    }
}