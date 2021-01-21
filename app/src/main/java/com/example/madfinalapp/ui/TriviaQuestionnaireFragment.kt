package com.example.madfinalapp.ui

import android.os.Bundle
import android.text.Html
import android.util.Log
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
    private var triviaRecordList: MutableList<TriviaRecord> = mutableListOf()
    private var triviaQuestionList: MutableList<String> = mutableListOf()
    private var counter: Int = 0
    private var buttons: ArrayList<Button> = arrayListOf()

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

        // Deletes everything in the ROOM database for now
//        CoroutineScope(Dispatchers.Main).launch {
//            triviaRecordRepository.deleteAll()
//        }

        // Initialize buttons array with four buttons
        buttons = arrayListOf(btnAnswerOne, btnAnswerTwo, btnAnswerThree, btnAnswerFour)

        // Provide the four buttons in the array with an action
        createButtons()

//        btnAnswerOne.setBackgroundColor(resources.getColor(R.color.mandy))
//        btnAnswerTwo.setBackgroundColor(resources.getColor(R.color.mandy))
//        btnAnswerThree.setBackgroundColor(resources.getColor(R.color.mandy))
//        btnAnswerFour.setBackgroundColor(resources.getColor(R.color.mandy))
//
//        observeButtonColorChange()
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
            findNavController().navigate(R.id.action_triviaQuestionnaireFragment_to_triviaCategoryFragment)
        }
    }

    private fun triviaAnswerValidation(counter: Int, answer: String) {
        if (counter <= 7) {
            val triviaRecord: TriviaRecord
            var triviaCategoryDatabaseList: List<String>
            val triviaItem = viewModel.trivia.value?.get(counter)
            val triviaCategory = triviaItem?.category.toString()
            val triviaQuestion = triviaItem?.question.toString()
            val triviaCorrectAnswer = triviaItem?.correct_answer.toString()

            // Check if the chosen answer is correct or wrong
            if (triviaCorrectAnswer == answer) {
                triviaRecord =
                    TriviaRecord(triviaCategory, triviaQuestion, triviaCorrectAnswer, answer, true)
                triviaRecordList.add(triviaRecord)

                Log.d("Test", "The answer is correct and is inserted in the list")
            } else {
                triviaRecord =
                    TriviaRecord(triviaCategory, triviaQuestion, triviaCorrectAnswer, answer, false)
                triviaRecordList.add(triviaRecord)

                Log.d("Test", "The answer is wrong but it is inserted in the list")
            }


            // Check if the triviaRecordList is populated with 8 records and insert it in the database
            if (triviaRecordList.size == 8) {

//                for (i in 0..7) {
//                    Log.d("Trivia Record List: ", triviaRecordList[i].chosenTriviaAnswer)
//                }

                CoroutineScope(Dispatchers.Main).launch {
                    withContext(Dispatchers.IO) {
                        triviaCategoryDatabaseList = triviaRecordRepository.getAllCategories()

                        if (triviaCategoryDatabaseList.isNotEmpty()) {
                            for (category in triviaCategoryDatabaseList) {
                                if (category == triviaCategory) {
                                    triviaRecordRepository.deleteTriviaRecord(category)
                                    triviaRecordRepository.insertTriviaRecord(triviaRecordList)
                                    break
                                }
                            }
                        } else {
                            triviaRecordRepository.insertTriviaRecord(triviaRecordList)
                        }
                    }
                }

                getTriviaRecords()

                triviaRecordList.clear()
            }
        }
    }

    private fun getTriviaRecords() {

        var triviaCategory: List<String> = listOf()

        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                triviaCategory = triviaRecordRepository.getAllCategories()
            }
        }

        if (triviaCategory.isEmpty()) {
            Log.d("List size: ", "empty")
        } else {
            Log.d("TriviaCategory: ", triviaCategory[0])
        }
    }

    private fun insertTriviaRecordInDatabase(triviaRecord: TriviaRecord) {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {

            }
        }
    }

//    private fun observeButtonColorChange() {
//        val numbers = resources.getIntArray(R.array.color_array)
//
//        setFragmentResultListener(REQ_COLOR_KEY) { key, bundle ->
//            bundle.getString(BUNDLE_COLOR_KEY)?.let {
//
//                val intValue = it.toInt()
//
//                btnAnswerOne.setBackgroundColor(resources.getColor(numbers[intValue]))
//                btnAnswerTwo.setBackgroundColor(resources.getColor(numbers[intValue]))
//                btnAnswerThree.setBackgroundColor(resources.getColor(numbers[intValue]))
//                btnAnswerFour.setBackgroundColor(resources.getColor(numbers[intValue]))
//
//            } ?: Log.e("ButtonColor", "Couldn't change button color")
//
//        }
//    }

}