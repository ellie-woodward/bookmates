package com.example.myapplication.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.example.myapplication.R
import com.example.myapplication.R.*
import com.example.myapplication.RegisterActivity
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    lateinit var newGameBtn: Button
    lateinit var selectGameBtn: Button
    private lateinit var appBarConfiguration: AppBarConfiguration



    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

         newGameBtn = root.findViewById<Button>(R.id.button_new_game)
         selectGameBtn = root.findViewById<Button>(R.id.button_select_game)

        newGameBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?){
                findNavController().navigate(R.id.newGameFragment)
            }
        })
        selectGameBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?){
                findNavController().navigate(R.id.gameListFragment)
            }
        })
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}