package seedu.recruit.model.commons;

import java.util.List;
import java.util.function.Predicate;

import seedu.recruit.commons.util.StringUtil;
import seedu.recruit.model.candidate.Candidate;

/**
 * Tests that a {@code Candidate}'s {@code Phone} matches any of the keywords given.
 */
public class AddressContainsKeywordsPredicate implements Predicate<Candidate> {

    private final List<String> keywords;

    public AddressContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Candidate candidate) {
        return keywords.stream()
                .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(candidate.getAddress().value, keyword));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddressContainsKeywordsPredicate // instanceof handles nulls
                && keywords.equals(((AddressContainsKeywordsPredicate) other).keywords)); // state check
    }
}
