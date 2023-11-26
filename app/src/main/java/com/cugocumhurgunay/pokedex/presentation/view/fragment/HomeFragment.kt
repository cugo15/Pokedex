package com.cugocumhurgunay.pokedex.presentation.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.PopupWindow
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.cugocumhurgunay.pokedex.R
import com.cugocumhurgunay.pokedex.databinding.FragmentHomeBinding
import com.cugocumhurgunay.pokedex.databinding.PopMenuSortBinding
import com.cugocumhurgunay.pokedex.presentation.view.adapter.PokeAdapter
import com.cugocumhurgunay.pokedex.presentation.viewmodel.HomeViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var rb1Checked = true
    private var rb2Checked = false
    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var pokeAdapter : PokeAdapter = PokeAdapter()
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
        binding.cardViewSort.setOnClickListener {
            showPopupMenu(it)
        }
        binding.rvPoke.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            //Paging
            //To load data with swipe bottom of the recyclerview
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as StaggeredGridLayoutManager
                val lastVisibleItemPositions = layoutManager.findLastCompletelyVisibleItemPositions(null)

                val lastVisibleItemPosition = lastVisibleItemPositions.maxOrNull() ?: 0
                val totalItemCount = layoutManager.itemCount

                // If you can see the last item completely, then load data automatically
                if (lastVisibleItemPosition == totalItemCount - 1) {
                        viewModel.loadData()
                }
            }
        })

        binding.searchViewPoke.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.searchPokemonList(newText)
                if (newText.isNullOrEmpty()){
                    val inputMethodManager =
                        requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(binding.searchViewPoke.windowToken, 0)
                }
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {

                return false
            }
        })


    }

    private fun initView() {
        val layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        binding.rvPoke.layoutManager = layoutManager
        binding.rvPoke.adapter = pokeAdapter
    }
    private fun initListener(){

        viewModel.searchedPokemonList.observe(viewLifecycleOwner){
            pokeAdapter.updatePokeList(it)
            if (rb2Checked) {
                viewModel.orderPokemonListByName()
            }
        }

        viewModel.pokemonList.observe(viewLifecycleOwner){
            pokeAdapter.updatePokeList(it)
            if (rb2Checked) {
                viewModel.orderPokemonListByName()
            }
        }

        viewModel.filtredPokemonList.observe(viewLifecycleOwner){
            pokeAdapter.updatePokeList(it)

        }

        viewModel.mutableIsLoading.observe(viewLifecycleOwner) {
            it?.let {
                if (it){
                    // If data is loading show poke progress ball
                    binding.progressPoke.visibility = View.VISIBLE
                    binding.rvPoke.alpha = 0.2F
                } else {
                    binding.progressPoke.visibility = View.GONE
                    binding.rvPoke.alpha = 1F
                }
            }
        }

        viewModel.mutableError.observe(viewLifecycleOwner) { isError ->
            if (isError == true) {
                val errorMessage = "An unknown error occurred"
                val snackbarDuration = if (viewModel.pokemonList.value.isNullOrEmpty()) {
                    // If there is no item in recyclerView duration must be indefinite
                    // Because user can not trigger loadData() with any other way
                    Snackbar.LENGTH_INDEFINITE
                } else {
                    // If there are items in recyclerView duration must be short
                    // Because user can trigger loadData() if swipe recyclerView again
                    Snackbar.LENGTH_SHORT
                }
                view?.let { it1 ->
                    Snackbar.make(it1, errorMessage, snackbarDuration).apply {
                        setActionTextColor(resources.getColor(R.color.primary))
                        setAction("Retry") {
                            viewModel.loadData()
                        }
                    }.show()
                }
            }
        }

    }
    private fun showPopupMenu(anchorView: View) {
        //Pop up menu to sort data
        val popupView = LayoutInflater.from(anchorView.context).inflate(R.layout.pop_menu_sort, null)
        val bind = PopMenuSortBinding.bind(popupView)
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        bind.rb1.isChecked = rb1Checked
        bind.rb2.isChecked = rb2Checked
        bind.rb1.setOnClickListener {
            //To understand that radio buttons selected
            rb1Checked = true
            rb2Checked = false
            binding.imageViewSort.setImageResource(R.drawable.tag)
            viewModel.orderPokemonListByID()
            Toast.makeText(anchorView.context, "Sorted by Number", Toast.LENGTH_SHORT).show()
            popupWindow.dismiss()
        }

        bind.rb2.setOnClickListener {
            //To understand that radio buttons selected
            rb1Checked = false
            rb2Checked = true
            binding.imageViewSort.setImageResource(R.drawable.sort)
            viewModel.orderPokemonListByName()
            Toast.makeText(anchorView.context, "Sorted by Name", Toast.LENGTH_SHORT).show()
            popupWindow.dismiss()
        }

        popupWindow.showAsDropDown(anchorView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: HomeViewModel by viewModels()
        viewModel = tempViewModel
        // I call data here if i comeback from details fragment I do not want to load data again
        viewModel.loadData()
    }
    override fun onResume() {
        super.onResume()
        val primaryColor = ContextCompat.getColor(requireContext(), R.color.primary)
        //If I comeback from details fragment change status bar color back
        activity?.window?.statusBarColor = primaryColor
    }

}