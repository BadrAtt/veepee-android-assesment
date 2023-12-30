package com.vp.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vp.core_models.MovieDetail
import com.vp.favorites.databinding.ItemListBinding

class ListAdapter : RecyclerView.Adapter<ListAdapter.ListViewHolder>() {
    private var listItems = arrayListOf<MovieDetail>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
            ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val listItem = listItems[position]
        if (NO_IMAGE != listItem.poster) {
            val density = holder.image.resources.displayMetrics.density
            Glide
                .with(holder.image)
                .load(listItem.poster)
                .override((300 * density).toInt(), (600 * density).toInt())
                .into(holder.image)
        } else {
            holder.image.setImageResource(R.drawable.placeholder)
        }
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    fun setItems(listItems: List<MovieDetail>) {
        this.listItems = listItems as ArrayList<MovieDetail>
        notifyDataSetChanged()
    }

    inner class ListViewHolder(itemView: ItemListBinding) : RecyclerView.ViewHolder(itemView.root) {
        var image: ImageView

        init {
            image = itemView.poster
        }
    }

    companion object {
        private const val NO_IMAGE = "N/A"
    }
}