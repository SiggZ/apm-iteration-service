package tum.sebis.apm.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class SprintTeamNotFoundException extends AbstractThrowableProblem {

    public SprintTeamNotFoundException() {
        super(null, Status.NOT_FOUND.getReasonPhrase(), Status.NOT_FOUND,
            ErrorConstants.SPRINT_TEAM_NOT_FOUND_MESSAGE);
    }
}
