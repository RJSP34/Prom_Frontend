package edu.ufp.pam2022.project.Alt_Detailed_Activity.ui.gallery

import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
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
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class GalleryViewModel(application: FragmentActivity) : ViewModel() {

    val _Backlog = MutableLiveData<List<Backlog>>()
    var Backlog : LiveData<List<Backlog>> = _Backlog

    val _Status = MutableLiveData<List<Status>>()
    var Status : LiveData<List<Status>> = _Status

    var user : User= User(-1,"","")
    var _user : LiveData<List<User>>

    private var Application : FragmentActivity

    private lateinit var repository: DBBackLogRepository
    private var repository_Status: DBStatusRepository
    private var repository_User: DBUserRepository
    var checked = false
    private val urlStr="http://192.168.1.97:8000"
    //192.168.1.97
    private var volleyRequestQueue: RequestQueue
    private val TAG_TO_CANCEL_HTTP_REQUEST = "TAG_TO_CANCEL_HTTP_REQUEST"

    private val _text = MutableLiveData<String>().apply {
        value = "This is gallery Fragment"
    }
    val text: LiveData<String> = _text

    init {

        val DataBaseUserDao = AppDatabase.getDatabase(application).databaseUserDao()
        repository_User = DBUserRepository(DataBaseUserDao)
        Application=application
        val DataBaseStatusDao = AppDatabase.getDatabase(application).databaseStatusDao()
        repository_Status = DBStatusRepository(DataBaseStatusDao)

        val DataBaseBacklogDao = AppDatabase.getDatabase(Application).databaseBacklogDao()
        repository = DBBackLogRepository(DataBaseBacklogDao)
        Backlog=repository.readAllData

        _user = repository_User.readAllData
        //=================== Setup Volley to make async HTTP Request ===================
        Log.e(this.javaClass.simpleName, "onCreate(): going to set VOLLEY context...")
        // Get RequestQueue to execute async calls
        this.volleyRequestQueue = VolleyRequest.getInstance(application).requestQueue
    }

    fun Get_Backlog_By_Id( id: Int){
        val testQueryStr = "/backlog/getbacklogbyuser"
        val json= JSONArray()
        try {
            json.put(0, JSONObject().put("userId", id))
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        launchVolleyAsyncHttpRequest(urlStr, testQueryStr, HttpService.HttpAsyncMethod.POST,json)
    }

    fun addStatus(status:Status){
        viewModelScope.launch(Dispatchers.IO) {
            repository_Status.addStatus(status)
        }
    }

    fun setRepository(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.Get_Backlog_By_Id(user.UserId)
            Backlog=repository.readAllData
        }

    }

    fun addlistsBacklogsStatus(list :ArrayList<Backlog> ,list_Status :ArrayList<Status>){
        viewModelScope.launch(Dispatchers.IO) {
            repository_Status.Clear_Status()
            repository.CLear_Backlog()
            for (j in 0 until list_Status.size)
            {
                repository_Status.addStatus(list_Status[j])
            }
            for (j in 0 until list.size){
                repository.addBacklog(list[j])
            }
        }
    }

    fun Delete_Backlog(BacklogId: Int){
        val testQueryStr = "/backlog/remove_movie_from_backlog"
        val json= JSONObject()
        try {
            json.put("backlogId", BacklogId)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        launchVolleyAsyncHttpRequest(urlStr, testQueryStr, HttpService.HttpAsyncMethod.POST, json)
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
                                    val list_Status :ArrayList<Status> = ArrayList()
                                    var backlog:Backlog
                                    for (i in 0 until response.length()) {
                                        val json:JSONObject = response[i] as JSONObject
                                        backlog = if (json.isNull("userRating")) {
                                            Backlog(
                                                json.getInt("backlogId"),
                                                json.getString("movieName"),
                                                user.UserId,
                                                json.getInt("movieId"),
                                                json.getString("watchedDate"),
                                                json.getInt("statusId"),
                                                -1)
                                        } else{
                                            Backlog(json.getInt("backlogId"),json.getString("movieName"), user.UserId,json.getInt("movieId"),json.getString("watchedDate") , json.getInt("statusId"),json.getInt("userRating"))
                                        }
                                        // var j1=-1
                                        var bool = false
                                        for (j in 0 until list_Status.size)
                                        {
                                            if (list_Status[j].StatusId == json.getInt("statusId"))
                                            {
                                                bool = true
                                                break
                                            }
                                        }
                                        if (!bool){
                                            list_Status.add(Status(json.getInt("statusId"),json.getString("status")))
                                        }
                                        list.add(backlog)
                                    }
                                    addlistsBacklogsStatus(list,list_Status)
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
                            "/backlog/remove_movie_from_backlog"-> {
                                try {
                                    viewModelScope.launch(Dispatchers.IO) {
                                        repository.CLear_Backlog()
                                        checked = false
                                    }
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
                        _Backlog.value= emptyList()
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