package com.revvo.viewmodel

import androidx.lifecycle.ViewModel
import com.revvo.data.model.User
import com.revvo.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UserViewModel : ViewModel() {

    private val repository = UserRepository()

    private val _user = MutableStateFlow(repository.getUser())
    val user: StateFlow<User> = _user

    fun loadUser() { _user.value = repository.getUser() }
}