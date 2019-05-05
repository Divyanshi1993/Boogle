package com.google.boogle

import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.SearchView
import android.view.View
import com.google.api.services.books.model.Volume
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

public class MainActivity : AppCompatActivity(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private lateinit var job: Job
    private lateinit var gridLayoutManager:GridLayoutManager
    private lateinit var volumeList: List<Volume>
    private lateinit var adapter:BookListAdapter
    private val bookSearchService = BookSearchService();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        job = Job()
        setAdapter();

        search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchBook(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })


    }
    private fun setAdapter() {
        gridLayoutManager = GridLayoutManager(this, if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 2 else 3)
        books_grid.layoutManager = gridLayoutManager
        adapter = BookListAdapter( gridLayoutManager.spanCount)
        books_grid.adapter = adapter
        }


    private fun searchBook(book_name: String) {
        //Finding Book using google book Api
        launch {
            loading_view.visibility = View.VISIBLE
            volumeList = withContext(Dispatchers.IO) { bookSearchService.findBook(book_name) }
            loading_view.visibility = View.GONE
            updateBookList(volumeList)
        }
    }

    fun updateBookList(volumeList: List<Volume>) {
        // set List to the adapter
       adapter.updateAdapter(volumeList)

    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }
}