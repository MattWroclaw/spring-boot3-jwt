package com.test.jwt.lesson.controlers;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.test.jwt.lesson.models.AppUser;
import com.test.jwt.lesson.models.LoginDto;
import com.test.jwt.lesson.models.RegisterDto;
import com.test.jwt.lesson.repository.AppUserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Value("${security.jwt.secret-key}")
    private String jwtSecretKey;

    @Value("${security.jwt.issuer}")
    private String jwtIssuer;

    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/profile")
    public ResponseEntity<Object> profile(Authentication authentication) {
        var response = new HashMap<String, Object>();
        response.put("User", authentication.getName());
        response.put("Authorities", authentication.getAuthorities());

        var appUser = appUserRepository.findByUsername(authentication.getName());
        response.put("User", appUser);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody RegisterDto registerDto) {
        var bCryptPasswordEncoder = new BCryptPasswordEncoder();

        AppUser appUser = new AppUser();
        appUser.setUsername(registerDto.getUsername());
        appUser.setPassword(bCryptPasswordEncoder.encode(registerDto.getPassword()));
        appUser.setEmail(registerDto.getEmail());
        appUser.setRole("client");

        // save the user to the database
        appUserRepository.save(appUser);

        var jwtToken = createJwtToken(appUser);
        var response = new HashMap<String, Object>();
        response.put("token", jwtToken);
        response.put("user", appUser);

        return ResponseEntity.ok(response);
    }


    private String createJwtToken(AppUser appUser) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(jwtIssuer)
                .issuedAt(now)
                .expiresAt(now.plusSeconds(3600))
                .subject(appUser.getUsername())
                .claim("role", appUser.getRole())
                .build();


        var encoder = new NimbusJwtEncoder(
                new ImmutableSecret<>(jwtSecretKey.getBytes()));

        var params = JwtEncoderParameters.from(
                JwsHeader.with(MacAlgorithm.HS256).build(), claims);

        return encoder.encode(params).getTokenValue();

    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginDto loginDto){
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())
            );

            AppUser appUser = appUserRepository.findByUsername(loginDto.getUsername());
            String jwtToken = createJwtToken(appUser);

            var response = new HashMap<String, Object>();
            response.put("token", jwtToken);
            response.put("user", appUser);
            return ResponseEntity.ok(response);


        } catch (AuthenticationException e) {
            throw new RuntimeException(e);
        }
//        return ResponseEntity.badRequest().body("Invalid username or password");
    }


}
