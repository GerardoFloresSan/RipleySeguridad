package com.example.myfirstapp.domain.useCase

import com.example.myfirstapp.data.repository.ParameterRepository
import com.example.myfirstapp.data.request.CheckPricesRequest
import com.example.myfirstapp.data.response.Parameter
import com.example.myfirstapp.domain.useCase.base.UseCase
import io.reactivex.Observable
import io.reactivex.Scheduler

class GetParameters(
    executorThread: Scheduler,
    uiThread: Scheduler,
    private var repository: ParameterRepository
) : UseCase<List<Parameter>>(executorThread, uiThread) {
    override fun createObservableUseCase(): Observable<List<Parameter>> {
        return repository.getParametersAll()
    }
}