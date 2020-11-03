package com.example.myfirstapp.domain.useCase

import com.example.myfirstapp.data.repository.SalesRepository
import com.example.myfirstapp.data.request.GetStateByDocRequest
import com.example.myfirstapp.data.response.SalesGetByDocResponse
import com.example.myfirstapp.domain.useCase.base.UseCase
import io.reactivex.Observable
import io.reactivex.Scheduler

class GetSalesByDoc(
    executorThread: Scheduler,
    uiThread: Scheduler,
    private var repository: SalesRepository
) : UseCase<List<SalesGetByDocResponse>>(executorThread, uiThread) {
    var request = GetStateByDocRequest()
    override fun createObservableUseCase(): Observable<List<SalesGetByDocResponse>> {
        return repository.getStateByDoc(request)
    }
}