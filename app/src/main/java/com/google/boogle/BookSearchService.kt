package com.google.boogle

import android.support.annotation.WorkerThread
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.extensions.android.json.AndroidJsonFactory
import com.google.api.services.books.Books
import com.google.api.services.books.model.Volume
import com.google.common.primitives.Ints
import java.io.IOException

class BookSearchService {
    @WorkerThread
    fun findBook(book_name: String): List<Volume> {
        var query = book_name

        // we add the isbn special
        if (Ints.tryParse(query) != null && (query.length == 10)) {
            query = "$query+isbn:$query"
        }

        // Creates the books api client
        val books = Books.Builder(AndroidHttp.newCompatibleTransport(), AndroidJsonFactory.getDefaultInstance(), null)
            .setApplicationName(BuildConfig.APPLICATION_ID)
            .build()

        try {
            val list = books.volumes().list(query).setProjection("LITE")
            return list.execute().getItems()
        } catch (e: IOException) {
            e.printStackTrace()
            return emptyList<Volume>()
        }
    }


}