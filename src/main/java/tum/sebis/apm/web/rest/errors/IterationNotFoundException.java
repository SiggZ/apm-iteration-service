package tum.sebis.apm.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class IterationNotFoundException extends AbstractThrowableProblem {

    public IterationNotFoundException() {
        super(null, Status.BAD_REQUEST.getReasonPhrase(), Status.BAD_REQUEST,
            ErrorConstants.SPRINT_NOT_FOUND_MESSAGE);
    }
}
