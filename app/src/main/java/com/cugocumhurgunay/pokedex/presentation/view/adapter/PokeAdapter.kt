package com.cugocumhurgunay.pokedex.presentation.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cugocumhurgunay.pokedex.databinding.PokeRowBinding

class PokeAdapter () : RecyclerView.Adapter<PokeAdapter.PokeHolder>(){

    private var pokeList: List<String> = listOf()
    inner class PokeHolder(val binding: PokeRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokeHolder {
        val itemBinding = PokeRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PokeHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return pokeList.size
    }

    override fun onBindViewHolder(holder: PokeHolder, position: Int) {
        val poke = pokeList[position]
        val pokeRow = holder.binding
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updatePokeList(item: List<String>) {
        item.let {
            pokeList = item
        }
        notifyDataSetChanged()
    }
}