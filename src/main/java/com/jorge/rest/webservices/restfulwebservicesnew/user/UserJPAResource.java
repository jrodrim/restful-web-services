package com.jorge.rest.webservices.restfulwebservicesnew.user;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.jorge.rest.webservices.restfulwebservicesnew.exception.UserNotFoundException;
import com.jorge.rest.webservices.restfulwebservicesnew.post.Post;
import com.jorge.rest.webservices.restfulwebservicesnew.post.PostRepository;

@RestController
public class UserJPAResource 
{
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PostRepository postRepository;
	
	//GET /jpa/users
	@GetMapping("/jpa/users")
	public List<User> retrieveAllUsers()
	{
		return userRepository.findAll();
	}
	
	//GET /jpa/users/{id}
	@GetMapping("/jpa/users/{id}")
	public Resource<User> retrieveUser(@PathVariable int id)
	{
		Optional<User> user = userRepository.findById(id);
		if(!user.isPresent())
		{
			throw new UserNotFoundException("id[" + id + "]");
		}
		
		//"all-users" , SERVER_PATH + "/users"
		//retrieveAllUsers
		//HATEOAS		
		Resource<User> resource = new Resource<User>(user.get());
		
		ControllerLinkBuilder linkTo = ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(this.getClass()).retrieveAllUsers());
		
		resource.add(linkTo.withRel("all-users"));
		
		return resource;
	}
	
	//POST /users/
	@PostMapping("/jpa/users/{id}/posts")
	public ResponseEntity<Object> createPost(@PathVariable int id, @RequestBody Post post)
	{
		Optional<User> userOptional = userRepository.findById(id);
		if(!userOptional.isPresent())
		{
			throw new UserNotFoundException("id[" + id + "]");
		}
		
		User user = userOptional.get();
		post.setUser(user);
		postRepository.save(post);
				
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/id").buildAndExpand(post.getId()).toUri();
		
		return ResponseEntity.created(location).build();
	}
	
	//POST /users/{}
	@PostMapping("/jpa/users")
	public ResponseEntity<Object> createUser(@Valid @RequestBody User user)
	{
		User savedUser = userRepository.save(user);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/id").buildAndExpand(savedUser.getId()).toUri();
		
		return ResponseEntity.created(location).build();
	}
	
	//GET /jpa/users
	@GetMapping("/jpa/users/{id}/posts")
	public List<Post> retrieveAllPostByUser(@PathVariable int id)
	{
		Optional<User> userOptional = userRepository.findById(id);
		if(!userOptional.isPresent())
		{
			throw new UserNotFoundException("id[" + id + "]");
		}
		
		return userOptional.get().getPosts();
	}
	
	//DELETE /jpa/users/
	@DeleteMapping("/jpa/users/{id}")
	public void deleteUser(@PathVariable int id)
	{
		userRepository.deleteById(id);
	}
}
