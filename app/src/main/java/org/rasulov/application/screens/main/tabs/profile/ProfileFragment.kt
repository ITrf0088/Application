package org.rasulov.application.screens.main.tabs.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import org.rasulov.application.R
import org.rasulov.application.databinding.FragmentProfileBinding
import org.rasulov.application.model.Repositories
import org.rasulov.application.model.accounts.core.entities.Account
import org.rasulov.application.utils.findTopNavController
import org.rasulov.application.utils.viewModelCreator
import org.rasulov.application.utils.observeEvent
import java.text.SimpleDateFormat
import java.util.*

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var binding: FragmentProfileBinding

    private val viewModel by viewModelCreator { ProfileViewModel(Repositories.accountsRepository) }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)

        binding.editProfileButton.setOnClickListener { onEditProfileButtonPressed() }
        binding.logoutButton.setOnClickListener { onLogoutButtonPressed() }
        observeAccountDetails()
        observeRestartAppFromLoginScreenEvent()
    }


    private fun observeAccountDetails() {
        val formatter = SimpleDateFormat.getDateTimeInstance()
        viewModel.account.observe(viewLifecycleOwner) { account ->
            if (account == null) return@observe
            binding.emailTextView.text = account.email
            binding.usernameTextView.text = account.username
            binding.createdAtTextView.text = if (account.createdAt == Account.UNKNOWN_CREATED_AT)
                getString(R.string.placeholder)
            else
                formatter.format(Date(account.createdAt))
        }
    }

    private fun onEditProfileButtonPressed() {
        findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
    }


    private fun observeRestartAppFromLoginScreenEvent() {
        viewModel.restartWithSignInEvent.observeEvent(viewLifecycleOwner) {
            findTopNavController().navigate(
                R.id.signInFragment,
                null,
                navOptions {
                    popUpTo(R.id.tabsFragment) {
                        inclusive = true
                    }
                }
            )
        }
    }

    private fun onLogoutButtonPressed() {
        viewModel.logout()
    }


}