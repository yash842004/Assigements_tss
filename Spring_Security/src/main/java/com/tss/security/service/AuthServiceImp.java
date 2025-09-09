package com.tss.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tss.security.dto.LoginDto;
import com.tss.security.dto.RegistrationDto;
import com.tss.security.dto.UserResponseDto;
import com.tss.security.entity.Role;
import com.tss.security.entity.User;
import com.tss.security.exception.UserAPIException;
import com.tss.security.repositary.RoleRepository;
import com.tss.security.repositary.UserRepository;
import com.tss.security.security.JwtTokenProvider;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@Service
@AllArgsConstructor
@RequiredArgsConstructor
public class AuthServiceImp implements AuthService {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private RoleRepository roleRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtTokenProvider tokenProvider;

	@Override
	public UserResponseDto register(RegistrationDto registrationDto) {
		if (userRepo.existsByUsername(registrationDto.getUsername())) {
			throw new UserAPIException(HttpStatus.BAD_REQUEST, "User already exists");
		}

		User user = new User();
		user.setUsername(registrationDto.getUsername());
		user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));

		Role userRole = roleRepo.findByRolename(registrationDto.getRole()).get();
		userRole.getUsers().add(user);
		user.setRole(userRole);

		user = userRepo.save(user);

		UserResponseDto dto = new UserResponseDto();
		dto.setUserId(user.getId());
		dto.setUsername(user.getUsername());

		return dto;
	}

	@Override
	public String login(LoginDto loginDto) {
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			String token = tokenProvider.generateToken(authentication);
			return token;
		} catch (BadCredentialsException e) {
			throw new UserAPIException(HttpStatus.NOT_FOUND, "Username or Password is incorrect");
		}
	}

}