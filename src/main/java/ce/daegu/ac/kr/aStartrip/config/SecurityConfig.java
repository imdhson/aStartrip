package ce.daegu.ac.kr.aStartrip.config;

import ce.daegu.ac.kr.aStartrip.handler.LoginFailureHandler;
import ce.daegu.ac.kr.aStartrip.handler.LoginSuccessHandler;
import org.apache.coyote.Request;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public LoginSuccessHandler loginSuccessHandler() {
        return new LoginSuccessHandler();
    }

    @Bean
    public LoginFailureHandler failureHandler() {
        return new LoginFailureHandler();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(AbstractHttpConfigurer::disable);

        return http
                .authorizeHttpRequests(request -> request
                        .requestMatchers(HttpMethod.POST, "/user").permitAll()
                        .requestMatchers(HttpMethod.POST, "/user/login").permitAll()
                        .anyRequest().permitAll()
                )
                .formLogin(request -> request.loginPage("/login").usernameParameter("ID")
                        .passwordParameter("PW").loginProcessingUrl("/loginProc").successHandler(loginSuccessHandler())
                        .failureHandler(failureHandler()))
                .logout(request -> request.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/loginAttempt").invalidateHttpSession(true)
                        .permitAll())
                .build();
    }
}
