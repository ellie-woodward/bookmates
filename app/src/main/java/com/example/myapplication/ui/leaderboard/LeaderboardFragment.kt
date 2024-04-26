package com.example.myapplication.ui.leaderboard

import android.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.SharedViewModel
import com.example.myapplication.databinding.FragmentLeaderboardBinding
import kotlinx.coroutines.launch
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Player
import com.example.myapplication.Template

class LeaderboardFragment : Fragment() {

    private var _binding: FragmentLeaderboardBinding? = null

    private val sharedViewModel: SharedViewModel by activityViewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val leaderboardViewModel =
            ViewModelProvider(this).get(LeaderboardViewModel::class.java)

        _binding = FragmentLeaderboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // val textView: TextView = binding.textLeaderboard
        // leaderboardViewModel.text.observe(viewLifecycleOwner) {
        //    textView.text = it
        // }

        val sortedPlayers = sharedViewModel.playerList.getPlayers().sortedByDescending { it.wins }
        binding.firstPlace.text = "1: ${sortedPlayers[0].playerName}"
        binding.secondPlace.text = "2:${sortedPlayers[1].playerName}"
        binding.thirdPlace.text = "3:${sortedPlayers[2].playerName}"

        return root




    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var templateTitles = sharedViewModel.templateList.getTemplateNames()
        var adapter = ArrayAdapter(requireContext(), R.layout.simple_dropdown_item_1line, templateTitles)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.autoCompleteTextView.setAdapter(adapter)
        binding.autoCompleteTextView.threshold = 0

        sharedViewModel.templates.observe(viewLifecycleOwner) { templates ->
            templateTitles = templates.map { it.gameName }
            adapter = ArrayAdapter(
                requireContext(),
                R.layout.simple_dropdown_item_1line,
                templateTitles
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.autoCompleteTextView.setAdapter(adapter)
        }


        binding.autoCompleteTextView.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
            val selectedTemplate = parent.getItemAtPosition(position) as String
            //val template = sharedViewModel.templateList.getTemplates().find { it.gameName == selectedTemplate }
            val template = sharedViewModel.templates.value?.find { it.gameName == selectedTemplate }
            val selectedTemplateId = template?.templateId
            // Check if a template is selected

            if (selectedTemplate !=  null) {
                // Filter games by the selected template
                filterGamesByTemplate(selectedTemplate)
            } else {
                // No template selected, filter games by most wins total
                filterGamesByMostWinsTotal()
            }
        }
    }

    private fun filterGamesByTemplate(selectedTemplate: String) {
        val template = sharedViewModel.templates.value?.find { it.gameName == selectedTemplate }

        val sortedPlayers = sharedViewModel.playerList.getPlayers().sortedByDescending { template?.let { it1 ->
            it.stats.get(
                it1.gameName)?.wins ?: 0
        } }
        // Update UI or perform actions with the filtered games

        updateUI(sortedPlayers)
    }

    private fun filterGamesByMostWinsTotal() {
        // Get games sorted by most wins total
        val sortedPlayers = sharedViewModel.playerList.getPlayers().sortedByDescending { it.wins }
        updateUI(sortedPlayers)
        // Update UI or perform actions with the filtered games
    }


    private fun updateUI(sortedPlayers: List<Player>){
        binding.firstPlace.text = "1: ${sortedPlayers[0].playerName}"
        binding.secondPlace.text = "2:${sortedPlayers[1].playerName}"
        binding.thirdPlace.text = "3:${sortedPlayers[2].playerName}"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }




}