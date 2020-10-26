package com.example.myfirstapp.domain.useCase

import com.example.myfirstapp.data.repository.SubsidiaryRepository
import com.example.myfirstapp.data.response.Subsidiary
import com.example.myfirstapp.domain.useCase.base.UseCase
import io.reactivex.Observable
import io.reactivex.Scheduler

class GetSubsidiary(
    executorThread: Scheduler,
    uiThread: Scheduler,
    private var repository: SubsidiaryRepository
) : UseCase<List<Subsidiary>>(executorThread, uiThread) {
    override fun createObservableUseCase(): Observable<List<Subsidiary>> {
        return repository.getSubsidiaries()
    }
}