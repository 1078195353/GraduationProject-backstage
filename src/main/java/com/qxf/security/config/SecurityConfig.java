package com.qxf.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @ClassName SecurityConfig
 * @Description TODO
 * @Author qiuxinfa
 * @Date 2020/5/8 20:06
 **/
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint authenticationErrorHandler;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final ApplicationContext applicationContext;

    @Autowired
    private TokenFilter tokenFilter;

//    @Autowired
//    private CorsFilter corsFilter;

    public SecurityConfig(TokenProvider tokenProvider,JwtAuthenticationEntryPoint authenticationErrorHandler, JwtAccessDeniedHandler jwtAccessDeniedHandler, ApplicationContext applicationContext) {
        this.tokenProvider = tokenProvider;
        this.authenticationErrorHandler = authenticationErrorHandler;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
        this.applicationContext = applicationContext;
    }

      // ???????????????????????????????????????????????????????????????????????????ROLE_ADMIN??????????????????????????????
//    @Bean
//    GrantedAuthorityDefaults grantedAuthorityDefaults() {
//        // ?????? ROLE_ ??????
//        return new GrantedAuthorityDefaults("");
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // ??????????????????
        return new BCryptPasswordEncoder();
    }

//    public static void main(String[] args) {
//        System.out.println(new BCryptPasswordEncoder().encode("123456"));
//    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()   // ?????? csrf
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // ?????? session
                .and()
                .cors()
                .and()
                .authorizeRequests()
                // httpMethod options
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // ?????????????????????"/api/file/**"?????????????????????
                .antMatchers("/auth/**","/api/file/**").permitAll()
                // webSocket
                .antMatchers("/ws/**").permitAll()
                // ?????????
                .antMatchers("/actuator/**").permitAll()
                // ????????????
                .antMatchers("/static/**").permitAll()
                .antMatchers("/assets/**").permitAll()
                .antMatchers("/css/**").permitAll()
                .antMatchers("/js/**").permitAll()
                .antMatchers("/loading/**").permitAll()
                .antMatchers("/logo.png").permitAll()
                .antMatchers("/index.html").permitAll()
                // swagger (???????????????????????????)
                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/v2/**").permitAll()
                .antMatchers("/webjars/**").permitAll()
//                .anyRequest().authenticated()
                //??????url???????????????
                .anyRequest().access("@baseUrlControl.canAccess(request,authentication)")
                .and()
                .headers().cacheControl();
        // ????????????????????????
        httpSecurity.addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);

        // ???????????? ????????????
        httpSecurity.exceptionHandling()
                .authenticationEntryPoint(authenticationErrorHandler)   // ???????????????
                .accessDeniedHandler(jwtAccessDeniedHandler);            // ???????????????
//        httpSecurity
//                // ?????? CSRF
//                .csrf().disable()
//                // ????????????
//                .exceptionHandling()
//                .authenticationEntryPoint(authenticationErrorHandler)
//                .accessDeniedHandler(jwtAccessDeniedHandler)
//
//                // ??????iframe ????????????
//                .and()
//                .headers()
//                .frameOptions()
//                .disable()
//
//                // ???????????????
//                .and()
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and().cors()
//
//                .and()
//                .authorizeRequests()
//                // httpMethod options
//                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//                // ??????????????????
//                .antMatchers(
//                        HttpMethod.GET,
//                        "/*.html",
//                        "/**/*.html",
//                        "/**/*.css",
//                        "/**/*.js",
//                        "/webSocket/**"
//                ).permitAll()
//                // swagger ??????
//                .antMatchers("/swagger-ui.html").permitAll()
//                .antMatchers("/swagger-resources/**").permitAll()
//                .antMatchers("/webjars/**").permitAll()
//                .antMatchers("/*/api-docs").permitAll()
//                // ??????
//                .antMatchers("/avatar/**").permitAll()
//                .antMatchers("/file/**").permitAll()
//                // ???????????? druid
//                .antMatchers("/druid/**").permitAll()
//                //?????????????????????
//                .antMatchers("/user/login","/user/register").permitAll()
//                // ??????OPTIONS??????
//                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//                // ???????????????????????????
//                .anyRequest().authenticated()
//                .and().addFilterBefore(tokenFilter,UsernamePasswordAuthenticationFilter.class);
    }

}
