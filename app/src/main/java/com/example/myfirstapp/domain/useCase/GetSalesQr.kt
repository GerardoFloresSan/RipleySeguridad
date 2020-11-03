package com.example.myfirstapp.domain.useCase

import com.example.myfirstapp.data.repository.SalesRepository
import com.example.myfirstapp.data.request.GetStateByQrRequest
import com.example.myfirstapp.data.response.SalesGetStateResponse
import com.example.myfirstapp.domain.useCase.base.UseCase
import io.reactivex.Observable
import io.reactivex.Scheduler

class GetSalesQr(
    executorThread: Scheduler,
    uiThread: Scheduler,
    private var repository: SalesRepository
) : UseCase<SalesGetStateResponse>(executorThread, uiThread) {
    var request = GetStateByQrRequest()
    override fun createObservableUseCase(): Observable<SalesGetStateResponse> {
        return repository.getStateByQr(request)
    }
}