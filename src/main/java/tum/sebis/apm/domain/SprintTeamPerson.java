package tum.sebis.apm.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class SprintTeamPerson {

    private static final long serialVersionUID = 1L;

    @Field("personId")
    private String personId;

    @Field("availableDays")
    private List<LocalDate> availableDays;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SprintTeamPerson sprintTeamPerson = (SprintTeamPerson) o;
        if (sprintTeamPerson.getPersonId() == null || getPersonId() == null) {
            return false;
        }
        return Objects.equals(getPersonId(), sprintTeamPerson.getPersonId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getPersonId());
    }

    @Override
    public String toString() {
        return "SprintTeamPerson{" +
            "personId=" + getPersonId() +
            "}";
    }
}
