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
import seedu.taskman.model.event.Status;

import java.util.logging.Logger;
//@@author A0140136W
/**
 * Panel containing the list of tasks.
 */
public class FloatingPanel extends UiPart implements ListPanel {
    private final Logger logger = LogsCenter.getLogger(FloatingPanel.class);
    private static final String FXML = "FloatingPanel.fxml";
    private AnchorPane panel;
    private AnchorPane placeHolderPane;

    @FXML
    private TableView<Activity> floatingTableView;

    public FloatingPanel() {
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

    public static FloatingPanel load(Stage primaryStage, AnchorPane taskListPlaceholder,
                                     ObservableList<Activity> taskList) {
        FloatingPanel deadlinePanel =
                UiPartLoader.loadUiPart(primaryStage, taskListPlaceholder, new FloatingPanel());
        deadlinePanel.configure(taskList);
        return deadlinePanel;
    }

    private void configure(ObservableList<Activity> taskList) {
        setConnections(taskList);
        addToPlaceholder();
    }

    // TODO Resolve generic type issue.
    private void setConnections(ObservableList<Activity> taskList) {      
        floatingTableView.setItems(taskList);
        
        TableColumn<Activity, String> numberColumn = new TableColumn<Activity, String>("#");
        numberColumn.setCellValueFactory(new Callback<CellDataFeatures<Activity, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<Activity, String> p) {
                return new ReadOnlyObjectWrapper<String>(floatingTableView.getItems().indexOf(p.getValue()) + 1 + "");
            }
        });   
        numberColumn.setMaxWidth(32);
        numberColumn.setMinWidth(32);
        numberColumn.setResizable(false);
        floatingTableView.getColumns().add(numberColumn);

        TableColumn<Activity, String> titleColumn = new TableColumn<Activity, String>("Floating");
        titleColumn.setCellValueFactory(new Callback<CellDataFeatures<Activity, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<Activity, String> p) {
                return new ReadOnlyObjectWrapper<String>(p.getValue().getTitle().title);
            }
        });
        floatingTableView.getColumns().add(titleColumn);

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
        floatingTableView.getColumns().add(statusColumn);

        setEventHandlerForSelectionChangeEvent();
    }

    private void addToPlaceholder() {
        placeHolderPane.getChildren().add(panel);
        FxViewUtil.applyAnchorBoundaryParameters(panel, 0.0, 0.0, 0.0, 0.0);
        FxViewUtil.applyAnchorBoundaryParameters(floatingTableView, 0.0, 0.0, 0.0, 0.0);
    }

    // TODO Edit
    private void setEventHandlerForSelectionChangeEvent() {
        floatingTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
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
            floatingTableView.scrollTo(index);
            floatingTableView.getSelectionModel().clearAndSelect(index);
        });
    }

    @Override
    public void clearSelection() {
        Platform.runLater(() -> {
            floatingTableView.getSelectionModel().clearSelection();
        });
    }

}
