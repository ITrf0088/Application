package org.rasulov.application.model.boxes.core

import kotlinx.coroutines.flow.Flow
import org.rasulov.application.model.boxes.core.entities.Box
import org.rasulov.application.model.boxes.core.entities.BoxSetting

interface BoxesRepository {

    /**
     * Get the list of boxes.
     * @param onlyActive if set to `true` then only active boxes are emitted.
     */
    suspend fun getBoxesAndSettings(onlyActive: Boolean): Flow<List<BoxSetting>>

    /**
     * Mark the specified box as active. Only active boxes are displayed in dashboard screen.
     */
    suspend fun activateBox(box: Box)

    /**
     * Mark the specified box as inactive. Inactive boxes are not displayed in dashboard screen.
     */
    suspend fun deactivateBox(box: Box)

}