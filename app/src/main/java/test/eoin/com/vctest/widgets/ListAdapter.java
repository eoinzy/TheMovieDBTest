package test.eoin.com.vctest.widgets;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import test.eoin.com.vctest.App;
import test.eoin.com.vctest.R;
import test.eoin.com.vctest.http.ImageDownloader;
import test.eoin.com.vctest.callbacks.MovieClickCallback;
import test.eoin.com.vctest.models.Movie;
import test.eoin.com.vctest.utils.GenreHelper;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private static final String TAG = ListAdapter.class.getSimpleName();

    private List<Movie> results;
    private MovieClickCallback callback;

    static class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout mLayout;

        ViewHolder(RelativeLayout mLayout) {
            super(mLayout);
            this.mLayout = mLayout;
        }
    }

    public ListAdapter(List<Movie> results, MovieClickCallback callback) {
        this.results = results;
        this.callback = callback;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        RelativeLayout mainView = (RelativeLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item, parent, false);
        return new ViewHolder(mainView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Movie movie = results.get(position);

        //Download movie poster first, as it's the slowest
        ImageView moviePoster = holder.mLayout.findViewById(R.id.movie_poster);
        ImageDownloader.downloadImage(App.getAppContext(), moviePoster, movie.getPosterPath());

        TextView movieTitle = holder.mLayout.findViewById(R.id.movie_title);
        TextView movieGenre = holder.mLayout.findViewById(R.id.movie_genre);
        TextView movieSummary = holder.mLayout.findViewById(R.id.movie_summary);
        TextView movieRating = holder.mLayout.findViewById(R.id.movie_rating);
        TextView moviePopularity = holder.mLayout.findViewById(R.id.movie_popularity);

        movieTitle.setText(movie.getTitle());
        movieGenre.setText(GenreHelper.convertGenreIdListToCommaSeparatedString(movie.getGenreIds()));
        movieSummary.setText(movie.getOverview());
        movieRating.setText(App.getAppContext().getString(R.string.rating, movie.getVoteAverage()));
        moviePopularity.setText(App.getAppContext().getString(R.string.popularity, movie.getPopularity()));

        ViewCompat.setTransitionName(moviePoster, "poster");

        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onMovieClick(movie);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return results.size();
    }
}
