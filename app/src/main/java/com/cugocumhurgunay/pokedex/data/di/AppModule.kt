package com.cugocumhurgunay.pokedex.data.di

import com.cugocumhurgunay.pokedex.data.repository.PokemonRepositoryImpl
import com.cugocumhurgunay.pokedex.data.retrofit.ApiUtils
import com.cugocumhurgunay.pokedex.data.retrofit.service.PokemonAPI
import com.cugocumhurgunay.pokedex.domain.repository.PokeRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providePokemonRepository(pokemonAPI: PokemonAPI) : PokeRepo {
        return PokemonRepositoryImpl(pokemonAPI)
    }

    @Provides
    @Singleton
    fun providePokemonAPI() : PokemonAPI {
        return ApiUtils.getPokemonAPI()
    }
}