package com.cugocumhurgunay.pokedex.data.di

import com.cugocumhurgunay.pokedex.data.retrofit.ApiUtils
import com.cugocumhurgunay.pokedex.data.retrofit.service.PokemonAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {


    @Provides
    @Singleton
    fun providePokemonAPI() : PokemonAPI {
        return ApiUtils.getPokemonAPI()
    }

}