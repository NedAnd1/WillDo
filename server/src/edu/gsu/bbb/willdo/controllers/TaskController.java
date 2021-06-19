package edu.gsu.bbb.willdo.controllers;

import java.util.*;

import edu.gsu.bbb.willdo.models.*;
import edu.gsu.bbb.willdo.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.*;

@RestController
@RequestMapping("/api")
public class TaskController {
    @Autowired
    TaskRepository repository;

	@GetMapping("/tasks") // get all of a user's tasks
	public Iterable<Task> all(@AuthenticationPrincipal User currentUser) {
		ArrayList<Task> allTasks= new ArrayList<Task>();
		for ( String groupId : currentUser.getGroups() )
			allTasks.addAll( repository.findAllByGroupId( groupId ) );
		return allTasks;
	}

    @GetMapping("/tasks/group/{groupId}") //get one specific task
    public Iterable<Task> taskFromGroup(@PathVariable String groupId) {
        return repository.findAllByGroupId(groupId); //Uses List from TaskRepository to generate queries by GroupId
    }

    @GetMapping("/tasks/{taskId}")
    public Optional<Task> findTask(@PathVariable String taskId){
        return repository.findById(taskId);
    }

    @PostMapping("/tasks") //saves new task as new doc in DB
    public Task newTask(@RequestBody Task newTask) {
        if(newTask.getGroupId() == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide Group Id");
        }
        if(newTask.getSummary() == null){
            newTask.setSummary("New Task");
        }
        return repository.save(newTask);
    }

    @PutMapping("/tasks/{taskId}") //updates task already in DB
    public Optional<Task> updateTask(@RequestBody Task newTask, @PathVariable String taskId) {
        return repository.findById(taskId)
                .map(task -> { //Gets the data from the Optional and maps them to a new Task
                    if (newTask.getSummary() != null) {
                        task.setSummary(newTask.getSummary());
                    }
                    if (newTask.getDescription() != null) {
                        task.setDescription(newTask.getDescription());
                    }
                    if (newTask.getDate() != null) {
                        task.setDate(newTask.getDate());
                    }
                    if (newTask.isState() != task.isState()) {
                        task.setState(newTask.isState());
                    }
                    if (newTask.getGroupId() != null) {
                        task.setGroupId(newTask.getGroupId());
                    }
                    if (newTask.getAssignedUsers() != null) {
                        task.setAssignedUsers(newTask.getAssignedUsers());
                    }
                    return repository.save(task);
                });
    }

	@DeleteMapping({ "/tasks/{taskId}", "/tasks/{taskId}/group/{groupId}" }) //delete task
    public void delete(@PathVariable String taskId, @PathVariable(required=false) String groupId) {
		if ( groupId != null )
			repository.deleteByIdAndGroupId(taskId, groupId);
		else repository.deleteById(taskId); 
    }
}
