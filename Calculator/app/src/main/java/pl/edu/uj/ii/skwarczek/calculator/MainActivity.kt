package pl.edu.uj.ii.skwarczek.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    var result = ""
    var num1 = ""
    var num2 = ""
    var isOperationClicked = false
    var shouldAdd = false
    var shouldSub = false
    var shouldMult = false
    var shouldDiv = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun clearCalcDisplay(view: View){
        val calcDisplay = findViewById<TextView>(R.id.display)
        calcDisplay.text = "0"
    }

    fun buttonAddClicked(view: View){
        shouldAdd = true
        isOperationClicked = true
    }

    fun buttonSubClicked(view: View){
        shouldSub = true
        isOperationClicked = true
    }

    fun buttonMultClicked(view: View){
        shouldMult = true
        isOperationClicked = true
    }

    fun buttonDivClicked(view: View){
        shouldDiv = true
        isOperationClicked = true
    }

    fun buttonEqualsClicked(view: android.view.View) {
        if(num1 != "" && num2 != ""){
            val calcDisplay = findViewById<TextView>(R.id.display)
            calculateResult(view)
            num1 = result
            num2 = ""
            calcDisplay.text = result
        }
    }

    fun buttonACClicked(view: View){
        clearCalcDisplay(view)
        result = ""
        num1 = ""
        num2 = ""
        isOperationClicked = false
        shouldAdd = false
        shouldDiv = false
        shouldMult = false
        shouldDiv = false
    }

    fun buttonNumberClick(view: View){
        val calcDisplay = findViewById<TextView>(R.id.display)
        val button = view as Button

        when(isOperationClicked){
            true -> {
                num2 += button.text.toString()
                calcDisplay.text = num2
            }
            false -> {
                num1 += button.text.toString()
                calcDisplay.text = num1
            }
        }
    }

    fun calculateResult(view: View){
        if(shouldAdd) {
            result = (num1.toFloat() + num2.toFloat()).toString()
            shouldAdd = false
            shouldSub = false
            shouldMult = false
            shouldDiv = false
        }
        else if(shouldSub) {
            result = (num1.toFloat() - num2.toFloat()).toString()
            shouldAdd = false
            shouldSub = false
            shouldMult = false
            shouldDiv = false
        }
        else if(shouldMult) {
            result = (num1.toFloat() * num2.toFloat()).toString()
            shouldAdd = false
            shouldSub = false
            shouldMult = false
            shouldDiv = false
        }
        else if(shouldDiv) {
            result = (num1.toFloat() / num2.toFloat()).toString()
            shouldAdd = false
            shouldSub = false
            shouldMult = false
            shouldDiv = false
        }
    }
}