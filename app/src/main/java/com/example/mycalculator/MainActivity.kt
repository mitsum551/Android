package com.example.mycalculator

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import com.example.vizual1.mediaActivity
import com.example.mycalculator.location



class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.GoCalculator).setOnClickListener({
            val calc_Intent = Intent(this, calulatorActivity::class.java)
            startActivity(calc_Intent)
        });
        findViewById<Button>(R.id.Gomedia).setOnClickListener({
            val med_Intent = Intent(this, mediaActivity::class.java)
            startActivity(med_Intent)
        });
        findViewById<Button>(R.id.Golocat).setOnClickListener({
            val local_Intent = Intent(this, location::class.java)
            startActivity(local_Intent)
        });
    }
}
