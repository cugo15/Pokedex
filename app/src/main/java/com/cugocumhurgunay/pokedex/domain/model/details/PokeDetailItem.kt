package com.cugocumhurgunay.pokedex.domain.model.details

data class PokeDetailItem (val pokemonID: Int ,
                           val pokemonImageUrl: String,
                           val pokemonName: String,
                           val typeNamesList: List<String>,
                           val pokemonWeight: String,
                           val pokemonHeight: String,
                           val pokemonMoves: String,
                           val pokemonStatsMap: Map<String, String>,
                           val color: List<String>){
}