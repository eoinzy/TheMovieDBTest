package test.eoin.com.vctest.http;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import test.eoin.com.vctest.R;

public class ImageDownloader {
    private static final String BASE_URL = "https://image.tmdb.org/t/p/w500"; //poster size is 500px

    /**
     * Downloads an image from the given URL and set it as the drawable in the provided imageView
     *
     * @param mContext    Current context
     * @param imageView   The view in which to set the image.
     * @param imgShortUrl The path of the image, without the base URL/domain.
     */
    public static void downloadImage(Context mContext, ImageView imageView, String imgShortUrl) {
        Picasso.with(mContext)
                .load(BASE_URL + imgShortUrl)
                .placeholder(R.drawable.placeholder)
                .into(imageView);
    }
}