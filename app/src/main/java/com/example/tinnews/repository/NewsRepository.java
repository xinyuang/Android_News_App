package com.example.tinnews.repository;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tinnews.TinNewsApplication;
import com.example.tinnews.database.AppDatabase;
import com.example.tinnews.model.Article;
import com.example.tinnews.model.NewsResponse;
import com.example.tinnews.network.NewsApi;
import com.example.tinnews.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NewsRepository {
    private final NewsApi newsApi;
    private final AppDatabase database;
    private AsyncTask asyncTask;

    public NewsRepository(Context context) {
        newsApi = RetrofitClient.newInstance(context).create(NewsApi.class);
        database = TinNewsApplication.getDatabase();
    }

    public LiveData<NewsResponse> getTopHeadlines(String country) {
        MutableLiveData<NewsResponse> topHeadLinesLiveData = new MutableLiveData<>();
        newsApi.getTopHeadlines(country)
                .enqueue(new Callback<NewsResponse>() {
                    @Override
                    public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                        if (response.isSuccessful()) {
                            topHeadLinesLiveData.setValue(response.body());
                        } else {
                            topHeadLinesLiveData.setValue(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<NewsResponse> call, Throwable t) {
                        topHeadLinesLiveData.setValue(null);
                    }
                });
        return topHeadLinesLiveData;
    }

    public LiveData<NewsResponse> searchNews(String query) {
        MutableLiveData<NewsResponse> everyThingLiveData = new MutableLiveData<>();
        newsApi.getEverything(query, 40)
                .enqueue(
                        new Callback<NewsResponse>() {
                            @Override
                            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                                if (response.isSuccessful()) {
                                    everyThingLiveData.setValue(response.body());
                                } else {
                                    everyThingLiveData.setValue(null);
                                }
                            }

                            @Override
                            public void onFailure(Call<NewsResponse> call, Throwable t) {
                                everyThingLiveData.setValue(null);
                            }
                        });
        return everyThingLiveData;
    }
    
    
    public LiveData<Boolean> favoriteArticle(Article article) {
        MutableLiveData<Boolean> isSuccessLiveData = new MutableLiveData<>();
        asyncTask =
                    new AsyncTask<Void, Void, Boolean>() {
        @Override
        protected Boolean doInBackground(Void... voids) {
                                    try {
                                            database.dao().saveArticle(article);
                                        } catch (Exception e) {
                                            Log.e("test", e.getMessage());
                                            return false;
                                        }
                                    return true;
                                }

        @Override
        protected void onPostExecute(Boolean isSuccess) {
                                    article.favorite = isSuccess;
                                    isSuccessLiveData.setValue(isSuccess);
                                }
            }.execute();
            return isSuccessLiveData;
        }

        public void onCancel() {
            if (asyncTask != null) {
                    asyncTask.cancel(true);
                }
        }

    
}
