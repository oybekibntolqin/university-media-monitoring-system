package uz.otfiv.universitymediamonitoringsystem.controller;


import uz.otfiv.universitymediamonitoringsystem.security.CustomUserDetailsService;
import uz.otfiv.universitymediamonitoringsystem.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/refresh")
public class RefreshController {
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;

    @GetMapping
    public String refresh() {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
        return "Bearer" + jwtUtil.generateToken(userDetails);
    }
}
