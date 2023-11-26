package com.cugocumhurgunay.pokedex.domain.model.details


import com.google.gson.annotations.SerializedName

data class GenerationV(
    @SerializedName("black-white")
    val blackWhite: BlackWhite
)