package com.surveyheartapp.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.io.Serializable


data class TodaListResponce(
    val todos: List<TodoItem>,
    val total: Int,
    val skip: Int,
    val limit: Int
)

open class TodoItem(
    var id: Int = 0,
    var todo: String = "",
    var completed: Boolean = false,
    var userId: Int = 0
) : RealmObject()