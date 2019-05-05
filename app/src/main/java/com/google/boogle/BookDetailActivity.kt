package com.google.boogle

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import com.bumptech.glide.Glide
import com.google.boogle.databinding.ActivityBookDetailBinding

class BookDetailActivity : AppCompatActivity() {

    internal lateinit var bookdata: Bundle
    internal lateinit var bookInfoBinding: ActivityBookDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bookInfoBinding = DataBindingUtil.setContentView(this, R.layout.activity_book_detail)

        bookdata = intent.getBundleExtra("bookdata")
        updateViews()
    }

    private fun updateViews() {

        Glide.with(this).load(bookdata.getString(Constants.IMAGE)).into(bookInfoBinding.cover)

        val unknown = getString(R.string.unknown)

        bookInfoBinding.bookTitle.text =
            if (bookdata.containsKey(Constants.TITLE)) bookdata.getString(Constants.TITLE) else unknown
        bookInfoBinding.contentBookInfo.publishedDate.append(
            if (bookdata.containsKey(Constants.PUBLISHED_DATE)) bookdata.getString(
                Constants.PUBLISHED_DATE
            ) else unknown
        )
        bookInfoBinding.contentBookInfo.publisher.append(
            if (bookdata.containsKey(Constants.PUBLISHER)) bookdata.getString(
                Constants.PUBLISHER
            ) else unknown
        )
        if (bookdata.containsKey(Constants.AUTHORS)) {
            var author = "\n"
            val authors = bookdata.getStringArray(Constants.AUTHORS)
            for (i in authors!!.indices) {
                val singleAuthor = authors[i]
                if (TextUtils.isEmpty(singleAuthor)) {
                    continue
                }
                author += singleAuthor + if (i == authors.size - 1) "" else "\n"
                bookInfoBinding.contentBookInfo.author.append(author)
            }
        }
        if (bookdata.containsKey(Constants.SUBTITLE)) {
            bookInfoBinding.subtitle.text = bookdata.getString(Constants.SUBTITLE)
        } else {
            bookInfoBinding.subtitle.visibility = View.GONE
        }
        if (bookdata.containsKey(Constants.DESCRIPTION)) {
            bookInfoBinding.contentBookInfo.description.text = bookdata.getString(Constants.DESCRIPTION)
        }
    }

}

