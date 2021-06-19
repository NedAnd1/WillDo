package edu.gsu.bbb.willdo.controllers;

import edu.gsu.bbb.willdo.repositories.UserRepository;
import edu.gsu.bbb.willdo.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.*;

import java.util.Optional;

@RestController("/api")
public class UserController implements UserDetailsService {
    @Autowired
    UserRepository repository;

	@Autowired
	BCryptPasswordEncoder encoder;

    @GetMapping("/user")
    public User getUser(@AuthenticationPrincipal User currentUser){
        return currentUser;
    }

	@PostMapping("/user")
    public User getToken(@AuthenticationPrincipal User currentUser){
        return currentUser;
    }

	@GetMapping("/users/by-name/{username}")
	public UserDetails loadUserByUsername(@PathVariable String username) throws UsernameNotFoundException {
		return repository.findByEmail(username)
						 .orElseThrow( () -> new UsernameNotFoundException("User not found") );
	}

    @GetMapping("/users/{id}")
    public Optional<User> getUser(@PathVariable String id, @AuthenticationPrincipal User currentUser) {
		return
			repository.findById(id)
				.map( internalUser -> {
					User publicUser= new User(); // makes sure we don't leak user info
					publicUser.setId( internalUser.getId() );
					publicUser.setDisplayName( internalUser.getDisplayName() );
					if ( currentUser != null && currentUser.getId() == id ) // only give the current user their email and groups
					{
						publicUser.setEmail( internalUser.getEmail() );
						publicUser.setGroups( internalUser.getGroups() );
					}
					return publicUser;
				});
    }

    @PostMapping("/users")
    public User newUser(@RequestBody User newUser)
	{
        if ( newUser.getEmail() == null )
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide Email");
		else if ( repository.existsByEmail(newUser.getEmail()) )
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Existing Email");

        if ( newUser.getDisplayName() == null )
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide Display Name");
		
 		if ( newUser.getPassword() != null )
			newUser.setPassword( encoder.encode(newUser.getPassword()) );
		else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide Password");

       return repository.save(newUser);
    }

	@PutMapping("/users/{id}")
    public User updateUser(@RequestBody ExtendedUser newUser, @PathVariable String id, @AuthenticationPrincipal User currentUser)
	{
		if ( currentUser != null && currentUser.getId() == id ) // only the current user can update themselves
			return repository.findById(id)
				   .map( user -> {

						if ( newUser.getGroups() != null ) {
		    				user.setGroups( newUser.getGroups() );
							currentUser.setGroups( user.getGroups() );
						}

						if ( user.getPassword() == encoder.encode(newUser.getPassword()) ) // require correct password to update advanced info
						{
							if ( newUser.getEmail() != null ) {
		    					user.setEmail( newUser.getEmail() );
								currentUser.setEmail( newUser.getEmail() );				
							}
							if ( newUser.getDisplayName() != null ) {
		    					user.setDisplayName( newUser.getDisplayName() );
								currentUser.setDisplayName( newUser.getDisplayName() );				
							}
							if ( newUser.password2 != null )
		    					user.setPassword( encoder.encode(newUser.password2) );
						}

		                return repository.save(user);
		            })
					.orElseThrow(
						() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could Not Update User")
					);

		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could Not Update User"); // don't tell the client why the update failed
    }

	public static void updateMembership(String userId, Group group, UserRepository repository, User currentUser) {
		repository.findById(userId)
			.map( user -> {
		   		if ( group.contains(userId) != user.belongsTo(group) ) {
					user.updateMembership(group);
					repository.save(user);
					if ( userId == currentUser.getId() )
						currentUser.setGroups( user.getGroups() );
				}
				return user;
			});
	}

}

class ExtendedUser extends User {
	public String password2;
}
