package seedu.taskman.ui;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import seedu.taskman.commons.core.LogsCenter;
import seedu.taskman.commons.events.ui.TaskPanelSelectionChangedEvent;
import seedu.taskman.commons.util.FxViewUtil;
import seedu.taskman.model.event.Activity;
import seedu.taskman.model.event.Schedule;

import java.util.logging.Logger;
//@@author A0140136W
/**
 * Panel containing the list of tasks.
 */
public class SchedulePanel extends UiPart implements ListPanel {
    private final Logger logger = LogsCenter.getLogger(SchedulePanel.class);
    private static final String FXML = "SchedulePanel.fxml";
    private AnchorPane panel;
    private AnchorPane placeHolderPane;

    @FXML
    private TableView<Activity> scheduleTableView;

    public SchedulePanel() {
        super();
    }

    @Override
    public void setNode(Node node) {
        panel = (AnchorPane) node;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }

    @Override
    public void setPlaceholder(AnchorPane pane) {
        this.placeHolderPane = pane;
    }

    public static SchedulePanel load(Stage primaryStage, AnchorPane taskListPlaceholder,
                                     ObservableList<Activity> taskList) {
        SchedulePanel deadlinePanel =
                UiPartLoader.loadUiPart(primaryStage, taskListPlaceholder, new SchedulePanel());
        deadlinePanel.configure(taskList);
        return deadlinePanel;
    }

    private void configure(ObservableList<Activity> taskList) {
        setConnections(taskList);
        addToPlaceholder();
    }

    // TODO Resolve generic type issue.
    private void setConnections(ObservableList<Activity> taskList) {    
        scheduleTableView.setItems(taskList);
        
        TableColumn<Activity, String> numberColumn = new TableColumn<Activity, String>("#");
        numberColumn.setCellValueFactory(new Callback<CellDataFeatures<Activity, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<Activity, String> p) {
                return new ReadOnlyObjectWrapper<String>(scheduleTableView.getItems().indexOf(p.getValue()) + 1 + "");
            }
        });
        numberColumn.setMaxWidth(32);
        numberColumn.setMinWidth(32);
        numberColumn.setResizable(false);
        scheduleTableView.getColumns().add(numberColumn);

        TableColumn<Activity, String> titleColumn = new TableColumn<Activity, String>("Activity");
        titleColumn.setCellValueFactory(new Callback<CellDataFeatures<Activity, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<Activity, String> p) {
                return new ReadOnlyObjectWrapper<String>(p.getValue().getTitle().title);
            }
        });
        scheduleTableView.getColumns().add(titleColumn);

        TableColumn<Activity, String> scheduleColumn = new TableColumn<Activity, String>("Schedule");
        scheduleColumn.setCellValueFactory(new Callback<CellDataFeatures<Activity, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<Activity, String> p) {
                return new ReadOnlyObjectWrapper<String>(p.getValue().getSchedule()
                        .map(Schedule::toString).orElse(""));
            }
        });
        scheduleColumn.setMaxWidth(150);
        scheduleColumn.setMinWidth(150);
        scheduleColumn.setResizable(false);
        scheduleTableView.getColumns().add(scheduleColumn);       

        setEventHandlerForSelectionChangeEvent();
    }

    private void addToPlaceholder() {
        placeHolderPane.getChildren().add(panel);
        FxViewUtil.applyAnchorBoundaryParameters(panel, 0.0, 0.0, 0.0, 0.0);
        FxViewUtil.applyAnchorBoundaryParameters(scheduleTableView, 0.0, 0.0, 0.0, 0.0);
    }

    // TODO Edit
    private void setEventHandlerForSelectionChangeEvent() {
        scheduleTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                logger.fine("Selection in task list panel changed to : '" + newValue + "'");
                raise(new TaskPanelSelectionChangedEvent(newValue));
            }
        });
    }

    // TODO Edit
    @Override
    public void scrollTo(int index) {
        Platform.runLater(() -> {
            scheduleTableView.scrollTo(index);
            scheduleTableView.getSelectionModel().clearAndSelect(index);
        });
    }

    @Override
    public void clearSelection() {
        Platform.runLater(() -> {
            scheduleTableView.getSelectionModel().clearSelection();
        });
    }

}
