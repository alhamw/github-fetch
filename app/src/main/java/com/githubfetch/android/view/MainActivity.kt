package com.githubfetch.android.view

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.githubfetch.android.R
import com.githubfetch.android.api.enum.Status
import com.githubfetch.android.view.adapter.MainPagingAdapter
import com.githubfetch.android.view.viewModel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

class MainActivity : AppCompatActivity() {

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val pagingAdapter = MainPagingAdapter(this)
        rv_main.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rv_main.adapter = pagingAdapter
        rv_main.isNestedScrollingEnabled = false

        val viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.pagingUsers.observe(this, Observer {
            pagingAdapter.submitList(it)
        })
        viewModel.state.observe(this, Observer {
            when (it.status) {
                Status.RUNNING -> tv_load.visibility = View.VISIBLE
                Status.FAILED -> {
                    tv_load.visibility = View.GONE
                    it.msg?.let { failedMessage ->
                        Toast.makeText(this, failedMessage, Toast.LENGTH_SHORT).show()
                    } ?: Toast.makeText(this, "Oops", Toast.LENGTH_SHORT).show()
                }
                Status.SUCCESS -> {
                    tv_load.visibility = View.GONE
                    tv_empty.visibility = View.GONE
                    rv_main.visibility = View.VISIBLE
                }
                Status.EMPTY -> {
                    tv_load.visibility = View.GONE
                    tv_empty.visibility = View.VISIBLE
                    rv_main.visibility = View.GONE
                }
            }
        })
        et_search.requestFocus()
        et_search.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                viewModel.searchUser(et_search.text.toString().trim())
                et_search.requestFocus()
                true
            } else {
                false
            }
        }
        et_search.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                viewModel.searchUser(et_search.text.toString().trim())
                et_search.requestFocus()
                true
            } else {
                false
            }
        }
    }
}
