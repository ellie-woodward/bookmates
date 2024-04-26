package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.FragmentNewGameBinding
import java.util.UUID

class NewGameFragment: Fragment() {

    private lateinit var sharedViewModel: SharedViewModel
    private var selectedTemplateId: UUID? = null
    lateinit var availablePlayers: List<String>

    private var _binding: FragmentNewGameBinding? = null

    private val binding
        get() = checkNotNull(_binding){
            "Cannot access"
        }




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)

        _binding=FragmentNewGameBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentNewGameBinding.bind(view)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        availablePlayers = sharedViewModel.playerList.getPlayerNames()

        binding.createNewGame.visibility = View.GONE
        var templateTitles = sharedViewModel.templateList.getTemplateNames()
        var adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, templateTitles)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.templateList.setAdapter(adapter)
        binding.templateList.threshold = 0

        sharedViewModel.templates.observe(viewLifecycleOwner) { templates ->
            templateTitles = templates.map { it.gameName }
            adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                templateTitles
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.templateList.setAdapter(adapter)
        }


        binding.templateList.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
            val selectedTemplate = parent.getItemAtPosition(position) as String
            //val template = sharedViewModel.templateList.getTemplates().find { it.gameName == selectedTemplate }
            val template = sharedViewModel.templates.value?.find { it.gameName == selectedTemplate }
            selectedTemplateId = template?.templateId

            println(selectedTemplateId)
            if (template != null) {
                println(template.gameName)
                updateCreateGame()
            }else{
                println("Template is null")
                updateCreateGame()
            }
        }

        binding.addButton.setOnClickListener{
            addPlayerSlot()
            updateCreateGame()
            print("selected players list after adding a player: " )
            println(selectedPlayersList)
        }
        binding.removeButton.setOnClickListener{
            removePlayerSlot()
            updateCreateGame()
            print("selected players list after removing a player: " )
            println(selectedPlayersList)
        }

        binding.createNewGame.setOnClickListener{
            createGame()
        }
    }


    override fun onDestroyView(){
        super.onDestroyView()


    }

    private val selectedPlayersList = mutableListOf<Player>()
    /*
        fun addPlayerSlot(){
            val playerSlot = layoutInflater.inflate(R.layout.new_player_entry, null)

            binding.addPlayersLayout.addView(playerSlot)
            binding.removeButton.visibility = View.VISIBLE

            val playerSpinner = playerSlot.findViewById<Spinner>(R.id.new_player_spinner)
            val adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, availablePlayers)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            playerSpinner.adapter = adapter

            playerSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val selectedPlayer = availablePlayers[position]

                    sharedViewModel.playerList.getPlayer(selectedPlayer)
                        ?.let { selectedPlayersList.add(it) }
                    //updateOtherSpinners(playerSpinner, selectedPlayer)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Do nothing
                }
            }

        }


     */
    /*
        fun addPlayerSlot() {
            // Inflate the player entry layout
            val playerSlot = layoutInflater.inflate(R.layout.new_player_entry, null)
            binding.addPlayersLayout.addView(playerSlot)
            binding.removeButton.visibility = View.VISIBLE

            val playerSpinner = playerSlot.findViewById<Spinner>(R.id.new_player_spinner)

            // Filter out players that are already selected
            val availablePlayersFiltered = availablePlayers.filterNot { player ->
                selectedPlayersList.any { it.playerName == player }
            }

            val adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, availablePlayersFiltered)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            playerSpinner.adapter = adapter

            playerSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val selectedPlayer = availablePlayersFiltered[position]


                    // Remove the previously selected player if any
                    //selectedPlayersList.clear()

                    // Add the newly selected player
                    sharedViewModel.playerList.getPlayer(selectedPlayer)?.let {
                        selectedPlayersList.add(it)
                    }


                    // Update button state
                    updateCreateGame()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Do nothing
                }
            }
        }


     */

    private val previousSelectedPlayerNames = mutableMapOf<Spinner, String>()

    fun addPlayerSlot() {
        // Inflate the player entry layout
        val playerSlot = layoutInflater.inflate(R.layout.new_player_entry, null)
        binding.addPlayersLayout.addView(playerSlot)
        binding.removeButton.visibility = View.VISIBLE

        val playerSpinner = playerSlot.findViewById<Spinner>(R.id.new_player_spinner)

        // Filter out players that are already selected
        val availablePlayersFiltered = availablePlayers.filterNot { player ->
            selectedPlayersList.any { it.playerName == player }
        }

        val adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, availablePlayersFiltered)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        playerSpinner.adapter = adapter

        playerSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedPlayerName = availablePlayersFiltered[position]

                // Remove the previously selected player if any
                previousSelectedPlayerNames[playerSpinner]?.let { previousSelectedPlayerName ->
                    if (previousSelectedPlayerName != selectedPlayerName) {
                        sharedViewModel.playerList.getPlayer(previousSelectedPlayerName)?.let {
                            selectedPlayersList.remove(it)
                        }
                    }
                }
                previousSelectedPlayerNames[playerSpinner] = selectedPlayerName

                // Add the newly selected player
                sharedViewModel.playerList.getPlayer(selectedPlayerName)?.let {
                    selectedPlayersList.add(it)
                }

                // Update button state
                updateCreateGame()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }
    }






    fun removePlayerSlot(){
        if(selectedPlayersList.isNotEmpty()){
            selectedPlayersList.removeAt(selectedPlayersList.size -1)
        }
        if(binding.addPlayersLayout.childCount > 1){

            //selectedPlayersList.removeAt(binding.addPlayersLayout.childCount-1)
            binding.addPlayersLayout.removeViewAt(binding.addPlayersLayout.childCount-1)


        }else{
            Toast.makeText(requireContext(), "No players to remove", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createGame(){
        val gameName = binding.newGameTitle.text.toString()
        val players = selectedPlayersList
        var scores: MutableMap<UUID, MutableList<Int>> = mutableMapOf()

        val numRows = sharedViewModel.templates.value?.find { it.templateId == selectedTemplateId }?.rowTitles?.size
        print("number of rows for template: ")
        println(numRows)
        for(i in players){
            //scores[i] = mutableListOf(0)

            val playerScores = MutableList(numRows ?: 0) { 0 }
            scores[i.playerId] = playerScores
            println(scores[i.playerId])
        }



        if (selectedTemplateId != null) {
            // Create your Game object with the selected template ID
            val newGame = Game(selectedTemplateId!!, gameName,players, scores)
            val gameId = sharedViewModel.addNewGame(newGame)
            print("selected players")
            println(newGame.players)
            //selectedPlayersList.clear()


            val action = GameFragmentDirections.actionGameFragment(gameId)
            val message = "New Game added: $gameName"
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            findNavController().navigate(action)
        } else {


            println("Can't find id")
            // Handle the case where no template has been selected
            // Show an error message or take appropriate action
        }

    }

    private fun updateCreateGame(){
        if(selectedTemplateId != null && selectedPlayersList.isNotEmpty()){
            binding.createNewGame.visibility = View.VISIBLE
        }else{
            binding.createNewGame.visibility = View.GONE
        }
    }


}