package test.eoin.com.vctest.utils;

import android.util.Log;

import java.util.List;

import test.eoin.com.vctest.App;
import test.eoin.com.vctest.data.AppPrefs;
import test.eoin.com.vctest.models.Movie;

public class FavouritesHelper {
    private final static String TAG = FavouritesHelper.class.getSimpleName();

    public static void addFavourite(Movie movie) {
        AppPrefs.getInstance(App.getAppContext()).addFavourite(movie);
    }

    public static void deleteFavourite(Movie movie) {
        AppPrefs.getInstance(App.getAppContext()).removeFavourite(movie);
    }

    public static boolean isFavourite(Movie movie) {
        List<Movie> faveMovies = AppPrefs.getInstance(App.getAppContext()).getFavourites();
        boolean isFave = faveMovies.contains(movie);
        Log.d(TAG, movie.getTitle() + " is face? " + isFave);
        return isFave;//faveMovies.contains(movie);
    }

    public static void clearFavourites() {
        AppPrefs.getInstance(App.getAppContext()).saveFavourites(null);
    }
}
