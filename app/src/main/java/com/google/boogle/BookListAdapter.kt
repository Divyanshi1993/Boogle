package com.google.boogle

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.api.services.books.model.Volume

class BookListAdapter(private val sCount: Int) :
    RecyclerView.Adapter<BookListAdapter.BookViewHolder>() {
    private var volumes: List<Volume>? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        return BookViewHolder(parent)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.setSpanCount(sCount)
        volumes?.get(position)?.let { holder.setVolume(it) }
    }

    fun updateAdapter(volumes: List<Volume>) {
        this.volumes = volumes;
        notifyDataSetChanged();
    }

    override fun getItemId(position: Int): Long {
        return volumes?.get(position)?.id.hashCode().toLong()
    }

    override fun getItemCount(): Int {
        if (volumes == null) {
            return 0
        }
        return volumes!!.size
    }

    class BookViewHolder(viewGroup: ViewGroup) :
        RecyclerView.ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.book_view, viewGroup, false)),
        View.OnClickListener {

        private var volume: Volume? = null
        private var sCount: Int = 0

        init {
            itemView.setOnClickListener(this)
        }

        fun setVolume(volume: Volume) {
            this.volume = volume

            println(volume.volumeInfo.infoLink)

            val approximateWidth = 300
            val approximateHeight = 300

            val displayMetrics = itemView.context.resources.displayMetrics

            val screenWidth = displayMetrics.widthPixels

            val width = screenWidth / sCount
            val height = approximateHeight * width / approximateWidth

            val params = itemView.layoutParams
            params.width = width
            params.height = height
            itemView.layoutParams = params
            itemView.invalidate()

            val imageLinks = volume.volumeInfo.imageLinks

            if (imageLinks != null) {
                var imageLink = imageLinks.smallThumbnail
                Glide.with(itemView.context)
                    .load(imageLink)
                    .into(itemView as ImageView)
            }
        }

        fun setSpanCount(sCount: Int) {
            this.sCount = sCount
        }

        override fun onClick(v: View) {

            val bookdata = Bundle()

            val volumeInfo = volume!!.volumeInfo
            val saleInfo = volume!!.saleInfo

            if (volumeInfo != null) {
                val imageLinks = volumeInfo.imageLinks
                if (volumeInfo.title != null) {
                    bookdata.putString(Constants.TITLE, volumeInfo.title)
                }
                if (volumeInfo.subtitle != null) {
                    bookdata.putString(Constants.SUBTITLE, volumeInfo.subtitle)
                }
                if (volumeInfo.description != null) {
                    bookdata.putString(Constants.DESCRIPTION, volumeInfo.description)
                }
                if (volumeInfo.publisher != null) {
                    bookdata.putString(Constants.PUBLISHER, volumeInfo.publisher)
                }
                if (volumeInfo.authors != null) {
                    bookdata.putStringArray(Constants.AUTHORS, volumeInfo.authors.toTypedArray())
                }
                if (volumeInfo.publishedDate != null) {
                    bookdata.putString(Constants.PUBLISHED_DATE, volumeInfo.publishedDate)
                }
                if (saleInfo != null) {
                    val retailPrice = saleInfo.retailPrice
                    val listPrice = saleInfo.listPrice
                    if (retailPrice != null) {
                        bookdata.putDouble(Constants.RETAIL_PRICE, retailPrice.amount!!)
                        bookdata.putString(Constants.RETAIL_PRICE_CURRENCY_CODE, retailPrice.currencyCode)
                    }
                    if (listPrice != null) {
                        bookdata.putDouble(Constants.LIST_PRICE, listPrice.amount!!)
                        bookdata.putString(Constants.LIST_PRICE_CURRENCY_CODE, listPrice.currencyCode)
                    }
                }
                if (imageLinks != null) {
                    bookdata.putString(Constants.IMAGE, imageLinks.thumbnail)
                }
            }

            val context = itemView.context
            context.startActivity(Intent(context, BookDetailActivity::class.java).putExtra("bookdata", bookdata))
        }
    }

}
