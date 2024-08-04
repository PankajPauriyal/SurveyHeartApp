package com.surveyheartapp.repo

import com.surveyheartapp.model.TodoItem
import com.verbio.verbioapp.Retrofit.ApiInterface
import io.realm.Realm

class TodoRepository(private val apiService: ApiInterface, internetAvailable: Boolean) {

    private val realm: Realm = Realm.getDefaultInstance()

    suspend fun getTodos(isInternetAvailable: Boolean): List<TodoItem> {
        return if (isInternetAvailable) {
            try {
                val response = apiService.getTodos()
                if (response.isSuccessful) {
                    response.body()?.todos?.also { todos ->
                        saveTodosToRealm(todos)
                    } ?: emptyList()
                } else {
                    throw Exception("Error fetching todos: ${response.message()}")
                }
            } catch (e: Exception) {
                getTodosFromRealm()
            }
        } else {
            getTodosFromRealm()
        }
    }

    private fun saveTodosToRealm(todos: List<TodoItem>) {
        realm.executeTransactionAsync { bgRealm ->
            bgRealm.insertOrUpdate(todos)
        }
    }

    private fun getTodosFromRealm(): List<TodoItem> {
        return realm.where(TodoItem::class.java).findAll()
    }


    fun deleteTodoItem(id: Int) {
        realm.executeTransactionAsync { bgRealm ->
            val result = bgRealm.where(TodoItem::class.java).equalTo("id", id).findFirst()
            result?.deleteFromRealm()
        }
    }
}
