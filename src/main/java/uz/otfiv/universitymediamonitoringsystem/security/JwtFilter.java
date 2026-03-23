package uz.otfiv.universitymediamonitoringsystem.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)  {
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7);
            if (jwtUtil.isValid(token)) {
                String email = jwtUtil.getUsername(token);
                List<GrantedAuthority> roles = jwtUtil.getRoles(token);
                var auth = new UsernamePasswordAuthenticationToken(
                        email, null, roles
                );
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        try {
            filterChain.doFilter(request, response);
        } catch (IOException | ServletException e) {
//            throw new RuntimeException(e);
            e.printStackTrace();
        }
    }
}
