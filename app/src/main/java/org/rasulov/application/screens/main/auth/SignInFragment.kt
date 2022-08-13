package org.rasulov.application.screens.main.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import org.rasulov.application.R
import org.rasulov.application.databinding.FragmentSignInBinding
import org.rasulov.application.model.Repositories
import org.rasulov.application.utils.findCurrentNavController
import org.rasulov.application.utils.viewModelCreator
import org.rasulov.application.utils.observeEvent

class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    private lateinit var binding: FragmentSignInBinding

    private val viewModel by viewModelCreator { SignInViewModel(Repositories.accountsRepository) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSignInBinding.bind(view)
        binding.signInButton.setOnClickListener { onSignInButtonPressed() }
        binding.signUpButton.setOnClickListener { onSignUpButtonPressed() }

        observeState()
        observeClearPasswordEvent()
        observeShowAuthErrorMessageEvent()
        observeNavigateToTabsEvent()
    }

    private fun onSignInButtonPressed() {
        viewModel.signIn(
            email = binding.emailEditText.text.toString(),
            password = binding.passwordEditText.text.toString()
        )
    }

    private fun observeState() = viewModel.state.observe(viewLifecycleOwner) {
        binding.emailTextInput.error =
            if (it.emptyEmailError) getString(R.string.field_is_empty) else null
        binding.passwordTextInput.error =
            if (it.emptyPasswordError) getString(R.string.field_is_empty) else null

        binding.emailTextInput.isEnabled = it.enableViews
        binding.passwordTextInput.isEnabled = it.enableViews
        binding.signInButton.isEnabled = it.enableViews
        binding.signUpButton.isEnabled = it.enableViews
        binding.progressBar.visibility = if (it.showProgress) View.VISIBLE else View.INVISIBLE
    }

    private fun observeShowAuthErrorMessageEvent() =
        viewModel.showAuthToastEvent.observeEvent(viewLifecycleOwner) {
            Toast.makeText(requireContext(), R.string.invalid_email_or_password, Toast.LENGTH_SHORT)
                .show()
        }

    private fun observeClearPasswordEvent() =
        viewModel.clearPasswordEvent.observeEvent(viewLifecycleOwner) {
            binding.passwordEditText.text?.clear()

        }

    private fun observeNavigateToTabsEvent() =
        viewModel.navigateToTabsEvent.observeEvent(viewLifecycleOwner) {
            findNavController().navigate(
                R.id.action_signInFragment_to_tabsFragment,
                null,
                navOptions {
                    popUpTo(R.id.signInFragment) {
                        inclusive = true
                    }
                }
            )
        }

    private fun onSignUpButtonPressed() {
        val email = binding.emailEditText.text.toString()
        val emailArg = if (email.isBlank())
            null
        else {
            email
        }

        // user want to create a new account
        val direction = SignInFragmentDirections.actionSignInFragmentToSignUpFragment(email)
        findNavController().navigate(direction)
    }

}