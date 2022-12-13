package devDojoAcademy.WebFluxEssentials.domain;

import com.fasterxml.jackson.annotation.JsonTypeId;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@With
public class Anime {
    @Id
    private Integer id;
    @NotNull
    @NotEmpty(message = "The name of the anime can not ber empty")
    private String name;

}
