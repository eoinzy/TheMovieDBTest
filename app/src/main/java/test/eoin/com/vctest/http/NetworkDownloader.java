package test.eoin.com.vctest.http;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import test.eoin.com.vctest.BuildConfig;
import test.eoin.com.vctest.callbacks.NetworkRequestCallback;
import test.eoin.com.vctest.data.AppPrefs;
import test.eoin.com.vctest.models.MovieResponse;

public class NetworkDownloader {
    private final static String TAG = NetworkDownloader.class.getSimpleName();

    public static void downloadMovieData(final Context context, String sortOrder, final NetworkRequestCallback networkRequestCallback) {
        Call<MovieResponse> call = RestClient.get().getMovies(BuildConfig.API_KEY, sortOrder);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                MovieResponse movieResponse = response.body();
                if (response.isSuccessful() && null != movieResponse) {
                    AppPrefs.getInstance(context).setCacheData(movieResponse);
                    networkRequestCallback.onSuccess(movieResponse);
                } else {
                    Log.e(TAG, "Error getting data:::" + response.code() + ":::" + response.message());
                    networkRequestCallback.onError(getCachedData(context));
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "Error getting data", t);
                networkRequestCallback.onFail(getCachedData(context), t);
            }
        });
    }

    private static MovieResponse getCachedData(Context context) {
        String cacheData = AppPrefs.getInstance(context).getCacheData();
        return new Gson().fromJson(cacheData, MovieResponse.class);
    }
}
