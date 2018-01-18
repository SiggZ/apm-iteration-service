package tum.sebis.apm.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class SprintNotFoundException extends AbstractThrowableProblem {

    public SprintNotFoundException() {
        super(null, Status.BAD_REQUEST.getReasonPhrase(), Status.BAD_REQUEST,
            ErrorConstants.SPRINT_NOT_FOUND_MESSAGE);
    }
}
