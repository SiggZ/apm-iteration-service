package tum.sebis.apm.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class PersonServiceException extends AbstractThrowableProblem {

    public PersonServiceException() {
        super(null, Status.BAD_REQUEST.getReasonPhrase(), Status.BAD_REQUEST,
            ErrorConstants.PERSON_SERVICE_PROBLEM_MESSAGE);
    }

    public PersonServiceException(String message) {
        super(null, Status.BAD_REQUEST.getReasonPhrase(), Status.BAD_REQUEST,
            ErrorConstants.PERSON_SERVICE_PROBLEM_MESSAGE + ": " + message);
    }
}
