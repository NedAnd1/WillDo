package edu.gsu.bbb.willdo.controllers;

import edu.gsu.bbb.willdo.models.*;
import edu.gsu.bbb.willdo.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class GroupController {
    @Autowired
    GroupRepository groupRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/groups")
    public Iterable<Group> getGroups(@AuthenticationPrincipal User currentUser) {
		return groupRepository.findAllById( currentUser.getGroups() );
    }

    @GetMapping("/groups/{groupId}")
    public Optional<Group> findGroup(@PathVariable String groupId){
        return groupRepository.findById(groupId);
    }

    @PostMapping("/groups")
    public Group addGroup(@RequestBody Group group, @AuthenticationPrincipal User currentUser) {

        if ( group.getName() == null )
            group.setName("Untitled Group");

		// makes sure the group has at least one user and admin
		group.validateUsers(currentUser);

		// makes sure the current user has the new group in their list of groups
		syncMembership(currentUser, group);

        return groupRepository.save(group);
    }

    @PutMapping("/groups/{groupId}")
    public Optional<Group> updateGroup(@RequestBody Group newGroup, @PathVariable String groupId, @AuthenticationPrincipal User currentUser){
        return 
			groupRepository.findById(groupId)
	        	.map( group -> {
					if ( group.contains(currentUser) )
					{

		                if ( newGroup.getName() != null )
		                    group.setName( newGroup.getName() );


						// updates the old group with a new list of users and admins, taking permissions into account 
		    			group.updateUsers( newGroup, currentUser );

						// updates the current user if they add or remove themselves from a group
						syncMembership(currentUser, group);

						//for ( String userId : group.getUsers() )
						//	UserController.updateMembership(userId, group, userRepository, currentUser);

						return groupRepository.save(group);
					}
					else return null;
	            });
    }

	// allows changes to a group to immediately update a user's membership
	private void syncMembership(User currentUser, Group newGroup) {
		if ( newGroup.contains(currentUser) != currentUser.belongsTo(newGroup) ) // memberships are out of syncs
			UserController.updateMembership(currentUser.getId(), newGroup, userRepository, currentUser);
	}
}

