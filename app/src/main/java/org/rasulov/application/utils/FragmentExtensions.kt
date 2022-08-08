package org.rasulov.application.utils

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import org.rasulov.application.R
import java.lang.IllegalStateException

fun Fragment.findTopNavController(): NavController {
    val topLevelHost =
        requireActivity().supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment?
    return topLevelHost?.navController ?: findNavController()
}


fun Fragment.findCurrentNavController(): NavController {
    val navHost = childFragmentManager.findFragmentById(R.id.tabsContainer)
    return if (navHost is NavHostFragment) navHost.navController
    else {
        val message = "This fragment must have fragment container with NavHostFragment as default"
        throw IllegalStateException(message)
    }
}