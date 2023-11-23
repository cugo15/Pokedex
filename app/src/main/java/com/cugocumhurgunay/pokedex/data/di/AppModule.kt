package com.cugocumhurgunay.pokedex.data.di

import com.cugocumhurgunay.pokedex.data.repository.PokemonRepositoryImpl
import com.cugocumhurgunay.pokedex.data.retrofit.ApiUtils
import com.cugocumhurgunay.pokedex.data.retrofit.service.PokemonAPI
import com.cugocumhurgunay.pokedex.domain.repository.PokemonRepository
import com.cugocumhurgunay.pokedex.domain.usecase.PokemonUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun providePokemonRepository(useCase: PokemonUseCase) = PokemonRepositoryImpl(useCase) as PokemonRepository

    @Provides
    @Singleton
    fun providePokemonAPI() : PokemonAPI {
        return ApiUtils.getPokemonAPI()
    }
}