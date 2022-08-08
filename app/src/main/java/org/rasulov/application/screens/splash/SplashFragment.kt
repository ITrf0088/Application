package org.rasulov.application.screens.splash

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import org.rasulov.application.R
import org.rasulov.application.databinding.FragmentSplashBinding
import org.rasulov.application.model.Repositories
import org.rasulov.application.screens.main.MainActivity
import org.rasulov.application.screens.main.MainActivityArgs
import org.rasulov.application.utils.viewModelCreator
import org.rasulov.application.utils.observeEvent

class SplashFragment : Fragment(R.layout.fragment_splash) {

    private lateinit var binding: FragmentSplashBinding

    private val viewModel by viewModelCreator { SplashViewModel(Repositories.accountsRepository) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSplashBinding.bind(view)

        // just some animations example
        renderAnimations()

        viewModel.launchMainScreenEvent.observeEvent(viewLifecycleOwner) {
            launchMainScreen(it)
        }
    }

    private fun launchMainScreen(isSignedIn: Boolean) {
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.putExtra(MainActivity.IS_SIGNED_IN, isSignedIn)
        startActivity(intent)
    }

    private fun renderAnimations() {
        binding.loadingIndicator.alpha = 0f
        binding.loadingIndicator.animate()
            .alpha(0.7f)
            .setDuration(1000)
            .start()

        binding.pleaseWaitTextView.alpha = 0f
        binding.pleaseWaitTextView.animate()
            .alpha(1f)
            .setStartDelay(500)
            .setDuration(1000)
            .start()

    }
}