package ce.daegu.ac.kr.aStartrip.config;

import ce.daegu.ac.kr.aStartrip.handler.LoginFailureHandler;
import ce.daegu.ac.kr.aStartrip.handler.LoginSuccessHandler;
import ce.daegu.ac.kr.aStartrip.service.CustomOAuth2UserService;
import ce.daegu.ac.kr.aStartrip.service.MemberDetailsService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final MemberDetailsService memberDetailsService;
    @Autowired
    private final CustomOAuth2UserService customOAuth2UserService;

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
                        .requestMatchers(HttpMethod.GET, "/search").permitAll()
                        .requestMatchers(HttpMethod.GET, "/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/user/password").permitAll()
                        .requestMatchers(HttpMethod.POST, "/email/verification-requests").permitAll()
                        .anyRequest().permitAll()
                )
                .formLogin(request -> request.loginPage("/login").usernameParameter("email")
                        .passwordParameter("PW").loginProcessingUrl("/loginProc").successHandler(loginSuccessHandler())
                        .failureHandler(loginFailureHandler()))
                .oauth2Login(request1 ->
                        request1.loginPage("/login").successHandler(loginSuccessHandler()).userInfoEndpoint(request2 ->
                                        request2.userService(customOAuth2UserService)))
                .logout(request -> request.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/").invalidateHttpSession(true)
                        .permitAll())
                .rememberMe(Customizer.withDefaults()).userDetailsService(memberDetailsService)
//                .rememberMe((remember) -> remember.rememberMeServices(myRememberMeServices))
                .build();

    }
}
