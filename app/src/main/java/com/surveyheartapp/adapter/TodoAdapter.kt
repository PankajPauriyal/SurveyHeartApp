package com.surveyheartapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.surveyheartapp.databinding.ItemTodoBinding
import com.surveyheartapp.model.TodoItem
import com.surveyheartapp.viewmodel.TodoListViewModel

class TodoAdapter(private var todos: List<TodoItem>, private val viewModel: TodoListViewModel) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {
    var clickonItem: OnClickOnItem? = null

    inner class TodoViewHolder(private val binding: ItemTodoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(todo: TodoItem) {
            binding.todoText.text = todo.todo

            binding.tvEdit.setOnClickListener {
                clickonItem?.setOnEditClick(position, todo.id.toString(),todo.todo)
            }

            binding.tvDelete.setOnClickListener {
                clickonItem?.setOnDeleteClick(position, todo.id.toString())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = ItemTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(todos[position])
    }

    override fun getItemCount(): Int = todos.size

    fun setOnclickInterface(clickonItem: OnClickOnItem) {
        this.clickonItem = clickonItem
    }

    interface OnClickOnItem {
        fun setOnEditClick(position: Int, id: String?, todo: String)
        fun setOnDeleteClick(position: Int, id: String?)
    }

    fun updateTodoList(newTodoItems: List<TodoItem>) {
        todos = newTodoItems
        notifyDataSetChanged()
    }
}
