package com.example.myapplication.ui.gamelist

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Game

import com.example.myapplication.GameHolder
import com.example.myapplication.GameListAdapter
import com.example.myapplication.SharedViewModel
import com.example.myapplication.databinding.FragmentGameListBinding


private const val TAG = "GameListFragment"

class GameListFragment : Fragment() {


    private var _binding: FragmentGameListBinding? = null

    private val binding
        get() = checkNotNull(_binding){
            "cannot access"
        }

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private lateinit var gameListAdapter: GameListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val gameListViewModel =
            ViewModelProvider(this).get(GameListViewModel::class.java)

        _binding = FragmentGameListBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDashboard
        gameListViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }




        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        getItemTouchHelper().attachToRecyclerView(binding.gameRecyclerView)

        binding.gameRecyclerView.layoutManager = LinearLayoutManager(context)



        gameListAdapter = GameListAdapter(emptyList()) { gameId ->
            findNavController().navigate(
                GameListFragmentDirections.showGameDetail(gameId)
            )
        }
        binding.gameRecyclerView.adapter = gameListAdapter
        print("Shared View model list size: ")

        sharedViewModel.games.observe(viewLifecycleOwner){games ->
            // println(games.size)
            //for (game in games) {
            //  print(sharedViewModel.templateList.getTemplates().find{it.templateId == game.templateId})
            //println(game.title)
            // }

            if(games.size == 0 ){
                binding.textDashboard.visibility = View.VISIBLE
            }else{
                binding.textDashboard.visibility = View.GONE
            }



            // gameListAdapter.updateGames(games)
            val filteredGames = filterGames(
                binding.FilterText.text.toString(),
                binding.FinishGameFilter.isChecked,

                games
            )
            gameListAdapter.updateGames(filteredGames)

            binding.gameRecyclerView.invalidate()
        }


        // Set up listener for filter text changes
        binding.FilterText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not used
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val filteredGames = filterGames(
                    s.toString(),
                    binding.FinishGameFilter.isChecked,

                    sharedViewModel.games.value ?: emptyList()
                )
                gameListAdapter.updateGames(filteredGames)
            }

            override fun afterTextChanged(s: Editable?) {
                // Not used
            }
        })

        binding.FinishGameFilter.setOnCheckedChangeListener { _, isChecked ->
            val filteredGames = filterGames(
                binding.FilterText.text.toString(),
                isChecked,
                sharedViewModel.games.value ?: emptyList()
            )
            gameListAdapter.updateGames(filteredGames)
        }



    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun getItemTouchHelper(): ItemTouchHelper {
        return ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                Log.w("-----DLF-----" , "Swipe LEFT detected!!")
                val gameHolder = viewHolder as GameHolder
                sharedViewModel.deleteGame(gameHolder.boundGame)
            }

        })
    }


    private fun filterGames(
        searchText: String,
        finishedOnly: Boolean,
        games: List<Game>
    ): List<Game> {
        return games.filter { game ->
            val titleMatches = game.title.contains(searchText, ignoreCase = true)
            val statusMatches = !finishedOnly || game.finished
            titleMatches && statusMatches
        }

    }


}
