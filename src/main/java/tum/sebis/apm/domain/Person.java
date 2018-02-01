package tum.sebis.apm.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * A Person entity from PersonService.
 */
@Getter
@Setter
public class Person {

    private String id;

    private String name;

    private String surname;

    private String location;

    private String grade;

    private Double projectAvailability;

    private Double sprintAvailability;

    private Double ahcr;
}
