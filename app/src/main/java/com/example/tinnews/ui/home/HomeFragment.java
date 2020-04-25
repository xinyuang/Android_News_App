package com.example.tinnews.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tinnews.R;
import com.example.tinnews.databinding.FragmentHomeBinding;
import com.example.tinnews.model.Article;
import com.example.tinnews.repository.NewsRepository;
import com.example.tinnews.repository.NewsViewModelFactory;
import com.example.tinnews.ui.search.SearchViewModel;
import com.mindorks.placeholderview.SwipeDecor;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private HomeViewModel viewModel;
    private FragmentHomeBinding binding;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
//        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding
            .swipeView
            .getBuilder()
            .setDisplayViewCount(3)
            .setSwipeDecor(
             new SwipeDecor()
                   .setPaddingTop(20)
                   .setRelativeScale(0.01f));
         
        binding.rejectBtn.setOnClickListener(v -> binding.swipeView.doSwipe(false));
        binding.acceptBtn.setOnClickListener(v -> binding.swipeView.doSwipe(true));
         
        NewsRepository repository = new NewsRepository(getContext());
        viewModel = new ViewModelProvider(this,new NewsViewModelFactory(repository))
                .get(HomeViewModel .class);

        viewModel.setCountryInput("us");
        viewModel.getTopHeadlines()
                .observe(getViewLifecycleOwner(),
                        newsResponse ->
                        {
                            if (newsResponse != null) {
                                Log.d("HomeFragment", newsResponse.toString());
                                for (Article article : newsResponse.articles) {
                                       TinNewsCard tinNewsCard = new TinNewsCard(article);
                                       binding.swipeView.addView(tinNewsCard);
                                    }
                            }
                        });
    }
}
