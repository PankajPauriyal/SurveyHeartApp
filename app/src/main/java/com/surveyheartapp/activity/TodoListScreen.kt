package com.surveyheartapp.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvmtutorial.ui.base.ViewModelFactory
import com.surveyheartapp.R
import com.surveyheartapp.adapter.TodoAdapter
import com.surveyheartapp.databinding.ActivityMainBinding
import com.surveyheartapp.dialog.ConfirmationDialog
import com.surveyheartapp.model.TodoItem
import com.surveyheartapp.network.NetworkUtils
import com.surveyheartapp.repo.TodoRepository
import com.surveyheartapp.utill.Resource
import com.surveyheartapp.viewmodel.TodoListViewModel
import com.verbio.verbioapp.Retrofit.APIClient
import com.verbio.verbioapp.Retrofit.ApiInterface

class TodoListScreen : AppCompatActivity(), View.OnClickListener {
    private lateinit var adapter: TodoAdapter
    private lateinit var binding: ActivityMainBinding
    private val viewModel: TodoListViewModel by viewModels {
        ViewModelFactory(applicationContext, TodoRepository(provideApiService(), isInternetAvailable()))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setContentView(binding.root)
        setAdapter()
        setListeners()
        observeViewModel()
        viewModel.fetchTodos(NetworkUtils.isInternetAvailable(this))
    }

    private fun observeViewModel() {
        viewModel.todos.observe(this, Observer { resource ->
            binding.swiperefresh.isRefreshing = false
            binding.progressBar.visibility = View.GONE

            when (resource.status) {
                Resource.Status.SUCCESS -> {
                    resource.data?.let { todos ->
                        adapter.updateTodoList(todos)
                    }
                }

                Resource.Status.ERROR -> {
                    // Handling error
                    Toast.makeText(this, resource.message ?: "Unknown error", Toast.LENGTH_LONG).show()
                }

                Resource.Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        })

        viewModel.deleteTodoResult.observe(this, Observer { resource ->
            when (resource.status) {
                Resource.Status.SUCCESS -> {
                    Toast.makeText(this, "Todo deleted successfully", Toast.LENGTH_SHORT).show()
                    viewModel.fetchTodos(NetworkUtils.isInternetAvailable(this))
                }

                Resource.Status.ERROR -> {
                    Toast.makeText(this, resource.message ?: "Error deleting todo", Toast.LENGTH_LONG).show()
                }

                Resource.Status.LOADING -> {
                    // You can handle loading state here if needed
                }
            }
        })
    }

    private fun setListeners() {
        binding.swiperefresh.setOnRefreshListener {
            viewModel.fetchTodos(NetworkUtils.isInternetAvailable(this))
        }
        binding.ivAddSurvey.setOnClickListener(this)
    }

    private fun setAdapter() {
        binding.rvSurvey.layoutManager = LinearLayoutManager(this)
        adapter = TodoAdapter(emptyList(), viewModel)
        binding.rvSurvey.adapter = adapter
        adapter.setOnclickInterface(object : TodoAdapter.OnClickOnItem {
            override fun setOnEditClick(position: Int, id: String?, todo: String) {
                id?.let {
                    // Create an updated TodoItem object and pass it to the ViewModel
                    val updatedTodo = TodoItem(id.toInt(), todo, false, 5)

                    startActivity(Intent(this@TodoListScreen, AddTodoListScreen::class.java)
                        .putExtra("editId", id)
                        .putExtra("updatedTodo", todo)
                        .putExtra("fromedit", "fromedit")
                    )
                }
            }

            override fun setOnDeleteClick(position: Int, id: String?) {
                id?.let {
                    showDeleteDialog(it.toInt())
                }
            }
        })
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.ivAddSurvey -> {
                    startActivity(Intent(this, AddTodoListScreen::class.java))
                }
            }
        }
    }

    private fun showDeleteDialog(id: Int) {
        val deleteDialogs = ConfirmationDialog(this)
        val dialog = deleteDialogs.dialog!!
        val tvTitle = dialog.findViewById<TextView>(R.id.tvTitle)
        dialog.findViewById<TextView>(R.id.btnCancel).setOnClickListener {
            deleteDialogs.dismiss()
        }
        dialog.findViewById<TextView>(R.id.btnOk).setOnClickListener {
            dialog.dismiss()
            viewModel.deleteTodoItem(id)
        }
        dialog.show()
    }

    private fun provideApiService(): ApiInterface {
        return APIClient.client.create(ApiInterface::class.java)
    }

    private fun isInternetAvailable(): Boolean {
        return NetworkUtils.isInternetAvailable(this)
    }
}
