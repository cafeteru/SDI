package com.uniovi.servicies;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.uniovi.entities.User;
import com.uniovi.repositories.UsersRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	private UsersRepository usersRepository;

	/**
	 * Si tuvieramos el campo username no haria falta sobreescribir Por defecto,
	 * Spring usa dos roles, admin y user. Nosotros no los usamos por ahora
	 * Escribir siempre los roles para ROLE para que lo reconozca Spring sin
	 * problemas.
	 */
	@Override
	public UserDetails loadUserByUsername(String dni)
			throws UsernameNotFoundException {
		User user = usersRepository.findByDni(dni);
		Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
		grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ESTUDIANTE"));

		// Este user es de Spring, NO EL NUESTRO
		return new org.springframework.security.core.userdetails.User(
				user.getDni(), user.getPassword(), grantedAuthorities);
	}

}
