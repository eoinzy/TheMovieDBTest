package test.eoin.com.vctest;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;

import java.util.List;

import test.eoin.com.vctest.callbacks.MovieClickCallback;
import test.eoin.com.vctest.callbacks.NetworkRequestCallback;
import test.eoin.com.vctest.data.AppPrefs;
import test.eoin.com.vctest.data.SortBy;
import test.eoin.com.vctest.http.NetworkDownloader;
import test.eoin.com.vctest.models.Movie;
import test.eoin.com.vctest.models.MovieResponse;
import test.eoin.com.vctest.widgets.ListAdapter;

public class MovieListActivity extends AppCompatActivity {

    private final static String TAG = MovieListActivity.class.getSimpleName();
    public static final String EXTRA_MOVIE = "123";
    private MenuItem defaultIcon, faveIcon, sortIcon;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        Log.d(TAG, "onCreate");
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        startMovieDownload();
    }

    private void startMovieDownload() {
        //Get sort order for movies. Either the last selected sort order, or the default
        String sortOrder = AppPrefs.getInstance(this).getLastSortOrder();
        if (null == sortOrder) {
            sortOrder = SortBy.getDefaultSortOrder();
        }
        downloadData(sortOrder);
    }

    private void downloadData(String sortOrder) {
        Log.d(TAG, "Downloading movie data");
        NetworkDownloader.downloadMovieData(this, sortOrder, new NetworkRequestCallback() {
            @Override
            public void onSuccess(MovieResponse movieResponse) {
                showMovieList(movieResponse);
            }

            @Override
            public void onError(MovieResponse movieResponse) {
                Snackbar.make(getWindow().getDecorView().getRootView(), R.string.data_error, Snackbar.LENGTH_LONG).show();
                showMovieList(movieResponse);
            }

            @Override
            public void onFail(MovieResponse movieResponse, Throwable t) {
                Snackbar.make(getWindow().getDecorView().getRootView(), R.string.server_error, Snackbar.LENGTH_LONG).show();
                showMovieList(movieResponse);
            }
        });
    }

    /**
     * Show the list of movies from the provided data
     * @param movieResponse The data containing the movie data to show.
     */
    private void showMovieList(MovieResponse movieResponse) {
        setToolbarText(R.string.title_activity_list);
        showFavouriteIcon(true);
        buildList(movieResponse);
    }

    /**
     * Extract the previously downloaded data and display.
     */
    private void showCachedMovieList() {
        String cacheData = AppPrefs.getInstance(this).getCacheData();
        showMovieList(new Gson().fromJson(cacheData, MovieResponse.class));
    }

    /**
     * Extract favourite movies and display.
     */
    private void showFavouriteList() {
        setToolbarText(R.string.title_activity_fave_list);
        showFavouriteIcon(false);
        buildList(getFavourites());
    }

    private void setToolbarText(int resId) {
        if(null != toolbar) {
            toolbar.setTitle(resId);
        }
    }

    /**
     * Build the list of movies and display on scren
     * @param response The object containing the movie list
     */
    private void buildList(MovieResponse response) {
        if (null != response) {
            Log.d(TAG, "Setting up list");
            //Setup list
            RecyclerView mRecyclerView = findViewById(R.id.main_list_view);
            mRecyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(mLayoutManager);

            // specify an adapter (see also next example)
            RecyclerView.Adapter mAdapter = new ListAdapter(response.getResults(), new MovieClickCallback() {
                @Override
                public void onMovieClick(Movie movie) {
                    Log.d(TAG, "onMovieClick::" + movie.getTitle());
                    launchDetailsScreen(movie);
                }
            });
            mRecyclerView.setAdapter(mAdapter);
        } else {
            Snackbar.make(getWindow().getDecorView().getRootView(), "No movie info found", Snackbar.LENGTH_LONG).show();
        }
    }

    private void launchDetailsScreen(Movie movie) {
        Log.d(TAG, "Launch details screen");
        Intent intent = new Intent(App.getAppContext(), MovieDetailActivity.class);
        intent.putExtra(EXTRA_MOVIE, movie);

        //Decide which views we want to animate/transition with.
        Pair<View, String> p1 = Pair.create(findViewById(R.id.movie_poster), "poster");
        Pair<View, String> p2 = Pair.create(findViewById(R.id.movie_title), "title");
//        Pair<View, String> p3 = Pair.create(findViewById(R.id.movie_genre), "genre");
//        Pair<View, String> p4 = Pair.create(findViewById(R.id.movie_summary), "summary");
//        Pair<View, String> p5 = Pair.create(findViewById(R.id.movie_popularity), "popularity");
        Pair<View, String> p6 = Pair.create(findViewById(R.id.movie_rating), "rating");
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MovieListActivity.this, p1, p2, p6);//p3, p4, p5, p6);

        startActivity(intent, options.toBundle());
    }

    private MovieResponse getFavourites() {
        List<Movie> cachedFavourites = AppPrefs.getInstance(this).getFavourites();
        MovieResponse movieResponse = new MovieResponse();
        movieResponse.setResults(cachedFavourites);
        return movieResponse;
    }

    /**
     * Toggle between showing favourits icon, and default icon.
     *
     * @param show IF true, show favourites icon, else show default icon.
     */
    private void showFavouriteIcon(boolean show) {
        faveIcon.setVisible(show);
        defaultIcon.setVisible(!show);
        sortIcon.setVisible(show);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movie_list, menu);
        defaultIcon = menu.findItem(R.id.action_show_default);
        faveIcon = menu.findItem(R.id.action_show_favourites);
        sortIcon = menu.findItem(R.id.action_sort);
        showFavouriteIcon(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String sortBy = null;
        int id = item.getItemId();
        switch (id) {
            case R.id.action_show_favourites:
                showFavouriteList();
                return true;
            case R.id.action_show_default:
                showCachedMovieList();
                return true;
            case R.id.sortPopularity:
                sortBy = SortBy.POPULARITY_DESC;
                break;
            case R.id.sortRating:
                sortBy = SortBy.VOTE_AVERAGE_DESC;
                break;
            case R.id.sortRevenue:
                sortBy = SortBy.REVENUE_DESC;
                break;
        }

        if (null != sortBy) {
            //Store sort order for next app launch
            AppPrefs.getInstance(this).setLastSortOrder(sortBy);
            downloadData(sortBy);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
