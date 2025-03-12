package edu.ufp.pam2022.project.main.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import edu.ufp.pam2022.project.main.login.ui.login.LoginMainActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = Intent(this@MainActivity, LoginMainActivity::class.java)
        intent.putExtra("EXIT", false)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }
}