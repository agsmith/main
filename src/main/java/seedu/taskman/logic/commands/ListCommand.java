package seedu.taskman.logic.commands;

import seedu.taskman.model.Model;

import java.util.HashSet;
import java.util.Set;

/**
 * Finds and lists all tasks in task man whose title contains any of the argument keywords.
 * Keyword matching is case sensitive.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all tasks whose titles contain any of "
            + "the specified keywords (case-sensitive) and displays them as a list with index numbers.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " alice bob charlie";
    
    public static final String MESSAGE_SUCCESS = "Listed all tasks";

    private final Model.FilterMode filterMode;
    private final Set<String> keywords;
    private final Set<String> tagNames;

    public ListCommand(Set<String> keywords) {
        this.filterMode = Model.FilterMode.TASK_ONLY;
        this.keywords = keywords;
        this.tagNames = new HashSet<>();
    }

    public ListCommand(Model.FilterMode filterMode, Set<String> keywords, Set<String> tags){
        this.filterMode = filterMode;
        this.keywords = keywords;
        this.tagNames = tags;
    }

    @Override
    public CommandResult execute() {
        model.updateFilteredActivityList(filterMode,keywords,tagNames);
        return new CommandResult(getMessageForTaskListShownSummary(model.getFilteredActivityList().size()));
    }

}
