package com.ankit.angularapp;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.jms.Queue;
import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@RestController
@CrossOrigin(origins = "localhost:4200")
//@CrossOrigin(origins = "splitter.ml")
public class UserController {
	
	@Autowired
    private UserRepository userRepository;
	@Autowired
	private HobbyRepository hobbyRepository;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	private UserFollowerRepository userFollowerRepository;
	@Autowired
	private GroupRepository groupRepository;
	@Autowired
	private GroupUserRepository groupUserRepository;
	@Autowired
	private ExpenseRepository expenseRepository;
	@Autowired
	private CommonService commonService;
	@Autowired
	private UserExpenseRepository userExpenseRepository;
	@Autowired
	private PaymentRepository paymentRepository;
	@Autowired
	private UserImageRepository userImageRepository;
	@Autowired
	private Queue queue;
	@Autowired
    private JmsTemplate jmsTemplate;
	@Autowired
	private MailService mailService;
	@Value("${send.external.mail}")
	private String sendExternalMail;

    @GetMapping("/users")
    public List<User> getUsers() {
    	System.out.println("get all user called");
        return (List<User>) userRepository.findAll();
    }

    @PostMapping("/saveuser")
    void addUser(@RequestBody User user) {
    	String mailUrl="";
    	System.out.println("save user called");
    	if(userRepository.getStudentByMailId(user.getEmail()) == null) {
    	String encodedPwd = bCryptPasswordEncoder.encode(user.getPassword()) ;
    	String encodedEmail = Base64.encodeBase64String(user.getEmail().getBytes());
    	mailUrl="http://splitter.ml/verifyemail/"+encodedEmail;
    	user.setPassword(encodedPwd);
    	user.setIsExternal(user.getIsExternal());
    	user.setCstatus(user.getCstatus());
    	Set<Hobby> hobbies = user.getHobbies();
    			hobbies.forEach(hobby -> hobby.setUser(user));
    	userRepository.save(user);
    	}
    	else {
    		User existingUser = userRepository.getStudentByMailId(user.getEmail());
    		String encodedPwd = bCryptPasswordEncoder.encode(user.getPassword()) ;
    		existingUser.setPassword(encodedPwd);
    		existingUser.setIsExternal(user.getIsExternal());
    		existingUser.setCstatus(user.getCstatus());
    		Set<Hobby> hobbies = user.getHobbies();
			hobbies.forEach(hobby -> hobby.setUser(existingUser));
			userRepository.save(existingUser);
    	}
		/*
		 * MessageConfigBean messageConfigBean = new MessageConfigBean(); int recId =
		 * Integer.parseInt(user.getId().toString());
		 * messageConfigBean.setFromMailId("ankitraj.raj82@gmail.com");
		 * messageConfigBean.setRecipientId(recId);
		 * messageConfigBean.setRecipientName(user.getName());
		 * messageConfigBean.setToMailId(user.getEmail());
		 * messageConfigBean.setMailSubject("Hello "+user.getName()
		 * +", Welcome To Splitter : ");
		 * messageConfigBean.setMailContent("<h1 style=\"color:blue;\">Hello "
		 * +user.getName()
		 * +"</h1><br><p style=\"color:red;\"> Please Click on The Below Link to Activate Your Account</p><br><a href=\""
		 * +mailUrl+"\">Verify Email</a>"); jmsTemplate.convertAndSend(queue,
		 * messageConfigBean);
		 */
    	//hobbyRepository.saveAll(hobbies);
    	if(sendExternalMail.equalsIgnoreCase("true")) {
    	mailService.sendMail("ankitraj.raj82@gmail.com", user.getEmail(), "Hello "+user.getName() +", Welcome To Splitter : ", "<h1 style=\"color:blue;\">Hello "
    			  +user.getName()
    			  +"</h1><br><p style=\"color:red;\"> Please Click on The Below Link to Activate Your Account</p><br><a href=\""
    			  +mailUrl+"\">Verify Email</a>");
    	}
    }
    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable("id") Long id) {
    	System.out.println("get by id called");
    	User user = (User) userRepository.findById(new Long(id)).get();
        return  user;
    }
    @GetMapping("/getUserByMailId/{emailId}")
    public User getUserByMailId(@PathVariable("emailId") String emailId) throws ParseException {
    	System.out.println("get by id called");
    	User user = (User) userRepository.getStudentByMailId(emailId);
    	Date parsedDate = commonService.parseDateFromDB(user.getDateOfBirth());
    	user.setDateOfBirth(parsedDate);
        return user;
    }
    
    @PutMapping("/users")
    void updateUser(@RequestBody User user) {
    	System.out.println("update user called");
    	String encodedPwd = bCryptPasswordEncoder.encode(user.getPassword()) ;
    	user.setPassword(encodedPwd);
    	hobbyRepository.deleteInBatch(userRepository.findById(user.getId()).get().getHobbies());
    	Set<Hobby> hobbies = user.getHobbies();
		hobbies.forEach(hobby -> hobby.setUser(user));
        userRepository.save(user);
    }
    @GetMapping("/test")
    public String getUserById() {
    	System.out.println("test called");
        return  "test";
    }
    @GetMapping("/searchusers/{param}/{fromMailId}")
    public List<Map<String,Object>> searchUsers(@PathVariable("param") String param,@PathVariable("fromMailId") String fromMailId) {
    	System.out.println("search users called");
    	User sessionUser = userRepository.getStudentByMailId(fromMailId);
    	Map<String,Object> map = null;
    	List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        List<User> users = userRepository.searchUser(param);
        if(users != null && !users.isEmpty()) {
        	for(User user:users) {
        		map = new HashMap<String,Object>();
        		map.put("id", user.getId());
        		map.put("name", user.getName());
        		map.put("email", user.getEmail());
        		map.put("followStatus", userFollowerRepository.userFollows(sessionUser.getId(), user.getId()) != null ? "Following" : "Not Following");
        		UserImage userImage = userImageRepository.getUserImage(user.getId());
        		if(userImage != null) {
        		byte[] imageBytes = commonService.decompressBytes(userImage.getImage());
    			userImage.setImage(imageBytes);
    			map.put("userImage", userImage);
    			}
        		list.add(map);
        	}
        }
        return list;
    }
    @PostMapping("/followuser")
    void followUser(@RequestParam String fromMailId,@RequestParam String toUserId) {
    	System.out.println("follow user called");
    	UserFollower userFollower = new UserFollower();
    	User follower = userRepository.getStudentByMailId(fromMailId);
    	User followed = userRepository.findById(new Long(toUserId)).get();
    	userFollower.setFollwerId(follower);
    	userFollower.setUserId(followed);
    	userFollower.setCreatedOn(new Date());
    	userFollowerRepository.save(userFollower);
    	//hobbyRepository.saveAll(hobbies);
    }
    @GetMapping("/userfollows/{sessionUserId}/{userId}")
    public boolean userFollows(@PathVariable("sessionUserId") String fromMailId,@PathVariable("userId") String toUserId) {
    	System.out.println("if user follows called");
    	User follower = userRepository.getStudentByMailId(fromMailId);
    	User followed = userRepository.findById(new Long(toUserId)).get();
        return userFollowerRepository.userFollows(follower.getId(), followed.getId()) != null;
    }
    @PostMapping("/unfollowuser")
    void unfollowUser(@RequestParam String fromMailId,@RequestParam String toUserId) {
    	System.out.println("unfollow user called");
    	User follower = userRepository.getStudentByMailId(fromMailId);
    	User followed = userRepository.findById(new Long(toUserId)).get();
    	UserFollower userFollower = userFollowerRepository.userFollows(follower.getId(), followed.getId());
    	userFollowerRepository.delete(userFollower);
    	//hobbyRepository.saveAll(hobbies);
    }
    @GetMapping("/getfollowingusers/{sessionUserId}")
    public List<Map<String,Object>> getFollowingUsers(@PathVariable("sessionUserId") String fromMailId) {
    	System.out.println("get following users called");
    	User follower = userRepository.getStudentByMailId(fromMailId);
    	Map<String,Object> map = null;
    	List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        List<User> users= userFollowerRepository.getFollowingUser(follower.getId());
        if(users != null && !users.isEmpty()) {
        	for(User user:users) {
        		map = new HashMap<String,Object>();
        		map.put("id", user.getId());
        		map.put("name", user.getName());
        		map.put("email", user.getEmail());
        		UserImage userImage = userImageRepository.getUserImage(user.getId());
        		if(userImage != null) {
        		byte[] imageBytes = commonService.decompressBytes(userImage.getImage());
    			userImage.setImage(imageBytes);
    			map.put("userImage", userImage);
    			}
        		list.add(map);
        	}
        }
        return list;
    }
    @GetMapping("/getfollowerusers/{sessionUserId}")
    public List<Map<String,Object>> getfollowerusers(@PathVariable("sessionUserId") String fromMailId) {
    	System.out.println("get follower users called");
    	Map<String,Object> map = null;
    	List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
    	User follower = userRepository.getStudentByMailId(fromMailId);
        List<User> users = userFollowerRepository.getFollowerUser(follower.getId());
        if(users != null && !users.isEmpty()) {
        	for(User user:users) {
        		map = new HashMap<String,Object>();
        		map.put("id", user.getId());
        		map.put("name", user.getName());
        		map.put("email", user.getEmail());
        		UserImage userImage = userImageRepository.getUserImage(user.getId());
        		if(userImage != null) {
        		byte[] imageBytes = commonService.decompressBytes(userImage.getImage());
    			userImage.setImage(imageBytes);
    			map.put("userImage", userImage);
    			}
        		list.add(map);
        	}
        }
        return list;
    }
    @PostMapping("/savegroup")
    void saveGroup(HttpServletRequest request) {
    	System.out.println("save group called");
    	JsonArray userArray = new JsonParser().parse(request.getParameter("params")).getAsJsonArray();
    	JsonObject groupObj = new JsonParser().parse(request.getParameter("groupDetail")).getAsJsonObject();
    	String sessionUserMail = request.getParameter("sessionUserEmail");
    	GroupMaster group = new GroupMaster();
    	Set<GroupUser> groupUsers = new HashSet<GroupUser>();
    	group.setGroupName(groupObj.get("groupName").getAsString());
    	group.setTotalExpense(new BigDecimal(0));
    	group.setTotalUsers(userArray.size()+1);
    	group.setCreatedBy(new Long(userRepository.getStudentByMailId(sessionUserMail).getId()));
    	group.setCreatedOn(new Date());
    	for(JsonElement jsonElement : userArray) {
    		JsonObject obj = jsonElement.getAsJsonObject();
    		String test = obj.get("name").getAsString();
    		System.out.println(test);
    		GroupUser groupUser = new GroupUser();
    		groupUser.setGroupMaster(group);
    		groupUser.setUser(new User(new Long(obj.get("id").getAsString())));
    		groupUser.setAddedOn(new Date());
    		groupUsers.add(groupUser);
    		}
    	User sessionUser = userRepository.getStudentByMailId(sessionUserMail);
    	GroupUser groupUser = new GroupUser();
		groupUser.setGroupMaster(group);
		groupUser.setUser(sessionUser);
		groupUser.setAddedOn(new Date());
		groupUsers.add(groupUser);
    	group.setGroupUsers(groupUsers);
    	groupRepository.save(group);
    	System.out.println("save group called");
    }
    @GetMapping("/grouplisting/{id}")
    public String getGroups(@PathVariable("id") String fromMailId) {
    	System.out.println("get all groups called");
    	User user = userRepository.getStudentByMailId(fromMailId);
    	List<GroupMaster> groups =groupRepository.findUserGroups(user.getId());
    	JsonArray jsonArray = new JsonArray();
    	for(GroupMaster group:groups) {
    	JsonArray arr = new JsonArray();
    	JsonObject groupDetail = new JsonObject(); 
    	groupDetail.addProperty("groupId", group.getGroupId());
    	groupDetail.addProperty("groupName",group.getGroupName()); 
    	groupDetail.addProperty("createdOn",group.getCreatedOn().toString()); 
    	groupDetail.addProperty("totalExpense",group.getTotalExpense()); 
    	groupDetail.addProperty("totalUsers",group.getTotalUsers()); 
    	groupDetail.addProperty("createdBy",group.getCreatedBy());
    	arr.add(groupDetail);
    	arr.add(commonService.getTotalBorrowedAmt(group.getGroupId(), userRepository.getStudentByMailId(fromMailId).getId()));
    	arr.add(commonService.getTotalLendedAmt(group.getGroupId(), userRepository.getStudentByMailId(fromMailId).getId()));
    	jsonArray.add(arr);
    	}
        return jsonArray.toString();
    }
	/*
	 * @GetMapping("/groupdetails/{groupId}/{mailId}") public String
	 * getGroupDetails(@PathVariable("groupId") Long groupId,@PathVariable("mailId")
	 * String fromMailId) { System.out.println("get group details called");
	 * StringBuilder sb = new StringBuilder(); List<GroupUser> groupUsers =
	 * groupUserRepository.getGroupUsers(groupId); groupUsers.forEach(groupUser ->
	 * sb.append(groupUser.getUser().getName()).append(",")); String paramUsers =
	 * sb.toString().substring(0,sb.toString().length()-1); JsonObject userDetails =
	 * new JsonObject(); userDetails.addProperty("userDetails", paramUsers);
	 * 
	 * GroupMaster group = (GroupMaster) groupRepository.getGroupById(groupId);
	 * JsonObject groupDetail = new JsonObject(); groupDetail.addProperty("groupId",
	 * group.getGroupId()); groupDetail.addProperty("groupName",
	 * group.getGroupId()); groupDetail.addProperty("createdOn",
	 * group.getGroupId()); groupDetail.addProperty("totalExpense",
	 * group.getGroupId()); groupDetail.addProperty("totalUsers",
	 * group.getGroupId()); groupDetail.addProperty("createdBy",
	 * group.getCreatedBy()); JsonArray jsonArray = new JsonArray();
	 * jsonArray.add(userDetails); jsonArray.add(groupDetail); return
	 * jsonArray.toString();
	 * 
	 * }
	 */
    @GetMapping("/getgroups")
    public List<GroupMaster>  getAllGroups(){
    	return groupRepository.findAll();
    }
    @GetMapping("/getgroup/{groupId}")
    public GroupMaster getGroup(@PathVariable("groupId") Long groupId) {
    	System.out.println("get all groups called");
        return groupRepository.findById(groupId).get();
    }
    @GetMapping("/groupdetails/{groupId}/{mailId}")
    public String getGroupDetails(@PathVariable("groupId") Long groupId,@PathVariable("mailId") String mailId)  {
    	GroupMaster group = groupRepository.findById(groupId).get();
    	Set<GroupUser> groupUsers = group.getGroupUsers();
    	List<String> userNames = groupUserRepository.getGroupUserNames(groupId);
    	JsonArray jsonArray = new JsonArray();
    	JsonObject userObj = new JsonObject();
    	StringBuilder sb = new StringBuilder();
    	userNames.forEach(userName -> sb.append(userName).append(","));
    	userObj.addProperty("userDetails", sb.toString().substring(0, sb.toString().length()-1));
    	JsonObject groupDetail = new JsonObject(); 
    	groupDetail.addProperty("groupId", group.getGroupId());
    	groupDetail.addProperty("groupName",group.getGroupName()); 
    	groupDetail.addProperty("createdOn",group.getCreatedOn().toString()); 
    	groupDetail.addProperty("totalExpense",group.getTotalExpense()); 
    	groupDetail.addProperty("totalUsers",group.getTotalUsers()); 
    	groupDetail.addProperty("createdBy",group.getCreatedBy());
    	jsonArray.add(userObj);
    	jsonArray.add(groupDetail);
    	jsonArray.add(commonService.getUserExpenseDetail(userRepository.getStudentByMailId(mailId).getId(), groupId));
    	jsonArray.add(commonService.getTotalBorrowedAmt(groupId, userRepository.getStudentByMailId(mailId).getId()));
    	jsonArray.add(commonService.getTotalLendedAmt(groupId, userRepository.getStudentByMailId(mailId).getId()));
    	jsonArray.add(commonService.getTotalExpenseByMe(groupId, userRepository.getStudentByMailId(mailId).getId()));
    	jsonArray.add(commonService.getSettleUpRequestDetails(userRepository.getStudentByMailId(mailId).getId(), groupId));
    	jsonArray.add(commonService.getGroupPaymentDetails(groupId,userRepository.getStudentByMailId(mailId).getId()));
    	jsonArray.add(commonService.getGroupByExpenseOfUser(groupId));
    	jsonArray.add(commonService.myShareDetails(groupId,userRepository.getStudentByMailId(mailId).getId()));
    	return jsonArray.toString();
    	
    }
    @GetMapping("/getgroupusers/{groupId}")
    public List<User> getGroupUsers(@PathVariable("groupId") Long groupId) {
    	System.out.println("get all group users called");
        return groupUserRepository.getGroupUsersByGroupId(groupId);
    }
    @PostMapping("/addexpense")
    void addExpense(HttpServletRequest request) {
    	System.out.println("add expense called");
    	String sessionUserMail = request.getParameter("sessionUserEmail");
    	String groupId = request.getParameter("groupId");
    	GroupMaster group = groupRepository.findById(new Long(groupId)).get();
    	Set<UserExpense> userExpenses = new HashSet<UserExpense>();
    	User sessionUser = userRepository.getStudentByMailId(sessionUserMail);
    	JsonArray userArray = new JsonParser().parse(request.getParameter("users")).getAsJsonArray();
    	JsonObject expenseDetail = new JsonParser().parse(request.getParameter("expenseDetail")).getAsJsonObject();
    	BigDecimal groupTotal = group.getTotalExpense().add(new BigDecimal(expenseDetail.get("amount").getAsString()));
    	group.setTotalExpense(groupTotal);
    	groupRepository.save(group);
    	Expense expense = new Expense();
    	expense.setDescription(expenseDetail.get("description").getAsString());
    	expense.setAmount(new BigDecimal(expenseDetail.get("amount").getAsString()));
    	expense.setCreatedOn(new Date());
    	expense.setGroupMaster(group);
    	expense.setUser(sessionUser);
    	BigDecimal[] splitAmt = commonService.divideExpense(expenseDetail.get("amount").getAsString(), userArray.size()+1);
    	UserExpense creatorExpense = new UserExpense();
    	creatorExpense.setAmount(splitAmt[0]);
    	creatorExpense.setCreatedBy(userRepository.getStudentByMailId(sessionUserMail));
    	creatorExpense.setCreatedOn(new Date());
    	creatorExpense.setExpense(expense);
    	creatorExpense.setGroup(group);
    	creatorExpense.setUserId(userRepository.getStudentByMailId(sessionUserMail));
    	creatorExpense.setCstatus(1);
		userExpenses.add(creatorExpense);
    	for(JsonElement jsonElement : userArray) {
    		JsonObject obj = jsonElement.getAsJsonObject();
    		String test = obj.get("name").getAsString();
    		System.out.println(test);
    		UserExpense userExpense = new UserExpense();
    		userExpense.setAmount(splitAmt[1]);
    		userExpense.setCreatedBy(userRepository.getStudentByMailId(sessionUserMail));
    		userExpense.setCreatedOn(new Date());
    		userExpense.setExpense(expense);
    		userExpense.setGroup(group);
    		userExpense.setUserId(new User(new Long(obj.get("id").getAsString())));
    		userExpense.setCstatus(0);
    		userExpenses.add(userExpense);
    		}
    	expense.setUserExpenses(userExpenses);
    	expenseRepository.save(expense);
    	System.out.println("user and expenses saved ...");
    }
    @GetMapping("/getdetails/{mailId}")
    public String getDetails(@PathVariable("mailId") String mailId) {
    	User user = userRepository.getStudentByMailId(mailId);
    	JsonArray arr = new JsonArray();
    	JsonObject getFollwingLen = new JsonObject();
    	getFollwingLen.addProperty("followingLen", userFollowerRepository.getFollowingUser(user.getId()).size());
    	arr.add(getFollwingLen);
    	JsonObject getFollwerLen = new JsonObject();
    	getFollwerLen.addProperty("followerLen", userFollowerRepository.getFollowerUser(user.getId()).size());
    	arr.add(getFollwerLen);
    	arr.add(commonService.getOverAllLendedAmt(user.getId()));
    	arr.add(commonService.getOverAllBorrowedAmt(user.getId()));
    	return arr.toString();
    }
    @GetMapping("/getSharedWithdetails/{userId}/{mailId}")
    public List<GroupMaster> getSharedWIthDetails(@PathVariable("userId") Long userId,@PathVariable("mailId") String mailId) {
    	List<GroupMaster> mutualRetGroups=null;
    	Long[] userIds = new Long[2];
    	userIds[0]=userId;
    	userIds[1]=userRepository.getStudentByMailId(mailId).getId();
    	List<GroupMaster> mutualGroups = groupUserRepository.getMutualGroups(userIds);
    	if(mutualGroups != null && !mutualGroups.isEmpty()) {
    		mutualRetGroups = new ArrayList<GroupMaster>();
    	for(GroupMaster group:mutualGroups) {
    		if(commonService.bothArePartOfGroup(group.getGroupId(), userIds)) {
    			mutualRetGroups.add(group);
    		}
    	}
    	}
    	return mutualRetGroups;
    }
    @GetMapping("/getsettleupdetails/{groupId}/{mailId}")
    public String getSettleUpDetails(@PathVariable("groupId") Long groupId,@PathVariable("mailId") String mailId) {
    	JsonArray arr = commonService.getSettleUpDetails(userRepository.getStudentByMailId(mailId).getId(),groupId);
    	return arr.toString();
    }
    
    @PutMapping("/settleuprequest")
    void settleUp(HttpServletRequest request) {
    	System.out.println("settle up called");
    	String groupId = request.getParameter("groupId");
    	String sessionUserEmail = request.getParameter("sessionUserEmail");
    	String toId = request.getParameter("toId");
    	String flag = request.getParameter("flag");
    	String amount = request.getParameter("amount");
    	String remarks = request.getParameter("remarks");
    	if(Integer.parseInt(flag) == 0) {
    	Payment payment = new Payment();
    	payment.setFromId(userRepository.getStudentByMailId(sessionUserEmail));
    	payment.setToId(new User(new Long(toId)));
    	payment.setAmount(new BigDecimal(amount));
    	payment.setCstatus(0);  //0 requested 1 approved 2 rejected
    	payment.setCreatedOn(new Date());
    	payment.setGroupId(groupRepository.findById(new Long(groupId)).get());
    	paymentRepository.save(payment);
    	}
    	else if(Integer.parseInt(flag) == 1){
    	List<UserExpense> getSettleUpDetails = userExpenseRepository.getSettleUpDetails(new Long(groupId),userRepository.getStudentByMailId(sessionUserEmail).getId() , new Long(toId));
    	if(getSettleUpDetails != null && !getSettleUpDetails.isEmpty()) {
    	for(UserExpense userExpense:getSettleUpDetails) {
    		userExpense.setCstatus(Integer.parseInt(flag)); 
    		userExpenseRepository.save(userExpense); 
    		}
    	}
    	Payment payment = paymentRepository.isSettleUpReqInitiated(userRepository.getStudentByMailId(sessionUserEmail).getId(),new Long(toId), new Long(groupId));
    	if(payment != null) {
		payment.setCstatus(1);
		payment.setRemarks(remarks);
		paymentRepository.save(payment);
    	}
    	}
    	else {
    		Payment payment = paymentRepository.isSettleUpReqInitiated(userRepository.getStudentByMailId(sessionUserEmail).getId(),new Long(toId), new Long(groupId));
    		if(payment != null) {
    		payment.setCstatus(2);
    		payment.setRemarks(remarks);
    		paymentRepository.save(payment);
    		}
    	}
		
    }
    
    @PostMapping("/uploadImage")
    public void uploadImage(@RequestParam("image") MultipartFile file,@RequestParam("userId") Long userId)
            throws IOException {
    	UserImage toDelete = userImageRepository.getUserImage(userId);
    	if(toDelete != null) {
    	userImageRepository.delete(toDelete);
    	}
    	UserImage userImage = new UserImage();
    	userImage.setImage(commonService.compressBytes(file.getBytes()));
    	userImage.setUser(new User(new Long(userId)));
    	userImage.setImageDescription("test image");
    	userImage.setImagePath("test path");
    	userImage.setImageType(file.getContentType());
    	userImageRepository.save(userImage);
    }
    

	
    @GetMapping("/getUserImages/{userId}")
	public UserImage getImage(@PathVariable("userId") Long userId) throws IOException {
    	UserImage img = null;
    	UserImage userImage = userImageRepository.getUserImage(userId);
    	if(userImage != null) {
		 img = new UserImage(userImage.getImageDescription(), userImage.getImagePath(),userImage.getImageType(),
				commonService.decompressBytes(userImage.getImage()));
		}
    	return img;
	}
    
    @GetMapping("/getAllUserImages/{groupId}")
	public Map<String,UserImage> getGroupUserImage(@PathVariable("groupId") Long groupId) throws IOException {
    	Map<String,UserImage> map = null;
    	List<UserImage> userImages = userImageRepository.getGroupUserImage(groupId);
    	if(userImages != null && !userImages.isEmpty()) {
    		map = new HashMap<String,UserImage>();
    		for(UserImage userImage:userImages) {
    			byte[] imageBytes = commonService.decompressBytes(userImage.getImage());
    			userImage.setImage(imageBytes);
    			map.put(userImage.getUser().getId().toString(), userImage);
    		}
    	}
    	return map;
	}
    @GetMapping("/verifyemail/{encodedMailId}")
    public String verifyEmail(@PathVariable("encodedMailId") String encodedMailId) {
    	String msg="0";
    	try {
    	System.out.println("verify mailId called");
    	byte[] decodedBytes = Base64.decodeBase64(encodedMailId);
    	String decodedString = new String(decodedBytes);
    	User user = userRepository.getStudentByMailId(decodedString);
    	if(user != null) {
    		if(user.getCstatus() != 1) {
    	user.setCstatus(1);
    	msg="1";  //email verified
    	userRepository.save(user);
    		}else {
    			msg="2"; //email already verified
    		}
    	}
        return msg;
    	}
    	catch(Exception e) {
    		return msg;
    	}
    }
    
    @PostMapping("/resetpassword")
    public void resetPassword(@RequestParam("emailId") String emailId) {
    	System.out.println("Reset Password called");
    	User user = userRepository.getStudentByMailId(emailId);
    	String encodedEmail = Base64.encodeBase64String(user.getEmail().getBytes());
    	String mailUrl="http://splitter.ml/resetpassword/"+encodedEmail;
    	if(user != null) {
			/*
			 * MessageConfigBean messageConfigBean = new MessageConfigBean(); int recId =
			 * Integer.parseInt(user.getId().toString());
			 * messageConfigBean.setFromMailId("ankitraj.raj82@gmail.com");
			 * messageConfigBean.setRecipientId(recId);
			 * messageConfigBean.setRecipientName(user.getName());
			 * messageConfigBean.setToMailId(user.getEmail());
			 * messageConfigBean.setMailSubject("Hello "+user.getName()
			 * +", Reset Splitter Password ");
			 * messageConfigBean.setMailContent("<h1 style=\"color:blue;\">Hello "
			 * +user.getName()
			 * +"</h1><br><p style=\"color:red;\"> Please Click on The Below Link to Reset Password</p><br><a href=\""
			 * +mailUrl+"\">Reset Password</a>"); jmsTemplate.convertAndSend(queue,
			 * messageConfigBean);
			 */
    		mailService.sendMail("ankitraj.raj82@gmail.com", user.getEmail(), "Hello "+user.getName() +", Reset Splitter Password ", "<h1 style=\"color:blue;\">Hello " +user.getName() +"</h1><br><p style=\"color:red;\"> Please Click on The Below Link to Reset Password</p><br><a href=\""+mailUrl+"\">Reset Password</a>");
    	}
    	
    }
    @PostMapping("/changepassword")
    void changePassword(@RequestParam("encodedMailId") String encodedMailId,@RequestParam("password") String password) {
    	System.out.println("change password called");
    	String encodedPwd = bCryptPasswordEncoder.encode(password) ;
    	byte[] decodedBytes = Base64.decodeBase64(encodedMailId);
    	String decodedString = new String(decodedBytes);
    	User user =userRepository.getStudentByMailId(decodedString);
    	if(user != null) {
    		user.setPassword(encodedPwd);
    		userRepository.save(user);
    	}
    }
    
    @GetMapping("/emailExists/{emailId}")
    public boolean emailExists(@PathVariable("emailId") String emailId) {
    	System.out.println("emailExists called");
    	User user = (User) userRepository.getStudentByMailId(emailId);
    	return user != null;
    }
}