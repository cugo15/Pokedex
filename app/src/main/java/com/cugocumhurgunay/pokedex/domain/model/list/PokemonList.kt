package com.cugocumhurgunay.pokedex.domain.model.list

data class PokemonList(
    val count: Int,
    val next: String,
    val previous: String,
    val results: List<Result>
)