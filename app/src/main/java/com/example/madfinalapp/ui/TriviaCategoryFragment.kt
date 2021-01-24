package com.example.madfinalapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.madfinalapp.R
import com.example.madfinalapp.model.Trivia
import com.example.madfinalapp.repository.TriviaRecordRepository
import com.example.madfinalapp.repository.TriviaRepository
import com.example.madfinalapp.vm.TriviaViewModel
import kotlinx.android.synthetic.main.fragment_trivia_category.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.concurrent.schedule


//const val REQ_COLOR_KEY = "req_color"
//const val BUNDLE_COLOR_KEY = "bundle_color"

class TriviaCategoryFragment : Fragment() {

    private val viewModel: TriviaViewModel by activityViewModels()
    private val triviaList = arrayListOf<Trivia>()
    private lateinit var triviaRecordRepository: TriviaRecordRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_trivia_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        triviaRecordRepository = TriviaRecordRepository(requireContext())
//
//        CoroutineScope(Dispatchers.Main).launch {
//            withContext(Dispatchers.IO) {
//                triviaRecordRepository.deleteAll()
//            }
//        }

        loadingUIToggler(false)

//        observeTrivia()

        btnGeneral.setOnClickListener {
//            changeAnswerButtonColor(0)
            navigateToQuestionnaire(9)
        }

        btnBooks.setOnClickListener {
//            changeAnswerButtonColor(1)
            navigateToQuestionnaire(10)
        }

        btnFilms.setOnClickListener {
//            changeAnswerButtonColor(2)
            navigateToQuestionnaire(11)
        }

        btnMusic.setOnClickListener {
//            changeAnswerButtonColor(3)
            navigateToQuestionnaire(12)
        }

        btnMusicals.setOnClickListener {
//            changeAnswerButtonColor(4)
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

    private fun navigateToQuestionnaire(category: Int) {
        viewModel.getTrivia(category)

        loadingUIToggler(true)

        Timer().schedule(2000) {
            findNavController().navigate(R.id.action_triviaCategoryFragment_to_triviaQuestionnaireFragment)
        }
    }


    private fun observeTrivia() {
        viewModel.trivia.observe(viewLifecycleOwner, Observer {
            triviaList.clear()
            triviaList.addAll(it)
        })

        viewModel.errorText.observe(viewLifecycleOwner, Observer {
            Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
        })
    }

    private fun loadingUIToggler(boolean: Boolean) {
        ivOverlay.isVisible = boolean
        loadingPanel.isVisible = boolean
    }

//    private fun changeAnswerButtonColor(colorIndex: Int) {
//        setFragmentResult(REQ_COLOR_KEY, bundleOf(Pair(BUNDLE_COLOR_KEY, colorIndex)))
//
//            findNavController().popBackStack()
//    }
}