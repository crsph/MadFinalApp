package com.example.madfinalapp.ui

import android.os.Bundle
import android.text.Html
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madfinalapp.R
import com.example.madfinalapp.adapter.TriviaHistoryDetailAdapter
import com.example.madfinalapp.repository.TriviaRecordRepository
import kotlinx.android.synthetic.main.fragment_trivia_history_detail.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class TriviaHistoryDetailFragment : Fragment() {

    private lateinit var triviaRecordRepository: TriviaRecordRepository
    private lateinit var triviaHistoryDetailAdapter: TriviaHistoryDetailAdapter
    private val triviaQuestionList = arrayListOf<String>()
    private val triviaChosenAnswerList = arrayListOf<String>()
    private val triviaCorrectAnswerList = arrayListOf<String>()
    private var position: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trivia_history_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        triviaRecordRepository = TriviaRecordRepository(requireContext())

        observeTriviaHistoryPosition()

        getTriviaHistoryFromDatabase()

        initViews()
    }

    private fun initViews() {
        triviaHistoryDetailAdapter = TriviaHistoryDetailAdapter(
            triviaQuestionList,
            triviaChosenAnswerList,
            triviaCorrectAnswerList
        )

        rvHistoryDetail.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvHistoryDetail.adapter = triviaHistoryDetailAdapter
        rvHistoryDetail.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    private fun getTriviaHistoryFromDatabase() {
        CoroutineScope(Dispatchers.Main).launch {
            val triviaCategoryList = withContext(Dispatchers.IO) {
                triviaRecordRepository.getAllCategories()
            }

            val triviaCategory = triviaCategoryList[position]

            val triviaRecordList = withContext(Dispatchers.IO) {
                triviaRecordRepository.getTriviaRecord(triviaCategory)
            }

            Log.d("Test: ", triviaRecordList.size.toString())

            triviaQuestionList.clear()
            triviaChosenAnswerList.clear()
            triviaCorrectAnswerList.clear()

            for (i in triviaRecordList) {
                triviaQuestionList.add(Html.fromHtml(i.triviaQuestion).toString())
                triviaChosenAnswerList.add(i.chosenTriviaAnswer)
                triviaCorrectAnswerList.add(i.correctTriviaAnswer)
            }

            triviaHistoryDetailAdapter.notifyDataSetChanged()
        }
    }

    private fun observeTriviaHistoryPosition() {
        setFragmentResultListener(REQ_TRIVIA_HISTORY_KEY) { _, bundle ->
            bundle.getInt(BUNDLE_TRIVIA_HISTORY_KEY).let {
                position = it
            }
        }
    }
}