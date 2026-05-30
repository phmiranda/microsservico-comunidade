package br.com.phmiranda.comunidade.config.security;

import br.com.phmiranda.comunidade.service.AuthService;
import br.com.phmiranda.comunidade.service.PerfilService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final AuthService authService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfiguration(AuthService authService, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.authService = authService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(authService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .antMatchers(
                "/v2/api-docs",
                "/swagger-resources",
                "/swagger-resources/**",
                "/configuration/ui",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**"
            ).permitAll()
            .antMatchers(HttpMethod.POST, "/auth/basica").permitAll()
            .antMatchers(HttpMethod.POST, "/usuarios").permitAll()
            .antMatchers(HttpMethod.GET, "/duvidas").permitAll()
            .antMatchers(HttpMethod.GET, "/duvidas/*").permitAll()
            .antMatchers(HttpMethod.GET, "/cursos").permitAll()
            .antMatchers(HttpMethod.GET, "/cursos/*").permitAll()
            .antMatchers(HttpMethod.GET, "/cursos/filtro/categoria").permitAll()
            .antMatchers(HttpMethod.GET, "/olamundo").permitAll()
            .antMatchers("/perfis/**").hasAuthority(PerfilService.PERFIL_ADMIN)
            .antMatchers("/usuarios/**").hasAuthority(PerfilService.PERFIL_ADMIN)
            .antMatchers(HttpMethod.POST, "/duvidas").hasAnyAuthority(PerfilService.PERFIL_ADMIN, PerfilService.PERFIL_PADRAO)
            .antMatchers(HttpMethod.PUT, "/duvidas/**").hasAnyAuthority(PerfilService.PERFIL_ADMIN, PerfilService.PERFIL_PADRAO)
            .antMatchers(HttpMethod.DELETE, "/duvidas/**").hasAuthority(PerfilService.PERFIL_ADMIN)
            .antMatchers(HttpMethod.POST, "/cursos").hasAuthority(PerfilService.PERFIL_ADMIN)
            .antMatchers(HttpMethod.PUT, "/cursos/**").hasAuthority(PerfilService.PERFIL_ADMIN)
            .antMatchers(HttpMethod.DELETE, "/cursos/**").hasAuthority(PerfilService.PERFIL_ADMIN)
            .antMatchers("/respostas/**").hasAuthority(PerfilService.PERFIL_ADMIN)
            .anyRequest().authenticated();

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/favicon.ico");
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
