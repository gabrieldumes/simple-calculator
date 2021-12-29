package com.example.mycalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private var textInputAndResults: TextView? = null
    private var lastNumeric: Boolean = false
    private var lastDot: Boolean = false
    private var dotPressed: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        textInputAndResults = findViewById(R.id.textInputAndResults)
    }

    fun onDigit(view: View) {
        if ((view as Button).text.equals(".")) {
            /*check if the dot is valid
            the dot will only be valid if:
            - the last digit was a number
            - the last digit was NOT a dot
            - the dot wasn't pressed*/
            if (lastNumeric && !lastDot && !dotPressed) {
                textInputAndResults?.append(view.text)
                lastNumeric = false
                lastDot = true
                dotPressed = true
            }
        } else {
            textInputAndResults?.append(view.text)
            lastNumeric = true
            lastDot = false
        }
    }

    fun onClr(view: View) {
        textInputAndResults?.text = ""
        dotPressed = false
    }

    fun onOperator(view: View) {
        textInputAndResults?.text.let {
            if (lastNumeric && !wasOperatorAdded(it.toString())) {
                textInputAndResults?.append((view as Button).text)
                lastNumeric = false
                dotPressed = false //enables using a dot in both n. - otherwise only the first n. could have a dot
            }
        }
    }

    private fun wasOperatorAdded(text: String): Boolean {
        return if (text.startsWith("-")) {
            false
        } else {
            text.contains("/") || text.contains("*") ||
                    text.contains("-") || text.contains("+")
        }
    }

    fun onEqual(view: View) {
        if (lastNumeric) {
            var textValue = textInputAndResults?.text.toString()

            var prefix = ""

            if (textValue.startsWith("-")) {
                prefix = "-"
                textValue = textValue.substring(1)
            }

            try {
                var result: Double = 0.0

                when {
                    textValue.contains("/") -> {
                        var splittedValue = textValue.split("/").toMutableList()

                        if (prefix == ("-")) {
                            splittedValue[0] = prefix + splittedValue[0]
                        }

                        result = splittedValue[0].toDouble() / splittedValue[1].toDouble()
                    }

                    textValue.contains("*") -> {
                        var splittedValue = textValue.split("*").toMutableList()

                        if (prefix == ("-")) {
                            splittedValue[0] = prefix + splittedValue[0]
                        }

                        result = splittedValue[0].toDouble() * splittedValue[1].toDouble()
                    }

                    textValue.contains("-") -> {
                        var splittedValue = textValue.split("-").toMutableList()

                        if (prefix == ("-")) {
                            splittedValue[0] = prefix + splittedValue[0]
                        }

                        result = splittedValue[0].toDouble() - splittedValue[1].toDouble()
                    }

                    textValue.contains("+") -> {
                        var splittedValue = textValue.split("+").toMutableList()

                        if (prefix == ("-")) {
                            splittedValue[0] = prefix + splittedValue[0]
                        }

                        result = splittedValue[0].toDouble() + splittedValue[1].toDouble()
                    }
                }

                if (result.toString().contains(".0")) {
                    textInputAndResults?.text = result.toString().substring(0, result.toString().length - 2)
                } else {
                    textInputAndResults?.text = result.toString()
                }
            } catch (e: ArithmeticException) {
                e.printStackTrace()
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            }
        }
        //TODO: Each time you press any button, it should test if can calculate
        //create a function to test if the calculation can be done
    }
}