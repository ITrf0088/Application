package org.rasulov.application.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.rasulov.application.model.accounts.AccountsRepository
import org.rasulov.application.utils.MutableLiveEvent
import org.rasulov.application.utils.publishEvent
import org.rasulov.application.utils.share

/**
 * SplashViewModel checks whether user is signed-in or not.
 */
class SplashViewModel(
    private val accountsRepository: AccountsRepository
) : ViewModel() {

    private val _launchMainScreenEvent = MutableLiveEvent<Boolean>()
    val launchMainScreenEvent = _launchMainScreenEvent.share()

    init {
        viewModelScope.launch {
            _launchMainScreenEvent.publishEvent(accountsRepository.isSignedIn())
        }
    }
}