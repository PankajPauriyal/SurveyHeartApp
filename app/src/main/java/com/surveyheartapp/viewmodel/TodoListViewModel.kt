package com.surveyheartapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.surveyheartapp.model.TodoItem
import com.surveyheartapp.repo.TodoRepository
import com.surveyheartapp.utill.Resource
import kotlinx.coroutines.launch

class TodoListViewModel(private val repository: TodoRepository) : ViewModel() {

    val todos = MutableLiveData<Resource<List<TodoItem>>>()
    val deleteTodoResult = MutableLiveData<Resource<Unit>>()

    fun fetchTodos(isInternetAvailable: Boolean) {
        viewModelScope.launch {
            todos.postValue(Resource.loading(null))
            try {
                val todosFromApi = repository.getTodos(isInternetAvailable)
                todos.postValue(Resource.success(todosFromApi))
            } catch (e: Exception) {
                todos.postValue(Resource.error("Something went wrong", null))
            }
        }
    }

    fun deleteTodoItem(id: Int) {
        viewModelScope.launch {
            try {
                repository.deleteTodoItem(id)
                deleteTodoResult.postValue(Resource.success(Unit))
            } catch (e: Exception) {
                deleteTodoResult.postValue(Resource.error("Error deleting todo", null))
            }
        }
    }
}
