package com.example.myfirstapp.domain.useCase

import com.example.myfirstapp.data.repository.SalesRepository
import com.example.myfirstapp.data.request.GetStateByQrRequest
import com.example.myfirstapp.data.response.SalesGetByResponse
import com.example.myfirstapp.domain.useCase.base.UseCase
import io.reactivex.Observable
import io.reactivex.Scheduler

class GetSalesQr(
    executorThread: Scheduler,
    uiThread: Scheduler,
    private var repository: SalesRepository
) : UseCase<SalesGetByResponse>(executorThread, uiThread) {
    var request = GetStateByQrRequest()
    override fun createObservableUseCase(): Observable<SalesGetByResponse> {
        return repository.getStateByQr(request)
    }
}