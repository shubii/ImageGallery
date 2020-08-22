package com.example.dunzoassignment.presentation.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.dunzoassignment.R
import com.example.dunzoassignment.databinding.ImageItemBinding
import com.example.dunzoassignment.presentation.getEachImageWidth
import com.example.dunzoassignment.presentation.models.Photo
import com.squareup.picasso.Picasso

class ImageListAdapter : RecyclerView.Adapter<ImageListAdapter.IamgesViewHolder>() {

    private var totalList : ArrayList<Photo> = ArrayList()

    fun setData (newList : ArrayList<Photo>) {
        totalList = newList
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IamgesViewHolder {
        val binding = ImageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return IamgesViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return totalList.size
    }


    override fun onBindViewHolder(holder: IamgesViewHolder, position: Int) {
        holder.bind(position, totalList.get(position))
    }

    class IamgesViewHolder(private val binding: ImageItemBinding): RecyclerView.ViewHolder(binding.root) {


        fun bind(position: Int, photo: Photo) {
             val url = "https://farm${photo.farm}.staticflickr.com/${photo.server}/${photo.id}_${photo.secret}_m.jpg"
            Picasso.get().load(url).
                placeholder(R.mipmap.ic_launcher_round)
                .fit()
//                .centerCrop()
                .centerInside()
                .into(binding.imageItem)
            binding.tvTitle.text = photo.title
        }
    }


}