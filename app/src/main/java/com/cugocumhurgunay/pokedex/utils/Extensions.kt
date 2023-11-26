package com.cugocumhurgunay.pokedex.utils

import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.cugocumhurgunay.pokedex.R
import com.cugocumhurgunay.pokedex.domain.model.mapper.PokeDetailMapper

fun ImageView.downloadUrl(url: String?, progressDrawable: CircularProgressDrawable){
    val options = RequestOptions()
        .placeholder(progressDrawable)
        .error(R.drawable.close)

    Glide.with(context)
        .setDefaultRequestOptions(options)
        .load(url)
        .into(this)
}

fun placeHolderProgressBar(context: Context): CircularProgressDrawable = CircularProgressDrawable(context)
    .apply {
        strokeWidth = 8f
        centerRadius = 40f
        start()
    }
fun extractPokemonId(url: String): Int? {
    val regex = Regex("/(\\d+)/?$")
    val matchResult = regex.find(url)
    return matchResult?.groupValues?.get(1)?.toIntOrNull()
}

fun getPokemonImageUrl(id: Int): String {
    return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"
}

fun Navigation.goTo(it: View, id: NavDirections){
    findNavController(it).navigate(id)
}

fun Navigation.goTo(it: View, id:Int){
    findNavController(it).navigate(id)
}

fun getPokemColors(typeNamesList: List<String>): List<String> {
    val lowercaseTypes = typeNamesList.map { it.toLowerCase() }
    val typeColorMap = mapOf(
        "bug" to "#A7B723",
        "dark" to "#75574C",
        "dragon" to "#7037FF",
        "electric" to "#F9CF30",
        "fairy" to "#E69EAC",
        "fighting" to "#C12239",
        "fire" to "#F57D31",
        "flying" to "#A891EC",
        "ghost" to "#70559B",
        "normal" to "#AAA67F",
        "grass" to "#74CB48",
        "ground" to "#DEC16B",
        "ice" to "#9AD6DF",
        "poison" to "#A43E9E",
        "psychic" to "#FB5584",
        "rock" to "#B69E31",
        "steel" to "#B7B9D0",
        "water" to "#6493EB"
    )
    return lowercaseTypes.mapNotNull { typeColorMap[it] } // Filter out null values (types not found) and return the list
}




