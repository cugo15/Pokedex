package com.cugocumhurgunay.pokedex.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.cugocumhurgunay.pokedex.MainCoroutineRule
import com.cugocumhurgunay.pokedex.data.repository.FakePokemonRepo
import com.cugocumhurgunay.pokedex.domain.usecase.GetPokeSpeciesUseCase
import com.cugocumhurgunay.pokedex.domain.usecase.GetPokemonDetailsUseCase
import com.cugocumhurgunay.pokedex.getOrAwaitValueTest
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class PokeDetailViewModelTest {

    @get :Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()




    private lateinit var viewModel : PokeDetailViewModel

    @Before
    @Test
    fun setup(){
        viewModel = PokeDetailViewModel(
            getPokemonDetailsUseCase = GetPokemonDetailsUseCase(FakePokemonRepo()),
            getPokeSpeciesUseCase = GetPokeSpeciesUseCase(FakePokemonRepo())
        )

    }

    @Test
    fun `id check`() = runTest{
        viewModel.loadData(1)
        val value = viewModel.pokemonDetail.getOrAwaitValueTest()
        if (value != null) {
            Truth.assertThat(value.pokemonID).isEqualTo(1)
        }
    }

    @Test
    fun `check if poke has more than one type`() = runTest{
        viewModel.loadData(1)
        val value = viewModel.pokemonDetail.getOrAwaitValueTest()
        if (value != null) {
            Truth.assertThat(value.typeNamesList.size).isGreaterThan(1)
        }
    }
    @Test
    fun `check that all the stat come successfully`() = runTest{
        viewModel.loadData(1)
        val value = viewModel.pokemonDetail.getOrAwaitValueTest()
        if (value != null) {
            Truth.assertThat(value.pokemonStatsMap.keys.size).isEqualTo(6)
        }
    }

    @Test
    fun `check the color`() = runTest{
        viewModel.loadData(1)
        val value = viewModel.pokemonDetail.getOrAwaitValueTest()
        if (value != null) {
            Truth.assertThat(value.color[0]).isEqualTo("Green")
        }
    }

    @Test
    fun `check number of color `() = runTest{
        viewModel.loadData(1)
        val value = viewModel.pokemonDetail.getOrAwaitValueTest()
        if (value != null) {
            Truth.assertThat(value.color.size).isGreaterThan(1)
        }
    }
    @Test
    fun `check first letter uppercase `() = runTest{
        viewModel.loadData(1)
        val value = viewModel.pokemonDetail.getOrAwaitValueTest()
        if (value != null) {
            Truth.assertThat(value.pokemonName[0].isUpperCase())
        }
    }

    @Test
    fun `check if flavor text empty`() = runTest{
        viewModel.loadSpecies(1)
        val value = viewModel.pokemonFlavorText.getOrAwaitValueTest()
        if (value != null) {
            Truth.assertThat(value).isEqualTo("")
        }
    }

}