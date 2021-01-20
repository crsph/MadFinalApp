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

        triviaRecordRepository = TriviaRecordRepository(requireContext())

        CoroutineScope(Dispatchers.Main).launch {
            triviaRecordRepository.deleteAll()
        }

        triviaQuestionIterator(counter)

        buttons = arrayListOf(btnAnswerOne, btnAnswerTwo, btnAnswerThree, btnAnswerFour)

        for (i in 0..3) {
            buttons[i].setOnClickListener {
                counter++
                triviaQuestionIterator(counter)
                triviaAnswerValidation(counter, buttons[i].text.toString())
            }
        }

//        btnAnswerOne.setOnClickListener {
//            counter++
//            triviaQuestionIterator(counter)
//            triviaAnswerValidation(counter, btnAnswerOne.text.toString())
//        }
//
//        btnAnswerTwo.setOnClickListener {
//            counter++
//            triviaQuestionIterator(counter)
//            triviaAnswerValidation(counter, btnAnswerTwo.text.toString())
//        }
//
//        btnAnswerThree.setOnClickListener {
//            counter++
//            triviaQuestionIterator(counter)
//            triviaAnswerValidation(counter, btnAnswerThree.text.toString())
//        }
//
//        btnAnswerFour.setOnClickListener {
//            counter++
//            triviaQuestionIterator(counter)
//            triviaAnswerValidation(counter, btnAnswerFour.text.toString())
//        }


//        btnAnswerOne.setBackgroundColor(resources.getColor(R.color.mandy))
//        btnAnswerTwo.setBackgroundColor(resources.getColor(R.color.mandy))
//        btnAnswerThree.setBackgroundColor(resources.getColor(R.color.mandy))
//        btnAnswerFour.setBackgroundColor(resources.getColor(R.color.mandy))
//
//        observeButtonColorChange()
    }


    private fun triviaQuestionIterator(counter: Int) {

        /**
         * An error occures when the player exceeds the 8th question. THe log gives an
         * IndexOutOfBound error
         */

        val triviaArraySize = viewModel.trivia.value?.size

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
                btnAnswerOne.text = triviaQuestionList[0]
                btnAnswerTwo.text = triviaQuestionList[1]
                btnAnswerThree.text = triviaQuestionList[2]
                btnAnswerFour.text = triviaQuestionList[3]

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

            val triviaItem = viewModel.trivia.value?.get(counter)
            val triviaCategory = triviaItem?.category.toString()
            val triviaQuestion = triviaItem?.question.toString()
            val triviaCorrectAnswer = triviaItem?.correct_answer.toString()

            if (triviaCorrectAnswer == answer) {
                val trivia = TriviaRecord(
                    triviaCategory, triviaQuestion, triviaCorrectAnswer, answer, true
                )

                CoroutineScope(Dispatchers.Main).launch {
                    withContext(Dispatchers.IO) {
                        triviaRecordRepository.insertTriviaRecord(trivia)
                    }
                }
            } else {
                val trivia = TriviaRecord(
                    triviaCategory, triviaQuestion, triviaCorrectAnswer, answer, false
                )

                CoroutineScope(Dispatchers.Main).launch {
                    withContext(Dispatchers.IO) {
                        triviaRecordRepository.insertTriviaRecord(trivia)
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