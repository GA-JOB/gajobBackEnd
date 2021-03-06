package com.gajob.config.security;

import com.gajob.jwt.JwtAccessDeniedHandler;
import com.gajob.jwt.JwtAuthenticationEntryPoint;
import com.gajob.jwt.JwtSecurityConfig;
import com.gajob.jwt.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsUtils;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final TokenProvider tokenProvider;
  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
  private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

  public SecurityConfig(
      TokenProvider tokenProvider,
      JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
      JwtAccessDeniedHandler jwtAccessDeniedHandler
  ) {
    this.tokenProvider = tokenProvider;
    this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  public void configure(WebSecurity web) {
    web
        .ignoring()
        .antMatchers(
            "/h2-console/**"
            , "/favicon.ico"
        );
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .cors().and()
        .csrf().disable()

        .exceptionHandling()
        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
        .accessDeniedHandler(jwtAccessDeniedHandler)

        .and()
        .headers()
        .frameOptions()
        .sameOrigin()

        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        .and()
        .authorizeRequests()
        // ??????????????? preflight??? ???????????? options ???????????? ????????? ???????????? ????????? ??? ????????? ????????? ??????
        .requestMatchers(CorsUtils::isPreFlightRequest)
        // ?????? API??? Token??? ????????? ?????? ??????
        .permitAll()
        .antMatchers("/signup").permitAll()
        .antMatchers("/login").permitAll()
        .antMatchers("/issue/news").permitAll()
        .antMatchers("/issue/exhibits").permitAll()
        .antMatchers("/issue/exhibit-rankings").permitAll()
        .antMatchers("/find-id").permitAll()
        .antMatchers("/find-password").permitAll()
        .antMatchers("/student-email").permitAll()
        .antMatchers("/studen-email-verify").permitAll()
        .anyRequest().authenticated()

        .and()
        .apply(new JwtSecurityConfig(tokenProvider));
  }

}