package dad.aplicacionweb.openars.security;

import java.security.SecureRandom;

import dad.aplicacionweb.openars.security.RepositoryUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
    RepositoryUserDetailsService userDetailsService;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(10, new SecureRandom());
	}




    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/static/**");
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {


    	// Public pages
        http.authorizeRequests().antMatchers("/").permitAll();
        http.authorizeRequests().antMatchers("/login").permitAll();		//por que no .loginPage()
        http.authorizeRequests().antMatchers("/loginerror").permitAll();
        http.authorizeRequests().antMatchers("/signup-user").permitAll();
        http.authorizeRequests().antMatchers("/logout").permitAll();
        http.authorizeRequests().antMatchers("/all-resources").permitAll();
        http.authorizeRequests().antMatchers("/all-resources/*").permitAll();
        //http.authorizeRequests().antMatchers("/css/styles.css").permitAll();


        // Private pages
        http.authorizeRequests().antMatchers("/addcomment").hasAnyRole("USER", "ADMIN");


        http.authorizeRequests().antMatchers("*/edit-resource/*").hasAnyRole("USER", "ADMIN");

        http.authorizeRequests().antMatchers("/profile").hasAnyRole("USER", "ADMIN");
        http.authorizeRequests().antMatchers("/edit-user").hasAnyRole("USER", "ADMIN");

        http.authorizeRequests().antMatchers("/all-users").hasAnyRole( "ADMIN");

        http.authorizeRequests().antMatchers("/remove-user").hasAnyRole( "ADMIN");

        //http.authorizeRequests().antMatchers("/editbook/*").hasAnyRole("USER");
        http.authorizeRequests().antMatchers("/all-comments/").hasAnyRole("ADMIN");
        http.authorizeRequests().antMatchers("/all-resources/addresource/*").hasAnyRole("USER", "ADMIN");//en template user no tiene acceso

        // Login form
        http.formLogin().loginPage("/login");
        http.formLogin().usernameParameter("username");
        http.formLogin().passwordParameter("password");
        http.formLogin().defaultSuccessUrl("/");
        http.formLogin().failureUrl("/loginerror");

        // Logout
        http.logout().logoutUrl("/logout");
        http.logout().logoutSuccessUrl("/");

    }
}
