package devDojoAcademy.WebFluxEssentials.services;

import devDojoAcademy.WebFluxEssentials.repository.DevDojoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@RequiredArgsConstructor
@Service
public class DevDojoUserService implements ReactiveUserDetailsService {
    private final DevDojoRepository devDojoRepository;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return devDojoRepository.findDevDojoUserByUsername(username).cast(UserDetails.class);
    }
}
