package com.cugocumhurgunay.pokedex.data.retrofit

import com.cugocumhurgunay.pokedex.data.retrofit.service.PokemonAPI
import com.cugocumhurgunay.pokedex.utils.Constants.BASE_URL

class ApiUtils {

    companion object{

        fun getPokemonAPI(): PokemonAPI{
            return RetrofitClient
                .getClient(BASE_URL)
                .create(PokemonAPI::class.java)
        }
    }
}