package com.cugocumhurgunay.pokedex.presentation.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.cugocumhurgunay.pokedex.databinding.PokeRowBinding
import com.cugocumhurgunay.pokedex.domain.model.list.PokeListItem
import com.cugocumhurgunay.pokedex.presentation.view.fragment.HomeFragmentDirections
import com.cugocumhurgunay.pokedex.utils.downloadUrl
import com.cugocumhurgunay.pokedex.utils.goTo
import com.cugocumhurgunay.pokedex.utils.placeHolderProgressBar

class PokeAdapter () : RecyclerView.Adapter<PokeAdapter.PokeHolder>(){

    private var pokeList: List<PokeListItem> = listOf()
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

        pokeRow.textViewPokeRowName.text = poke.pokemonName
        pokeRow.textViewPokeRowID.text = "#${"%03d".format(poke.pokemonID)}"
        pokeRow.imageViewPokeRow.downloadUrl(poke.imageUrl, placeHolderProgressBar(pokeRow.imageViewPokeRow.context))

        pokeRow.cardViewPokeRow.setOnClickListener {
            val goTo = HomeFragmentDirections.homeToDetail(pokeId = poke.pokemonID)
            Navigation.goTo(it,goTo)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updatePokeList(item: List<PokeListItem>) {
        item.let {
            pokeList = item
        }
        notifyDataSetChanged()
    }
}
