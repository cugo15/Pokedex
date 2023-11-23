package com.cugocumhurgunay.pokedex.domain.usecase

import com.cugocumhurgunay.pokedex.data.retrofit.service.PokemonAPI
import javax.inject.Inject

class PokemonUseCase @Inject constructor (val api : PokemonAPI) {

}