package tum.sebis.apm.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class ServiceNotReachableException extends AbstractThrowableProblem {

    public ServiceNotReachableException(String serviceName) {
        super(null, Status.SERVICE_UNAVAILABLE.getReasonPhrase(), Status.SERVICE_UNAVAILABLE,
            "Service not reachable or no instance found for " + serviceName);
    }
}
