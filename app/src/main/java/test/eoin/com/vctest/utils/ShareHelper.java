package test.eoin.com.vctest.utils;

import android.content.Context;
import android.content.Intent;

import test.eoin.com.vctest.R;
import test.eoin.com.vctest.models.Movie;

public class ShareHelper {

    /**
     * Method to demonstrate sharing some info about the movie to another app.
     * We just share movie name and a link to the movie on TMDB.
     *
     * @param context Context.
     * @param movie The movie to share.
     */
    public static void shareMovie(Context context, Movie movie) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.share_text, movie.getTitle(), "https://www.themoviedb.org/movie/" + movie.getId()));
        sendIntent.setType("text/plain");
        context.startActivity(sendIntent);
    }
}
