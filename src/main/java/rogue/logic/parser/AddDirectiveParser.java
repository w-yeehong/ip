package rogue.logic.parser;

import rogue.commons.util.DateTimeUtil;
import rogue.logic.parser.exceptions.IncorrectInputException;
import rogue.logic.directives.AddDirective;
import rogue.model.argument.Action;
import rogue.model.argument.Argument;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Parses user inputs into an {@code AddDirective}.
 */
public class AddDirectiveParser {
    /** Option to add a description to a {@code Task}. */
    private static final String OPTION_DESCRIPTION = "/d";

    /** Option to add a date and time to a {@code Deadline}. */
    private static final String OPTION_DEADLINE_DATETIME = "/by";

    /** Option to add a date and time to an {@code Event}. */
    private static final String OPTION_EVENT_DATETIME = "/at";

    /** Message for when the description is missing. */
    private static final String ERROR_MISSING_DESC = "sToP TrYiNg tO FoOl mE. "
            + "a taSK MuSt bE FoLlOwEd bY A DesCrIpTiOn.";

    /** Message for when the date is missing from a datetime. */
    private static final String ERROR_MISSING_DATE = "sToP TrYiNg tO FoOl mE."
            + " tHe dAtE MuSt cOmE RiGhT AfTeR \"%s\".";

    /** Message for when the format of the dead is incorrect. */
    private static final String ERROR_INCORRECT_DATE_FORMAT = "sToP TrYiNg tO FoOl mE."
            + " dAtE MuSt bE In yEaR-MoNtH-DaY FoRmAt.";

    /** Message for when the action does not match any valid task. */
    private static final String ERROR_INVALID_TASK = "sToP TrYiNg tO FoOl mE."
            + " sUcH A TaSk cAnNoT Be aDdEd.";

    /**
     * Creates an {@code AddDirective} for a {@code Task}.
     *
     * OPTION_DESCRIPTION: the description of a {@code Task}.
     * OPTION_DEADLINE_DATETIME: the datetime for a {@code Deadline}.
     * OPTION_EVENT_DATETIME: the datetime for an {@code Event}.
     *
     * @param args The action and options to be parsed.
     * @return An {@code AddDirective} for the requested {@code Task}
     * @throws IncorrectInputException if no task description or an invalid action is provided.
     */
    public AddDirective parse(Argument args) throws IncorrectInputException {
        String description = args.getOptionValue(OPTION_DESCRIPTION);

        if (description.equals("")) {
            throw new IncorrectInputException(ERROR_MISSING_DESC);
        }

        switch (args.getAction()) {
        case ADD_TODO:
            return new AddDirective(Action.ADD_TODO, description);

        case ADD_DEADLINE:
            return parseDeadline(description, args);

        case ADD_EVENT:
            return parseEvent(description, args);

        default:
            throw new IncorrectInputException(ERROR_INVALID_TASK); // Should not be reached
        }
    }

    private AddDirective parseDeadline(String description, Argument args) throws IncorrectInputException {
        String dateString = args.getOptionValue(OPTION_DEADLINE_DATETIME);

        if (dateString.equals("")) {
            throw new IncorrectInputException(String.format(ERROR_MISSING_DATE, "/by"));
        }

        try {
            LocalDate by = DateTimeUtil.parseStringToDate(dateString);

            return new AddDirective(Action.ADD_DEADLINE, description, by);
        } catch (DateTimeParseException e) {
            throw new IncorrectInputException(ERROR_INCORRECT_DATE_FORMAT);
        }
    }

    private AddDirective parseEvent(String description, Argument args) throws IncorrectInputException {
        String dateString = args.getOptionValue(OPTION_EVENT_DATETIME);

        if (dateString.equals("")) {
            throw new IncorrectInputException(String.format(ERROR_MISSING_DATE, "/at"));
        }

        try {
            LocalDate at = DateTimeUtil.parseStringToDate(dateString);

            return new AddDirective(Action.ADD_EVENT, description, at);
        } catch (DateTimeParseException e) {
            throw new IncorrectInputException(ERROR_INCORRECT_DATE_FORMAT);
        }
    }
}
