package tum.sebis.apm.web.rest.errors;

import java.net.URI;

public final class ErrorConstants {

    // Error Types
    public static final String ERR_CONCURRENCY_FAILURE = "error.concurrencyFailure";
    public static final String ERR_VALIDATION = "error.validation";
    public static final String PROBLEM_BASE_URL = "http://www.jhipster.tech/problem";
    public static final URI DEFAULT_TYPE = URI.create(PROBLEM_BASE_URL + "/problem-with-message");
    public static final URI CONSTRAINT_VIOLATION_TYPE = URI.create(PROBLEM_BASE_URL + "/contraint-violation");
    public static final URI PARAMETERIZED_TYPE = URI.create(PROBLEM_BASE_URL + "/parameterized");
    public static final URI INVALID_PASSWORD_TYPE = URI.create(PROBLEM_BASE_URL + "/invalid-password");
    public static final URI EMAIL_ALREADY_USED_TYPE = URI.create(PROBLEM_BASE_URL + "/email-already-used");
    public static final URI LOGIN_ALREADY_USED_TYPE = URI.create(PROBLEM_BASE_URL + "/login-already-used");
    public static final URI EMAIL_NOT_FOUND_TYPE = URI.create(PROBLEM_BASE_URL + "/email-not-found");

    // Error Messages
    public static final String TEAM_ALREADY_IN_SPRINT_MESSAGE = "Team is already in sprint";
    public static final String TEAM_ALREADY_EXISTS_MESSAGE = "A team with the given name already exists";
    public static final String SPRINT_NOT_FOUND_MESSAGE = "A sprint with the given id could not be found";
    public static final String TEAM_NOT_FOUND_MESSAGE = "A team with the given id could not be found";
    public static final String SPRINT_TEAM_NOT_FOUND_MESSAGE = "A sprint team with the given id could not be found";

    private ErrorConstants() {
    }
}
