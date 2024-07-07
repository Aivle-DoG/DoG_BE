package aivle.dog.security.jwt;

import aivle.dog.domain.user.dto.CustomAdminDetails;
import aivle.dog.domain.user.dto.CustomUserDetails;
import aivle.dog.domain.user.entity.Admin;
import aivle.dog.domain.user.entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

@Log4j2
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {

        this.jwtUtil = jwtUtil;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //request에서 Authorization 헤더를 찾음
        String authorization = request.getHeader("Authorization");

        //Authorization 헤더 검증
        if (authorization == null || !authorization.startsWith("Bearer ")) {

            System.out.println("token null");
            filterChain.doFilter(request, response);

            //조건이 해당되면 메소드 종료 (필수)
            return;
        }

        //Bearer 부분 제거 후 순수 토큰만 획득
        String token = authorization.split(" ")[1];

        //토큰 소멸 시간 검증
        if (jwtUtil.isExpired(token)) {

            System.out.println("token expired");
            filterChain.doFilter(request, response);

            //조건이 해당되면 메소드 종료 (필수)
            return;
        }

        //토큰에서 username과 role 획득
        String username = jwtUtil.getUsername(token);
        String[] rolesArray = jwtUtil.getRole(token).substring(1, jwtUtil.getRole(token).length() - 1).split(", ");

        // ArrayList에 권한 추가
        ArrayList<String> roles = new ArrayList<>(Arrays.asList(rolesArray));

        log.info("JWTFilter/doFilterInternal : " + username);
        log.info("JWTFilter/doFilterInternal : " + roles);

        Authentication authToken = null;
        if (roles.contains("ROLE_USER")) {
            //userEntity를 생성하여 값 set            
            User user = User.builder().username(username).password("temppassword").build();
            //UserDetails에 회원 정보 객체 담기
            CustomUserDetails customUserDetails = new CustomUserDetails(user);
            //스프링 시큐리티 인증 토큰 생성
            authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        } else if (roles.contains("ROLE_ADMIN")) {
            Admin admin = Admin.builder().username(username).password("temppassword").build();
            CustomAdminDetails customAdminDetails = new CustomAdminDetails(admin);
            authToken = new UsernamePasswordAuthenticationToken(customAdminDetails, null, customAdminDetails.getAuthorities());
        } else{
            log.info("올바르지 않은 권한");
        }
        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}