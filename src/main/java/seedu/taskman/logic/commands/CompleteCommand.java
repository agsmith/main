package seedu.taskman.logic.commands;

import javafx.util.Pair;
import seedu.taskman.commons.core.Messages;
import seedu.taskman.commons.core.UnmodifiableObservableList;
import seedu.taskman.commons.exceptions.IllegalValueException;
import seedu.taskman.model.event.Activity;
import seedu.taskman.model.event.Status;
import seedu.taskman.model.event.Task;
import seedu.taskman.model.event.UniqueActivityList;

import java.util.Optional;

import static seedu.taskman.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

/**
 * Completes an existing task
 * 
 * Note: Deeply resembles DeleteCommand (in order to get the Activity reference from the index)
 *       and EditCommand (make a new Activity with the correct status and then deleting the old one)
 */
public class CompleteCommand extends Command {

    public static final String COMMAND_WORD = "complete";
    private static final String STATUS_COMPLETE = "complete";

    // todo: change me
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Marks an existing task as complete./n"
            + "Parameters: INDEX\n"
            + "Example: " + COMMAND_WORD
            + " 1";

    public static final String MESSAGE_SUCCESS = "Task completed: %1$s";
    public static final String MESSAGE_DUPLICATE_TASK = "A task with the same name already exists in TaskMan";

    private Activity.ActivityType activityType;
    private Activity activityToComplete;
    private Activity afterComplete;
    private int targetIndex;
    private Activity.PanelType panelType;

    private CompleteCommand(Activity.PanelType panelType, int targetIndex) {
        super(true);
        this.panelType = panelType;
        this.targetIndex = targetIndex;
    }

    public static Command prepareComplete(String arguments) {
        Optional<Pair<Activity.PanelType, Integer>> panelIndexPair = parsePanelTypeWithIndexOnly(arguments);
        if(!panelIndexPair.isPresent()){
            return new IncorrectCommand(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_USAGE));
        }


        return new CompleteCommand(
                panelIndexPair.get().getKey(),
                panelIndexPair.get().getValue());
    }

    @Override
    public CommandResult execute() {
        assert model != null;

        try {
            initMembers();
        } catch (IllegalValueException e) {
            return new CommandResult(e.getMessage(), false);
        }

        try {
            model.deleteActivity(activityToComplete);
            model.addActivity(afterComplete);
            return new CommandResult(String.format(MESSAGE_SUCCESS, afterComplete.getTitle().title), true);
        } catch (UniqueActivityList.ActivityNotFoundException notFound) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(Messages.MESSAGE_INVALID_EVENT_DISPLAYED_INDEX, false);
        } catch (UniqueActivityList.DuplicateActivityException e) {
            throw new AssertionError("Duplicate activity present, could not add activity back after deleting", null);
        }
    }

    private void initMembers() throws IllegalValueException {
        UnmodifiableObservableList<Activity> lastShownList = model.getActivityListForPanelType(panelType);

        if (lastShownList.size() < targetIndex) {
            indicateAttemptToExecuteIncorrectCommand();
            throw new IllegalValueException(Messages.MESSAGE_INVALID_EVENT_DISPLAYED_INDEX);
        }

        activityToComplete = lastShownList.get(targetIndex - 1);
        activityType = activityToComplete.getType();

        switch (activityType){
            case EVENT: {
                throw new IllegalValueException(Messages.MESSAGE_INVALID_COMMAND_FOR_EVENT);
            }
            case TASK: {
                Task task = new Task(
                        activityToComplete.getTitle(),
                        activityToComplete.getTags(),
                        activityToComplete.getDeadline().orElse(null),
                        activityToComplete.getSchedule().orElse(null),
                        activityToComplete.getFrequency().orElse(null));
                task.setStatus(new Status(STATUS_COMPLETE));
                afterComplete = new Activity(task);
                break;
            }
            default: {
                assert false : "Activity is neither an event nor a task.";
            }
        }
    }
}
