package edu.ufp.pam2022.project.listMovie


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import edu.ufp.pam2022.project.MovieItemRecyclerViewAdapter
import edu.ufp.pam2022.project.BacklogRecyclerViewAdapter
import edu.ufp.pam2022.project.R
import edu.ufp.pam2022.project.databinding.ActivityMainMovieFragmentsBinding
import edu.ufp.pam2022.project.library.AppDatabase
import edu.ufp.pam2022.project.library.Backlog
import edu.ufp.pam2022.project.library.User
import edu.ufp.pam2022.project.main.login.ui.login.LoginMainActivity
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MainMovieFragments : AppCompatActivity(), PopupMenu.OnMenuItemClickListener {
    private lateinit var binding:ActivityMainMovieFragmentsBinding
    private lateinit var scrollingProjectActivityViewModel : ScrollingProjectActivityViewModel
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var user : User
    private var movie= true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainMovieFragmentsBinding.inflate(layoutInflater)
        val stringinput = binding.inputString
        setContentView(binding.root)

        user=User(intent.getIntExtra("UserId",0),
            intent.getStringExtra("Username").toString(),
            intent.getStringExtra("Username").toString())
        if(user.UserId==0){
            val intent = Intent(this@MainMovieFragments, LoginMainActivity::class.java)
            intent.putExtra("EXIT", false)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

         scrollingProjectActivityViewModel = ViewModelProvider(this, ScrollingProjectActivityViewModelFactory(this))[ScrollingProjectActivityViewModel::class.java]
         scrollingProjectActivityViewModel.Get_Movies()
         scrollingProjectActivityViewModel.Get_Backlog_By_Id(user.UserId)
        val reclird = findViewById<View>(R.id.ReclienerMovies) as RecyclerView
        val button:Button=findViewById(R.id.button_Input)
        reclird.layoutManager=LinearLayoutManager(this)

         scrollingProjectActivityViewModel.Movies.observe(this@MainMovieFragments, Observer
         {
            val loginState = it ?: return@Observer

             if (loginState.isNotEmpty()&& movie){
                     val movieItemRecyclerViewAdapter=
                         MovieItemRecyclerViewAdapter(loginState)
                     reclird.adapter=movieItemRecyclerViewAdapter
                 reclird.layoutManager = LinearLayoutManager(this)
                 }
         })

        scrollingProjectActivityViewModel.Backlog.observe(this@MainMovieFragments, Observer
        {
            val loginState = it ?: return@Observer
            if (loginState.isNotEmpty() && !movie) {
                val backlogRecyclerViewAdapter =
                    BacklogRecyclerViewAdapter(loginState)
                reclird.adapter = backlogRecyclerViewAdapter
                reclird.layoutManager = LinearLayoutManager(this)
            }
        })

        button.setOnClickListener {
            val inputRec=stringinput.text.toString()
            if (inputRec.isEmpty()){
                Toast.makeText(this,"String Empty", Toast.LENGTH_SHORT).show()
            }
            else
            {
                if (movie) {
                    val movies = scrollingProjectActivityViewModel.Movies.value
                    if (movies != null) {
                        if (!movies.isEmpty()) {
                            for (i in movies.indices) {
                                if (movies[i].Name == inputRec) {
                                    val inflater =
                                        getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
                                    val popupView: View =
                                        inflater.inflate(R.layout.add_backlog, null)

                                    // create the popup window
                                    val imm =
                                        this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                                    imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
                                    // create the popup window
                                    val width = LinearLayout.LayoutParams.MATCH_PARENT
                                    val height = LinearLayout.LayoutParams.MATCH_PARENT
                                    val focusable =
                                        true // lets taps outside the popup also dismiss it

                                    val popupWindow =
                                        PopupWindow(popupView, width, height, focusable)

                                    popupWindow.showAtLocation(
                                        this.currentFocus,
                                        Gravity.CENTER,
                                        0,
                                        0
                                    )
                                    val tex =
                                        popupWindow.contentView.findViewById<TextView>(R.id.popup_Movie_Name)
                                    tex.text = inputRec
                                    val but =
                                        popupWindow.contentView.findViewById<Button>(R.id.pop_button)

                                    val userstatus1 =
                                        popupWindow.contentView.findViewById<RadioButton>(R.id.button1)
                                    val userstatus2 =
                                        popupWindow.contentView.findViewById<RadioButton>(R.id.button2)
                                    val userstatus3 =
                                        popupWindow.contentView.findViewById<RadioButton>(R.id.button3)
                                    val userstatus4 =
                                        popupWindow.contentView.findViewById<RadioButton>(R.id.button4)
                                    val userstatus5 =
                                        popupWindow.contentView.findViewById<RadioButton>(R.id.button5)
                                    val userstatus6 =
                                        popupWindow.contentView.findViewById<RadioButton>(R.id.button6)

                                    var bool1 = false
                                    var bool2 = false
                                    var bool3 = false
                                    var bool4 = false
                                    var bool5 = false
                                    var bool6 = false

                                    userstatus1.setOnClickListener() {
                                        if (bool1) {
                                            userstatus1.isChecked = false
                                            bool1 = false
                                        } else {
                                            userstatus1.isChecked = true
                                            bool1 = true
                                        }
                                    }

                                    userstatus2.setOnClickListener() {
                                        if (bool2) {
                                            userstatus2.isChecked = false
                                            bool2 = false
                                        } else {
                                            userstatus2.isChecked = true
                                            bool2 = true
                                        }
                                    }

                                    userstatus3.setOnClickListener() {
                                        if (bool3) {
                                            userstatus3.isChecked = false
                                            bool3 = false
                                        } else {
                                            userstatus3.isChecked = true
                                            bool3 = true
                                        }
                                    }

                                    userstatus4.setOnClickListener() {
                                        if (bool4) {
                                            userstatus4.isChecked = false
                                            bool4 = false
                                        } else {
                                            userstatus4.isChecked = true
                                            bool4 = true
                                        }
                                    }

                                    userstatus5.setOnClickListener() {
                                        if (bool5) {
                                            userstatus5.isChecked = false
                                            bool5 = false
                                        } else {
                                            userstatus5.isChecked = true
                                            bool5 = true
                                        }
                                    }

                                    userstatus6.setOnClickListener() {
                                        if (bool6) {
                                            userstatus6.isChecked = false
                                            bool6 = false
                                        } else {
                                            userstatus6.isChecked = true
                                            bool6 = true
                                        }
                                    }

                                    but.setOnClickListener() {
//                                        val date =
//                                            popupWindow.contentView.findViewById<TextView>(R.id.id_date_rel)
//                                        val impleDateFormat =
//                                            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
//                                        val localDateTime =
//                                            LocalDateTime.parse(date.text, impleDateFormat)
                                        val date = popupWindow.contentView.findViewById<DatePicker>(R.id.datePicker1)
                                        val dateObj = LocalDate.of(date.year, date.month+1, date.dayOfMonth)
                                        val dateFormat = "MM-dd-yyyy" // mention the format you need
                                        val dateStr = dateObj.format(DateTimeFormatter.ISO_DATE)
//                                        val sdf = SimpleDateFormat(dateFormat, Locale.US)
//                                        val dateStr = sdf.format(dateObj)
                                        val userrating =
                                            popupWindow.contentView.findViewById<TextView>(R.id.popup_Movie_rating_input)
                                        for (j in 1 until 7) {
                                            when (j) {
                                                1 -> {
                                                    if (bool1) {
                                                        scrollingProjectActivityViewModel.Insert_Backlog(
                                                            user.UserId,
                                                            movies[i].MovieId,
                                                            dateStr,
                                                            Integer.parseInt(userrating.text.toString()),
                                                            1
                                                        )
                                                    }
                                                }
                                                2 -> {
                                                    if (bool2) {
                                                        scrollingProjectActivityViewModel.Insert_Backlog(
                                                            user.UserId,
                                                            movies[i].MovieId,
                                                            dateStr,
                                                            Integer.parseInt(userrating.text.toString()),
                                                            2
                                                        )
                                                    }
                                                }
                                                3 -> {
                                                    if (bool3) {
                                                        scrollingProjectActivityViewModel.Insert_Backlog(
                                                            user.UserId,
                                                            movies[i].MovieId,
                                                            dateStr,
                                                            Integer.parseInt(userrating.text.toString()),
                                                            3
                                                        )
                                                    }
                                                }
                                                4 -> {
                                                    if (bool4) {
                                                        scrollingProjectActivityViewModel.Insert_Backlog(
                                                            user.UserId,
                                                            movies[i].MovieId,
                                                            dateStr,
                                                            Integer.parseInt(userrating.text.toString()),
                                                            4
                                                        )
                                                    }
                                                }
                                                5 -> {
                                                    if (bool5) {
                                                        scrollingProjectActivityViewModel.Insert_Backlog(
                                                            user.UserId,
                                                            movies[i].MovieId,
                                                            dateStr,
                                                            Integer.parseInt(userrating.text.toString()),
                                                            5
                                                        )
                                                    }
                                                }
                                                6 -> {
                                                    if (bool6) {
                                                        scrollingProjectActivityViewModel.Insert_Backlog(
                                                            user.UserId,
                                                            movies[i].MovieId,
                                                            dateStr,
                                                            Integer.parseInt(userrating.text.toString()),
                                                            6
                                                        )
                                                    }
                                                }
                                            }

                                        }
                                        popupWindow.dismiss()
                                    }
                                    break
                                }

                            }
                        } else {
                            Toast.makeText(
                                this,
                                "Cant add to collections , list empty",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }else{
                    val backlog = scrollingProjectActivityViewModel.Backlog.value
                    if (backlog != null)
                    {
                        if (backlog.isNotEmpty()) {
                            for (i in backlog.indices) {
                                try {
                                    if (backlog[i].BackLogId == Integer.parseInt(inputRec)) {
                                        scrollingProjectActivityViewModel.Delete_Backlog(backlog[i].BackLogId)

                                    }
                                }catch (nfe: NumberFormatException){
                                    break
                                }
                            }
                        }else{

                            Toast.makeText(
                                this,
                                "Cant remove collection , list empty",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                    }
                }
            }
    }

    fun showPopup(view: View) {
        PopupMenu(this, view).apply {
            // MainActivity implements OnMenuItemClickListener
            setOnMenuItemClickListener(this@MainMovieFragments)
            inflate(R.menu.navbar)
            show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.navbar, menu)
        return true
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        return when (item.title) {
            "My Collections"-> {
                if (movie) {
                    movie =false
                    val button:Button=findViewById(R.id.button_Input)
                    button.text="Remove"
                    scrollingProjectActivityViewModel.Get_Backlog_By_Id(user.UserId)
                }
                else
                {
                    Toast.makeText(this,"You already are in Movies",Toast.LENGTH_SHORT)
                }
                true
            }
            "Movie"-> {
                if (!movie){
                    movie =true
                    val button:Button=findViewById(R.id.button_Input)
                    button.text="Add"
                    scrollingProjectActivityViewModel.Get_Movies()
                }
                else{
                    Toast.makeText(this,"You already are in Collections",Toast.LENGTH_SHORT)
                }
                true
            }
            "Logout"-> {
                Toast.makeText(this,"Bye"+user.Username, Toast.LENGTH_SHORT).show()
                val switchActivityIntent = Intent(this, LoginMainActivity::class.java)
                startActivity(switchActivityIntent)
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }
}
