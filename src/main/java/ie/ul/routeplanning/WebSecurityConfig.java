package ie.ul.routeplanning;

import ie.ul.routeplanning.services.SecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

import javax.servlet.http.HttpServletRequest;

/**
 * This class configures the security config for the application
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Qualifier("userDetailsService")
    @Autowired
    private UserDetailsService userDetailsService;

    private static final String REMEMBER_ME_KEY = "route-planning-remember-me";

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        if (!SecurityService.BYPASS_AUTH) {
            http.authorizeRequests()
                    .antMatchers("/resources/**", "/css/**", "/js/**", "/registration", "/session-timeout")
                        .permitAll().anyRequest()
                    .authenticated()
                    .and()
                    .rememberMe().userDetailsService(userDetailsService).key(REMEMBER_ME_KEY).rememberMeServices(rememberMeServices())
                    .and()
                    .formLogin()
                    .loginPage("/login")
                    .permitAll()
                    .usernameParameter("username").passwordParameter("password")
                    .and()
                    .sessionManagement()
                    .invalidSessionUrl("/session-timeout")
                    .and()
                    .logout().invalidateHttpSession(false).deleteCookies("JSESSIONID", "remember-me")
                    .permitAll();
        }
    }

    @Bean
    public AuthenticationManager customAuthenticationManager() throws Exception {
        return authenticationManager();
    }

    @Bean
    public RememberMeServices rememberMeServices() {
        return new TokenBasedRememberMeServices(REMEMBER_ME_KEY, userDetailsService) {
            @Override
            protected boolean rememberMeRequested(HttpServletRequest request, String parameter) {
                boolean rememberRequested = super.rememberMeRequested(request, parameter);

                // Spring's RememberServices misses the remember-me parameter if is it set with an empty value.
                // If it is not null, it is still requested even if empty, so catch it here
                if (!rememberRequested && request.getParameter(parameter) != null) {
                    Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);
                    logger.warn("Spring's default implementation of RememberMeServices#rememberMeRequested(HttpServletRequest, String)" +
                     " ignores parameter " + parameter + " with an the provided value. Setting as requested, but you should consider" +
                            " submitting the parameter " + parameter + " with a value of true if checked");

                    rememberRequested = true;
                }

                return rememberRequested;
            }
        };
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }
}
