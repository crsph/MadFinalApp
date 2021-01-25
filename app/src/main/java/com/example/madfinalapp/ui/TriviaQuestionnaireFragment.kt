package com.example.madfinalapp.ui

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.madfinalapp.R
import com.example.madfinalapp.model.TriviaRecord
import com.example.madfinalapp.repository.TriviaRecordRepository
import com.example.madfinalapp.vm.TriviaViewModel
import kotlinx.android.synthetic.main.fragment_trivia_questionnaire.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class TriviaQuestionnaireFragment : Fragment() {

    private val viewModel: TriviaViewModel by activityViewModels()
    private lateinit var triviaRecordRepository: TriviaRecordRepository
    private var triviaCategoryDatabaseList: List<String> = listOf()
    private var triviaRecordList: MutableList<TriviaRecord> = mutableListOf()
    private var triviaQuestionList: MutableList<String> = mutableListOf()
    private var buttons: ArrayList<Button> = arrayListOf()
    private var counter: Int = 0
    private var totalCorrectAnswers: Double = 0.0
    private var totalWrongAnswers: Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_trivia_questionnaire, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize triviaRecordRepository
        triviaRecordRepository = TriviaRecordRepository(requireContext())

        // Populate triviaCategoryDatabaseList with all the categories from the database
//        triviaCategoryDatabaseList = getTriviaRecords()

        // Initialize buttons array with four buttons
        buttons = arrayListOf(btnAnswerOne, btnAnswerTwo, btnAnswerThree, btnAnswerFour)

        // Provide the four buttons in the array with an action
        createButtons()
    }

    private fun createButtons() {
        triviaQuestionIterator(counter)

        for (i in 0..3) {
            buttons[i].setOnClickListener {
                triviaAnswerValidation(counter, buttons[i].text.toString())
                counter++
                triviaQuestionIterator(counter)
            }
        }
    }

    private fun triviaQuestionIterator(counter: Int) {
        if (counter <= 7) {
            val trivia = viewModel.trivia.value?.get(counter)

            if (trivia != null) {
                triviaQuestionList.add(trivia.correct_answer) // Add the correct answer to triviaQuestionList
                triviaQuestionList.addAll(trivia.incorrect_answers) // Add the incorrect answer to triviaQuestionList
                triviaQuestionList.shuffle() // Shuffle the answers in the list

                tvQuestionLabel.text = String.format("Question #%d", counter + 1)
                tvQuestion.text = Html.fromHtml(trivia.question)
                    .toString() // The HTML.fromHtml() decodes the HTML entity (e.g. &quot; to ")
                btnAnswerOne.text = Html.fromHtml(triviaQuestionList[0])
                btnAnswerTwo.text = Html.fromHtml(triviaQuestionList[1])
                btnAnswerThree.text = Html.fromHtml(triviaQuestionList[2])
                btnAnswerFour.text = Html.fromHtml(triviaQuestionList[3])

                triviaQuestionList.clear() // Empty the triviaQuestionList
            }
        } else {
            val triviaCategory = viewModel.trivia.value?.get(0)?.category.toString()

            deleteTriviaRecordsFromDatabase(triviaCategory)

            insertTriviaRecordsInDatabase()

            findNavController().navigate(R.id.action_triviaQuestionnaireFragment_to_triviaCategoryFragment)
        }
    }

    private fun triviaAnswerValidation(counter: Int, answer: String) {
        if (counter <= 7) {
            val triviaRecord: TriviaRecord

            val triviaItem = viewModel.trivia.value?.get(counter)
            val triviaCategory = triviaItem?.category.toString().replace("Entertainment: ", "")
            val triviaQuestion = triviaItem?.question.toString()
            val triviaCorrectAnswer = triviaItem?.correct_answer.toString()

            // Check if the chosen answer is correct or wrong
            if (triviaCorrectAnswer == answer) {
                triviaRecord =
                    TriviaRecord(triviaCategory, triviaQuestion, triviaCorrectAnswer, answer, true)
                triviaRecordList.add(triviaRecord)
                totalCorrectAnswers++
            } else {
                triviaRecord =
                    TriviaRecord(triviaCategory, triviaQuestion, triviaCorrectAnswer, answer, false)
                triviaRecordList.add(triviaRecord)
                totalWrongAnswers++
            }
        }
    }

    private fun getTriviaRecords(): List<String> {
        var triviaCategoryDatabaseList: List<String> = listOf()

        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                triviaCategoryDatabaseList = triviaRecordRepository.getAllCategories()
            }
        }

        return triviaCategoryDatabaseList
    }

    private fun insertTriviaRecordsInDatabase() {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                triviaRecordRepository.insertTriviaRecord(triviaRecordList)
            }
        }
    }

    private fun deleteTriviaRecordsFromDatabase(triviaCategory: String) {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                triviaRecordRepository.deleteTriviaRecord(triviaCategory)
            }
        }
    }
}