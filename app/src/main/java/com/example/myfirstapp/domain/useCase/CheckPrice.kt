package com.example.myfirstapp.domain.useCase

import com.example.myfirstapp.data.repository.CheckRepository
import com.example.myfirstapp.data.request.CheckPricesRequest
import com.example.myfirstapp.data.response.CheckPricesResponse
import com.example.myfirstapp.domain.useCase.base.UseCase
import io.reactivex.Observable
import io.reactivex.Scheduler

class CheckPrice(
    executorThread: Scheduler,
    uiThread: Scheduler,
    private var repository: CheckRepository
) : UseCase<CheckPricesResponse>(executorThread, uiThread) {
    var request = CheckPricesRequest()
    override fun createObservableUseCase(): Observable<CheckPricesResponse> {
        return repository.checkPrices(request)
    }
}