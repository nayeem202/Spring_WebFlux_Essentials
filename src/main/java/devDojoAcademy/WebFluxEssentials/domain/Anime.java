package devDojoAcademy.WebFluxEssentials.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import javax.validation.constraints.NotEmpty;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@With
@Table(name = "anime")


public class Anime {

    @Id
    private long id;

    @NotEmpty
    private String name;

}