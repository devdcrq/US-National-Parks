package com.devdcrq.usastateparks.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devdcrq.usastateparks.databinding.ParkRowBinding
import com.devdcrq.usastateparks.model.Park
import com.devdcrq.usastateparks.model.SimplePark
import com.devdcrq.usastateparks.util.Equatable

class ParksAdapter : RecyclerView.Adapter<ParksAdapter.ParkViewHolder>() {

    private var onItemClickListener: ((Equatable) -> Unit)? = null

    private val differCallBack = object : DiffUtil.ItemCallback<Equatable>() {
        override fun areItemsTheSame(oldItem: Equatable, newItem: Equatable): Boolean {
            return when {
                oldItem is Park && newItem is Park -> {
                    oldItem.id == newItem.id
                }

                oldItem is SimplePark && newItem is SimplePark -> {
                    oldItem.id == newItem.id
                }

                else -> false
            }
        }

        override fun areContentsTheSame(oldItem: Equatable, newItem: Equatable): Boolean {
            return when {
                oldItem is Park && newItem is Park -> {
                    oldItem == newItem
                }

                oldItem is SimplePark && newItem is SimplePark -> {
                    oldItem == newItem
                }

                else -> false
            }
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)

    fun setOnItemClickListener(listener: (Equatable) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ParkViewHolder(ParkRowBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: ParkViewHolder, position: Int) =
        holder.bind(differ.currentList[position])

    inner class ParkViewHolder(private val binding: ParkRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(park: Equatable) {
            when (park) {
                is Park -> {
                    binding.apply {
                        Glide.with(itemView).load(park.images[0].url).into(ivParkImage)
                        tvParkName.text = park.name
                        tvParkType.text = park.designation
                        tvStateCode.text = park.states
                    }
                    itemView.setOnClickListener{ onItemClickListener?.let { it(park) } }
                }

                is SimplePark -> {
                    binding.apply {
                        Glide.with(itemView).load(park.images[0]).into(ivParkImage)
                        tvParkName.text = park.name
                        tvParkType.text = park.designation
                        tvStateCode.text = park.states
                    }
                    itemView.setOnClickListener{ onItemClickListener?.let { it(park) } }
                }
            }

        }
    }
}