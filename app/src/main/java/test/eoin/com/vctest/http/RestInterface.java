package test.eoin.com.vctest.http;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import test.eoin.com.vctest.models.MovieResponse;

public interface RestInterface {

    @GET("/3/discover/movie?language=en-US&include_adult=false&include_video=false&page=1")
    Call<MovieResponse> getMovies(@Query("api_key") String apiKey, @Query("sort_by") String sortBy);

}
