package pl.edu.uj.ii.skwarczek.productlist.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import pl.edu.uj.ii.skwarczek.productlist.R

class SignUpTabFragment : Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.signup_tab_fragment, container, false)
    }
}