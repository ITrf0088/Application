package org.rasulov.application.model

import org.rasulov.application.model.accounts.AccountsRepository
import org.rasulov.application.model.accounts.InMemoryAccountsRepository
import org.rasulov.application.model.boxes.BoxesRepository
import org.rasulov.application.model.boxes.InMemoryBoxesRepository

object Repositories {

    val accountsRepository: AccountsRepository = InMemoryAccountsRepository()

    val boxesRepository: BoxesRepository = InMemoryBoxesRepository(accountsRepository)

}