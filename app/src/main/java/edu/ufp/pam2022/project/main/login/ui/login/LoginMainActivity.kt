package edu.ufp.pam2022.project.main.login.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import edu.ufp.pam2022.project.Alt_Detailed_Activity.DrawerMainActivity
import edu.ufp.pam2022.project.databinding.ActivityLoginMainBinding
import edu.ufp.pam2022.project.library.User
import edu.ufp.pam2022.project.listMovie.MainMovieFragments
import edu.ufp.pam2022.project.main.login.ui.Registration.RegisterActivity
import edu.ufp.pam2022.project.services.SoundService


class LoginMainActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val email = binding.email
        val password = binding.password
        val login = binding.LogIn
        val loading = binding.loading
        loginViewModel = ViewModelProvider(this, LoginViewModelFactory(this))[LoginViewModel::class.java]

        loginViewModel.loginFormState.observe(this@LoginMainActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid

            if (loginState.EmailError != null) {
                email.error = getString(loginState.EmailError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        loginViewModel.loginResult.observe(this@LoginMainActivity, Observer {
            val loginResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                updateUiWithUser(loginResult.success!!)
            }
            setResult(Activity.RESULT_OK)

            //Complete and destroy login activity once successful
            finish()
        })

        startService(Intent(this, SoundService::class.java).
            setAction("0"));


        email.afterTextChanged {
            loginViewModel.loginDataChanged(
                email.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    email.text.toString(),
                    password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(
                            email.text.toString(),
                            password.text.toString()
                        )
                }
                false
            }

            login.setOnClickListener {
                loading.visibility = View.VISIBLE
                loginViewModel.login(email.text.toString(), password.text.toString())
            }
        }
    }

    private fun updateUiWithUser(model: User) {
        val switchActivityIntent = Intent(this, DrawerMainActivity::class.java)
        switchActivityIntent.putExtra("Username",model.Username)
        switchActivityIntent.putExtra("UserId",model.UserId)
        switchActivityIntent.putExtra("Email",model.Email)
        startActivity(switchActivityIntent)
    }

    private fun showLoginFailed( errorString: Int) {
        Toast.makeText(applicationContext, "failed", Toast.LENGTH_SHORT).show()
        val intent = intent
        finish()
        startActivity(intent)

    }

    fun registerMessage(view: View) {
        val switchActivityIntent = Intent(this, RegisterActivity::class.java)
        startActivity(switchActivityIntent)
    }




}
/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}