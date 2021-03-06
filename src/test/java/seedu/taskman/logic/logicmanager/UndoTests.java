package seedu.taskman.logic.logicmanager;

import org.junit.Test;
import seedu.taskman.logic.commands.CommandResult;
import seedu.taskman.model.TaskMan;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

//@@author A0139019E
public class UndoTests extends LogicManagerTestBase {

    @Test
    public void execute_undoNothingToUndo_failure() {
        assertTrue(!logic.execute("undo").succeeded);
    }

    @Test
    public void execute_undoWithoutIndexAfterSingleCommand_success() throws Exception {
        TaskMan before = new TaskMan(model.getTaskMan());
        assertExecuteCommandWithStateChange("do something");
        assertExecuteCommandWithStateChange("undo");
        assertEquals("failed to undo", before, model.getTaskMan());
    }

    @Test
    public void execute_undoWithIndexAfterSingleCommand_success() throws Exception {
        TaskMan before = new TaskMan(model.getTaskMan());
        assertExecuteCommandWithStateChange("do something");
        assertExecuteCommandWithStateChange("undo 1");
        assertEquals("failed to undo", before, model.getTaskMan());
    }

    @Test
    public void execute_undoThreeCommands_success() throws Exception {
        assertExecuteCommandWithStateChange("do something");

        TaskMan before = new TaskMan(model.getTaskMan());
        assertExecuteCommandWithStateChange("do something else");
        assertExecuteCommandWithStateChange("do something else 1");
        assertExecuteCommandWithStateChange("do something else 2");
        assertExecuteCommandWithStateChange("undo 3");
        assertEquals("failed to undo", before, model.getTaskMan());
    }

    private void assertExecuteCommandWithStateChange(String command) {
        TaskMan before = new TaskMan(model.getTaskMan());
        CommandResult result = logic.execute(command);
        assertTrue("Invalid command entered!", result.succeeded);
        assertNotEquals("Command does not change state", before, model.getTaskMan());
    }
}
