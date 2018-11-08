package seedu.recruit.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.recruit.model.Model.PREDICATE_SHOW_ALL_COMPANIES;

import seedu.recruit.commons.core.EventsCenter;
import seedu.recruit.commons.events.ui.FocusOnCompanyBookRequestEvent;
import seedu.recruit.commons.events.ui.ShowUpdatedCompanyJobListRequestEvent;
import seedu.recruit.logic.CommandHistory;
import seedu.recruit.logic.commands.exceptions.CommandException;
import seedu.recruit.model.Model;
import seedu.recruit.model.UserPrefs;

/**
 * Reverts the {@code model}'s CompanyBook to its previous state.
 */

public class UndoCompanyBookCommand extends Command {

    public static final String COMMAND_WORD = "undoC";
    public static final String MESSAGE_SUCCESS = "Undo success!";
    public static final String MESSAGE_FAILURE = "No more CompanyBook commands to undo!";

    @Override
    public CommandResult execute(Model model, CommandHistory history, UserPrefs userPrefs) throws CommandException {
        requireNonNull(model);
        EventsCenter.getInstance().post(new FocusOnCompanyBookRequestEvent());

        if (!model.canUndoCompanyBook()) {
            throw new CommandException(MESSAGE_FAILURE);
        }

        model.undoCompanyBook();
        model.updateFilteredCompanyList(PREDICATE_SHOW_ALL_COMPANIES);
        EventsCenter.getInstance().post(new ShowUpdatedCompanyJobListRequestEvent(
                model.getFilteredCompanyJobList().size()
        ));
        return new CommandResult(MESSAGE_SUCCESS);
    }

}