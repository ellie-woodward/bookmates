package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.databinding.FragmentGameBinding
import java.util.UUID

class GameFragment: Fragment() {
    private lateinit var sharedViewModel: SharedViewModel
    private var _binding: FragmentGameBinding? = null

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

        _binding = FragmentGameBinding.inflate(inflater, container, false)





        return binding.root
    }






    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        val gameID = arguments?.getSerializable("gameId") as UUID?


        val game = gameID?.let{fetchGame(gameID)}

        if (game != null) {
            println(game.scores)
        }

        game?.let {


            updateUI(game)

            binding.gameTitle.doOnTextChanged { text, _, _, _ ->
                game.updateTitle(text.toString())
            }

            binding.finishedCheckbox.setOnClickListener {
                //game.updateFinished()
                sharedViewModel.UpdateFinished(game)
            }

        }

        binding.ImagePickerButton.setOnClickListener {
            if (game != null) {
                updateScoresWithArray(game)
            }
        // selectImageFromGallery(view)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        //saveScoresToStorage()
    }

    private fun fetchGame(gameId: UUID?): Game?{


        //   return gameId?.let { Games.getGame(it) }!!
        return sharedViewModel.games.value?.find { it.gameId == gameId}


    }

    private fun updateUI(game: Game) {
        binding.gameTitle.setText(game.title)
        binding.finishedCheckbox.isChecked = game.finished

        val players = game.players
        val templateid = game.templateId
        val template = sharedViewModel.templates.value?.find { it.templateId == templateid }
        val rowTitles = template?.rowTitles

        // Inflate the XML layout
        val scrollView = binding.playerList.parent as ScrollView
        val contentLayout = scrollView.getChildAt(0) as LinearLayout
        contentLayout.removeAllViews() // Clear previous content

        val playerScoresMap = game.scores
        // Iterate over players to create sections
        for (player in players) {
            // Create a header for each player
            val playerHeader = TextView(requireContext())
            playerHeader.text = player.playerName
            playerHeader.textSize = 18f
            playerHeader.setPadding(0, 16, 0, 8)
            contentLayout.addView(playerHeader)


            // Create a row for each player
            val rowLayout = LinearLayout(requireContext())
            rowLayout.orientation = LinearLayout.VERTICAL
            val rowLayoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            rowLayout.layoutParams = rowLayoutParams

            // Add EditText fields for row titles and scores
            if (rowTitles != null) {
                for (rowTitle in rowTitles) {
                    val row = LinearLayout(requireContext())
                    row.orientation = LinearLayout.HORIZONTAL
                    row.layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )

                    // Add EditText for row title
                    val rowTitleTextView = TextView(requireContext()).apply {
                        hint = rowTitle
                        layoutParams = LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            1f
                        )
                    }
                    row.addView(rowTitleTextView)

                    // Add EditText for score
                    val scoreEditText = EditText(requireContext()).apply {
                        hint = "Score"
                        val score = game.scores [player.playerId]?.get(rowTitles.indexOf(rowTitle))
                        // game.scores[player]?.get((rowTitles.indexOf(rowTitle)))?.let { setText(it) }
                        setText(score?.toString()?: "")
                        inputType = InputType.TYPE_CLASS_NUMBER
                        layoutParams = LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            1f
                        )

                    }
                    row.addView(scoreEditText)

                    // Add TextWatcher to score EditText
                    scoreEditText.addTextChangedListener(object : TextWatcher {
                        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                        override fun afterTextChanged(s: Editable?) {
                            val score = s.toString().toIntOrNull() ?: 0
                            //val playerId = player.playerId
                            val playerScores = game.scores.getOrPut(player.playerId) { mutableListOf() }
                            //val scoreIndex = rowLayout.indexOfChild(row) / 2 // Assuming 2 views per row (title and score)

                            val scoreIndex = rowTitles.indexOf(rowTitle)

                            print("score: ")
                            println(score)
                            print("playerScores: ")
                            println(playerScores)
                            print("scoreIndex: ")
                            println(scoreIndex)

                            if (scoreIndex < playerScores.size) {
                                playerScores[scoreIndex] = score
                            } else {
                                playerScores.add(scoreIndex, score)
                            }

                            print("this is the score that was changed: ")
                            print(score)
                            print(" for ")
                            println(player.playerName)
                            print("player's scores: ")
                            println(playerScores)
                            sharedViewModel.updateGameScores(game, player, playerScores)
                            println(game.scores)
                        }
                    })

                    rowLayout.addView(row)
                }
            }

            // Add the row layout to the content layout
            contentLayout.addView(rowLayout)
        }
    }


    fun saveScores(game: Game){

    }

    private val PICK_IMAGE_REQUEST = 1

    fun selectImageFromGallery(view: View){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            // Get the URI of the selected image
            val imageUri = data.data
            // Now you can do something with this URI, such as displaying the image in an ImageView
        }
    }

    fun updateScoresWithArray(game:Game){
        val rows = 2
        val cols = 2
        val array = arrayOf(
                intArrayOf(500, 35),
                intArrayOf(808, 72),
                intArrayOf(63, 44),
                intArrayOf(0, 0),
                intArrayOf(0, 90),
                intArrayOf(603, 0),
                intArrayOf(54, 70),
                intArrayOf(23, 7682)
            )

        if (array.size >game.scores[game.scores.keys.elementAt(0)]?.size!!){
            println("first one")
            println(array.size)
            println(game.scores.size)
            Toast.makeText(requireContext(), "There are more players/rows than game has. Please Try again", Toast.LENGTH_LONG).show()
        }else if(array.get(0).size > game.scores.size ){
            println("second one")
            println(array[0].size)
            println(game.scores[game.scores.keys.elementAt(0)]?.size)
            Toast.makeText(requireContext(), "There are more players/rows than game has. Please Try again", Toast.LENGTH_LONG).show()
        }else {

            for ((rowIndex, row) in array.withIndex()) {

                for ((colIndex, value) in row.withIndex()) {
                    val playerKeyIndex = colIndex % game.scores.size // Cycling through player keys
                    val playerKey = game.scores.keys.toList()[playerKeyIndex]
                    //game.scores[playerKey]?.add(value)
                    game.scores[playerKey]?.set(rowIndex, value)

                }
            }

            game.scores.forEach { (playerID, scores) ->
                println("Player: $playerID, Scores: $scores")
            }
            updateUI(game)
        }

    }

}




private fun Int?.getOrNull(index: Int) {

}