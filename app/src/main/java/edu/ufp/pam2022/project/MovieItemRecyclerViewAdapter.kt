package edu.ufp.pam2022.project

import android.content.Context
import android.view.Gravity
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import edu.ufp.pam2022.project.databinding.FragmentMovieBinding
import edu.ufp.pam2022.project.library.AppDatabase
import edu.ufp.pam2022.project.library.DBMovieRepository
import edu.ufp.pam2022.project.library.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * [RecyclerView.Adapter] that can display a [Movie].
 * TODO: Replace the implementation with code for your data type.
 */
class MovieItemRecyclerViewAdapter(private val values: List<Movie>) : RecyclerView.Adapter<MovieItemRecyclerViewAdapter.ViewHolder>() {

    lateinit var Movies : LiveData<List<Movie>>
    private lateinit var repository: DBMovieRepository
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val Moviedao = AppDatabase.getDatabase(parent.context).databaseMovieDao()
        repository = DBMovieRepository(Moviedao)
        Movies = repository.readAllData
        return ViewHolder(
            FragmentMovieBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = values[position]
        holder.idView.text = item.MovieId.toString()
        holder.contentView.text = item.Name
        holder.date.text= item.ReleaseDate
        holder.itemView.setOnClickListener {
            val inflater =LayoutInflater.from(holder.itemView.context)
                val popupView: View =
                inflater.inflate(R.layout.popwindow, null)

            // create the popup window
            val imm =
                holder.itemView.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(holder.itemView.windowToken, 0)
            // create the popup window
            val width = LinearLayout.LayoutParams.MATCH_PARENT
            val height = LinearLayout.LayoutParams.MATCH_PARENT
            val focusable =
                true // lets taps outside the popup also dismiss it

            val popupWindow =
                PopupWindow(popupView, width, height, focusable)

            popupWindow.showAtLocation(
                holder.itemView.rootView,
                Gravity.CENTER,
                0,
                0
            )

            popupWindow.contentView.setOnClickListener {
                popupWindow.dismiss()
            }

            val title = popupWindow.contentView.findViewById<TextView>(R.id.popup_Movie_Name)
            title.text = item.Name

            val table_Name = popupWindow.contentView.findViewById<TextView>(R.id.Name_id_R)
            table_Name.text = item.Name

            val table_runningtime = popupWindow.contentView.findViewById<TextView>(R.id.runTime_id_R)
            table_runningtime.text = item.runTime

            val table_realease = popupWindow.contentView.findViewById<TextView>(R.id.realease_id_R)
            table_realease.text = item.ReleaseDate

            val table_ageRating = popupWindow.contentView.findViewById<TextView>(R.id.ageRating_id_R)
            table_ageRating.text = item.ageRatingId
        }

    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentMovieBinding) : RecyclerView.ViewHolder(binding.root) {

        val idView: TextView = binding.MovieId
        val contentView: TextView = binding.MovieName
        val date: TextView = binding.MovieDateRelease

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }

}