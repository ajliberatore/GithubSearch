package com.example.githubsearch.search

import android.app.Activity
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.ViewModelProvider
import com.example.githubsearch.databinding.SearchUserFragmentBinding
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class SearchUserFragment : DaggerFragment() {

    @Inject internal lateinit var factory: SearchUserViewModel.Factory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = SearchUserFragmentBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(this, factory).get(SearchUserViewModel::class.java)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        initSearchList(binding, viewModel)

        return binding.root
    }

    private fun initSearchList(binding: SearchUserFragmentBinding, viewModel: SearchUserViewModel) {
        val pagingAdapter = UserAdapter()
        binding.searchResultList.adapter = pagingAdapter
        binding.searchInput.setOnKeyListener { _, keyCode, keyEvent ->
            if ((keyEvent.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                viewModel.searchUser(binding.searchInput.text.toString())
                hideKeyboard()
                true
            } else {
                false
            }
        }

        viewModel.searchResults.observe(viewLifecycleOwner, {
            it?.let {
                pagingAdapter.submitList(it)
            }
        })
    }

    private fun hideKeyboard() {
        val imm: InputMethodManager = requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }
}
