package seedu.taskman.model;

import seedu.taskman.commons.core.UnmodifiableObservableList;
import seedu.taskman.logic.commands.ListCommand;
import seedu.taskman.model.event.Activity;
import seedu.taskman.model.event.Event;
import seedu.taskman.model.event.UniqueActivityList;

import java.util.Set;

/**
 * The API of the Model component.
 */
public interface Model {

    /** Clears existing backing model and replaces with the provided new data. */
    void resetData(ReadOnlyTaskMan newData);

    ReadOnlyTaskMan getTaskMan();

    void deleteActivity(Activity target) throws UniqueActivityList.ActivityNotFoundException;

    void addActivity(Event task) throws UniqueActivityList.DuplicateActivityException;

    void addActivity(Activity activity) throws UniqueActivityList.DuplicateActivityException;

    /**
     * Returns the filtered list of activity as an {@code UnmodifiableObservableList<Activity>}
     */
    UnmodifiableObservableList<Activity> getFilteredActivityList();
    
    /**
     * Returns the filtered list of activities with schedules as an {@code UnmodifiableObservableList<Activity>}
     */
    UnmodifiableObservableList<Activity> getFilteredScheduleList();
    
    /**
     * Returns the filtered task list of tasks with deadlines as an {@code UnmodifiableObservableList<Activity>}
     */
    UnmodifiableObservableList<Activity> getFilteredDeadlineList();
    
    /**
     * Returns the filtered list of tasks without deadlines as an {@code UnmodifiableObservableList<Activity>}
     */
    UnmodifiableObservableList<Activity> getFilteredFloatingList();

    /**
     * Updates the filter of the filtered activity list to show all activities
     */
    void updateFilteredListToShowAll();

    /** Updates the filter of the filtered activity list to filter by the given mode, the given keywords and the given tag names*/
    void updateFilteredActivityList(ListCommand.FilterMode filterMode, Set<String> keywords, Set<String> tagNames);

}
