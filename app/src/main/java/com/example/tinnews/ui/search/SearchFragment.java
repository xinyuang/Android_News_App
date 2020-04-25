package com.example.tinnews.ui.search;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import com.example.tinnews.R;
import com.example.tinnews.databinding.FragmentSearchBinding;
import com.example.tinnews.repository.NewsRepository;
import com.example.tinnews.repository.NewsViewModelFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {
    private SearchViewModel viewModel;
    private FragmentSearchBinding binding;
    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_search, container, false);
           binding = FragmentSearchBinding.inflate(inflater, container, false);
           return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
         
        binding.searchView.setOnEditorActionListener(
                        (v, actionId, event) -> {
                                String searchText = binding.searchView.getText().toString();
                                 if (actionId == EditorInfo.IME_ACTION_DONE && !searchText.isEmpty()) {
                                       viewModel.setSearchInput(searchText);
                                         return true;
                                       } else {
                                        return false;
                                       }
                            });
         
                
        NewsRepository repository = new NewsRepository(getContext());
        viewModel = new ViewModelProvider(this, new NewsViewModelFactory(repository))
                .get(SearchViewModel.class);
//        viewModel.setSearchInput("Covid-19");
        viewModel
                .searchNews()
                .observe(
                        getViewLifecycleOwner(),
                        newsResponse -> {
                            if (newsResponse != null) {
                                Log.d("SearchFragment", newsResponse.toString());
                            }
                        });
    }
}
