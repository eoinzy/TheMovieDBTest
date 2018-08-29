package test.eoin.com.vctest;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import test.eoin.com.vctest.http.ImageDownloader;
import test.eoin.com.vctest.models.Movie;
import test.eoin.com.vctest.utils.FavouritesHelper;
import test.eoin.com.vctest.utils.GenreHelper;
import test.eoin.com.vctest.utils.ShareHelper;

public class MovieDetailActivity extends AppCompatActivity {
    private final static String TAG = MovieDetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Log.d(TAG, "onCreate in " + TAG);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Movie movie = (Movie) getIntent().getSerializableExtra(MovieListActivity.EXTRA_MOVIE);

        FloatingActionButton fab = findViewById(R.id.fabFave);
        fab.setImageResource(FavouritesHelper.isFavourite(movie) ? R.drawable.ic_action_favorite_on : R.drawable.ic_action_favorite_off);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleFavouriteClick(movie);
            }
        });

        FloatingActionButton fabShare = findViewById(R.id.fabShare);
        fabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareMovie(movie);
            }
        });

        buildUI(movie);
        setTitle(movie.getTitle());
    }

    private void buildUI(final Movie movie) {
        Log.d(TAG, "Building UI");
        final ImageView poster = findViewById(R.id.movie_poster);
        ImageView backdrop = findViewById(R.id.movie_backdrop);

        //download images first, as they take the longest
        ImageDownloader.downloadImage(this, poster, movie.getPosterPath());
        ImageDownloader.downloadImage(this, backdrop, movie.getBackdropPath());

        final TextView title = findViewById(R.id.movie_title);
        TextView genre = findViewById(R.id.movie_genre);
        TextView summary = findViewById(R.id.movie_summary);
        TextView releaseDate = findViewById(R.id.movie_release_date);
        TextView popularity = findViewById(R.id.movie_popularity);
        TextView rating = findViewById(R.id.movie_rating);
        title.setText(movie.getTitle());
        genre.setText(GenreHelper.convertGenreIdListToCommaSeparatedString(movie.getGenreIds()));
        summary.setText(movie.getOverview());
        releaseDate.setText(movie.getReleaseDate());
        rating.setText(App.getAppContext().getString(R.string.rating, movie.getVoteAverage()));
        popularity.setText(App.getAppContext().getString(R.string.popularity, movie.getPopularity()));

        poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MovieDetailActivity.this, ImageZoomActivity.class);
                intent.putExtra(ImageZoomActivity.EXTRA_IMAGE_URL, movie.getPosterPath());
                intent.putExtra(ImageZoomActivity.EXTRA_SUBTITLE, movie.getTitle());

                Pair<View, String> p1 = Pair.create(findViewById(R.id.movie_poster), "poster");
                Pair<View, String> p2 = Pair.create(findViewById(R.id.movie_title), "title");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MovieDetailActivity.this, p1, p2);

                startActivity(intent, options.toBundle());
            }
        });
    }

    /**
     * Handle clicking of the favourite button.
     * If a movie is already a favourite, it will remove it from favourites.
     * If it is not a favourite, it will be added to the list of favourites.
     * In both cases, it will change the icon to the relevant one for a visual indication of whether
     * its a favourite movie or not.
     *
     * @param movie The movie to evaluate
     */
    private void handleFavouriteClick(final Movie movie) {
        boolean isFave = FavouritesHelper.isFavourite(movie);

        FloatingActionButton fab = findViewById(R.id.fabFave);

        if (isFave) {
            FavouritesHelper.deleteFavourite(movie);
            Snackbar.make(fab, getString(R.string.favourite_remove, movie.getTitle()), Snackbar.LENGTH_SHORT).show();
        } else {
            FavouritesHelper.addFavourite(movie);
            Snackbar.make(fab, getString(R.string.favourite_add, movie.getTitle()), Snackbar.LENGTH_SHORT).show();
        }

        fab.setImageResource(isFave? R.drawable.ic_action_favorite_off : R.drawable.ic_action_favorite_on);
    }

    /**
     * Share movie with another app.
     *
     * @param movie The movie to share.
     */
    private void shareMovie(final Movie movie) {
        ShareHelper.shareMovie(this, movie);
    }

    //TODO: Override onBackButtonPressed and use "finishAfterTransition();" to reverse animations
}
