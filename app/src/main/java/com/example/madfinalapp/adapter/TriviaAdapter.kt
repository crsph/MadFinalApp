package com.example.madfinalapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.madfinalapp.R
import kotlinx.android.synthetic.main.item_trivia_record.view.*

class TriviaAdapter(
    private val triviaCategoryList: List<String>,
    private val triviaTotalPercentageCorrectList: List<Double>,
    private val onClick: (Int) -> Unit
) :
    RecyclerView.Adapter<TriviaAdapter.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context

        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_trivia_record, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(triviaCategoryList[position], triviaTotalPercentageCorrectList[position])
    }


    override fun getItemCount(): Int {
        return triviaCategoryList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                onClick(adapterPosition)
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(triviaCategory: String, triviaTotalPercentage: Double) {
            itemView.tvCategory.text = triviaCategory
            itemView.tvPercentage.text = "$triviaTotalPercentage%"
        }
    }
}