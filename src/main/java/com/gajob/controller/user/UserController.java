package com.gajob.controller.user;


import com.gajob.dto.user.JwtResponseDto;
import com.gajob.dto.user.LoginDto;
import com.gajob.dto.user.UserDto;
import com.gajob.entity.user.User;
import com.gajob.jwt.TokenProvider;
import com.gajob.service.user.UserService;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:3000/")

public class UserController {

  private final TokenProvider tokenProvider;
  private final AuthenticationManagerBuilder authenticationManagerBuilder;
  private final PasswordEncoder passwordEncoder;
  private final UserService userService;

  @PostMapping("/signup") //회원가입
  public ResponseEntity<User> signup(
      @Valid @RequestBody UserDto userDto
  ) {
    return ResponseEntity.ok(userService.signup(userDto));
  }

  @PostMapping("/login") //로그인
  public ResponseEntity<JwtResponseDto> login(@Valid @RequestBody LoginDto loginDto,
      HttpServletResponse httpServletResponse) {

    return ResponseEntity.ok(userService.login(loginDto, httpServletResponse));
  }

//  @PostMapping("/login") //로그인
//  public ResponseEntity<JwtResponseDto> authorize(@Valid @RequestBody LoginDto loginDto) {
//    UsernamePasswordAuthenticationToken authenticationToken =
//        new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());
//
//    Authentication authentication = authenticationManagerBuilder.getObject()
//        .authenticate(authenticationToken);
//    SecurityContextHolder.getContext().setAuthentication(authentication);
//
//    String jwt = tokenProvider.createToken(authentication);
//
//    HttpHeaders httpHeaders = new HttpHeaders();
//    httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
//
//    User user = userRepository.findOneWithAuthoritiesByEmail(loginDto.getEmail()).get();
//
//    return new ResponseEntity<>(new JwtResponseDto(jwt, user.getNickname()), httpHeaders,
//        HttpStatus.OK);
//  }

  @GetMapping("/user") // 현재 로그인 한 유저 정보 조회
  @PreAuthorize("hasAnyRole('USER','ADMIN')")
  public ResponseEntity<User> getMyUserInfo() {
    return ResponseEntity.ok(userService.getMyUserWithAuthorities().get());
  }

  @GetMapping("/user/{email}")
  @PreAuthorize("hasAnyRole('ADMIN')")
  public ResponseEntity<User> getUserInfo(@PathVariable String email) {
    return ResponseEntity.ok(userService.getUserWithAuthorities(email).get());
  }

  @DeleteMapping("/user/{email}") //회원정보 삭제
  @PreAuthorize("hasAnyRole('USER','ADMIN')")
  public ResponseEntity deleteUserWithAuthorities(@PathVariable String email) {
    return ResponseEntity.ok(userService.deleteUserWithAuthorities(email));
  }
}