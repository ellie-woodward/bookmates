package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.FragmentNewPlayerBinding
import java.util.Random
import java.util.UUID

class NewPlayerFragment: Fragment() {

    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var availablePlayers: List<String>
    private var _binding: FragmentNewPlayerBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        availablePlayers = sharedViewModel.playerList.getPlayerNames()
        // You can use binding to access views and set up UI components here



        binding.addNewPlayer.setOnClickListener {
            val playerName = binding.playerName.text.toString().trim()
            if (playerName.isNotEmpty()) {
                val player = Player(UUID.randomUUID(), playerName,0,0, mutableMapOf() )
                sharedViewModel.playerList.addPlayer(player)
                // Navigate back to the home page

                val message = "New Player added: $playerName"
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_GameList)
            }
        }

        binding.playerName.addTextChangedListener { text ->
            val playerName = text.toString().trim()
            binding.addNewPlayer.visibility = if (playerName.isNotEmpty()) View.VISIBLE else View.GONE
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Clear the binding reference to avoid memory leaks
    }

}