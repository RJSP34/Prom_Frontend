package edu.ufp.pam2022.project.services.repositoryVolley

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class VolleyRequest constructor(context: Context) {
    companion object {
        @Volatile
        private var INSTANCE: VolleyRequest? = null

        fun getInstance(context: Context) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: VolleyRequest(context).also {
                    INSTANCE = it
                }
            }
    }

    val requestQueue: RequestQueue by lazy {
        // The applicationContext is key, it keeps from leaking the Activity or BroadcastReceiver.
        Volley.newRequestQueue(context.applicationContext)
    }

    fun <T> addToRequestQueue(req: Request<T>) {
        requestQueue.add(req)
    }


}