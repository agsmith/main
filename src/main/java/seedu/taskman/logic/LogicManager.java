package seedu.taskman.logic;

import javafx.collections.ObservableList;
import seedu.taskman.commons.core.ComponentManager;
import seedu.taskman.commons.core.LogsCenter;
import seedu.taskman.logic.commands.Command;
import seedu.taskman.logic.commands.CommandResult;
import seedu.taskman.logic.parser.CommandParser;
import seedu.taskman.model.Model;
import seedu.taskman.model.event.Activity;
import seedu.taskman.storage.Storage;

import java.util.logging.Logger;

/**
 * The main LogicManager of the app.
 */
public class LogicManager extends ComponentManager implements Logic {
    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final CommandParser commandParser;
    private final Storage storage;

    public LogicManager(Model model, Storage storage) {
        this.model = model;
        this.commandParser = new CommandParser();
        this.storage = storage;
    }

    @Override
    public CommandResult execute(String commandText) {
        logger.info("----------------[USER COMMAND][" + commandText + "]");
        Command command = commandParser.parseCommand(commandText);
        command.setData(model);
        command.setStorage(storage);
        return command.execute();
    }

    @Override
    public ObservableList<Activity> getFilteredActivityList() {
        return model.getFilteredActivityList();
    }

    @Override
    public ObservableList<Activity> getFilteredScheduleList() {
        return model.getFilteredScheduleList();
    }

    @Override
    public ObservableList<Activity> getFilteredDeadlineList() {
        return model.getFilteredDeadlineList();
    }

    @Override
    public ObservableList<Activity> getFilteredFloatingList() {
        return model.getFilteredFloatingList();
    }
}
