package edu.gsu.bbb.willdo.models;

import org.springframework.data.annotation.Id;

import java.util.*;

public class Group {
    @Id
    private String id;
    private String name;
	private Collection<String> users;
	private Collection<String> admins;

    public Group() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

	public Collection<String> getUsers() {
        return users;
    }

	public Collection<String> getAdmins() {
        return admins;
    }


	public void validateUsers(User currentUser)
	{
		String currentUserId= currentUser.getId();
		admins= validateList(admins, currentUserId); // makes sure there's at least one admin
		users= validateList(users, currentUserId); // makes sure admins are also users
		users.addAll(admins); // makes sure all admins are also users
	}

	// make sure the list has at least one user, while removing duplicates
	private Collection<String> validateList(Collection<String> list, String currentUserId)
	{
		list= new HashSet<String> ( list ); // remove duplicate users
		if ( list.size() == 0 )
			list.add(currentUserId);
		return list;
	}

	public void updateUsers(Group newGroup, User currentUser)
	{
		String currentUserId= currentUser.getId();

		if ( admins.contains( currentUserId )  ) // makes sure the current user is an admin
			if ( newGroup.admins != null )
				admins= validateList(newGroup.admins, currentUserId); // makes sure there's at least one admin

		if ( newGroup.users != null )
			users= validateList(newGroup.users, currentUserId); // makes sure there's at least one user

		users.addAll( admins ); // makes sure all admins are also users
	}

	public boolean contains(User currentUser) {
		return  currentUser != null  &&  this.users != null  &&  this.users.contains( currentUser.getId() ) ;
	}

	public boolean contains(String userId) {
		return  userId != null  &&  this.users != null  &&  this.users.contains( userId ) ;
	}

}
