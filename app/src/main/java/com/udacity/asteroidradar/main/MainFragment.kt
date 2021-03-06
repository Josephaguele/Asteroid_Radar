package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.AsteroidGridAdapter
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        // Sets the adapter of the photosGrid RecyclerView
        binding.asteroidRecycler.adapter = AsteroidGridAdapter(AsteroidGridAdapter.josephOnClickListener
        {
            // add a click listener that passes the selected property to viewModel.displayAsteroidDetails()
            viewModel.displayAsteroidDetails(it)
        })

        // Here, we add an observer on navigateToSelectedAsteroid that calls navigate() to go to the
        // details screen when the Asteroid is not null.
        // and then we don't forget to call displayAsteroidDetailsComplete() when the navigation is done
        viewModel.navigateToSelectedProperty.observe(viewLifecycleOwner, Observer
        {
            if(null != it)
            {
                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                viewModel.displayAsteroidDetailsComplete()
            }
        })


        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }
}
