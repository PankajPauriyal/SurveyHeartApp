// AddTodoRepository.kt
package com.surveyheartapp.repo

import com.surveyheartapp.model.TodoItem
import com.verbio.verbioapp.Retrofit.ApiInterface

class AddTodoRepository(private val apiService: ApiInterface) {

    suspend fun addTodo(todo: TodoItem): TodoItem {
        val response = apiService.addTodo(todo)
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Error adding todo: Empty response")
        } else {
            throw Exception("Error adding todo: ${response.message()}")
        }
    }
    suspend fun editTodoItem(id: String, updatedTodo: String?): TodoItem {
        val response = apiService.editTodoItem(id, updatedTodo)
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Error editing todo: ${response.message()}")
        } else {
            throw Exception("Error editing todo: ${response.message()}")
        }
    }
}
