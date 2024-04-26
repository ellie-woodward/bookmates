package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ListItemGamesBinding
import java.util.UUID




class GameHolder(
    private val binding: ListItemGamesBinding
) : RecyclerView.ViewHolder(binding.root) {

    lateinit var boundGame: Game
        private set


    fun bind(game: Game, onGameClicked: (gameId: UUID) -> Unit) {

        boundGame = game

        binding.root.setOnClickListener {
            onGameClicked(game.gameId)
        }



        binding.gameItemTitle.text = game.title



        val playerNames = game.players.map {it.playerName }
        binding.listOfPlayers.text = "Players: ${playerNames.joinToString(", ") }"

        with(binding.listItemImage){
            when{
                game.finished -> {
                    visibility = View.VISIBLE
                    setImageResource(R.drawable.finished_game)
                }
                !game.finished ->{
                    visibility = View.GONE
                }
            }
        }

    }


}




class GameListAdapter(
    private var games: List<Game>,
    private val onGameClicked: (UUID) -> Unit
) : RecyclerView.Adapter<GameHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GameHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemGamesBinding.inflate(inflater, parent, false)
        return GameHolder(binding)
    }

    override fun onBindViewHolder(holder: GameHolder, position: Int) {
        val game = games[position]

        holder.bind(game, onGameClicked)
    }

    override fun getItemCount() :Int {
        return games.size
    }

    fun updateGames(gameList: List<Game>){
        print("This is the updated Game list:")
        println(gameList.size)
        games = gameList
        notifyDataSetChanged()


    }
}