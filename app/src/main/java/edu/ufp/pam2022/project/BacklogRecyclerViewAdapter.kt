package edu.ufp.pam2022.project

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView

import edu.ufp.pam2022.project.databinding.FragmentBackLogBinding
import edu.ufp.pam2022.project.library.Backlog

/**
 * [RecyclerView.Adapter] that can display a [Backlog].
 * TODO: Replace the implementation with code for your data type.
 */
class BacklogRecyclerViewAdapter(
    private val values: List<Backlog>
) : RecyclerView.Adapter<BacklogRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentBackLogBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.idView.text = (item.BackLogId).toString()
        holder.contentView.text = item.MovieName
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentBackLogBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.itemNumber
        val contentView: TextView = binding.content

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }

}