package edu.ufp.pam2022.project.Alt_Detailed_Activity.ui.slideshow

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import edu.ufp.pam2022.project.MovieItemRecyclerViewAdapter
import edu.ufp.pam2022.project.R
import edu.ufp.pam2022.project.databinding.FragmentSlideshowBinding
import edu.ufp.pam2022.project.library.Movie
import edu.ufp.pam2022.project.services.HttpService
import org.json.JSONException
import org.json.JSONObject
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class SlideshowFragment : Fragment() {

    private var _binding: FragmentSlideshowBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val slideshowViewModel =
            ViewModelProvider(this, SlideshowViewModelFactory(this.requireActivity()))[SlideshowViewModel::class.java]

        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val reclird = binding.MovieReclinerId
        val stringinput = binding.inputString
        val button: Button = binding.buttonInput


        val textView: TextView = binding.textSlideshow
        slideshowViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        slideshowViewModel._user.observe(viewLifecycleOwner,Observer{
            val UserList = it ?: return@Observer
            slideshowViewModel.user= UserList[0]
        })

        slideshowViewModel.Movies.observe(viewLifecycleOwner, Observer {
            val movieList = it ?: return@Observer
            if (!movieList.isEmpty()) {
                val movieItemRecyclerViewAdapter=
                    MovieItemRecyclerViewAdapter(movieList)
                reclird.adapter=movieItemRecyclerViewAdapter
                reclird.layoutManager = LinearLayoutManager(this.context)
            }
            else
            {
                if (slideshowViewModel.checked) {
                    val empty_Movie = Movie(-1, "Movies Empty", "", 0, "", "")
                    val empty: MutableList<Movie> = mutableListOf(empty_Movie)
                    val movieItemRecyclerViewAdapter =
                        MovieItemRecyclerViewAdapter(empty)
                    reclird.adapter = movieItemRecyclerViewAdapter
                    reclird.layoutManager = LinearLayoutManager(this.context)
                }
                else
                {
                    slideshowViewModel.Get_Movies()
                    slideshowViewModel.checked=true
                }
            }
        })

        button.setOnClickListener {
            val inputRec=stringinput.text.toString()
            if (inputRec.isEmpty()){
                Toast.makeText(this.requireContext(),"String Empty", Toast.LENGTH_SHORT).show()
            }
            else
            {
                val movies = slideshowViewModel.Movies.value
                if (movies != null) {
                    if (movies.isNotEmpty()) {
                        for (i in movies.indices) {
                            if (movies[i].Name == inputRec) {
                                val inflater =
                                    this.requireContext().getSystemService(AppCompatActivity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                                val popupView: View =
                                    inflater.inflate(R.layout.add_backlog, null)

                                // create the popup window
                                val imm =
                                    this.requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                                imm.hideSoftInputFromWindow(this.requireActivity().currentFocus?.windowToken, 0)
                                // create the popup window
                                val width = LinearLayout.LayoutParams.MATCH_PARENT
                                val height = LinearLayout.LayoutParams.MATCH_PARENT
                                val focusable =
                                    true // lets taps outside the popup also dismiss it

                                val popupWindow =
                                    PopupWindow(popupView, width, height, focusable)

                                popupWindow.showAtLocation(
                                    this.requireActivity().currentFocus,
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
                                        var userstatus = false
                                        when (j) {
                                            1 -> {
                                                userstatus = bool1
                                            }
                                            2 -> {
                                                userstatus = bool2
                                            }
                                            3 -> {
                                                userstatus = bool3
                                            }
                                            4 -> {
                                                userstatus = bool4
                                            }
                                            5 -> {
                                                userstatus = bool5
                                            }
                                            6 -> {
                                                userstatus = bool6
                                            }
                                        }

                                        if (!userstatus) continue

                                        slideshowViewModel.insertBacklog(movies[i].MovieId, dateStr, Integer.parseInt(userrating.text.toString()), j)

//                                            if (bool2) {
//                                                scrollingProjectActivityViewModel.Insert_Backlog(
//                                                    user.UserId,
//                                                    movies[i].MovieId,
//                                                    dateStr,
//                                                    Integer.parseInt(userrating.text.toString()),
//                                                    j
//                                                )
//                                            }
                                        }
                                        popupWindow.dismiss()
                                    }

                                }

                            }
                        }
                    } else {
                        Toast.makeText(
                            this.requireContext(),
                            "Cant add to collections , list empty",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}