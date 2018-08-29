package test.eoin.com.vctest.callbacks;

import test.eoin.com.vctest.models.MovieResponse;

public interface NetworkRequestCallback {

    void onSuccess(MovieResponse movieResponse);
    void onError(MovieResponse movieResponse);
    void onFail(MovieResponse movieResponse, Throwable t);

}
