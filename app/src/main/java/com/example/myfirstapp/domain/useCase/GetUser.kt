package com.example.myfirstapp.domain.useCase

import com.example.myfirstapp.data.repository.UserRepository
import com.example.myfirstapp.data.response.User
import com.example.myfirstapp.domain.useCase.base.UseCase
import io.reactivex.Observable
import io.reactivex.Scheduler

class GetUser(
    executorThread: Scheduler,
    uiThread: Scheduler,
    private var repository: UserRepository
) : UseCase<User>(executorThread, uiThread) {
    var dni = ""
    override fun createObservableUseCase(): Observable<User> {
        return repository.getUserByDni(dni)
    }
}