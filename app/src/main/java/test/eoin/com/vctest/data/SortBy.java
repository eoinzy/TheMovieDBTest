package test.eoin.com.vctest.data;

/**
 * Class containing constants for all possible
 * sort options
 */
public final class SortBy {

    public static final String POPULARITY_ASC = "popularity.asc";
    public static final String POPULARITY_DESC = "popularity.desc";
    public static final String RELEASE_DATE_ASC = "release_date.asc";
    public static final String RELEASE_DATE_DESC = "release_date.desc";
    public static final String REVENUE_ASC = "revenue.asc";
    public static final String REVENUE_DESC = "revenue.desc";
    public static final String PRIMARY_RELEASE_DATE_ASC = "primary_release_date.asc";
    public static final String PRIMARY_RELEASE_DATE_DESC = "primary_release_date.desc";
    public static final String ORIGINAL_TITLE_ASC = "original_title.asc";
    public static final String ORIGINAL_TITLE_DESC = "original_title.desc";
    public static final String VOTE_AVERAGE_ASC = "vote_average.asc";
    public static final String VOTE_AVERAGE_DESC = "vote_average.desc";
    public static final String VOTE_COUNT_ASC = "vote_count.asc";
    public static final String VOTE_COUNT_DESC = "vote_count.desc";

    /**
     * We've just selected a random sort order by default
     *
     * @return The default order to sort the list of movies.
     */
    public static String getDefaultSortOrder() {
        return POPULARITY_DESC;
    }
}
