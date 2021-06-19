package edu.gsu.bbb.willdo.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.security.core.authority.*;
import org.springframework.security.core.*;

import javax.validation.constraints.Email;
import java.util.*;

public class User implements org.springframework.security.core.userdetails.UserDetails {
    @Id
    private String id;

	@Indexed
    private String email;

    private String password;

	private String displayName;

    private Collection<String> groups;

    public User() {}

    public String getId() {
        return id;
    }

	public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

	public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Collection<String> getGroups() {
        return groups;
    }

    public void setGroups(Collection<String> groups) {
        this.groups = new HashSet<String> ( groups ); // remove duplicate groups
    }

	public void updateMembership(Group newGroup) {
		this.groups = new HashSet<String> ( this.groups ); // remove duplicate groups
		if ( newGroup.contains(this) )
			this.groups.add( newGroup.getId() );
		else this.groups.remove( newGroup.getId() );
	}

    public boolean belongsTo(Group currentGroup) {
        return  groups != null  &&  currentGroup != null  &&  groups.contains( currentGroup.getId() ) ;
    }


	/// continues simple implementation of UserDetails class

	public String getUsername() {
		return email;
	}

	public boolean isAccountNonExpired() {
		return true;
	}

	public boolean isAccountNonLocked() {
		return true;
	}

	public boolean isCredentialsNonExpired() {
		return true;
	}

	public boolean isEnabled() {
		return true;
	}

	public Collection<GrantedAuthority> getAuthorities() {
		return Arrays.asList( new SimpleGrantedAuthority("user") );
	}
}
