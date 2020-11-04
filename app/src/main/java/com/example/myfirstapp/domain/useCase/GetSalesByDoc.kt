package com.example.myfirstapp.domain.useCase

import com.example.myfirstapp.data.repository.SalesRepository
import com.example.myfirstapp.data.request.GetStateByDocRequest
import com.example.myfirstapp.data.response.SalesGetByResponse
import com.example.myfirstapp.domain.useCase.base.UseCase
import io.reactivex.Observable
import io.reactivex.Scheduler

class GetSalesByDoc(
    executorThread: Scheduler,
    uiThread: Scheduler,
    private var repository: SalesRepository
) : UseCase<List<SalesGetByResponse>>(executorThread, uiThread) {
    var request = GetStateByDocRequest()
    override fun createObservableUseCase(): Observable<List<SalesGetByResponse>> {
        return repository.getStateByDoc(request)
    }
}