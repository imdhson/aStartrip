package ce.daegu.ac.kr.aStartrip.config;

import ce.daegu.ac.kr.aStartrip.handler.LoginFailureHandler;
import ce.daegu.ac.kr.aStartrip.handler.LoginSuccessHandler;
import ce.daegu.ac.kr.aStartrip.service.MemberDetailsService;
import org.apache.coyote.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    public LoginFailureHandler loginFailureHandler() {
        return new LoginFailureHandler();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(AbstractHttpConfigurer::disable);

        return http
                .authorizeHttpRequests(request -> request
                        .requestMatchers(HttpMethod.POST, "/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/").permitAll()
                        .requestMatchers(HttpMethod.GET, "/register").permitAll()
                        .anyRequest().permitAll()
                )
                .formLogin(request -> request.loginPage("/login").usernameParameter("email")
                        .passwordParameter("PW").loginProcessingUrl("/loginProc").successHandler(loginSuccessHandler())
                        .failureHandler(loginFailureHandler()))
                .logout(request -> request.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/login").invalidateHttpSession(true)
                        .permitAll())
                .build();
    }
}
