package org.rasulov.application.screens.main.tabs

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import org.rasulov.application.R
import org.rasulov.application.databinding.FragmentTabsBinding
import org.rasulov.application.utils.findCurrentNavController

class TabsFragment : Fragment(R.layout.fragment_tabs) {

    private lateinit var binding: FragmentTabsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTabsBinding.bind(view)

        val navController = findCurrentNavController()
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)

    }
}