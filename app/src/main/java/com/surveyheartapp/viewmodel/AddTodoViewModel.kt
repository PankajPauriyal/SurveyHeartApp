// AddTodoViewModel.kt
package com.surveyheartapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.surveyheartapp.model.TodoItem
import com.surveyheartapp.repo.AddTodoRepository
import com.surveyheartapp.utill.Resource
import kotlinx.coroutines.launch

class AddTodoViewModel(private val repository: AddTodoRepository) : ViewModel() {
    var addTodotext: String? = null
    var userId=145

    val todo = MutableLiveData<Resource<TodoItem>>()
    val editTodoResult = MutableLiveData<Resource<TodoItem>>()


    fun fetchAddTodo() {
        viewModelScope.launch {
            todo.postValue(Resource.loading(null))
            try {
                val todoItem = addTodotext?.let { TodoItem(0, it, false,userId) }
                if (todoItem != null) {
                    val addedTodo = repository.addTodo(todoItem)
                    todo.postValue(Resource.success(addedTodo))
                } else {
                    todo.postValue(Resource.error("Todo item is empty", null))
                }
            } catch (e: Exception) {
                todo.postValue(Resource.error("Something went wrong: ${e.message}", null))
            }
        }
    }

    fun editTodoItem(id: String, updatedTodo: String?) {
        viewModelScope.launch {
            editTodoResult.postValue(Resource.loading(null))
            try {
                val editedTodo = repository.editTodoItem(id, updatedTodo)
                editTodoResult.postValue(Resource.success(editedTodo))
            } catch (e: Exception) {
                editTodoResult.postValue(Resource.error("Something went wrong", null))
            }
        }
    }
}
