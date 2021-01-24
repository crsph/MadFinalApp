package com.example.madfinalapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madfinalapp.R
import com.example.madfinalapp.adapter.TriviaAdapter
import com.example.madfinalapp.repository.TriviaRecordRepository
import kotlinx.android.synthetic.main.fragment_trivia_history.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class TriviaHistoryFragment : Fragment() {

    private lateinit var triviaRecordRepository: TriviaRecordRepository
    private val triviaCategory = arrayListOf<String>()
    private val triviaTotalPercentage = arrayListOf<Double>()
    private lateinit var triviaRecordAdapter: TriviaAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trivia_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        triviaRecordRepository = TriviaRecordRepository(requireContext())

        getTriviaRecordsFromDatabase()

        initViews()
    }

    private fun initViews() {
        triviaRecordAdapter = TriviaAdapter(triviaCategory, triviaTotalPercentage, ::onMovieClick)

        rvTriviaHistory.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvTriviaHistory.adapter = triviaRecordAdapter
        rvTriviaHistory.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )

        createItemTouchHelper().attachToRecyclerView(rvTriviaHistory)
    }

    private fun onMovieClick(index: Int) {
        Toast.makeText(context, "Hello World!", Toast.LENGTH_SHORT).show()
    }

    private fun createItemTouchHelper(): ItemTouchHelper {
        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            // Callback triggered when a user swiped an item.
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val triviaRecordToDelete = triviaCategory[position]

                CoroutineScope(Dispatchers.Main).launch {
                    withContext(Dispatchers.IO) {
                        triviaRecordRepository.deleteTriviaRecord(triviaRecordToDelete)
                    }
                    getTriviaRecordsFromDatabase()
                }
            }
        }
        return ItemTouchHelper(callback)
    }

    private fun getTriviaRecordsFromDatabase() {
        CoroutineScope(Dispatchers.Main).launch {
            val category = withContext(Dispatchers.IO) {
                triviaRecordRepository.getAllCategories()
            }

            val totalCorrectAnswer = withContext(Dispatchers.IO) {
                triviaRecordRepository.getTotalCorrectAnswers()
            }

            val percentage = convertToPercentage(totalCorrectAnswer)

            this@TriviaHistoryFragment.triviaCategory.clear()
            this@TriviaHistoryFragment.triviaTotalPercentage.clear()

            this@TriviaHistoryFragment.triviaCategory.addAll(category)
            this@TriviaHistoryFragment.triviaTotalPercentage.addAll(percentage)

            triviaRecordAdapter.notifyDataSetChanged()
        }
    }

    private fun convertToPercentage(totalCorrectAnswer: List<Int>): List<Double> {
        val percentageInDoubleList: MutableList<Double> = mutableListOf()

        for (i in totalCorrectAnswer) {
            val percentage = ((100.0 / 8.0) * i)
            percentageInDoubleList.add(percentage)
        }
        return percentageInDoubleList
    }
}
