package com.example.madfinalapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
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

const val REQ_TRIVIA_HISTORY_KEY = "req_trivia_history"
const val BUNDLE_TRIVIA_HISTORY_KEY = "bundle_trivia_history"

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

    /**
     * Prepares the recyclerview
     */
    private fun initViews() {
        triviaRecordAdapter = TriviaAdapter(triviaCategory, triviaTotalPercentage, ::onTriviaRecordClick)

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

    /**
     * Provides the upfollowing fragment the position of the clicked Trivia Record
     */
    private fun onTriviaRecordClick(position: Int) {
        setFragmentResult(
            REQ_TRIVIA_HISTORY_KEY,
            bundleOf(Pair(BUNDLE_TRIVIA_HISTORY_KEY, position))
        )

        findNavController().navigate(R.id.action_triviaHistoryFragment_to_triviaHistoryDetailFragment)
    }

    /**
     * This makes it able for the user to delete a Trivia Record history
     */
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

    /**
     * Retrieves all the Trivia Records from the database
     */
    private fun getTriviaRecordsFromDatabase() {
        CoroutineScope(Dispatchers.Main).launch {

            // Retrieves all the categories
            val category = withContext(Dispatchers.IO) {
                triviaRecordRepository.getAllCategories()
            }

            // Retrieves a list of the amount of correct answer per category
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

    /**
     * Converts the amount of correct answers into a percentage
     */
    private fun convertToPercentage(totalCorrectAnswer: List<Int>): List<Double> {
        val percentageInDoubleList: MutableList<Double> = mutableListOf()

        // Loops through of all the amount of correct answers and convert it to percentage
        for (i in totalCorrectAnswer) {
            val percentage = ((100.0 / 8.0) * i)
            percentageInDoubleList.add(percentage)
        }
        return percentageInDoubleList
    }
}
