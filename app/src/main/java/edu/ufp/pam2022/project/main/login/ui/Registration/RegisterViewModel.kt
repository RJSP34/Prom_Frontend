package edu.ufp.pam2022.project.main.login.ui.Registration

import android.util.Log
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import edu.ufp.pam2022.project.R
import edu.ufp.pam2022.project.library.User
import edu.ufp.pam2022.project.main.login.ui.login.LoginFormState
import edu.ufp.pam2022.project.main.login.ui.login.LoginResult
import edu.ufp.pam2022.project.services.HttpService
import edu.ufp.pam2022.project.services.repositoryVolley.VolleyRequest
import org.json.JSONException
import org.json.JSONObject

class RegisterViewModel (App: AppCompatActivity) : ViewModel() {
    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm
    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult
    private val urlStr="http://10.100.237.231:8000"
    private var volleyRequestQueue: RequestQueue
    private val TAG_TO_CANCEL_HTTP_REQUEST = "TAG_TO_CANCEL_HTTP_REQUEST"
    init
    {
        //=================== Setup Volley to make async HTTP Request ===================
        Log.e(this.javaClass.simpleName, "onCreate(): going to set VOLLEY context...")
        // Get RequestQueue to execute async calls
        this.volleyRequestQueue = VolleyRequest.getInstance(App).requestQueue
    }
    fun register(Username:String,Email: String, Password: String) {
        // can be launched in a separate asynchronous job
        val testQueryStr = "/register"
        //2. Use Volley async HttpRequest
        val json=JSONObject()
        try {
            json.put("username", Username)
            json.put("email", Email)
            json.put("password", Password)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
         launchVolleyAsyncHttpRequest(urlStr,testQueryStr,
            HttpService.HttpAsyncMethod.POST,json)

    }

    fun registerDataChanged(username:String,email: String, password: String) {
       if (!isUsernamevalid(username)) {
           _loginForm.value = LoginFormState(UsernameError = R.string.invalid_username)
    }else if (!isEmailvalid(email)) {
            _loginForm.value = LoginFormState(EmailError = R.string.invalid_email)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    private fun isUsernamevalid(username: String): Boolean {
        return username.length > 5
    }

    // A placeholder username validation check
    private fun isEmailvalid(email: String): Boolean {
        return if (email.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(email).matches()
        } else {
            email.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
    fun launchVolleyAsyncHttpRequest(urlStr: String, queryStr: String, type: HttpService.HttpAsyncMethod, Json: JSONObject): Any{

        val url = "$urlStr$queryStr"
        Log.e(this.javaClass.simpleName, "launchVolleyAsyncHttpRequest(): url=$url")

        when (type) {
            HttpService.HttpAsyncMethod.POST -> {
                val stringRequest = JsonObjectRequest(
                    Request.Method.POST, url, Json, { //Handle Response
                            response ->
                        Log.e(
                            this.javaClass.simpleName,
                            "launchVolleyAsyncHttpRequest(): Response.Listener Response=${response}"
                        )
                        _loginResult.value= LoginResult(User(-1,"Username","-1"),)
                    }, { //Handle Error
                            error ->
                        Log.e(
                            this.javaClass.simpleName,
                            "launchVolleyAsyncHttpRequest(): Response.Listener Error=$error"
                        )
                        _loginResult.value= LoginResult(null,404)
                    }
                )
                // Set the cancel tag on the request
                stringRequest.tag = TAG_TO_CANCEL_HTTP_REQUEST
                // Add the request to the RequestQueue.
                volleyRequestQueue.add(stringRequest)
                return stringRequest
            }
            HttpService.HttpAsyncMethod.PUT -> {
                val stringRequest = JsonObjectRequest(
                    Request.Method.PUT, url, Json,
                    { //Handle Response
                            response ->
                        Log.e(
                            this.javaClass.simpleName,
                            "launchVolleyAsyncHttpRequest(): Response.Listener Response=${response}"
                        )
                    },
                    { //Handle Error
                            error ->
                        Log.e(
                            this.javaClass.simpleName,
                            "launchVolleyAsyncHttpRequest(): Response.Listener Error=$error"
                        )
                    }
                )
                // Set the cancel tag on the request

                stringRequest.tag = TAG_TO_CANCEL_HTTP_REQUEST
                // Add the request to the RequestQueue.
                volleyRequestQueue.add(stringRequest)
                return stringRequest
            }
            else -> return ""
        }
    }
    fun launchVolleyAsyncHttpRequest(urlStr: String, queryStr: String, type: HttpService.HttpAsyncMethod): Any{

        val url = "$urlStr$queryStr"
        Log.e(this.javaClass.simpleName, "launchVolleyAsyncHttpRequest(): url=$url")

        when (type) {
            HttpService.HttpAsyncMethod.GET-> {
                val stringRequest = StringRequest(
                    Request.Method.GET, url,
                    { //Handle Response
                            response ->
                        Log.e(this.javaClass.simpleName,
                            "launchVolleyAsyncHttpRequest(): Response.Listener Response=${response}")

                    },
                    { //Handle Error
                            error ->
                        Log.e(this.javaClass.simpleName,
                            "launchVolleyAsyncHttpRequest(): Response.Listener Error=$error")
                    }
                )
                // Set the cancel tag on the request
                stringRequest.tag = TAG_TO_CANCEL_HTTP_REQUEST
                // Add the request to the RequestQueue.
                return  stringRequest
            }
            else-> return ""
        }
    }
}
