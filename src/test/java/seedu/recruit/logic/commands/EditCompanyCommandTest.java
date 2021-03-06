package seedu.recruit.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static seedu.recruit.logic.commands.CommandTestUtil.DESC_ALFA;
import static seedu.recruit.logic.commands.CommandTestUtil.DESC_BMW;
import static seedu.recruit.logic.commands.CommandTestUtil.VALID_EMAIL_ALFA;
import static seedu.recruit.logic.commands.CommandTestUtil.VALID_NAME_ALFA;
import static seedu.recruit.logic.commands.CommandTestUtil.VALID_PHONE_ALFA;
import static seedu.recruit.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.recruit.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.recruit.logic.commands.CommandTestUtil.showCompanyAtIndex;
import static seedu.recruit.testutil.TypicalCompaniesAndJobOffers.getTypicalCompanyBook;
import static seedu.recruit.testutil.TypicalIndexes.INDEX_FIRST;
import static seedu.recruit.testutil.TypicalIndexes.INDEX_SECOND;

import org.junit.Test;

import seedu.recruit.commons.core.Messages;
import seedu.recruit.commons.core.index.Index;
import seedu.recruit.logic.CommandHistory;
import seedu.recruit.logic.commands.EditCompanyCommand.EditCompanyDescriptor;
import seedu.recruit.logic.parser.exceptions.ParseException;
import seedu.recruit.model.CandidateBook;
import seedu.recruit.model.CompanyBook;
import seedu.recruit.model.Model;
import seedu.recruit.model.ModelManager;
import seedu.recruit.model.UserPrefs;
import seedu.recruit.model.company.Company;
import seedu.recruit.testutil.CompanyBuilder;
import seedu.recruit.testutil.EditCompanyDescriptorBuilder;

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand)
 * and unit tests for EditCompanyCommand.
 */

public class EditCompanyCommandTest {

