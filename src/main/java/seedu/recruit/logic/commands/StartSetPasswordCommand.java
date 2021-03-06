package seedu.recruit.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.recruit.commons.core.EventsCenter;
import seedu.recruit.commons.events.logic.ChangeLogicStateEvent;
import seedu.recruit.logic.CommandHistory;

import seedu.recruit.logic.commands.exceptions.CommandException;
import seedu.recruit.model.Model;
import seedu.recruit.model.UserPrefs;

/**
 * Starts the Set Password Interface
 */

public class StartSetPasswordCommand extends Command {

    public static final String COMMAND_WORD = "setPW";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Starts the Set Password Interface ";

    public StartSetPasswordCommand() {

    }

    @Override
    public CommandResult execute(Model model, CommandHistory history, UserPrefs userPrefs) throws CommandException {
        requireNonNull(model);
        EventsCenter.getInstance().post(new ChangeLogicStateEvent(SetPasswordCommand.COMMAND_WORD));

        return new CommandResult(SetPasswordCommand.MESSAGE_USAGE);
    }
}
