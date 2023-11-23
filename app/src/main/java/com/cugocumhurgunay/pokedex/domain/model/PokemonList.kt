package com.cugocumhurgunay.pokedex.domain.model

data class PokemonList(
    val count: Int,
    val next: String,
    val previous: String,
    val results: List<Result>
)