
package devDojoAcademy.WebFluxEssentials.config;

import devDojoAcademy.WebFluxEssentials.services.DevDojoUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;


import org.w3c.dom.UserDataHandler;



@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@Configuration
public class SecurityConfig {
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http){
        //@formatter::off
        return http
                .csrf().disable()
                .authorizeExchange()
                    .pathMatchers(HttpMethod.POST, "/anime/**").hasRole("ADMIN")
                    .pathMatchers(HttpMethod.PUT, "/anime/**").hasRole("ADMIN")
                    .pathMatchers(HttpMethod.DELETE, "/anime/**").hasRole("ADMIN")
                    .pathMatchers(HttpMethod.GET, "/anime/**").hasRole("USER")
                .anyExchange().authenticated()
                    .and()
                .formLogin()
                    .and()
                .httpBasic()
                    .and()
                .build();
        //formatter:on
    }


  /*  @Bean
    public MapReactiveUserDetailsService UserDetailsService(){
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        UserDetails user = User.withUsername("user")
                .password(passwordEncoder.encode("devdojo"))
                .roles("USER")
                .build();
        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder.encode("devdojo"))
                .roles("USER","ADMIN")
                .build();
        return new MapReactiveUserDetailsService(user, admin);
    }
*/

    @Bean
    ReactiveAuthenticationManager authenticationManager(DevDojoUserService devDojoUserService){
        return new UserDetailsRepositoryReactiveAuthenticationManager(devDojoUserService);
    }


}

