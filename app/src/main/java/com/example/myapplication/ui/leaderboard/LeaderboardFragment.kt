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
import com.example.myapplication.data.model.CreatedPlayer
import com.example.myapplication.Player
import com.example.myapplication.Template
import com.example.myapplication.UserStorage.Companion.user

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

        var result = handleRankings()

//        val sortedPlayers = sharedViewModel.playerList.getPlayers().sortedByDescending { it.wins }
        if (result.size == 0){
            binding.firstPlace.text = "No players have been created"
        }
        else if(result.size == 1) {
            binding.firstPlace.text = "1: ${result[0]}"
        }
        else if(result.size == 2) {
            binding.firstPlace.text = "1: ${result[0]}"
            binding.secondPlace.text = "2:${result[1]}"
        }
        else {
            binding.firstPlace.text = "1: ${result[0]}"
            binding.secondPlace.text = "2:${result[1]}"
            binding.thirdPlace.text = "3:${result[2]}"
        }

        return root

    }

    private fun handleRankings(): List<String> {
        var final_result = ArrayList<String>()
        var player_list: List<CreatedPlayer>? = user.createdPlayers

        var max = 0
        var maxPlayer = ""
        if (player_list != null) {
            for (player: CreatedPlayer in player_list) {
                for (player2: CreatedPlayer in player_list) {
                    if (player2.wins > max && !final_result.contains(player2.name)){
                        max = player2.wins
                        maxPlayer = player2.name
                    }
                }
                final_result.add(maxPlayer)
                if (final_result.size >= 4){
                    break
                }
                max = 0
                maxPlayer = ""
            }
        }
        return final_result
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
            }
        }
    }

    private fun filterGamesByTemplate(selectedTemplate: String) {
        val template = sharedViewModel.templates.value?.find { it.gameName == selectedTemplate }?.gameName
        Log.d("template", template.toString())

        var sortedPlayers = ArrayList<String>()
        var player_list: List<CreatedPlayer>? = user.createdPlayers

//        val sortedPlayers = sharedViewModel.playerList.getPlayers().sortedByDescending { template?.let { it1 ->
//            it.stats.get(
//                it1.gameName)?.wins ?: 0
//        } }

        var max = 0
        var maxPlayer = ""
        if (player_list != null) {
            for (player: CreatedPlayer in player_list) {
                for (player2: CreatedPlayer in player_list) {
                    try {
                        var player_record_score = player2.record[template].toString().get(1).code
                        if (player_record_score > max && !sortedPlayers.contains(player2.name)){
                            max = player_record_score
                            maxPlayer = player2.name
                        }
                    } catch (_:Exception){

                    }
                }
                sortedPlayers.add(maxPlayer)
                if (sortedPlayers.size >= 4) {
                    break
                }
                max = 0
                maxPlayer = ""
            }
        }
        // Update UI or perform actions with the filtered games

        updateUI(sortedPlayers)
    }
    private fun updateUI(sortedPlayers: List<String>){

        if (sortedPlayers.size == 0){
            binding.firstPlace.text = "No players have been recorded"
        }
        else if(sortedPlayers.size == 1) {
            binding.firstPlace.text = "1: ${sortedPlayers[0]}"
        }
        else if(sortedPlayers.size == 2) {
            binding.firstPlace.text = "1: ${sortedPlayers[0]}"
            binding.secondPlace.text = "2:${sortedPlayers[1]}"
        }
        else {
            binding.firstPlace.text = "1: ${sortedPlayers[0]}"
            binding.secondPlace.text = "2:${sortedPlayers[1]}"
            binding.thirdPlace.text = "3:${sortedPlayers[2]}"
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }




}