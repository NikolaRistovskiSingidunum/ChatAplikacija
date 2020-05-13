package com.nsa.chatapp.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import com.nsa.chatapp.websocket.WebSocketSessionInformation;


@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityJavaConfig extends WebSecurityConfigurerAdapter {
 
//	@Autowired
//	private DataSource dataSource;
	
	@Autowired
	private AuthService authService;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//	    auth.inMemoryAuthentication()
//	        .withUser("admin").password(encoder().encode("adminPass")).roles("ADMIN")
//	        .and()
//	        .withUser("user").password(encoder().encode("userPass")).roles("USER");
	    auth.userDetailsService(authService);
	   
	}
	
    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.authorizeRequests()
        .antMatchers("/admin/find/*").hasRole("LOGGEDUSER")
        .antMatchers("/index.html").hasRole("LOGGEDUSER")
//        .antMatchers("/**").hasRole("LOGGEDUSER")
        .antMatchers("/css/*").hasRole("LOGGEDUSER")
        .antMatchers("/js/*").hasRole("LOGGEDUSER")
        .antMatchers("/userfiles/*").hasRole("ADMIN")
//        .and().anonymous().authorities("ROLE_SOMEBODY")
        
        //.and().authorizeRequests().antMatchers("/comment").hasIpAddress("127.0.0.1")
//        .and().authorizeRequests().antMatchers("/comment").hasRole("SOMEBODY")
//       
//        .antMatchers(HttpMethod.POST, "/comment").hasRole("ADMIN")
//        
//        
 
        //.antMatchers("/").permitAll()
        .and().formLogin()
        .and().csrf().disable();
       
        http.sessionManagement().maximumSessions(1).sessionRegistry(sessionRegistry());
   
        http
        .logout()
        .logoutSuccessUrl("/index.html")
        .invalidateHttpSession(true)
        .clearAuthentication(true)
        .deleteCookies("JSESSIONID").addLogoutHandler(new LogoutHandler() {
			
			@Override
			public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
				// TODO Auto-generated method stub
				
			 	String sessionID = request.getSession().getId();
			 	WebSocketSessionInformation.removeSessionByID(sessionID);
			}
		});
        
//        http.anonymous().and().authorizeRequests()
//        .antMatchers("/comment").hasIpAddress("127.0.0.1");

        //http.anonymous().
        
        //.anyRequest().access("hasIpAddress('127.0.0.1')");
          //...
        
//        http
//        .authorizeRequests()
//            .anyRequest().hasIpAddress("0.0.0.0/0");
//        
//        http
//        .authorizeRequests()
//            .anyRequest().hasIpAddress("127.0.0.1");
   }
	
	@Bean
	public BCryptPasswordEncoder  encoder() {
	    return new BCryptPasswordEncoder();
		
		//return new Ps i;
	}
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
}


