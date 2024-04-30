package com.example.myapplication.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentAccountBinding
import com.example.myapplication.UserStorage.Companion.user


class AccountFragment : Fragment() {
    private var _binding: FragmentAccountBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textview_username = root.findViewById<TextView>(R.id.account_username)
        val textview_email = root.findViewById<TextView>(R.id.account_email)
        val textview_unique = root.findViewById<TextView>(R.id.account_unique)
        val textview_games = root.findViewById<TextView>(R.id.account_games_played)

        var sb = StringBuilder()
        textview_username.text = sb.append("Account Username: ").append(user.username)
        sb = StringBuilder()
        textview_email.text = sb.append("Account Email: ").append(user.email)
        sb = StringBuilder()
        textview_games.text = sb.append(user.username).append(" has finished ").append(user.gamesLogged.toString()).append(" games")
        sb = StringBuilder()
        textview_unique.text = sb.append(user.username).append(" has finished ").append(user.uniqueGames.toString()).append(" games created from a custom template")

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}