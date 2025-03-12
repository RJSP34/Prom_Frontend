package edu.ufp.pam2022.project.Alt_Detailed_Activity.ui.slideshow

import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import edu.ufp.pam2022.project.library.*
import edu.ufp.pam2022.project.services.HttpService
import edu.ufp.pam2022.project.services.repositoryVolley.VolleyRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject

class SlideshowViewModel(application: FragmentActivity) : ViewModel() {

    val _Movies = MutableLiveData<List<Movie>>()
    var Movies : LiveData<List<Movie>> = _Movies

    var user : User = User(-1,"","")
    var _user : LiveData<List<User>>

    private var repository: DBMovieRepository
    private var repository_Backlog: DBBackLogRepository

    var checked = false
    private val urlStr="http://192.168.1.97:8000"
    //192.168.1.97
    private var volleyRequestQueue: RequestQueue
    private var repository_User: DBUserRepository
    private val TAG_TO_CANCEL_HTTP_REQUEST = "TAG_TO_CANCEL_HTTP_REQUEST"

    private val _text = MutableLiveData<String>().apply {
        value = "This is slideshow Fragment"
    }

    val text: LiveData<String> = _text
    init {
        val Moviedao = AppDatabase.getDatabase(application).databaseMovieDao()
        repository = DBMovieRepository(Moviedao)
        Movies = repository.readAllData

        val DataBaseUserDao = AppDatabase.getDatabase(application).databaseUserDao()
        repository_User = DBUserRepository(DataBaseUserDao)
        _user = repository_User.readAllData

        val DataBaseBacklogDao = AppDatabase.getDatabase(application).databaseBacklogDao()
        repository_Backlog = DBBackLogRepository(DataBaseBacklogDao)

        //=================== Setup Volley to make async HTTP Request ===================
        Log.e(this.javaClass.simpleName, "onCreate(): going to set VOLLEY context...")
        // Get RequestQueue to execute async calls
        this.volleyRequestQueue = VolleyRequest.getInstance(application).requestQueue
    }

    fun addMovie(movie:Movie){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addMovie(movie)
        }
    }

    fun Get_Movies(){
        val testQueryStr = "/movies/getmovies"
        launchVolleyAsyncHttpRequest(urlStr, testQueryStr, HttpService.HttpAsyncMethod.GET)
    }

    fun insertBacklog(movieid: Int, date: String, userrating: Int, userstatus: Int ) {
        val testQueryStr = "/backlog/update_backlog"
        val json= JSONObject()
        try {
            json.put("userId", user.UserId)
            json.put("movieId", movieid)
            json.put("watchedDate", date)
            json.put("statusId", userstatus)
            json.put("rating", userrating)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        launchVolleyAsyncHttpRequest(urlStr, testQueryStr,
            HttpService.HttpAsyncMethod.POST, json)
    }

    fun launchVolleyAsyncHttpRequest(urlStr: String, queryStr: String, type: HttpService.HttpAsyncMethod, Json: JSONObject = JSONObject()): Any {

        val url = "$urlStr$queryStr"
        Log.e(this.javaClass.simpleName, "launchVolleyAsyncHttpRequest(): url=$url")

        when (type) {
            HttpService.HttpAsyncMethod.GET -> {
                val stringRequest = JsonArrayRequest(
                    Request.Method.GET, url,null,
                    { //Handle Response
                            response ->
                        Log.e(
                            this.javaClass.simpleName,
                            "launchVolleyAsyncHttpRequest(): Response.Listener Response=${response}"
                        )
                        try {
                            val list :MutableList<Movie> = ArrayList()
                            for (i in 0 until response.length()) {
                                val json: JSONObject = response[i] as JSONObject
                                val movie =Movie(json.getInt("id"),json.getString("name"), json.getString("releaseDate"), json.getInt("imdbRating"),json.getString("runTime") ,json.getString("ageRating"))
                                list.add(movie)
                                addMovie(movie)
                            }
                            _Movies.value=list
                        }catch (JSONException : JSONException){
                            Log.e(
                                this.javaClass.simpleName,
                                "launchVolleyAsyncHttpRequest(): Response.Listener Error=${JSONException}"
                            )
                        }
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
            HttpService.HttpAsyncMethod.POST -> {
                val stringRequest = JsonObjectRequest(
                    Request.Method.POST, url, Json, { //Handle Response
                            response ->
                        Log.e(
                            this.javaClass.simpleName,
                            "launchVolleyAsyncHttpRequest(): Response.Listener Response=${response}"
                        )

                        when(queryStr){
                            "/backlog/update_backlog"-> {
                                try {

                                }catch (JSONException :JSONException){
                                    Log.e(
                                        this.javaClass.simpleName,
                                        "launchVolleyAsyncHttpRequest(): Response.Listener Error=${JSONException}"
                                    )
                                }
                            }
                        }
                    }, { //Handle Error
                            error ->
                        Log.e(
                            this.javaClass.simpleName,
                            "launchVolleyAsyncHttpRequest(): Response.Listener Error=$error"
                        )
                        _Movies.value= emptyList()
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

}