package tum.sebis.apm.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

/**
 * Created by Christian on 14.12.2017.
 */
public class TeamAlreadyExistsException extends AbstractThrowableProblem {

    public TeamAlreadyExistsException() {
        super(null, Status.BAD_REQUEST.getReasonPhrase(), Status.BAD_REQUEST,
            ErrorConstants.TEAM_ALREADY_EXISTS_MESSAGE);
    }
}
