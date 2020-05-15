package stroom.core.query;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class TestSuggestionsServiceImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestSuggestionsServiceImpl.class);

    @TestFactory
    List<DynamicTest> fuzzyMatcherTestFactory() {
        return List.of(

                makeDynamicTest("Starts with",
                        "^this_",
                        List.of("THIS_IS_MY_FEED", "this_is_my_feed", "THIS_IS_MY_FEED_TOO", "this_is_my_feed_too"),
                        List.of("NOT_THIS_IS_MY_FEED")),

                makeDynamicTest("Ends with",
                        "feed$",
                        List.of("THIS_IS_MY_FEED", "this_is_my_feed", "SO_IS_THIS_IS_MY_FEED", "so_is_this_is_my_feed"),
                        List.of("THIS_IS_MY_FEED_NOT")),

                makeDynamicTest("Exact match",
                        "^this_is_my_feed$",
                        List.of("THIS_IS_MY_FEED", "this_is_my_feed"),
                        List.of("NOT_THIS_IS_MY_FEED", "NOT_THIS_IS_MY_FEED_NOT", "THIS_IS_MY_FEED_NOT")),

                makeDynamicTest("Chars anywhere",
                        "timf",
                        List.of("THIS_IS_MY_FEED", "this_is_my_feed", "SO_IS_THIS_IS_MY_FEED", "timf", "TIMF"),
                        List.of("NOT_THIS_IS_MY_XEED", "fmit", "FMIT"))

                // TODO migrate cases from below into here
        );
    }


    @Test
    void fuzzyMatcher() {
        // Ones that match
        doFuzzyMatchTest("^this_", "THIS_IS_MY_FEED", true);

        doFuzzyMatchTest("^this_",
                List.of("THIS_IS_MY_FEED", "this_is_my_feed"),
                List.of("NOT_THIS_IS_MY_FEED"));

        doFuzzyMatchTest("^this_is_my_feed$",
                List.of("THIS_IS_MY_FEED", "this_is_my_feed"),
                List.of("NOT_THIS_IS_MY_FEED", "NOT_THIS_IS_MY_FEED_NOT", "THIS_IS_MY_FEED_NOT"));

        doFuzzyMatchTest("^this_is_my_feed$", "THIS_IS_MY_FEED", true);
        doFuzzyMatchTest("feed$", "THIS_IS_MY_FEED", true);

        doFuzzyMatchTest("THIS_", "THIS_IS_MY_FEED", true);
        doFuzzyMatchTest("MY_FEED", "THIS_IS_MY_FEED", true);
        doFuzzyMatchTest("T_I_M_F", "THIS_IS_MY_FEED", true);
        doFuzzyMatchTest("T_I_M_F", "THIS_IS_MF_FEED", true);
        doFuzzyMatchTest("TH_IS_MY_FE", "THIS_IS_MY_FEED", true);
        doFuzzyMatchTest("T___F", "THIS_IS_MY_FEED", true);
        doFuzzyMatchTest("TIMF", "THIS_IS_MY_FEED", true);
        doFuzzyMatchTest("TIISMYFE", "THIS_IS_MY_FEED", true);

        doFuzzyMatchTest("99", "THIS_IS_MY_99_FEED", true);
        doFuzzyMatchTest("_99_", "THIS_IS_MY_99_FEED", true);
        doFuzzyMatchTest("99_FEED", "THIS_IS_MY_99_FEED", true);
        doFuzzyMatchTest("T_I_M_99_F", "THIS_IS_MY_99_FEED", true);

        // Ones that don't
        doFuzzyMatchTest("T_X__F", "THIS_IS_MY_FEED", false);
    }

    private void doFuzzyMatchTest(final String input, final String text, final boolean shouldMatch) {
        LOGGER.info("-----------------------------------------------------");
        LOGGER.debug("Testing input: {} against text: {}, shouldMatch: {}", input, text, shouldMatch);
//        long startNs = System.nanoTime();
//        int runCount = 20;
//        for (int i = 0; i < 20; i++) {
//            SuggestionsServiceImpl.createMatchPredicate(input).test(text);
//        }
//        long endNs = System.nanoTime();
//        LOGGER.debug("Completed in {} micro secs", (endNs - startNs) / 1000 / runCount);

        boolean actualResult = SuggestionsServiceImpl.createFuzzyMatchPredicate(input).test(text);
        Assertions.assertThat(actualResult).isEqualTo(shouldMatch);
        LOGGER.info("-----------------------------------------------------");
    }

    private void doFuzzyMatchTest(final String userInput,
                                  final List<String> expectedMatches,
                                  final List<String> expectedNonMatches) {


        final List<String> actualMatches = Stream.concat(expectedMatches.stream(),
                expectedNonMatches.stream())
                .filter(SuggestionsServiceImpl.createFuzzyMatchPredicate(userInput))
                .collect(Collectors.toList());

        Assertions.assertThat(actualMatches)
                .containsExactlyInAnyOrderElementsOf(expectedMatches);
    }

    private DynamicTest makeDynamicTest(final String testName,
                                        final String userInput,
                                        final List<String> expectedMatches,
                                        final List<String> expectedNonMatches) {
        return DynamicTest.dynamicTest(testName, () ->
                doFuzzyMatchTest(userInput, expectedMatches, expectedNonMatches));
    }
}