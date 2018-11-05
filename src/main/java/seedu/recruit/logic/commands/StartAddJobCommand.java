package seedu.recruit.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.recruit.commons.core.EventsCenter;
import seedu.recruit.commons.events.ui.ShowCompanyBookRequestEvent;
import seedu.recruit.logic.CommandHistory;
import seedu.recruit.logic.LogicManager;
import seedu.recruit.logic.commands.exceptions.CommandException;
import seedu.recruit.model.Model;

/**
 *  Starts the add job offer interface
 *  Users can continue adding jobs until in this interface until user inputs 'cancel'
 */

public class StartAddJobCommand extends Command {
    public static final String COMMAND_WORD = "addj";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Starts the add job offer interface. ";

    public StartAddJobCommand() {

    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        EventsCenter.getInstance().post(new ShowCompanyBookRequestEvent());

        LogicManager.setLogicState("Add Job Interface");
        return new CommandResult(AddJobDetailsCommand.MESSAGE_USAGE);
    }



}
