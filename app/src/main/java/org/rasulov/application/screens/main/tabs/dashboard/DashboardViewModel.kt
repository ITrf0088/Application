package org.rasulov.application.screens.main.tabs.dashboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.rasulov.application.model.boxes.BoxesRepository
import org.rasulov.application.model.boxes.entities.Box
import org.rasulov.application.utils.share

class DashboardViewModel(
    private val boxesRepository: BoxesRepository
) : ViewModel() {

    private val _boxes = MutableLiveData<List<Box>>()
    val boxes = _boxes.share()

    init {
        viewModelScope.launch {
            boxesRepository.getBoxes(onlyActive = true).collect {
                _boxes.value = it
            }
        }
    }

}