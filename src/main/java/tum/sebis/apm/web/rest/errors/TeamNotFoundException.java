package tum.sebis.apm.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class TeamNotFoundException extends AbstractThrowableProblem {

    public TeamNotFoundException() {
        super(null, Status.BAD_REQUEST.getReasonPhrase(), Status.BAD_REQUEST,
            ErrorConstants.TEAM_NOT_FOUND_MESSAGE);
    }
}
