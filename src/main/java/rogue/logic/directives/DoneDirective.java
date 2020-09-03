package rogue.logic.directives;

import rogue.model.report.Report;
import rogue.logic.directives.exceptions.ExecutionException;
import rogue.model.task.TaskList;
import rogue.storage.Storage;
import rogue.storage.exceptions.StorageException;
import rogue.ui.Ui;

/**
 * Marks a {@code Task} as complete.
 */
public class DoneDirective implements Executable {
    /** Message for when index is invalid. */
    private static final String ERROR_INCORRECT_INDEX = "sToP TrYiNg tO FoOl mE. taSK #%d dOeS NoT ExIsT.";

    /** Message for when {@code Task} is marked as done successfully. */
    private static final String MESSAGE_DONE_SUCCESS =
            "fInAlLy, I feLL AsLeEp wHiLe wAiTiNg fOr yOu tO FiNiSh: %s";

    private final int index;

    /**
     * Constructs a {@code DoneDirective}.
     *
     * @param index The index of the {@code Task} to mark as done.
     */
    public DoneDirective(int index) {
        this.index = index;
    }

    /**
     * Marks a {@code Task} from the {@code TaskList} as complete.
     *
     * @param storage   An instance of {@code Storage}.
     * @param tasks     The {@code TaskList} from which a {@code Task} is marked as complete.
     * @param ui        An instance of {@code Ui}.
     * @return A {@code Report} with a success message
     * @throws ExecutionException if the index provided is invalid.
     * @throws StorageException if data cannot be saved to file.
     */
    @Override
    public Report execute(Storage storage, TaskList tasks, Ui ui)
            throws ExecutionException, StorageException {
        try {
            tasks.get(index).markAsDone();

            storage.save(tasks.getTaskList());

            return new Report(String.format(MESSAGE_DONE_SUCCESS, tasks.get(index).toString()));
        } catch (IndexOutOfBoundsException e) {
            throw new ExecutionException(String.format(ERROR_INCORRECT_INDEX, index + 1));
        }
    }
}
