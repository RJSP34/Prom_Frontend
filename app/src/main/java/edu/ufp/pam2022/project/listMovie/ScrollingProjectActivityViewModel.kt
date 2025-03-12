package edu.ufp.pam2022.project.listMovie

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import edu.ufp.pam2022.project.library.Backlog
import edu.ufp.pam2022.project.library.Movie
import edu.ufp.pam2022.project.library.Status
import edu.ufp.pam2022.project.services.HttpService
import edu.ufp.pam2022.project.services.repositoryVolley.VolleyRequest
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class ScrollingProjectActivityViewModel(App: AppCompatActivity) : ViewModel() {

    private val urlStr="http://10.100.190.89:8000"
    //192.168.1.97
    private var volleyRequestQueue: RequestQueue
    private val TAG_TO_CANCEL_HTTP_REQUEST = "TAG_TO_CANCEL_HTTP_REQUEST"
    private val _Movies = MutableLiveData<List<Movie>>()
    var Movies:LiveData<List<Movie>> = _Movies
    private val _BackLog = MutableLiveData<List<Backlog>>()
    var Backlog:LiveData<List<Backlog>> = _BackLog
    private val _Status:ArrayList<Status> = ArrayList()


    init
    {
        //=================== Setup Volley to make async HTTP Request ===================
        Log.e(this.javaClass.simpleName, "onCreate(): going to set VOLLEY context...")
        // Get RequestQueue to execute async calls
        this.volleyRequestQueue = VolleyRequest.getInstance(App).requestQueue
    }

    fun Get_Movies(){
        val testQueryStr = "/movies/getmovies"
        launchVolleyAsyncHttpRequest(urlStr, testQueryStr,HttpService.HttpAsyncMethod.GET)
    }

    fun Get_Backlog_By_Id( id: Int){
        val testQueryStr = "/backlog/getbacklogbyuser"
        val json= JSONArray()
        try {
            json.put(0,JSONObject().put("userId", id))
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        launchVolleyAsyncHttpRequest(urlStr, testQueryStr,HttpService.HttpAsyncMethod.POST,json)
    }

    fun Insert_Backlog(userid: Int, movieid: Int, date: String, userrating: Int, userstatus: Int ){
        val testQueryStr = "/backlog/update_backlog"
        val json= JSONObject()
        try {
            json.put("userId", userid)
            json.put("movieId", movieid)
            json.put("watchedDate", date)
            json.put("statusId", userstatus)
            json.put("rating", userrating)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        launchVolleyAsyncHttpRequest(urlStr, testQueryStr,HttpService.HttpAsyncMethod.POST,json)
    }

    fun Delete_Backlog(BacklogId: Int){
        val testQueryStr = "/backlog/remove_movie_from_backlog"
        val json= JSONObject()
        try {
            json.put("backlogId", BacklogId)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        launchVolleyAsyncHttpRequest(urlStr, testQueryStr,HttpService.HttpAsyncMethod.POST,json)
    }

    fun launchVolleyAsyncHttpRequest(urlStr: String, queryStr: String, type: HttpService.HttpAsyncMethod): Any {

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
                            val list :ArrayList<Movie> =ArrayList()
                            for (i in 0 until response.length()) {
                              val json:JSONObject = response[i] as JSONObject
                              val movie =Movie(json.getInt("id"),json.getString("name"), json.getString("releaseDate"), json.getInt("imdbRating"),json.getString("runTime") ,json.getString("ageRating"))
                              list.add(movie)
                            }
                            _Movies.value=list
                        }catch (JSONException :JSONException){
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
            else -> return ""
        }
    }

    fun launchVolleyAsyncHttpRequest(urlStr: String, queryStr: String, type: HttpService.HttpAsyncMethod, Json: JSONArray): Any{

        val url = "$urlStr$queryStr"
        Log.e(this.javaClass.simpleName, "launchVolleyAsyncHttpRequest(): url=$url")

        when (type) {
            HttpService.HttpAsyncMethod.POST -> {
                val stringRequest = JsonArrayRequest(
                    Request.Method.POST, url, Json, { //Handle Response
                            response ->
                        Log.e(
                            this.javaClass.simpleName,
                            "launchVolleyAsyncHttpRequest(): Response.Listener Response=${response}"
                        )

                        when(queryStr){
                            "/backlog/getbacklogbyuser"-> {
                                try {
                                    val list :ArrayList<Backlog> =ArrayList()
                                    var backlog:Backlog
                                    for (i in 0 until response.length()) {
                                        val json:JSONObject = response[i] as JSONObject
                                        backlog = if (json.isNull("userRating")) {
                                            Backlog(
                                                json.getInt("backlogId"),
                                                json.getString("movieName"),
                                                1,
                                                json.getInt("movieId"),
                                                json.getString("watchedDate"),
                                                json.getInt("statusId"),
                                                -1)
                                        } else{
                                            Backlog(json.getInt("backlogId"),json.getString("movieName"),json.getInt("movieId"), 1, json.getString("watchedDate") , json.getInt("statusId"),json.getInt("userRating"))
                                        }
                                        var j1=-1
                                        for (j in 0 until _Status.size)
                                        {
                                            if (_Status[j].StatusId== json.getInt("statusId"))
                                            {
                                                j1=j
                                                break
                                            }
                                            else
                                            {
                                                j1=j
                                            }
                                        }
                                        if (j1==-1|| j1+1==_Status.size){
                                            _Status.add(  Status(json.getInt("statusId"),json.getString("status"))
                                            )
                                        }
                                        list.add(backlog)
                                    }
                                    _BackLog.value=list
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
            HttpService.HttpAsyncMethod.PUT -> {
                val stringRequest = JsonArrayRequest(
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

}