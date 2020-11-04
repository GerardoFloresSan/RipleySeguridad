package com.example.myfirstapp.domain.useCase

import com.example.myfirstapp.data.repository.UserRepository
import com.example.myfirstapp.data.request.LoginRequest
import com.example.myfirstapp.data.response.LoginResponse
import com.example.myfirstapp.domain.useCase.base.UseCase
import io.reactivex.Observable
import io.reactivex.Scheduler

class GetUser(
    executorThread: Scheduler,
    uiThread: Scheduler,
    private var repository: UserRepository
) : UseCase<LoginResponse>(executorThread, uiThread) {
    var request = LoginRequest()
    override fun createObservableUseCase(): Observable<LoginResponse> {
        return repository.login(request)
    }
}