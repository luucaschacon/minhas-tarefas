//package br.com.tarefas.security;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//@Configuration
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
//public class WebSecurityConfig extends WebSecurityConfigurationAdapter {
//	
//	@Bean
//	public PasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
//	}
//	
//	@Bean
//	@Override
//	protected AuthenticationManager authenticationManagerBean() throws Exception {
//		return super.authenticationManagerBean();
//	}
//	
//	// AUTENTICAÇÃO
//	private void configure(AuthenticatorManagerBuuilder auth) throws Exception {
//		super.configure(auth);
//
//	}
//	
//	// AUTORIZAÇÃO
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		super.configure(http);
//	}
//
//}

package br.com.tarefas.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {
	
	private static final String[] PATHS = new String[] {"/tarefa/**", "/categoria/**", "/usuario/**"};
	
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
//    @Bean // AUTENTICAÇÃO
//    AuthenticationManager authenticationManager( 
//            AuthenticationConfiguration configuration) throws Exception {
//        return configuration.getAuthenticationManager();
//    }

    // AUTENTICAÇÃO
    @Bean
    UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {

        UserDetails user = User.builder()
                .username("usuario")
                .password(passwordEncoder.encode("senha"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user);
    }

    // AUTORIZAÇÃO
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> {})
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth

                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers(HttpMethod.POST, PATHS).hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, PATHS).hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, PATHS).hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, PATHS)
                    .hasAnyRole("ADMIN", "USER")

                .requestMatchers("/h2-console/**").permitAll()
                .anyRequest().authenticated())
            .httpBasic(httpBasic -> {});

        return http.build();
    }
}
