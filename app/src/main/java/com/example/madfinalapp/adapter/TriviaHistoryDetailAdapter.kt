package com.example.madfinalapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.madfinalapp.R
import kotlinx.android.synthetic.main.item_trivia_history_detail.view.*

class TriviaHistoryDetailAdapter(
    private val triviaQuestionList: List<String>,
    private val triviaChosenAnswerList: List<String>,
    private val triviaCorrectAnswerList: List<String>
) : RecyclerView.Adapter<TriviaHistoryDetailAdapter.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context

        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_trivia_history_detail, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(
            triviaQuestionList[position],
            triviaChosenAnswerList[position],
            triviaCorrectAnswerList[position]
        )
    }

    override fun getItemCount(): Int {
        return triviaQuestionList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(triviaQuestion: String, triviaChosenAnswer: String, triviaCorrectAnswer: String) {
            itemView.tvHistoryDetailQuestion.text = triviaQuestion
            itemView.tvHistoryDetailChosenAnswer.text = triviaChosenAnswer
            itemView.tvHistoryDetailCorrectAnswer.text = triviaCorrectAnswer
        }
    }


}