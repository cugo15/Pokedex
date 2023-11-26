package com.cugocumhurgunay.pokedex.data.retrofit.service

import com.cugocumhurgunay.pokedex.domain.model.details.PokeDetail
import com.cugocumhurgunay.pokedex.domain.model.details.species.PokeSpecies
import com.cugocumhurgunay.pokedex.domain.model.list.PokemonList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonAPI {
    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Response<PokemonList>

  @GET("pokemon/{id}")
    suspend fun getPokemonDetail(
        @Path("id") id: Int
    ): Response<PokeDetail>

    @GET("pokemon-species/{id}")
    suspend fun getPokemonSpecies(
        @Path("id") id: Int
    ): Response<PokeSpecies>
}