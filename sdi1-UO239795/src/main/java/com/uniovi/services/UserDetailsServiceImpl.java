package com.uniovi.services;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import com.uniovi.repositories.UsersRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	private UsersRepository usersRepository;

	@Override
	public UserDetails loadUserByUsername(String email)
			throws UsernameNotFoundException {
		com.uniovi.entities.User user = usersRepository.findByEmail(email);
		Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
		grantedAuthorities.add(new SimpleGrantedAuthority(user.getRole()));
		return new User(user.getEmail(), user.getPassword(),
				grantedAuthorities);
	}

}
