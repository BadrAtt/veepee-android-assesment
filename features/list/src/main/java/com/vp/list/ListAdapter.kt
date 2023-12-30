package com.vp.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.widget.ContentLoadingProgressBar
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.vp.list.model.ListItem

class ListAdapter : RecyclerView.Adapter<ViewHolder>() {

    private var listItems = arrayListOf<ListItem>()
    private val EMPTY_ON_ITEM_CLICK_LISTENER: OnItemClickListener =
        object : OnItemClickListener {
            override fun onItemClick(imdbID: String?) {

            }
        }

    private var onItemClickListener = EMPTY_ON_ITEM_CLICK_LISTENER

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == TYPE_ITEM) {
            ListViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
            )
        } else {
            LoaderViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_loader_view, parent, false)
            )
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is ListViewHolder -> {
                val item = listItems[position]
                if (NO_IMAGE != item.poster) {
                    val density = holder.image.resources.displayMetrics.density
                    GlideApp
                        .with(holder.image)
                        .load(item.poster)
                        .override((300 * density).toInt(), (600 * density).toInt())
                        .into(holder.image)
                } else {
                    holder.image.setImageResource(R.drawable.placeholder)
                }
            }

            is LoaderViewHolder -> {
                holder.bind()
            }
        }
    }

    override fun getItemCount(): Int {
        return listItems.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position != listItems.size) {
            TYPE_ITEM
        } else {
            TYPE_LOADER
        }
    }

    fun setItems(listItems: List<ListItem>) {
        this.listItems = listItems as ArrayList<ListItem>
        notifyDataSetChanged()
    }

    fun clearItems() {
        listItems.clear()
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        if (onItemClickListener != null) {
            this.onItemClickListener = onItemClickListener
        } else {
            this.onItemClickListener = EMPTY_ON_ITEM_CLICK_LISTENER
        }
    }

    inner class ListViewHolder(itemView: View) : ViewHolder(itemView),
        View.OnClickListener {
        var image: ImageView

        init {
            itemView.setOnClickListener(this)
            image = itemView.findViewById(R.id.poster)
        }

        override fun onClick(v: View) {
            onItemClickListener.onItemClick(listItems[layoutPosition].imdbID)
        }
    }

    inner class LoaderViewHolder(itemView: View) :
        ViewHolder(itemView) {
        private var progressBar: ContentLoadingProgressBar

        init {
            progressBar = itemView.findViewById(R.id.loader)
        }

        fun bind() {
            progressBar.isIndeterminate = true
        }
    }

    interface OnItemClickListener {
        fun onItemClick(imdbID: String?)
    }

    companion object {
        private const val NO_IMAGE = "N/A"
        const val TYPE_ITEM = 0
        const val TYPE_LOADER = 1
    }
}