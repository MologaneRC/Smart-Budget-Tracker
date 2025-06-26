package com.example.smartbudgettrack

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class CalculatorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator)

        val firstNum = findViewById<EditText>(R.id.firstNumberEditText)
        val secondNum = findViewById<EditText>(R.id.secondNumberEditText)
        val resultText = findViewById<TextView>(R.id.resultTextView)

        val addButton = findViewById<Button>(R.id.buttonAdd)
        val subtractButton = findViewById<Button>(R.id.buttonSubtract)
        val multiplyButton = findViewById<Button>(R.id.buttonMultiply)
        val divideButton = findViewById<Button>(R.id.buttonDivide)

        fun getNumbers(): Pair<Double?, Double?> {
            val num1 = firstNum.text.toString().toDoubleOrNull()
            val num2 = secondNum.text.toString().toDoubleOrNull()
            return Pair(num1, num2)
        }

        addButton.setOnClickListener {
            val (num1, num2) = getNumbers()
            if (num1 != null && num2 != null) {
                resultText.text = "Result: ${num1 + num2}"
            } else {
                resultText.text = "Please enter valid numbers"
            }
        }

        subtractButton.setOnClickListener {
            val (num1, num2) = getNumbers()
            if (num1 != null && num2 != null) {
                resultText.text = "Result: ${num1 - num2}"
            } else {
                resultText.text = "Please enter valid numbers"
            }
        }

        multiplyButton.setOnClickListener {
            val (num1, num2) = getNumbers()
            if (num1 != null && num2 != null) {
                resultText.text = "Result: ${num1 * num2}"
            } else {
                resultText.text = "Please enter valid numbers"
            }
        }

        divideButton.setOnClickListener {
            val (num1, num2) = getNumbers()
            if (num1 != null && num2 != null) {
                if (num2 != 0.0) {
                    resultText.text = "Result: ${num1 / num2}"
                } else {
                    resultText.text = "Cannot divide by zero"
                }
            } else {
                resultText.text = "Please enter valid numbers"
            }
        }
    }
}
