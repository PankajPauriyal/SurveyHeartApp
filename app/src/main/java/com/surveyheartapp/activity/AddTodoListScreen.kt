package com.surveyheartapp.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.mvvmtutorial.ui.base.ViewModelFactory
import com.surveyheartapp.R
import com.surveyheartapp.databinding.ActivityAddTodoListScreenBinding
import com.surveyheartapp.repo.AddTodoRepository
import com.surveyheartapp.utill.Resource
import com.surveyheartapp.viewmodel.AddTodoViewModel
import com.verbio.verbioapp.Retrofit.APIClient
import com.verbio.verbioapp.Retrofit.ApiInterface

class AddTodoListScreen : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityAddTodoListScreenBinding
    var fromedit: String? = null
    var updatedTodo: String? = null
    var editId: String? = null
    private val viewModel: AddTodoViewModel by viewModels {
        ViewModelFactory(
            applicationContext,
            addTodoRepository = AddTodoRepository(provideApiService())
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTodoListScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setlistener()
        getdata()
        getObserverResponce()
    }

    private fun getdata() {
         editId = intent.getStringExtra("editId")
         updatedTodo = intent.getStringExtra("updatedTodo")
        fromedit = intent.getStringExtra("fromedit")

        if (!updatedTodo.isNullOrEmpty()) {
            binding.etTodo.setText(updatedTodo)
        }
    }

    private fun getObserverResponce() {
        lifecycleScope.launchWhenStarted {
            viewModel.todo.observe(this@AddTodoListScreen) { resource ->
                binding.progressBar.visibility = View.GONE

                when (resource.status) {
                    Resource.Status.SUCCESS -> {
                        startActivity(Intent(this@AddTodoListScreen, TodoListScreen::class.java))
                    }

                    Resource.Status.ERROR -> {
                        Toast.makeText(this@AddTodoListScreen, resource.message, Toast.LENGTH_SHORT)
                            .show()
                    }

                    Resource.Status.LOADING -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                }
            }
        }

        viewModel.editTodoResult.observe(this, Observer { resource ->
            when (resource.status) {
                Resource.Status.SUCCESS -> {
                    Toast.makeText(this, "Todo edited successfully", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, TodoListScreen::class.java))
                }

                Resource.Status.ERROR -> {
                    Toast.makeText(
                        this,
                        resource.message ?: "Error editing todo",
                        Toast.LENGTH_LONG
                    ).show()
                }

                Resource.Status.LOADING -> {
                    // You can handle loading state here if needed
                }
            }
        })

    }

    private fun setlistener() {
        binding.btnAddTodo.setOnClickListener(this)
        binding.ivBack.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.btnAddTodo -> {
                    adddata()
                }

                R.id.ivBack -> {
                    onBackPressed()
                }
            }
        }
    }

    private fun adddata() {
        val todoText = binding.etTodo.text.toString()
        if (todoText.isNotEmpty()) {
            viewModel.addTodotext = todoText
            if (!fromedit.isNullOrEmpty())
            {
                viewModel.editTodoItem(editId.toString(), updatedTodo)
            }else{
                viewModel.fetchAddTodo()
            }
        } else {
            Toast.makeText(this, "Please enter a todo", Toast.LENGTH_SHORT).show()
        }
    }

    private fun provideApiService(): ApiInterface {
        return APIClient.client.create(ApiInterface::class.java)
    }
}
