package com.arjun.cubewealth.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.arjun.cubewealth.R
import com.arjun.cubewealth.dataModels.ItemImageWithLabelDisplay
import com.arjun.cubewealth.utills.MovieDBPathToImageLink
import com.bumptech.glide.Glide

class AdapterRoundImageWithLabel :
    RecyclerView.Adapter<AdapterRoundImageWithLabel.ViewHolderCreditsAndProduction>() {

    inner class ViewHolderCreditsAndProduction(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView =
            itemView.findViewById(R.id.imageView_labelImage_creditImageLayout)
        val textView: TextView = itemView.findViewById(R.id.textView_labelText_creditImageLayout)
    }

    private val diffUtilCallback =
        object : DiffUtil.ItemCallback<ItemImageWithLabelDisplay>() {
            override fun areItemsTheSame(
                oldItem: ItemImageWithLabelDisplay,
                newItem: ItemImageWithLabelDisplay
            ): Boolean {
                return oldItem.imagePath == newItem.imagePath
            }

            override fun areContentsTheSame(
                oldItem: ItemImageWithLabelDisplay,
                newItem: ItemImageWithLabelDisplay
            ): Boolean {
                return oldItem.labelText == newItem.labelText
            }
        }

    val differList = AsyncListDiffer(this, diffUtilCallback)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolderCreditsAndProduction {
        return ViewHolderCreditsAndProduction(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_round_image_with_label,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differList.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolderCreditsAndProduction, position: Int) {
        val posData = differList.currentList[position]

        Glide.with(holder.imageView)
            .load(MovieDBPathToImageLink.convertPathToImage((posData.imagePath)))
            .placeholder(R.drawable.ic_loading)
            .error(R.drawable.ic_profile_error)
            .centerInside()
            .into(holder.imageView)

        holder.textView.text = posData.labelText
    }
}