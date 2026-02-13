package com.example.mycalculator

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class calulatorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_calulator)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        var tvoperation: TextView = findViewById(R.id.operation)
        tvoperation.text = ""

        var tvresult: TextView = findViewById(R.id.result)
        tvresult.text = ""

        val buttonmu = findViewById<Button>(R.id.b_multiply)
        buttonmu.setOnClickListener {
            tvoperation.setText("${tvoperation.text}*")
        }

        val buttonshare = findViewById<Button>(R.id.b_share)
        buttonshare.setOnClickListener {
            tvoperation.setText("${tvoperation.text}/")
        }

        val buttonplus = findViewById<Button>(R.id.b_plus)
        buttonplus.setOnClickListener {
            tvoperation.setText("${tvoperation.text}+")
        }

        val buttonminus = findViewById<Button>(R.id.b_minus)
        buttonminus.setOnClickListener {
            tvoperation.setText("${tvoperation.text}-")
        }

        val button1 = findViewById<Button>(R.id.b_1)
        button1.setOnClickListener {
            tvoperation.setText("${tvoperation.text}1")
        }

        val button2 = findViewById<Button>(R.id.b_2)
        button2.setOnClickListener {
            tvoperation.setText("${tvoperation.text}2")
        }

        val button3 = findViewById<Button>(R.id.b_3)
        button3.setOnClickListener {
            tvoperation.setText("${tvoperation.text}3")
        }

        val button4 = findViewById<Button>(R.id.b_4)
        button4.setOnClickListener {
            tvoperation.setText("${tvoperation.text}4")
        }

        val button5 = findViewById<Button>(R.id.b_5)
        button5.setOnClickListener {
            tvoperation.setText("${tvoperation.text}5")
        }

        val button6 = findViewById<Button>(R.id.b_6)
        button6.setOnClickListener {
            tvoperation.setText("${tvoperation.text}6")
        }

        val button7 = findViewById<Button>(R.id.b_7)
        button7.setOnClickListener {
            tvoperation.setText("${tvoperation.text}7")
        }

        val button8 = findViewById<Button>(R.id.b_8)
        button8.setOnClickListener {
            tvoperation.setText("${tvoperation.text}8")
        }

        val button9 = findViewById<Button>(R.id.b_9)
        button9.setOnClickListener {
            tvoperation.setText("${tvoperation.text}9")
        }
        val button0 = findViewById<Button>(R.id.b_0)
        button0.setOnClickListener {
            tvoperation.setText("${tvoperation.text}0")
        }
        val buttonac = findViewById<Button>(R.id.b_clear)
        buttonac.setOnClickListener {
            tvoperation.setText("")
        }
        val buttoneq = findViewById<Button>(R.id.b_equally)
        buttoneq.setOnClickListener {
            val expression = tvoperation.text.toString()
            var firstNumber = ""
            var secondNumber = ""
            var operator: Char? = null
            var foundOperator = false

            for (i in expression.indices) {
                val char = expression[i]

                if (char in "+-*/" && !foundOperator) {
                    operator = char
                    foundOperator = true
                } else {
                    if (!foundOperator) {
                        firstNumber += char
                    } else {
                        secondNumber += char
                    }
                }
            }
            if (foundOperator && firstNumber.isNotEmpty() && secondNumber.isNotEmpty() && operator != null) {
                try {
                    val num1 = firstNumber.toDouble()
                    val num2 = secondNumber.toDouble()
                    val result = when (operator) {
                        '+' -> num1 + num2
                        '-' -> num1 - num2
                        '*' -> num1 * num2
                        '/' -> num1 / num2
                        else -> 0.0
                    }
                    tvresult.text = result.toString()
                } catch (e: Exception) {
                    tvresult.text = "Ошибка"
                }
            } else {
                tvresult.text = "Ошибка в выражении"
            }
            tvoperation.text = ""
        }
    }
}
