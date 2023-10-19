package oit.is.z1828.kaizi.janken.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class JankenAuthConfiguration {
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.formLogin(login -> login.permitAll())
        .logout(logout -> logout.logoutUrl("/logout")
            .logoutSuccessUrl("/"))
        .authorizeHttpRequests(authz -> authz.requestMatchers(AntPathRequestMatcher.antMatcher("/janken/**"))
            .authenticated().requestMatchers(AntPathRequestMatcher.antMatcher("/**")).permitAll())
        .csrf(csrf -> csrf
            .ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/*")))
        .headers(headers -> headers
            .frameOptions(frameOptions -> frameOptions
                .sameOrigin()));

    return http.build();
  }

  @Bean
  public InMemoryUserDetailsManager userDetailsService() {

    UserDetails user1 = User.withUsername("user1")
        .password("{bcrypt}$2y$10$DPZvoBEzZrecXwgAhq/69OoHbWam2Kq0taE9gXsEHJ4q9yRzGtvHO")
        .roles("USER").build();
    UserDetails user2 = User.withUsername("user2")
        .password("{bcrypt}$2y$10$DPZvoBEzZrecXwgAhq/69OoHbWam2Kq0taE9gXsEHJ4q9yRzGtvHO").roles("USER").build();
    UserDetails user3 = User.withUsername("ほんだ")
        .password("{bcrypt}$2y$10$AQOcWiX2.MA6Czw4p4OYzuw25.7Vcmgav6gaQDKFsu8opn8BdrjDG").roles("USER").build();

    return new InMemoryUserDetailsManager(user1, user2, user3);
  }
}
