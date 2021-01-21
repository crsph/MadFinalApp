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
        for (i in 0..3) {
            triviaQuestionIterator(counter, buttons[i].text.toString())

            buttons[i].setOnClickListener {
                counter++
                triviaQuestionIterator(counter, buttons[i].text.toString())
            }
        }
    }

    private fun triviaQuestionIterator(counter: Int, answer: String) {

        triviaAnswerValidation(counter, answer)

        if (counter <= 7) {

            val trivia = viewModel.trivia.value?.get(counter)

            if (trivia != null) {
                // Add the answers to a list
                triviaQuestionList.add(trivia.correct_answer)

                for (i in 0..2) {
                    triviaQuestionList.add(trivia.incorrect_answers[i])
                }

                // Shuffle the list with answers
                triviaQuestionList.shuffle()

                tvQuestionLabel.text = String.format("Question #%d", counter + 1)

                // The HTML.fromHtml() decodes the HTML entity (e.g. &quot; to ")
                tvQuestion.text = Html.fromHtml(trivia.question).toString()
                btnAnswerOne.text = Html.fromHtml(triviaQuestionList[0])
                btnAnswerTwo.text = Html.fromHtml(triviaQuestionList[1])
                btnAnswerThree.text = Html.fromHtml(triviaQuestionList[2])
                btnAnswerFour.text = Html.fromHtml(triviaQuestionList[3])

                triviaQuestionList.clear()
            }
        } else {
            findNavController().navigate(R.id.action_triviaQuestionnaireFragment_to_triviaCategoryFragment)
        }

//        val trivia = viewModel.trivia.value?.get(counter)

//        if (trivia != null) {
//            // Add the answers to a list
//            triviaQuestionList.add(trivia.correct_answer)
//
//            for (i in 0..2) {
//                triviaQuestionList.add(trivia.incorrect_answers[i])
//            }
//
//            // Shuffle the list with answers
//            triviaQuestionList.shuffle()
//
//
//            tvQuestionLabel.text = String.format("Question #%d", counter + 1)
//
//            // The HTML.fromHtml() decodes the HTML entity (e.g. &quot; to ")
//            tvQuestion.text = Html.fromHtml(trivia.question).toString()
//            btnAnswerOne.text = triviaQuestionList[0]
//            btnAnswerTwo.text = triviaQuestionList[1]
//            btnAnswerThree.text = triviaQuestionList[2]
//            btnAnswerFour.text = triviaQuestionList[3]
//
//            triviaQuestionList.clear()
//        }
    }

    private fun triviaAnswerValidation(counter: Int, answer: String) {
        if (counter <= 7) {
            val triviaRecord: TriviaRecord
            val triviaItem = viewModel.trivia.value?.get(counter)
            val triviaCategory = triviaItem?.category.toString()
            val triviaQuestion = triviaItem?.question.toString()
            val triviaCorrectAnswer = triviaItem?.correct_answer.toString()

            // Check if the chosen answer is correct or wrong
            if (triviaCorrectAnswer == answer) {
                triviaRecord =
                    TriviaRecord(triviaCategory, triviaQuestion, triviaCorrectAnswer, answer, true)
                triviaRecordList.add(triviaRecord)
            } else {
                triviaRecord =
                    TriviaRecord(triviaCategory, triviaQuestion, triviaCorrectAnswer, answer, false)
                triviaRecordList.add(triviaRecord)
            }

            // Check if the triviaRecordList is populated with 8 records and insert it in the database
            if (triviaRecordList.size == 8) {
                CoroutineScope(Dispatchers.Main).launch {
                    withContext(Dispatchers.IO) {
                        triviaRecordRepository.deleteTriviaRecord(triviaCategory)

                        Log.d("Test","Gaat het hier doorheen?------------------------------------------------")

                        for (i in 0..7) {
                            triviaRecordRepository.insertTriviaRecord(triviaRecordList[i])
                        }
                    }
                }
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