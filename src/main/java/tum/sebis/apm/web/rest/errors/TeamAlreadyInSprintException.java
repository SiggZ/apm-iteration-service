package tum.sebis.apm.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

/**
 * Created by Christian Ziegner on 14.12.2017.
 */
public class TeamAlreadyInSprintException extends AbstractThrowableProblem {

    public TeamAlreadyInSprintException() {
        super(null, Status.BAD_REQUEST.getReasonPhrase(), Status.BAD_REQUEST,
            ErrorConstants.TEAM_ALREADY_IN_SPRINT_MESSAGE);
    }
}