    private Model model = new ModelManager(new CandidateBook(), getTypicalCompanyBook(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();
    private UserPrefs userPrefs = new UserPrefs();

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Company editedCompany = new CompanyBuilder().build();
        EditCompanyDescriptor descriptor = new EditCompanyDescriptorBuilder(editedCompany).build();
        EditCompanyCommand editCompanyCommand = new EditCompanyCommand(INDEX_FIRST, descriptor);

        String expectedMessage = String.format(editCompanyCommand.MESSAGE_EDIT_COMPANY_SUCCESS, editedCompany);

        Model expectedModel = new ModelManager(new CandidateBook(model.getCandidateBook()), model.getCompanyBook(),
                new UserPrefs());
        expectedModel.updateCompany(model.getFilteredCompanyList().get(0), editedCompany);
        expectedModel.commitRecruitBook();

        assertCommandSuccess(editCompanyCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastPerson = Index.fromOneBased(model.getFilteredCompanyList().size());
        Company lastCompany = model.getFilteredCompanyList().get(indexLastPerson.getZeroBased());

        CompanyBuilder companyInList = new CompanyBuilder(lastCompany);
        Company editedCompany = companyInList.withCompanyName(VALID_NAME_ALFA).withPhone(VALID_PHONE_ALFA)
                .withEmail(VALID_EMAIL_ALFA).build();

        EditCompanyDescriptor descriptor = new EditCompanyDescriptorBuilder().withCompanyName(VALID_NAME_ALFA)
                .withPhone(VALID_PHONE_ALFA).withEmail(VALID_EMAIL_ALFA).build();
        EditCompanyCommand editCompanyCommand = new EditCompanyCommand(indexLastPerson, descriptor);

        String expectedMessage = String.format(editCompanyCommand.MESSAGE_EDIT_COMPANY_SUCCESS, editedCompany);

        Model expectedModel = new ModelManager(new CandidateBook(), model.getCompanyBook(),
                new UserPrefs());
        expectedModel.updateCompany(lastCompany, editedCompany);
        expectedModel.commitRecruitBook();

        assertCommandSuccess(editCompanyCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        EditCompanyCommand editCompanyCommand = new EditCompanyCommand(INDEX_FIRST,
                new EditCompanyDescriptor());
        Company editedCompany = model.getFilteredCompanyList().get(INDEX_FIRST.getZeroBased());

        String expectedMessage = String.format(editCompanyCommand.MESSAGE_EDIT_COMPANY_SUCCESS, editedCompany);

        Model expectedModel = new ModelManager(new CandidateBook(model.getCandidateBook()), model.getCompanyBook(),
                new UserPrefs());
        expectedModel.commitRecruitBook();

        assertCommandSuccess(editCompanyCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() throws ParseException {
        showCompanyAtIndex(model, INDEX_FIRST);

        Company companyInFilteredList = model.getFilteredCompanyList().get(INDEX_FIRST.getZeroBased());
        Company editedCompany = new CompanyBuilder(companyInFilteredList).withCompanyName(VALID_NAME_ALFA).build();
        EditCompanyCommand editCompanyCommand = new EditCompanyCommand(INDEX_FIRST,
                new EditCompanyDescriptorBuilder().withCompanyName(VALID_NAME_ALFA).build());

        String expectedMessage = String.format(editCompanyCommand.MESSAGE_EDIT_COMPANY_SUCCESS, editedCompany);

        Model expectedModel = new ModelManager(new CandidateBook(model.getCandidateBook()), model.getCompanyBook(),
                new UserPrefs());
        expectedModel.updateCompany(model.getFilteredCompanyList().get(0), editedCompany);
        expectedModel.commitRecruitBook();

        assertCommandSuccess(editCompanyCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateCompanyFilteredList_failure() {
        Company firstCompany = model.getFilteredCompanyList().get(INDEX_FIRST.getZeroBased());
        EditCompanyDescriptor descriptor = new EditCompanyDescriptorBuilder(firstCompany).build();
        EditCompanyCommand editCompanyCommand = new EditCompanyCommand(INDEX_SECOND, descriptor);

        assertCommandFailure(editCompanyCommand, model, commandHistory,
                editCompanyCommand.MESSAGE_DUPLICATE_COMPANY);
    }

    @Test
    public void execute_duplicateCompanyUnfilteredList_failure() throws ParseException {
        showCompanyAtIndex(model, INDEX_FIRST);

        Company companyInList = model.getCompanyBook().getCompanyList().get(INDEX_SECOND.getZeroBased());
        EditCompanyCommand editCompanyCommand = new EditCompanyCommand(INDEX_FIRST,
                new EditCompanyDescriptorBuilder(companyInList).build());

        assertCommandFailure(editCompanyCommand, model, commandHistory,
                editCompanyCommand.MESSAGE_DUPLICATE_COMPANY);
    }

    @Test
    public void execute_invalidCompanyIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredCompanyList().size() + 1);
        EditCompanyDescriptor descriptor = new EditCompanyDescriptorBuilder().withCompanyName(VALID_NAME_ALFA).build();
        EditCompanyCommand editCompanyCommand = new EditCompanyCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editCompanyCommand, model, commandHistory,
                Messages.MESSAGE_INVALID_COMPANY_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of company book
     */
    @Test
    public void execute_invalidCompanyIndexFilteredList_failure() throws ParseException {
        showCompanyAtIndex(model, INDEX_FIRST);
        Index outOfBoundIndex = INDEX_SECOND;
        // ensures that outOfBoundIndex is still in bounds of company book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getCompanyBook().getCompanyList().size());

        EditCompanyCommand editCompanyCommand = new EditCompanyCommand(outOfBoundIndex,
                new EditCompanyDescriptorBuilder().withCompanyName(VALID_NAME_ALFA).build());

        assertCommandFailure(editCompanyCommand, model, commandHistory,
                Messages.MESSAGE_INVALID_COMPANY_DISPLAYED_INDEX);
    }

    @Test
    public void executeUndoRedo_validIndexUnfilteredList_success() throws Exception {
        Company editedCompany = new CompanyBuilder().build();
        Company companyToEdit = model.getFilteredCompanyList().get(INDEX_FIRST.getZeroBased());
        EditCompanyDescriptor descriptor = new EditCompanyDescriptorBuilder(editedCompany).build();
        EditCompanyCommand editCompanyCommand = new EditCompanyCommand(INDEX_FIRST, descriptor);
        Model expectedModel = new ModelManager(new CandidateBook(), new CompanyBook(model.getCompanyBook()),
                new UserPrefs());
        expectedModel.updateCompany(companyToEdit, editedCompany);
        expectedModel.commitRecruitBook();

        // edit -> first company edited
        editCompanyCommand.execute(model, commandHistory, userPrefs);

        // undo -> reverts Companybook back to previous state and filtered company list to show all companies

        expectedModel.undoRecruitBook();
        assertCommandSuccess(new UndoCommand(), model, commandHistory,
                UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // redo -> same first company edited again
        expectedModel.redoRecruitBook();
        assertCommandSuccess(new RedoCommand(), model, commandHistory,
                RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void executeUndoRedo_invalidIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredCompanyList().size() + 1);
        EditCompanyDescriptor descriptor = new EditCompanyDescriptorBuilder().withCompanyName(VALID_NAME_ALFA).build();
        EditCompanyCommand editCompanyCommand = new EditCompanyCommand(outOfBoundIndex, descriptor);

        // execution failed -> company book state not added into model
        assertCommandFailure(editCompanyCommand, model, commandHistory,
                Messages.MESSAGE_INVALID_COMPANY_DISPLAYED_INDEX);

        // single company book state in model -> undoCommand and redoCommand fail
        assertCommandFailure(new UndoCommand(), model, commandHistory,
                UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(new RedoCommand(), model, commandHistory,
                RedoCommand.MESSAGE_FAILURE);
    }

    /**
     * 1. Edits a {@code Company} from a filtered list.
     * 2. Undo the edit.
     * 3. The unfiltered list should be shown now. Verify that the index of the previously edited company in the
     * unfiltered list is different from the index at the filtered list.
     * 4. Redo the edit. This ensures {@code RedoCommand} edits the company object regardless of indexing.
     */
    @Test
    public void executeUndoRedo_validIndexFilteredList_sameCompanyEdited() throws Exception {
        Company editedCompany = new CompanyBuilder().build();
        EditCompanyDescriptor descriptor = new EditCompanyDescriptorBuilder(editedCompany).build();
        EditCompanyCommand editCompanyCommand = new EditCompanyCommand(INDEX_FIRST, descriptor);
        Model expectedModel = new ModelManager(new CandidateBook(), new CompanyBook(model.getCompanyBook()),
                new UserPrefs());

        showCompanyAtIndex(model, INDEX_SECOND);
        Company companyToEdit = model.getFilteredCompanyList().get(INDEX_FIRST.getZeroBased());
        expectedModel.updateCompany(companyToEdit, editedCompany);
        expectedModel.commitRecruitBook();


        // edit -> edits second company in unfiltered company list / first company in filtered company list
        editCompanyCommand.execute(model, commandHistory, userPrefs);

        // undo -> reverts companybook back to previous state and filtered company list to show all persons
        expectedModel.undoRecruitBook();
        assertCommandSuccess(new UndoCommand(), model, commandHistory,
                UndoCommand.MESSAGE_SUCCESS, expectedModel);

        assertNotEquals(model.getFilteredCompanyList().get(INDEX_FIRST.getZeroBased()), companyToEdit);
        // redo -> edits same second company in unfiltered company list
        expectedModel.redoRecruitBook();
        assertCommandSuccess(new RedoCommand(), model, commandHistory,
                RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void equals() {
        final EditCompanyCommand standardCommand = new EditCompanyCommand(INDEX_FIRST, DESC_ALFA);

        // same values -> returns true
        EditCompanyDescriptor copyDescriptor = new EditCompanyDescriptor(DESC_ALFA);
        EditCompanyCommand commandWithSameValues = new EditCompanyCommand(INDEX_FIRST, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCompanyBookCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditCompanyCommand(INDEX_SECOND, DESC_ALFA)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditCompanyCommand(INDEX_FIRST, DESC_BMW)));
    }

}
