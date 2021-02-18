package com.example.madfinalapp.ui

import android.os.Bundle
import android.text.Html
import android.view.*
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
    private var buttons: ArrayList<Button> = arrayListOf()
    private var counter: Int = 0

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

    /**
     * Iterates through the questions
     */
    private fun triviaQuestionIterator(counter: Int) {
        if (counter <= 7) {
            val trivia = viewModel.trivia.value?.get(counter)

            // Checks if the repository is populated
            if (trivia != null) {
                // Add the correct answer to triviaQuestionList
                triviaQuestionList.add(trivia.correct_answer)
                // Add the incorrect answer to triviaQuestionList
                triviaQuestionList.addAll(trivia.incorrect_answers)
                // Shuffle the answers in the list
                triviaQuestionList.shuffle()

                // Binds the question and answers to the view objects
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
            // Retrieves the category
            val triviaCategory = viewModel.trivia.value?.get(0)?.category.toString()

            // Deletes the category
            deleteTriviaRecordsFromDatabase(triviaCategory)

            // Inserts a list with all the new TriviaRecords
            insertTriviaRecordsInDatabase()

            // Navigates from Questionnaire to Category
            findNavController().navigate(R.id.action_triviaQuestionnaireFragment_to_triviaCategoryFragment)
        }
    }

    /**
     * Retrieves the current question from the repository and checks if the chosen answer
     * is correct. Correct and wrong answers are translated to objects which than are
     * inserted into a list
     */
    private fun triviaAnswerValidation(counter: Int, answer: String) {
        if (counter <= 7) {
            val triviaRecord: TriviaRecord

            val triviaItem = viewModel.trivia.value?.get(counter)
            // Strips "Entertainment: " from the string
            val triviaCategory = triviaItem?.category.toString().replace("Entertainment: ", "")
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
        }
    }

    /**
     * Inserts a list of TriviaRecord objects in the database
     */
    private fun insertTriviaRecordsInDatabase() {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                triviaRecordRepository.insertTriviaRecord(triviaRecordList)
            }
        }
    }

    /**
     * Deletes all the TriviaRecord objects from the database that contains the triviaCategory
     */
    private fun deleteTriviaRecordsFromDatabase(triviaCategory: String) {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                triviaRecordRepository.deleteTriviaRecord(triviaCategory)
            }
        }
    }
}