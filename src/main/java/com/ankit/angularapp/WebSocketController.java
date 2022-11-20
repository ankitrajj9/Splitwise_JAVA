package com.ankit.angularapp;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
@CrossOrigin(origins = "localhost:4200")
public class WebSocketController {
	
	private final SimpMessagingTemplate template;
	
	@Autowired
	WebSocketController(SimpMessagingTemplate template){
		this.template=template;
	}
	@Autowired
    private UserRepository userRepository;
	
	@MessageMapping("/send/message")
	public void onReceivedMessage(String message) {
		System.out.println(message);
		byte[] decodedBytes = Base64.decodeBase64(message);
    	String decodedString = new String(decodedBytes);
    	User user = (User) userRepository.findById(new Long(decodedString.split("_")[1])).get();
		this.template.convertAndSend("/chat/"+decodedString.split("_")[0]+"/"+user.getEmail(),new SimpleDateFormat("HH:mm:ss").format(new Date())+"-"+message);
	}

}
