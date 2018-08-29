package test.eoin.com.vctest;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import test.eoin.com.vctest.utils.GenreHelper;

import static org.junit.Assert.assertEquals;

public class GenreHelperTest {

    @Test
    public void testConvertGenreIdToString() {
        assertEquals(GenreHelper.convertGenreIdToString(28), "Action");
    }

    @Test
    public void testConvertGenreIdListToCommaSeparatedString() {
        List<Integer> genreList = Arrays.asList(28, 12, 16);
        assertEquals(GenreHelper.convertGenreIdListToCommaSeparatedString(genreList), "Action, Adventure, Animation");
    }
}
