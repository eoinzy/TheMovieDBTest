package test.eoin.com.vctest.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenreHelper {

    private static final Map<Integer, String> genreMap = new HashMap<Integer, String>() {{
        put(28, "Action");
        put(12, "Adventure");
        put(16, "Animation");
        put(35, "Comedy");
        put(80, "Crime");
        put(99, "Documentary");
        put(18, "Drama");
        put(10751, "Family");
        put(14, "Fantasy");
        put(36, "History");
        put(27, "Horror");
        put(10402, "Music");
        put(9648, "Mystery");
        put(10749, "Romance");
        put(878, "Science Fiction");
        put(10770, "TV Movie");
        put(53, "Thriller");
        put(10752, "War");
        put(37, "Western");
    }};

    public static String convertGenreIdToString(final int genreId) {
        return resolveId(genreId);
    }

    public static String convertGenreIdListToCommaSeparatedString(final List<Integer> genreIdArray) {
        String separator = ", ";
        StringBuilder strGenreBuilder = new StringBuilder();
        for (Integer id : genreIdArray) {
            strGenreBuilder.append(convertGenreIdToString(id)).append(separator);
        }

        //Remove last occurrence of comma separator
        int index = strGenreBuilder.lastIndexOf(separator);
        strGenreBuilder.replace(index, strGenreBuilder.length() + index, separator);

        //Return comma separated string of genres (and remove trailing comma+space)
        return strGenreBuilder.toString().replaceAll(separator+"$", "");
    }

    /**
     * Maps a genre ID to a genre text value
     *
     * @param genreId The genre ID to resolve.
     * @return The genre in plain text, or N/A if genre ID cannot be resolved.
     */
    private static String resolveId(int genreId) {
        String genre = genreMap.get(genreId);
        if (null == genre) {
            genre = "N/A";
        }
        return genre;
    }
}
