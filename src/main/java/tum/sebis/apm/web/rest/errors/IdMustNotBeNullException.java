package tum.sebis.apm.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class IdMustNotBeNullException extends AbstractThrowableProblem {

    public IdMustNotBeNullException(String entity) {
        super(null, Status.BAD_REQUEST.getReasonPhrase(), Status.BAD_REQUEST,
            entity + " " + ErrorConstants.ID_MUST_NOT_BE_NULL_MESSAGE);
    }
}
