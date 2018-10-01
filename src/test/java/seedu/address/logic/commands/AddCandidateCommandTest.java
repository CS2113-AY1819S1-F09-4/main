package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.collections.ObservableList;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.CandidateBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyCandidateBook;
import seedu.address.model.candidate.Candidate;
import seedu.address.testutil.PersonBuilder;

public class AddCandidateCommandTest {

    private static final CommandHistory EMPTY_COMMAND_HISTORY = new CommandHistory();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void constructor_nullPerson_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new AddCandidateCommand(null);
    }

    @Test
    public void execute_personAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded();
        Candidate validCandidate = new PersonBuilder().build();

        CommandResult commandResult = new AddCandidateCommand(validCandidate).execute(modelStub, commandHistory);

        assertEquals(String.format(AddCandidateCommand.MESSAGE_SUCCESS, validCandidate), commandResult.feedbackToUser);
        assertEquals(Arrays.asList(validCandidate), modelStub.personsAdded);
        assertEquals(EMPTY_COMMAND_HISTORY, commandHistory);
    }

    @Test
    public void execute_duplicatePerson_throwsCommandException() throws Exception {
        Candidate validCandidate = new PersonBuilder().build();
        AddCandidateCommand addCommand = new AddCandidateCommand(validCandidate);
        ModelStub modelStub = new ModelStubWithPerson(validCandidate);

        thrown.expect(CommandException.class);
        thrown.expectMessage(AddCandidateCommand.MESSAGE_DUPLICATE_PERSON);
        addCommand.execute(modelStub, commandHistory);
    }

    @Test
    public void equals() {
        Candidate alice = new PersonBuilder().withName("Alice").build();
        Candidate bob = new PersonBuilder().withName("Bob").build();
        AddCandidateCommand addAliceCommand = new AddCandidateCommand(alice);
        AddCandidateCommand addBobCommand = new AddCandidateCommand(bob);

        // same object -> returns true
        assertTrue(addAliceCommand.equals(addAliceCommand));

        // same values -> returns true
        AddCandidateCommand addAliceCommandCopy = new AddCandidateCommand(alice);
        assertTrue(addAliceCommand.equals(addAliceCommandCopy));

        // different types -> returns false
        assertFalse(addAliceCommand.equals(1));

        // null -> returns false
        assertFalse(addAliceCommand.equals(null));

        // different candidate -> returns false
        assertFalse(addAliceCommand.equals(addBobCommand));
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void addPerson(Candidate candidate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void resetData(ReadOnlyCandidateBook newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyCandidateBook getAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasPerson(Candidate candidate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deletePerson(Candidate target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updatePerson(Candidate target, Candidate editedCandidate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Candidate> getFilteredPersonList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredPersonList(Predicate<Candidate> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean canUndoAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean canRedoAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void undoAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void redoAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void commitAddressBook() {
            throw new AssertionError("This method should not be called.");
        }
    }

    /**
     * A Model stub that contains a single candidate.
     */
    private class ModelStubWithPerson extends ModelStub {
        private final Candidate candidate;

        ModelStubWithPerson(Candidate candidate) {
            requireNonNull(candidate);
            this.candidate = candidate;
        }

        @Override
        public boolean hasPerson(Candidate candidate) {
            requireNonNull(candidate);
            return this.candidate.isSamePerson(candidate);
        }
    }

    /**
     * A Model stub that always accept the candidate being added.
     */
    private class ModelStubAcceptingPersonAdded extends ModelStub {
        final ArrayList<Candidate> personsAdded = new ArrayList<>();

        @Override
        public boolean hasPerson(Candidate candidate) {
            requireNonNull(candidate);
            return personsAdded.stream().anyMatch(candidate::isSamePerson);
        }

        @Override
        public void addPerson(Candidate candidate) {
            requireNonNull(candidate);
            personsAdded.add(candidate);
        }

        @Override
        public void commitAddressBook() {
            // called by {@code AddCandidateCommand#execute()}
        }

        @Override
        public ReadOnlyCandidateBook getAddressBook() {
            return new CandidateBook();
        }
    }

}
