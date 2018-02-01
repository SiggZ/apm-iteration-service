package tum.sebis.apm.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * A SprintTeam.
 */
@Getter
@Setter
@Document(collection = "sprint_team")
public class SprintTeam implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String id;

    @NotNull
    @DBRef
    @Field("team")
    private Team team;

    @NotNull
    @DBRef
    @Field("sprint")
    private Iteration sprint;

    @Field("sprintTeamPersons")
    private List<SprintTeamPerson> sprintTeamPersons;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public SprintTeam team(Team team) {
        this.team = team;
        return this;
    }

    public SprintTeam sprint(Iteration sprint) {
        this.sprint = sprint;
        return this;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SprintTeam sprintTeam = (SprintTeam) o;
        if (sprintTeam.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), sprintTeam.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SprintTeam{" +
            "id=" + getId() +
            ", teamId='" + getTeam() + "'" +
            ", sprintId='" + getSprint() + "'" +
            "}";
    }
}
