package edu.ufp.pam2022.project.Alt_Detailed_Activity.ui.gallery

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
import edu.ufp.pam2022.project.BacklogRecyclerViewAdapter
import edu.ufp.pam2022.project.MovieItemRecyclerViewAdapter
import edu.ufp.pam2022.project.R
import edu.ufp.pam2022.project.databinding.FragmentGalleryBinding
import edu.ufp.pam2022.project.library.AppDatabase
import edu.ufp.pam2022.project.library.DBBackLogRepository
import edu.ufp.pam2022.project.library.Movie
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val galleryViewModel =
            ViewModelProvider(this,GalleryViewModelFactory(this.requireActivity()))[GalleryViewModel::class.java]

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val reclird = binding.BacklogReclinerId
        val stringinput = binding.inputString
        val button: Button = binding.buttonInput

        galleryViewModel._user.observe(viewLifecycleOwner,Observer{
            val UserList = it ?: return@Observer
            galleryViewModel.user= UserList[0]
            galleryViewModel.setRepository()
            galleryViewModel.Backlog.observe(viewLifecycleOwner, Observer {
                val BacklogList = it ?: return@Observer
                if (BacklogList.isNotEmpty() && galleryViewModel.checked) {
                    val backlogItemRecyclerViewAdapter=
                        BacklogRecyclerViewAdapter(BacklogList)
                    reclird.adapter=backlogItemRecyclerViewAdapter
                    reclird.layoutManager = LinearLayoutManager(this.context)
                    galleryViewModel._Backlog.value= BacklogList
                }
                else
                {
                    if (galleryViewModel.checked) {
                        val empty_Movie = Movie(-1, "Movies Empty", "", 0, "", "")
                        val empty: MutableList<Movie> = mutableListOf(empty_Movie)
                        val movieItemRecyclerViewAdapter =
                            MovieItemRecyclerViewAdapter(empty)
                        reclird.adapter = movieItemRecyclerViewAdapter
                        reclird.layoutManager = LinearLayoutManager(this.context)
                    }
                    else
                    {
                        galleryViewModel.Get_Backlog_By_Id(galleryViewModel.user.UserId)
                        galleryViewModel.checked=true
                    }
                }
            })
        })

        button.setOnClickListener {
            val inputRec=stringinput.text.toString()
            if (inputRec.isEmpty()){
                Toast.makeText(this.requireContext(),"String Empty", Toast.LENGTH_SHORT).show()
            }
            else
            {
                val backlog = galleryViewModel._Backlog.value
                if (backlog != null)
                {
                    if (backlog.isNotEmpty()) {
                        for (i in backlog.indices) {
                            try {
                                if (backlog[i].BackLogId == Integer.parseInt(inputRec)) {
                                    galleryViewModel.Delete_Backlog(backlog[i].BackLogId)

                                }
                            }catch (nfe: NumberFormatException){
                                break
                            }
                        }
                    }else{

                        Toast.makeText(
                            this.requireContext(),
                            "Cant remove collection , list empty",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }

        val textView: TextView = binding.textGallery
        galleryViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}