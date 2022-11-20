package com.ankit.angularapp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class AngularAppApplication 
//extends SpringBootServletInitializer
{

	@Autowired
	RoleRepo roleRepo;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public static void main(String[] args) {
		SpringApplication.run(AngularAppApplication.class, args);

	}
	
	/*
	 * @Override protected SpringApplicationBuilder
	 * configure(SpringApplicationBuilder builder) { return
	 * builder.sources(AngularAppApplication.class); }
	 */
	 

	/*
	 * @Bean CommandLineRunner init(UserRepository userRepository,HobbyRepository
	 * hobbyRepository) {
	 * 
	 * return args -> { Role role = new Role(); role.setRoleName("ADMIN");
	 * roleRepo.save(role);
	 * 
	 * User user1 = new User(); user1.setAge(28);
	 * user1.setEmail("ankit@localmail.com"); //user1.setPassword(
	 * "$2a$12$VB9U202gmjW1mZIW0lXNkujjm1a.heTOE/MUtcvCgclwMpTaAWvt6");
	 * user1.setName("Ankit");
	 * user1.setPassword(bCryptPasswordEncoder.encode("ankit@123"));
	 * System.out.println("ANKIT : "+bCryptPasswordEncoder.encode("ankit@123"));
	 * userRepository.save(user1); jdbcTemplate.update(
	 * "INSERT INTO users_roles VALUES (?, ?)", user1.getId(),1); Hobby user1Hobby1
	 * = new Hobby(); user1Hobby1.setHobbyName("Cricket");
	 * user1Hobby1.setUser(user1); Hobby user1Hobby2 = new Hobby();
	 * user1Hobby2.setHobbyName("FootBall"); user1Hobby2.setUser(user1); Set<Hobby>
	 * user1Hobbies = new HashSet<Hobby>(); user1Hobbies.add(user1Hobby1);
	 * user1Hobbies.add(user1Hobby2); hobbyRepository.saveAll(user1Hobbies);
	 * 
	 * User user2 = new User(); user2.setAge(22);
	 * user2.setEmail("juhi@localmail.com"); //user2.setPassword(
	 * "$2a$12$O1IhKQqyTXQxGT4ZPyxpcuInlgV1gS9Klz.b8MIbB3gOcBCtMNoiW");
	 * user2.setName("Juhi");
	 * user2.setPassword(bCryptPasswordEncoder.encode("juhi@123"));
	 * 
	 * userRepository.save(user2); jdbcTemplate.update(
	 * "INSERT INTO users_roles VALUES (?, ?)", user2.getId(),1); Hobby user2Hobby1
	 * = new Hobby(); user2Hobby1.setHobbyName("VolleyBall");
	 * user2Hobby1.setUser(user2); Hobby user2Hobby2 = new Hobby();
	 * user2Hobby2.setHobbyName("BasketBall"); user2Hobby2.setUser(user2);
	 * Set<Hobby> user2Hobbies = new HashSet<Hobby>();
	 * user2Hobbies.add(user2Hobby1); user2Hobbies.add(user2Hobby2);
	 * hobbyRepository.saveAll(user2Hobbies);
	 * 
	 * 
	 * User user3 = new User(); user3.setAge(26);
	 * user3.setEmail("vishal@localmail.com"); //user3.setPassword(
	 * "$2a$12$sT8Qo4XaQYUeyawxWBlP6OZsZuS4x8AJBA9OlcX3hwFfHm/9f2Q.G");
	 * user3.setName("Vishal");
	 * user3.setPassword(bCryptPasswordEncoder.encode("vishal@123"));
	 * userRepository.save(user3); jdbcTemplate.update(
	 * "INSERT INTO users_roles VALUES (?, ?)", user3.getId(),1); Hobby user3Hobby1
	 * = new Hobby(); user3Hobby1.setHobbyName("Cricket");
	 * user3Hobby1.setUser(user3); Hobby user3Hobby2 = new Hobby();
	 * user3Hobby2.setHobbyName("FootBall"); user3Hobby2.setUser(user3); Hobby
	 * user3Hobby3 = new Hobby(); user3Hobby3.setHobbyName("VolleyBall");
	 * user3Hobby3.setUser(user3); Set<Hobby> user3Hobbies = new HashSet<Hobby>();
	 * user3Hobbies.add(user3Hobby1); user3Hobbies.add(user3Hobby2);
	 * user3Hobbies.add(user3Hobby3); hobbyRepository.saveAll(user3Hobbies);
	 * 
	 * User user4 = new User(); user4.setAge(28);
	 * user4.setEmail("test@localmail.com"); //user4.setPassword(
	 * "$2a$12$cj3r/WbbOIcd4Vki9sAwkeAJkagS7FHlFzN1WIMiLDGG3eylkkF72");
	 * user4.setName("Test");
	 * user4.setPassword(bCryptPasswordEncoder.encode("test@123"));
	 * userRepository.save(user4); jdbcTemplate.update(
	 * "INSERT INTO users_roles VALUES (?, ?)", user4.getId(),1); Hobby user4Hobby1
	 * = new Hobby(); user4Hobby1.setHobbyName("BasketBall");
	 * user4Hobby1.setUser(user4); Set<Hobby> user4Hobbies = new HashSet<Hobby>();
	 * user4Hobbies.add(user4Hobby1); hobbyRepository.saveAll(user4Hobbies);
	 * 
	 * 
	 * Stream.of("John", "Julie", "Jennifer", "Helen", "Rachel").forEach(name -> {
	 * User user = new User(name, name.toLowerCase() + "@domain.com",25);
	 * userRepository.save(user); });
	 * userRepository.findAll().forEach(System.out::println); };
	 * 
	 * }
	 */

}
