package com.cugocumhurgunay.pokedex.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.cugocumhurgunay.pokedex.MainCoroutineRule
import com.google.common.truth.Truth.assertThat
import com.cugocumhurgunay.pokedex.data.repository.FakePokemonRepo
import com.cugocumhurgunay.pokedex.domain.usecase.GetPokemonListUseCase
import com.cugocumhurgunay.pokedex.getOrAwaitValueTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class HomeViewModelTest {

    @get :Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

       @get:Rule
       var mainCoroutineRule = MainCoroutineRule()




    private lateinit var viewModel : HomeViewModel

    @Before
    @Test
    fun setup(){
        viewModel = HomeViewModel(getPokemonListUseCase = GetPokemonListUseCase(FakePokemonRepo()))

    }

    @Test
    fun `data size`() = runTest{
        viewModel.loadData()
        val value = viewModel.pokemonList.getOrAwaitValueTest()
        assertThat(value.size).isEqualTo(20)
    }

}