package seedu.recruit.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.recruit.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.recruit.testutil.TypicalPersons.ALICE;
import static seedu.recruit.testutil.TypicalPersons.BENSON;

import java.nio.file.Paths;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.recruit.logic.parser.exceptions.ParseException;
import seedu.recruit.testutil.RecruitBookBuilder;

public class ModelManagerTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private ModelManager modelManager = new ModelManager();

    // ================================== Candidate Book functions ====================================== //

    @Test
    public void hasCandidate_nullCandidate_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        modelManager.hasCandidate(null);
    }

    @Test
    public void hasCandidate_candidateNotInCandidateBook_returnsFalse() {
        assertFalse(modelManager.hasCandidate(ALICE));
    }

    @Test
    public void hasCandidate_candidateInCandidateBook_returnsTrue() {
        modelManager.addCandidate(ALICE);
        assertTrue(modelManager.hasCandidate(ALICE));
    }

    @Test
    public void getFilteredCandidateList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        modelManager.getFilteredCandidateList().remove(0);
    }

    @Test
    public void equals() throws ParseException {
        CandidateBook candidateBook = new
                RecruitBookBuilder().withCandidate(ALICE).withCandidate(BENSON).buildCandidateBook();
        CandidateBook differentCandidateBook = new CandidateBook();
        UserPrefs userPrefs = new UserPrefs();

        // same values -> returns true
        modelManager = new ModelManager(candidateBook, new CompanyBook(), userPrefs);
        ModelManager modelManagerCopy = new ModelManager(candidateBook, new CompanyBook(), userPrefs);
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // null -> returns false
        assertFalse(modelManager.equals(null));

        // different types -> returns false
        assertFalse(modelManager.equals(5));

        // different candidateBook -> returns false
        assertFalse(modelManager.equals(new ModelManager(differentCandidateBook, new CompanyBook(), userPrefs)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredCandidateList(PREDICATE_SHOW_ALL_PERSONS);

        // different userPrefs -> returns true
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setCandidateBookFilePath(Paths.get("differentFilePath"));
        assertTrue(modelManager.equals(new ModelManager(candidateBook, new CompanyBook(), differentUserPrefs)));
    }

    // ================================== Shortlist functions ====================================== //

    @Test
    public void shortlistCandidateToJobOffer_candidateSuccessfullyShortlisted_returnsTrue() {

        //modelManager.shortlistCandidateToJobOffer(ALICE,);
    }

    @Test
    public void shortlistCandidateToJobOffer_candidateAlreadyShortlisted_returnsFalse() {

        //modelManager.shortlistCandidateToJobOffer(ALICE,);
    }

    @Test
    public void deleteShortlistedCandidateFromJobOffer_noCandidatesInShortlistedCandidateList_returnsFalse() {

    }

    @Test
    public void deleteShortlistedCandidateFromJobOffer_candidateInShortlistedCandidateList_returnsTrue() {

    }
}
