package seedu.taskman.model.event;

import seedu.taskman.model.tag.UniqueTagList;

import java.util.Objects;
import java.util.Optional;

/**
 * Wrapper for both Event and Task
 */
public class Activity implements ReadOnlyEvent, MutableTagsEvent {

    private static final String DEADLINE_STRING = "d";
    private static final String SCHEDULE_STRING = "s";
    private static final String FLOATING_STRING = "f";

    public enum ActivityType {EVENT, TASK}

    private MutableTagsEvent activity;
    private ActivityType type;

    public Activity(Event event){
        if (event.getClass().getName().equals(Task.class.getName())) {
            activity = event;
            type = ActivityType.TASK;
        } else {
            activity = event;
            type = ActivityType.EVENT;
        }
    }

    public Activity(Task task) {
        activity = task;
        type = ActivityType.TASK;
    }

    public Activity(Activity source) {
        switch (source.getType()) {
            case TASK: {
                this.activity = new Task((ReadOnlyTask) source.activity);
                break;
            }
            case EVENT: {
                this.activity = new Event((ReadOnlyEvent) source.activity);
                break;
            }
            default: {
                this.activity = new Event((ReadOnlyEvent) source.activity);
            }
        }
        type = source.getType();
    }

    public ActivityType getType() {
        return type;
    }

    public Optional<ReadOnlyTask> getTask() {
        if (type != ActivityType.TASK) {
            return Optional.empty();
        }
        return Optional.of((ReadOnlyTask) activity);
    }

    public Optional<ReadOnlyEvent> getEvent(){
        if(type != ActivityType.EVENT){
            return Optional.empty();
        }
        return Optional.of((ReadOnlyEvent) activity);
    }

    public Optional<Status> getStatus() {
        switch (type) {
            case TASK: {
                return Optional.ofNullable(((ReadOnlyTask) activity).getStatus());
            }
            case EVENT: {
                return Optional.empty();
            }
            default: {
                return Optional.empty();
            }
        }
    }

    public Optional<Deadline> getDeadline() {
        switch (type) {
            case TASK: {
                return ((ReadOnlyTask) activity).getDeadline();
            }
            case EVENT: {
                return Optional.empty();
            }
            default: {
                return Optional.empty();
            }
        }
    }

    @Override
    public Title getTitle() {
        return activity.getTitle();
    }

    @Override
    public Optional<Frequency> getFrequency() {
        return activity.getFrequency();
    }

    @Override
    public Optional<Schedule> getSchedule() {
        return activity.getSchedule();
    }

    @Override
    public void setTags(UniqueTagList replacement) {
        activity.setTags(replacement);
    }

    @Override
    public UniqueTagList getTags() {
        return activity.getTags();
    }

    public boolean isSameStateAs(Activity other) {
        if (type != other.type) return false;

        switch (type) {
            case TASK: {
                return ((ReadOnlyTask) activity).isSameStateAs((ReadOnlyTask) other.activity);
            }
            case EVENT: {
                return ((ReadOnlyEvent) activity).isSameStateAs((ReadOnlyEvent) other.activity);
            }
            default: {
                return activity.isSameStateAs(other.activity);
            }
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Activity // instanceof handles nulls
                && this.isSameStateAs((Activity) other));
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(
                getTitle(),
                getDeadline(),
                getStatus(),
                getFrequency(),
                getSchedule(),
                getTags()
        );
    }

    @Override
    public String getAsText() {
        return activity.getAsText();
    }

    @Override
    public String toString() {
        return activity.toString();
    }

    public enum PanelType {
        DEADLINE(DEADLINE_STRING),
        SCHEDULE(SCHEDULE_STRING),
        FLOATING(FLOATING_STRING);

        private final String string;

        PanelType(String string) {
            this.string = string;
        }

        public static PanelType fromString(String str) {
            switch (str) {
                case DEADLINE_STRING: {
                    return DEADLINE;
                }
                case SCHEDULE_STRING: {
                    return SCHEDULE;
                }
                case FLOATING_STRING: {
                    return FLOATING;
                }
                default:
                    return null;
            }
        }

        @Override
        public String toString() {
            return string;
        }
    }
}
