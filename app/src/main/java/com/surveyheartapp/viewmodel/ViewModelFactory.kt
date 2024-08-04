package com.example.mvvmtutorial.ui.base

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.surveyheartapp.repo.AddTodoRepository
import com.surveyheartapp.repo.TodoRepository
import com.surveyheartapp.viewmodel.AddTodoViewModel
import com.surveyheartapp.viewmodel.TodoListViewModel

class ViewModelFactory(
    private val context: Context,
    private val todoRepository: TodoRepository? = null,
    private val addTodoRepository: AddTodoRepository? = null
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(TodoListViewModel::class.java) && todoRepository != null -> {
                TodoListViewModel(todoRepository) as T
            }
            modelClass.isAssignableFrom(AddTodoViewModel::class.java) && addTodoRepository != null -> {
                AddTodoViewModel(addTodoRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
