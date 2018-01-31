package tum.sebis.apm.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;


/**
 * A Iteration.
 */
@Getter
@Setter
@Document(collection = "iteration")
public class Iteration implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("name")
    private String name;

    @Field("start")
    private LocalDate start;

    @Field("end")
    private LocalDate end;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Iteration name(String name) {
        this.name = name;
        return this;
    }

    public Iteration start(LocalDate start) {
        this.start = start;
        return this;
    }

    public Iteration end(LocalDate end) {
        this.end = end;
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
        Iteration iteration = (Iteration) o;
        if (iteration.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), iteration.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Iteration{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", start='" + getStart() + "'" +
            ", end='" + getEnd() + "'" +
            "}";
    }
}
