package tum.sebis.apm.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A SprintTeam.
 */
@Document(collection = "sprint_team")
public class SprintTeam implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String id;

    @NotNull
    @Field("team_id")
    private String teamId;

    @NotNull
    @Field("sprint_id")
    private String sprintId;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTeamId() {
        return teamId;
    }

    public SprintTeam teamId(String teamId) {
        this.teamId = teamId;
        return this;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getSprintId() {
        return sprintId;
    }

    public SprintTeam sprintId(String sprintId) {
        this.sprintId = sprintId;
        return this;
    }

    public void setSprintId(String sprintId) {
        this.sprintId = sprintId;
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
            ", teamId='" + getTeamId() + "'" +
            ", sprintId='" + getSprintId() + "'" +
            "}";
    }
}
