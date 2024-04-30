package practice.communityservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig{
    private final JwtUtill jwtUtill;
    private final JwtAuthEntryPoint jwtAuthEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        //CSRF, CORS
        http.csrf((csrf)-> csrf.disable());
        // 다른 도메인에서 접근 허용
        http.cors(Customizer.withDefaults());
        // Session 비활성화
        http.sessionManagement((sessionManagement) -> sessionManagement.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS)
        );
        // Form 로그인, BasicHttp 비활성화
        http.formLogin((form) -> form.disable());
        http.httpBasic(AbstractHttpConfigurer::disable);
        // 권한 규칙 정
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(
                        "/members/signin",
                        "/members/signup"
//                        "/**"
                ).permitAll()
                .anyRequest().authenticated()
        );
        // Jwt Execptionhandling
        http.exceptionHandling((exceptionHandling) -> exceptionHandling
                .authenticationEntryPoint(this.jwtAuthEntryPoint));

        // Jwt Filter 추가
        http.addFilterBefore(new JwtAuthFilter(jwtUtill), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


}
