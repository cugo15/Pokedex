package com.cugocumhurgunay.pokedex.domain.repository

import com.cugocumhurgunay.pokedex.domain.model.PokemonList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PokemonRepository {

    @GET("pokemon")
    suspend fun getPokemons(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Response<PokemonList>

}