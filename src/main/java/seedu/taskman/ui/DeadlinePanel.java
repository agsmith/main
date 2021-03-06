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
import seedu.taskman.model.event.Deadline;
import seedu.taskman.model.event.Status;

import java.util.logging.Logger;
//@@author A0140136W
/**
 * Panel containing the list of tasks.
 */
public class DeadlinePanel extends UiPart implements ListPanel {
    private final Logger logger = LogsCenter.getLogger(DeadlinePanel.class);
    private static final String FXML = "DeadlinePanel.fxml";
    private AnchorPane panel;
    private AnchorPane placeHolderPane;

    @FXML
    private TableView<Activity> deadlineTableView;

    public DeadlinePanel() {
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

    public static DeadlinePanel load(Stage primaryStage, AnchorPane taskListPlaceholder,
                                     ObservableList<Activity> taskList) {
        DeadlinePanel deadlinePanel =
                UiPartLoader.loadUiPart(primaryStage, taskListPlaceholder, new DeadlinePanel());
        deadlinePanel.configure(taskList);
        return deadlinePanel;
    }

    private void configure(ObservableList<Activity> taskList) {
        setConnections(taskList);
        addToPlaceholder();
    }

    // TODO Resolve generic type issue.
    private void setConnections(ObservableList<Activity> taskList) {      
        deadlineTableView.setItems(taskList);

        TableColumn<Activity, String> numberColumn = new TableColumn<Activity, String>("#");
        numberColumn.setCellValueFactory(new Callback<CellDataFeatures<Activity, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<Activity, String> p) {
                return new ReadOnlyObjectWrapper<String>(deadlineTableView.getItems().indexOf(p.getValue()) + 1 + "");
            }
        });   
        numberColumn.setMaxWidth(32);
        numberColumn.setMinWidth(32);
        numberColumn.setResizable(false);
        deadlineTableView.getColumns().add(numberColumn);
        
        TableColumn<Activity, String> titleColumn = new TableColumn<Activity, String>("Deadline");
        titleColumn.setCellValueFactory(new Callback<CellDataFeatures<Activity, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<Activity, String> p) {
                return new ReadOnlyObjectWrapper<String>(p.getValue().getTitle().title);
            }
        });
        deadlineTableView.getColumns().add(titleColumn);

        TableColumn<Activity, String> statusColumn = new TableColumn<Activity, String>("Status");
        statusColumn.setCellValueFactory(new Callback<CellDataFeatures<Activity, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<Activity, String> p) {
                return new ReadOnlyObjectWrapper<String>(p.getValue().getStatus()
                        .map(Status::toString).orElse(""));
            }
        });
        statusColumn.setMaxWidth(90);
        statusColumn.setMinWidth(90);
        statusColumn.setResizable(false);
        deadlineTableView.getColumns().add(statusColumn);

        TableColumn<Activity, String> deadlineColumn = new TableColumn<Activity, String>("Due");
        deadlineColumn.setCellValueFactory(new Callback<CellDataFeatures<Activity, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<Activity, String> p) {
                return new ReadOnlyObjectWrapper<String>(p.getValue().getDeadline()
                        .map(Deadline::toString).orElse(""));
            }
        });
        deadlineColumn.setMaxWidth(135);
        deadlineColumn.setMinWidth(135);
        deadlineColumn.setResizable(false);
        deadlineTableView.getColumns().add(deadlineColumn);

        setEventHandlerForSelectionChangeEvent();
    }

    private void addToPlaceholder() {
        placeHolderPane.getChildren().add(panel);
        FxViewUtil.applyAnchorBoundaryParameters(panel, 0.0, 0.0, 0.0, 0.0);
        FxViewUtil.applyAnchorBoundaryParameters(deadlineTableView, 0.0, 0.0, 0.0, 0.0);
    }

    // TODO Edit
    private void setEventHandlerForSelectionChangeEvent() {
        deadlineTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
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
            deadlineTableView.scrollTo(index);
            deadlineTableView.getSelectionModel().clearAndSelect(index);
        });
    }

    @Override
    public void clearSelection() {
        Platform.runLater(() -> {
            deadlineTableView.getSelectionModel().clearSelection();
        });
    }


}
