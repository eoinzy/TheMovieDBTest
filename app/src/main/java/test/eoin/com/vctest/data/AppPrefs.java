package test.eoin.com.vctest.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import test.eoin.com.vctest.models.Movie;
import test.eoin.com.vctest.models.MovieResponse;

/**
 * Class to sit on top of Android system preferences for easier access.
 */
public class AppPrefs {

    public long getCacheDate() {
        return sharedPrefs.getLong(CACHE_DATE, -1);
    }

    public void setCacheDate(final long cacheDate) {
        editor.putLong(CACHE_DATE, cacheDate);
        editor.apply();
    }

    public String getCacheData() {
        return sharedPrefs.getString(CACHE_DATA, null);
    }

    public void setCacheData(final String cacheData) {
        editor.putString(CACHE_DATA, cacheData);
        editor.apply();
        setCacheDate(System.currentTimeMillis());
    }

    public void setCacheData(final MovieResponse movieResponse) {
        String jsonResponse = null;
        if (null != movieResponse) {
            jsonResponse = new Gson().toJson(movieResponse);
        }

        setCacheData(jsonResponse);
        setCacheDate(System.currentTimeMillis());
    }

    public void clearCache() {
        editor.remove(CACHE_DATE);
        editor.remove(CACHE_DATA);
        editor.apply();
    }

    public void saveFavourites(List<Movie> faveMovies) {
        if (null == faveMovies || faveMovies.size() == 0) {
            editor.putString(FAVOURITES, null);
        } else {
            Gson gson = new Gson();
            String jsonFaves = gson.toJson(faveMovies);
            editor.putString(FAVOURITES, jsonFaves);
        }
        editor.commit();
    }

    public void addFavourite(Movie movie) {
        List<Movie> faveMovies = getFavourites();
        if (faveMovies == null) {
            faveMovies = new ArrayList<>();
        }
        faveMovies.add(movie);
        saveFavourites(faveMovies);
    }

    public void removeFavourite(Movie movie) {
        List<Movie> faveMovies = getFavourites();
        if (faveMovies != null) {
            faveMovies.remove(movie);
            saveFavourites(faveMovies);
        }
    }

    public List<Movie> getFavourites() {
        String jsonFaves = sharedPrefs.getString(FAVOURITES, null);
        Movie[] faves = new Gson().fromJson(jsonFaves, Movie[].class);
        if (faves == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(Arrays.asList(faves));
    }

    public void clearFavourites() {
        editor.remove(FAVOURITES);
        editor.apply();
    }

    public void setLastSortOrder(String sortOrder) {
        editor.putString(LAST_SORT_ORDER, sortOrder);
        editor.apply();
    }

    public String getLastSortOrder() {
        return sharedPrefs.getString(LAST_SORT_ORDER, null);
    }

    /******************************************************/
    /******************************************************/
    /******************************************************/
    /*******************PREFERENCE KEYS********************/
    /******************************************************/
    /******************************************************/
    /******************************************************/

    private static final String PREFERENCES_KEY = "PREFERENCES";
    private static final String CACHE_DATE = "CACHE_DATE";
    private static final String CACHE_DATA = "CACHE_DATA";
    private static final String FAVOURITES = "FAVOURITES";
    private static final String LAST_SORT_ORDER = "LAST_SORT_ORDER";

    /******************************************************/
    /******************************************************/
    /******************************************************/
    /*******************STANDARD METHODS*******************/
    /******************************************************/
    /******************************************************/
    /******************************************************/

    private final String TAG = AppPrefs.class.getSimpleName();
    private static AppPrefs mInstance;

    private SharedPreferences sharedPrefs;
    private final SharedPreferences.Editor editor;

    public static AppPrefs getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new AppPrefs(context);
        }
        return mInstance;
    }

    private AppPrefs(Context ctx) {
        sharedPrefs = ctx.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
        //sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        editor = sharedPrefs.edit();
        editor.apply();
    }

    public void clearAllPreferences() {
        editor.clear();
        editor.apply();
    }
}
