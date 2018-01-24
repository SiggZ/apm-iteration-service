package tum.sebis.apm.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.text.MessageFormat;

public class PersonNotFoundException extends AbstractThrowableProblem {

    public PersonNotFoundException() {
        super(null, Status.BAD_REQUEST.getReasonPhrase(), Status.BAD_REQUEST,
            ErrorConstants.PERSON_NOT_FOUND_MESSAGE);
    }

    public PersonNotFoundException(String id) {
        super(null, Status.BAD_REQUEST.getReasonPhrase(), Status.BAD_REQUEST,
            MessageFormat.format("A person with id {0} could not be found", id));
    }
}
