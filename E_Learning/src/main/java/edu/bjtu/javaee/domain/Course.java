package edu.bjtu.javaee.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.validation.annotation.Validated;
import org.springframework.hateoas.RepresentationModel;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Validated
@Entity
@Table(name = "course")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
public class Course extends RepresentationModel<Course> implements Serializable {
    private static final long serialVersionUID = 4048798961366546485L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Schema(description = "Course's id.", required = true)
    private Long id;

    @NotBlank
    @Size(max = 100)
    @Schema(description = "Course's name.", required = true)
    private String name;

    @Size(max = 50)
    @Schema(description = "Course's classification.", required = true)
    private String classify;

    @Size(max = 50)
    @Schema(description = "Course's teacher.", required = true)
    private String teacher;

    @Size(max = 20)
    @Schema(description = "Course's time.", required = true)
    private String time;

    @Column(length = 4000)
    @Schema(description = "Course's description.", required = true)
    private String description;

    @Override
    public String toString() {
        return "Course [id=" + id + ", name=" + name + ", teacher=" + teacher + ", time=" + time + ",description="+description+"]";
    }
}
