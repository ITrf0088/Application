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

        val childFragmentManager =
            childFragmentManager.findFragmentById(R.id.tabsContainer)?.childFragmentManager
        childFragmentManager?.addOnBackStackChangedListener {

            Log.d("itrt0088", "tabs backStack: ${childFragmentManager.backStackEntryCount}")

            Log.d("itdes0088", "${navController.currentDestination?.javaClass}")
            Log.d("itdes0088", "${navController.currentDestination?.id}")
            Log.d("itdes0088", "${navController.currentDestination?.label}")
            Log.d("itdes0088", "${navController.currentDestination?.displayName}")
            Log.d("itdes0088", "${navController.currentDestination?.parent?.navigatorName}")
            Log.d("itdes0088", "${navController.currentDestination?.parent?.displayName}")
            Log.d("itdes0088", "${navController.currentDestination?.parent?.nodes}")
            Log.d("itdes0088", "${navController.currentDestination?.parent}")
            Log.d("itdes0088", "${navController.currentDestination?.parent?.parent}")
            Log.d(
                "itdes0088",
                "${(navController.graph.findNode(R.id.tab_dashboard) as NavGraph).findNode(R.id.boxFragment)}"
            )
            Log.d("itdes0088", "${navController.graph.nodes}")
            Log.d("itdes0088", "------------------------------------------------------------")
        }

    }

    override fun onResume() {
        super.onResume()
        Log.d(
            "itrr0088",
            "backstack ${requireActivity().supportFragmentManager.backStackEntryCount}"
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("it0088", "onDestroyView: TabsFragment")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("it0088", "onDestroy: TabsFragment")
    }

}