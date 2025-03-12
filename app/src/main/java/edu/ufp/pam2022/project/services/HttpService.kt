package edu.ufp.pam2022.project.services
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import edu.ufp.pam2022.project.library.User
import edu.ufp.pam2022.project.main.login.ui.login.LoginMainActivity
import edu.ufp.pam2022.project.services.repositoryVolley.VolleyRequest
import org.json.JSONException
import org.json.JSONObject


class HttpService(App: AppCompatActivity) {
    enum class HttpAsyncMethod {
        GET, POST, PUT
    }


}