package com.example.myfirstapp.domain.useCase

import com.example.myfirstapp.data.repository.SalesRepository
import com.example.myfirstapp.data.request.CloseCartRequest
import com.example.myfirstapp.data.response.CloseCartResponse
import com.example.myfirstapp.domain.useCase.base.UseCase
import io.reactivex.Observable
import io.reactivex.Scheduler

class CloseSales(
    executorThread: Scheduler,
    uiThread: Scheduler,
    private var repository: SalesRepository
) : UseCase<CloseCartResponse>(executorThread, uiThread) {
    var request = CloseCartRequest()
    override fun createObservableUseCase(): Observable<CloseCartResponse> {
        return repository.salesCloseCart(request)
    }
}