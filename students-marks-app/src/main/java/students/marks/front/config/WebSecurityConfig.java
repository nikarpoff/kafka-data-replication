package students.marks.front.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.csrf.CsrfAuthenticationStrategy;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import students.marks.front.controller.MyAccessDeniedHandler;

import javax.sql.DataSource;
import java.security.SecureRandom;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    final DataSource dataSource;
    private final UserDetailsService userDetailsService;

    public WebSecurityConfig(DataSource dataSource, UserDetailsService userDetailsService) {
        this.dataSource = dataSource;
        this.userDetailsService = userDetailsService;
    }


    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        HttpSessionCsrfTokenRepository csrfTokenRepository = new HttpSessionCsrfTokenRepository();

        http
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler())
                .and()
                    .csrf(csrf -> csrf.csrfTokenRepository(csrfTokenRepository)
                        .sessionAuthenticationStrategy(new CsrfAuthenticationStrategy(csrfTokenRepository)))
                    .authorizeRequests().antMatchers("/js/**", "/css/**").permitAll()
                .and()
                    .authorizeRequests()
                    .antMatchers("/marks_table/api/**").hasRole("ADMIN")
                    .anyRequest().authenticated()
                .and()
                    .formLogin()
                    .loginPage("/login")
                    .permitAll()
                .and()
                    .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                    .permitAll()
                .and()
                    .headers()
                    .xssProtection();

    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

    // Аудит
    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return new MyAccessDeniedHandler();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        int strength = 10; // work factor of bcrypt
        return new BCryptPasswordEncoder(strength, new SecureRandom());
    }
}