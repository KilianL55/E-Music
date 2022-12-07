package edu.caensup.sio.emusic.config;

import edu.caensup.sio.emusic.service.DbUserLoginService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Bean // (2)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers( "/init/**","/","/h2-console/**","/admin/**", "/img/**", "/css/**","/accueil", "/classes", "/signup", "/sendEmailVerif", "/codeVerif","/saveChildren","/addChildren").permitAll() // (3)
                .anyRequest().authenticated() // (4)
                .and().formLogin() // (5)
                .loginPage("/login").defaultSuccessUrl("/dashboard")// (5)
                .permitAll().and().logout().logoutSuccessUrl("/")// (6)
                .permitAll().and().httpBasic().and().exceptionHandling().accessDeniedPage("/403");
        http.headers().frameOptions().sameOrigin(); //// (8)
        http.csrf().disable();
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new DbUserLoginService(); // (2)
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() { // (2)
        return new BCryptPasswordEncoder();
    }
}
