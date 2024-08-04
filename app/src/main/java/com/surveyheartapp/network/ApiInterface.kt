package com.verbio.verbioapp.Retrofit

import com.surveyheartapp.model.TodaListResponce
import com.surveyheartapp.model.TodoItem
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


interface ApiInterface {

    @GET("todos")
    suspend fun getTodos(): Response<TodaListResponce>

    @POST("todos/add")
    suspend fun addTodo(@Body todo: TodoItem): Response<TodoItem>


    @PUT("todos/{id}")
    suspend fun editTodoItem(@Path("id") id: String, @Body updatedTodo: String?): Response<TodoItem>

    @DELETE("todos/{id}")
    suspend fun deleteTodoItem(@Path("id") id: String): Response<Unit>
}
