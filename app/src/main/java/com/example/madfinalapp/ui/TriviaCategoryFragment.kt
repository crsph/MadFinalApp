package com.example.madfinalapp.ui

import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.madfinalapp.R
import com.example.madfinalapp.vm.TriviaViewModel
import kotlinx.android.synthetic.main.fragment_trivia_category.*
import java.util.*
import kotlin.concurrent.schedule


class TriviaCategoryFragment : Fragment() {

    private val viewModel: TriviaViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_trivia_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // loadingUIToggler set on false to prevent the loading circle to start loading at boot up
        loadingUIToggler(false)

        btnGeneral.setOnClickListener {
            navigateToQuestionnaire(9)
        }

        btnBooks.setOnClickListener {
            navigateToQuestionnaire(10)
        }

        btnFilms.setOnClickListener {
            navigateToQuestionnaire(11)
        }

        btnMusic.setOnClickListener {
            navigateToQuestionnaire(12)
        }

        btnMusicals.setOnClickListener {
            navigateToQuestionnaire(13)
        }

        btnTelevision.setOnClickListener {
            navigateToQuestionnaire(14)
        }

        btnVideoGames.setOnClickListener {
            navigateToQuestionnaire(15)
        }

        btnBoardGames.setOnClickListener {
            navigateToQuestionnaire(16)
        }
    }

    /**
     * Navigates from the category fragment to the questionnaire fragment
     */
    private fun navigateToQuestionnaire(category: Int) {
        // Makes the API call to retrieve all the questions as a JSON object
        viewModel.getTrivia(category)

        loadingUIToggler(true)

        // A timer has been schedule so that the system gets enough time to retrieve the questions
        Timer().schedule(2000) {
            findNavController().navigate(R.id.action_triviaCategoryFragment_to_triviaQuestionnaireFragment)
        }
    }

    /**
     * Displays the progressive loading circle when a category has been chosen
     */
    private fun loadingUIToggler(boolean: Boolean) {
        ivOverlay.isVisible = boolean
        loadingPanel.isVisible = boolean
    }
}