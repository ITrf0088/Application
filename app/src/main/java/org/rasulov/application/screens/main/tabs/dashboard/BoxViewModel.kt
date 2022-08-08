package org.rasulov.application.screens.main.tabs.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.rasulov.application.model.boxes.BoxesRepository
import org.rasulov.application.utils.MutableLiveEvent
import org.rasulov.application.utils.publishEvent
import org.rasulov.application.utils.share

class BoxViewModel(
    private val boxId: Int,
    private val boxesRepository: BoxesRepository
) : ViewModel() {

    private val _shouldExitEvent = MutableLiveEvent<Boolean>()
    val shouldExitEvent = _shouldExitEvent.share()

    init {
        viewModelScope.launch {
            boxesRepository.getBoxes(onlyActive = true)
                .map { boxes -> boxes.firstOrNull { it.id == boxId } }
                .collect { currentBox ->
                    _shouldExitEvent.publishEvent(currentBox == null)
                }
        }
    }
}