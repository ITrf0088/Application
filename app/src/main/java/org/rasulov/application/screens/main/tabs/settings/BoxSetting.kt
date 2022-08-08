package org.rasulov.application.screens.main.tabs.settings

import org.rasulov.application.model.boxes.entities.Box

data class BoxSetting(
    val box: Box,
    val enabled: Boolean
)