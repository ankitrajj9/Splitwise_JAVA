package com.ankit.angularapp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.Optional;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Service
public class CommonService {
	@Autowired
	private UserExpenseRepository userExpenseRepository;
	@Autowired
	private GroupUserRepository groupUserRepository;
	@Autowired
	private ExpenseRepository expenseRepository;
	@Autowired
	private PaymentRepository paymentRepository;
	@Autowired
	private UserRepository userRepository;
	
	public JsonArray getUserExpenseDetail(Long userId,Long groupId) {
		JsonArray jsonArray = new JsonArray();
    	JsonObject userExpenseObj = null;
    	List<User> groupUsers = groupUserRepository.getGroupUsersByGroupId(groupId);
    	List<Expense> myExpenses= expenseRepository.totalExpenseByMeWIthGroup(groupId);
    	
    	
		for(User user:groupUsers) {
			if(!user.getId().equals(userId)) {
				userExpenseObj= new JsonObject();
			String totalExpense="";
			List<UserExpense> paidByMeWithThis = userExpenseRepository.paidByMeWithThem(userId, user.getId(),groupId);
			List<UserExpense> paidByThemWithMe = userExpenseRepository.paidByThemWithMe(user.getId(), userId,groupId);
			BigDecimal spentByMeWithThemAndReturnedByThem = userExpenseRepository.spentByMeWithThemAndReturnedByThem(userId, user.getId(),groupId);
			BigDecimal spentByThemWithMeAndReturnedByMe = userExpenseRepository.spentByThemWithMeAndReturnedByMe(user.getId(), userId,groupId);
			BigDecimal totalPaidByMe=new BigDecimal(0);
			BigDecimal totalPaidByThem=new BigDecimal(0);
			BigDecimal totalWithThem=new BigDecimal(0);
			for(UserExpense userExpense:paidByMeWithThis) {
				if(userExpense.getCstatus() == 0) {
					totalPaidByMe=totalPaidByMe.add(userExpense.getAmount());
					totalWithThem = totalWithThem.add(userExpense.getAmount());
					if(spentByMeWithThemAndReturnedByThem != null && false) {
						totalPaidByMe = totalPaidByMe.subtract(spentByMeWithThemAndReturnedByThem);
						totalWithThem = totalWithThem.subtract(spentByMeWithThemAndReturnedByThem);
					}
				}
			}
			for(UserExpense userExpense:paidByThemWithMe) {
				if(userExpense.getCstatus() == 0) {
					totalPaidByThem=totalPaidByThem.add(userExpense.getAmount());
					totalWithThem = totalWithThem.subtract(userExpense.getAmount());
					if(spentByThemWithMeAndReturnedByMe != null && false) {
						totalPaidByThem = totalPaidByThem.subtract(spentByThemWithMeAndReturnedByMe);
						totalWithThem = totalWithThem.add(spentByThemWithMeAndReturnedByMe);
					}
				}
			}
			if(totalWithThem.compareTo(BigDecimal.ZERO) != 0) {
				String total = new Double(totalWithThem.toString()).toString();
				if(total.charAt(0) == '-') {
					total=total.substring(1, total.length());
					totalWithThem = new BigDecimal(total);
				}
			if(Double.parseDouble(totalPaidByMe.toString()) > Double.parseDouble(totalPaidByThem.toString())) {
				totalExpense = "You are Owed : "+totalWithThem +" INR with "+user.getName()+"("+user.getEmail()+")";
				userExpenseObj.addProperty("styling", "alert-success");
				userExpenseObj.addProperty("userIdDtl", user.getId());
				userExpenseObj.addProperty("userName", userRepository.findById(user.getId()).get().getName());
				if(paymentRepository.isSettleUpReqInitiated(userId,user.getId(), groupId) != null) {
					userExpenseObj.addProperty("reqDtl", "Settle Up Request Received");	
				}
				
			}else {
				totalExpense = "You owe : "+totalWithThem +" INR with "+user.getName()+"("+user.getEmail()+")";
				userExpenseObj.addProperty("styling", "alert-danger");
				userExpenseObj.addProperty("userIdDtl", user.getId());
				userExpenseObj.addProperty("userName", userRepository.findById(user.getId()).get().getName());
				if(paymentRepository.isSettleUpReqInitiated(user.getId(), userId, groupId) != null) {
					userExpenseObj.addProperty("reqDtl", "Settle Up Requested");
				}
				
			}
			userExpenseObj.addProperty("expenseDtl", totalExpense);
			jsonArray.add(userExpenseObj);
			}
			}
		}
		if(myExpenses != null && !myExpenses.isEmpty()) {
    		for(Expense expense:myExpenses){
    			String spentWith="";
    			Set<UserExpense> userExpenses = expense.getUserExpenses();
    			for(UserExpense userExpense:userExpenses) {
    				if(!userExpense.getUserId().getId().equals(userExpense.getCreatedBy().getId())) {
    				spentWith=spentWith+userExpense.getUserId().getName()+",";
    				}
    			}
    			spentWith = spentWith.substring(0, spentWith.length()-1);
    			userExpenseObj = new JsonObject();
    			if(expense.getUser().getId().equals(userId)) {
    				userExpenseObj.addProperty("expenseDtl", "You spent : "+expense.getAmount() +" INR for "+expense.getDescription()+" With ("+spentWith+") On "+expense.getCreatedOn().toLocaleString() );
    				userExpenseObj.addProperty("expenseName", expense.getDescription());
    				userExpenseObj.addProperty("userIdDtl", userId);
    				userExpenseObj.addProperty("userName", expense.getUser().getName());
    			}
    			else {
    				userExpenseObj.addProperty("expenseDtl", expense.getUser().getName()+"("+expense.getUser().getEmail()+")" +" spent : "+expense.getAmount() +" INR for "+expense.getDescription()+" With ("+spentWith+") On "+expense.getCreatedOn().toLocaleString());
    				userExpenseObj.addProperty("userIdDtl", expense.getUser().getId());
    				userExpenseObj.addProperty("expenseName", expense.getDescription());
    				userExpenseObj.addProperty("userName", expense.getUser().getName());
    			}
    			List<UserExpense> expensesSharedWithMe = userExpenseRepository.partOfExpense(expense.getExpenseId(), userId);
    			userExpenseObj.addProperty("styling", expensesSharedWithMe != null && !expensesSharedWithMe.isEmpty() ? "alert-warning" : "alert-dark");
    			jsonArray.add(userExpenseObj);
    		}
    	}
		return jsonArray;
	}
	public JsonObject getTotalBorrowedAmt(Long groupId,Long userId) {
		JsonObject obj = new JsonObject();
		BigDecimal lendedAmt=new BigDecimal(0);
		BigDecimal borrowedAmt=new BigDecimal(0);
		BigDecimal amount=new BigDecimal(0);
		List<UserExpense> totalLended = userExpenseRepository.getTotalLendedAmt(userId, groupId);
		List<UserExpense> totalBorrowed = userExpenseRepository.getTotalBorrowedAmt(userId, groupId);
		if(totalLended != null && !totalLended.isEmpty()) {
			for(UserExpense expense:totalLended) {
				lendedAmt = lendedAmt.add(expense.getAmount());
			}
		}
		if(totalBorrowed != null && !totalBorrowed.isEmpty()) {
			for(UserExpense expense:totalBorrowed) {
				borrowedAmt = borrowedAmt.add(expense.getAmount());
			}
		}
		if(borrowedAmt.compareTo(BigDecimal.ZERO) != 0) {
			if((lendedAmt.equals(borrowedAmt)) || (Double.parseDouble(borrowedAmt.toString()) < Double.parseDouble(lendedAmt.toString()))) {
				amount=new BigDecimal(0);
			}
			else {
				amount = borrowedAmt.subtract(lendedAmt);
			}
		}
		else {
			amount=new BigDecimal(0);
		}
		obj.addProperty("borrowedAmt", amount);
		return obj;
	}
	
	public JsonObject getTotalLendedAmt(Long groupId,Long userId) {
		JsonObject obj = new JsonObject();
		BigDecimal lendedAmt=new BigDecimal(0);
		BigDecimal borrowedAmt=new BigDecimal(0);
		BigDecimal amount=new BigDecimal(0);
		List<UserExpense> totalLended = userExpenseRepository.getTotalLendedAmt(userId, groupId);
		List<UserExpense> totalBorrowed = userExpenseRepository.getTotalBorrowedAmt(userId, groupId);
		if(totalLended != null && !totalLended.isEmpty()) {
			for(UserExpense expense:totalLended) {
				lendedAmt = lendedAmt.add(expense.getAmount());
			}
		}
		if(totalBorrowed != null && !totalBorrowed.isEmpty()) {
			for(UserExpense expense:totalBorrowed) {
				borrowedAmt = borrowedAmt.add(expense.getAmount());
			}
		}
		if(lendedAmt.compareTo(BigDecimal.ZERO) != 0) {
			if((lendedAmt.equals(borrowedAmt)) || (Double.parseDouble(lendedAmt.toString()) < Double.parseDouble(borrowedAmt.toString()))) {
				amount=new BigDecimal(0);
			}
			else {
				amount = lendedAmt.subtract(borrowedAmt);
			}
		}
		else {
			amount=new BigDecimal(0);
		}
		obj.addProperty("lendedAmt", amount);
		return obj;
	}
	public JsonObject getTotalExpenseByMe(Long groupId,Long userId) {
		JsonObject obj = new JsonObject();
		Double totalExpenseByMe = userExpenseRepository.getTotalExpenseByMe(userId, groupId);
		obj.addProperty("totalExpenseByMe", totalExpenseByMe == null ? 0 : totalExpenseByMe);
		return obj;
	}
	public BigDecimal[] divideExpense(String amt,int people) {
		BigDecimal[] retVal = new BigDecimal[2];
		BigDecimal amount = new BigDecimal(amt);
		BigDecimal divideBy = new BigDecimal(people);
		BigDecimal extra = amount.divide(divideBy, 2, RoundingMode.DOWN);
		if(!extra.multiply(new BigDecimal(people)).equals(new BigDecimal(amt))) {
			BigDecimal remainder = amount.subtract(extra.multiply(new BigDecimal(people)));
			retVal[0] = extra.add(remainder);
			retVal[1] = extra;
		}
		else {
			retVal[0] = extra.multiply(new BigDecimal(people));
			retVal[1] = extra.multiply(new BigDecimal(people));
		}
		return retVal;
	}
	
	/*
	 * public static void main(String[] args) { CommonService cm = new
	 * CommonService(); BigDecimal[] arr = cm.divideExpense("537", 7);
	 * System.out.println(arr[0].toString()); System.out.println(arr[1].toString());
	 * }
	 */
public JsonArray getSettleUpDetails(Long userId,Long groupId) {

	JsonArray retVal = new JsonArray();
	JsonObject userExpenseObj = null;
	List<User> groupUsers = groupUserRepository.getGroupUsersByGroupId(groupId);
	List<Expense> myExpenses= expenseRepository.totalExpenseByMeWIthGroup(groupId);
	
	if(myExpenses != null && !myExpenses.isEmpty()) {
		for(Expense expense:myExpenses){
			String spentWith="";
			Set<UserExpense> userExpenses = expense.getUserExpenses();
			for(UserExpense userExpense:userExpenses) {
				if(!userExpense.getUserId().getId().equals(userExpense.getCreatedBy().getId())) {
				spentWith=spentWith+userExpense.getUserId().getName()+",";
				}
			}
			spentWith = spentWith.substring(0, spentWith.length()-1);
			userExpenseObj = new JsonObject();
			if(expense.getUser().getId().equals(userId)) {
				userExpenseObj.addProperty("expenseDtl", "You spent : "+expense.getAmount() +" INR for "+expense.getDescription()+" With ("+spentWith+") On "+expense.getCreatedOn().toLocaleString() );
			}
			else {
				userExpenseObj.addProperty("expenseDtl", expense.getUser().getName()+"("+expense.getUser().getEmail()+")" +" spent : "+expense.getAmount() +" INR for "+expense.getDescription()+" With ("+spentWith+") On "+expense.getCreatedOn().toLocaleString());
			}
			List<UserExpense> expensesSharedWithMe = userExpenseRepository.partOfExpense(expense.getExpenseId(), userId);
			userExpenseObj.addProperty("styling", expensesSharedWithMe != null && !expensesSharedWithMe.isEmpty() ? "alert-warning" : "alert-dark");
			//jsonArray.add(userExpenseObj);
		}
	}
	for(User user:groupUsers) {
		if(!user.getId().equals(userId)) {
			userExpenseObj= new JsonObject();
		String totalExpense="";
		List<UserExpense> paidByMeWithThis = userExpenseRepository.paidByMeWithThem(userId, user.getId(),groupId);
		List<UserExpense> paidByThemWithMe = userExpenseRepository.paidByThemWithMe(user.getId(), userId,groupId);
		BigDecimal totalPaidByMe=new BigDecimal(0);
		BigDecimal totalPaidByThem=new BigDecimal(0);
		BigDecimal totalWithThem=new BigDecimal(0);
		for(UserExpense userExpense:paidByMeWithThis) {
			if(userExpense.getCstatus() == 0) {
				totalPaidByMe=totalPaidByMe.add(userExpense.getAmount());
				totalWithThem = totalWithThem.add(userExpense.getAmount());
			}
		}
		for(UserExpense userExpense:paidByThemWithMe) {
			if(userExpense.getCstatus() == 0) {
				totalPaidByThem=totalPaidByThem.add(userExpense.getAmount());
				totalWithThem = totalWithThem.subtract(userExpense.getAmount());
			}
		}
		if(totalWithThem.compareTo(BigDecimal.ZERO) != 0) {
			String total = new Double(totalWithThem.toString()).toString();
			if(total.charAt(0) == '-') {
				total=total.substring(1, total.length());
				totalWithThem = new BigDecimal(total);
			}
		if(Double.parseDouble(totalPaidByMe.toString()) > Double.parseDouble(totalPaidByThem.toString())) {
			totalExpense = "You are Owed : "+totalWithThem +" INR with "+user.getName()+"("+user.getEmail()+")";
			userExpenseObj.addProperty("styling", "alert-success");
		}else {
			JsonArray userDetails = new JsonArray();
			JsonObject userObj = new JsonObject();
			JsonObject debtObj = new JsonObject();
			userObj.addProperty("id", user.getId());
			userObj.addProperty("name", user.getName());
			userObj.addProperty("email", user.getEmail());
			debtObj.addProperty("debt", totalWithThem);
			debtObj.addProperty("reqStatus", paymentRepository.isSettleUpReqInitiated(user.getId(), userId, groupId) != null ? 1 : 0);
			userDetails.add(userObj);
			userDetails.add(debtObj);
			retVal.add(userDetails);
			totalExpense = "You owe : "+totalWithThem +" INR with "+user.getName()+"("+user.getEmail()+")";
			
		}
		userExpenseObj.addProperty("expenseDtl", totalExpense);
		//jsonArray.add(userExpenseObj);
		}
		}
	}
	return retVal;

}
public JsonArray getSettleUpRequestDetails(Long userId,Long groupId) {
	JsonArray arr = new JsonArray();
	List<Payment> settleUpRequests = paymentRepository.getSettleUpRequest(userId, groupId);
	if(settleUpRequests != null && !settleUpRequests.isEmpty()) {
		for(Payment payment:settleUpRequests) {
		JsonObject obj = new JsonObject();
		obj.addProperty("userId", payment.getFromId().getId());
		obj.addProperty("name", payment.getFromId().getName());
		obj.addProperty("amount", payment.getAmount());
		obj.addProperty("reqDtl", payment.getFromId().getName()+" Has Requested "+payment.getAmount().toString() +" INR SettleUp");
		arr.add(obj);
		}
	}
	return arr;
}

public JsonArray getGroupPaymentDetails(Long groupId,Long userId) {
	JsonArray arr = new JsonArray();
	List<Payment> payments = paymentRepository.getGroupPaymentDtls(groupId);
	if(payments != null && !payments.isEmpty()) {
		for(Payment payment:payments) {
		if(payment.getFromId().getId().equals(userId) || payment.getToId().getId().equals(userId)){
		JsonObject obj = new JsonObject();
		if(payment.getCstatus() == 1) {
		obj.addProperty("paymentDtl", payment.getFromId().getName()+" Paid "+payment.getAmount().toString() +" INR To "+payment.getToId().getName());
		obj.addProperty("styling", "alert-success");
		obj.addProperty("userIdDtl", payment.getFromId().getId());
		obj.addProperty("userName", payment.getFromId().getName());
		}
		else {
		obj.addProperty("paymentDtl", payment.getToId().getName()+" Rejected Request of "+payment.getAmount().toString() +" INR From "+payment.getFromId().getName());
		obj.addProperty("styling", "alert-danger");
		obj.addProperty("userIdDtl", payment.getToId().getId());
		obj.addProperty("userName", payment.getToId().getName());
		}
		if(payment.getRemarks() != null && !payment.getRemarks().equalsIgnoreCase("")) {
		obj.addProperty("remarkDtl", payment.getRemarks());
		}
		arr.add(obj);
		}
		}
	}
	return arr;
}
public JsonArray getGroupByExpenseOfUser(Long groupId) {
	JsonArray arr = null;
	List<Object[]> expenses = userExpenseRepository.getGroupByExpenseOfUser(groupId);
	if(expenses != null && !expenses.isEmpty()) {
		arr = new JsonArray();
		for(Object[] val:expenses) {
		JsonObject obj = new JsonObject();
		obj.addProperty("name",val[0].toString());
		obj.addProperty("value",  Double.parseDouble(val[1].toString()));
		arr.add(obj);
		}
	}
	return arr;
}

public boolean bothArePartOfGroup(Long groupId,Long[] userIds) {
	boolean user1 = groupUserRepository.isPartOfGroup(groupId, userIds[0]) != null;
	boolean user2 = groupUserRepository.isPartOfGroup(groupId, userIds[1]) != null;
	return user1 == true && user2 == true;
}

public JsonArray myShareDetails(Long groupId,Long userId) {
	JsonArray arr = new JsonArray();
	List<UserExpense> expenses = userExpenseRepository.getShareDetails(groupId,userId);
	if(expenses != null && !expenses.isEmpty()) {
		for(UserExpense val:expenses) {
		JsonObject obj = new JsonObject();
		obj.addProperty("shareDtl", val.getAmount()+" INR For "+val.getExpense().getDescription());
		arr.add(obj);
		}
	}
	return arr;
}
public static byte[] compressBytes(byte[] data) {
	Deflater deflater = new Deflater();
	deflater.setInput(data);
	deflater.finish();

	ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
	byte[] buffer = new byte[1024];
	while (!deflater.finished()) {
		int count = deflater.deflate(buffer);
		outputStream.write(buffer, 0, count);
	}
	try {
		outputStream.close();
	} catch (IOException e) {
	}
	System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);

	return outputStream.toByteArray();
}

// uncompress the image bytes before returning it to the angular application
public static byte[] decompressBytes(byte[] data) {
	Inflater inflater = new Inflater();
	inflater.setInput(data);
	ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
	byte[] buffer = new byte[1024];
	try {
		while (!inflater.finished()) {
			int count = inflater.inflate(buffer);
			outputStream.write(buffer, 0, count);
		}
		outputStream.close();
	} catch (IOException ioe) {
	} catch (DataFormatException e) {
	}
	return outputStream.toByteArray();
}
public JsonObject getOverAllBorrowedAmt(Long userId) {
	JsonObject obj = new JsonObject();
	BigDecimal lendedAmt=new BigDecimal(0);
	BigDecimal borrowedAmt=new BigDecimal(0);
	BigDecimal amount=new BigDecimal(0);
	List<UserExpense> totalLended = userExpenseRepository.getOverAllLendedAmt(userId);
	List<UserExpense> totalBorrowed = userExpenseRepository.getOverAllBorrowedAmt(userId);
	if(totalLended != null && !totalLended.isEmpty()) {
		for(UserExpense expense:totalLended) {
			lendedAmt = lendedAmt.add(expense.getAmount());
		}
	}
	if(totalBorrowed != null && !totalBorrowed.isEmpty()) {
		for(UserExpense expense:totalBorrowed) {
			borrowedAmt = borrowedAmt.add(expense.getAmount());
		}
	}
	if(borrowedAmt.compareTo(BigDecimal.ZERO) != 0) {
		if((lendedAmt.equals(borrowedAmt)) || (Double.parseDouble(borrowedAmt.toString()) < Double.parseDouble(lendedAmt.toString()))) {
			amount=new BigDecimal(0);
		}
		else {
			amount = borrowedAmt.subtract(lendedAmt);
		}
	}
	else {
		amount=new BigDecimal(0);
	}
	obj.addProperty("overAllBorrowedAmt", amount);
	return obj;
}

public JsonObject getOverAllLendedAmt(Long userId) {
	JsonObject obj = new JsonObject();
	BigDecimal lendedAmt=new BigDecimal(0);
	BigDecimal borrowedAmt=new BigDecimal(0);
	BigDecimal amount=new BigDecimal(0);
	List<UserExpense> totalLended = userExpenseRepository.getOverAllLendedAmt(userId);
	List<UserExpense> totalBorrowed = userExpenseRepository.getOverAllBorrowedAmt(userId);
	if(totalLended != null && !totalLended.isEmpty()) {
		for(UserExpense expense:totalLended) {
			lendedAmt = lendedAmt.add(expense.getAmount());
		}
	}
	if(totalBorrowed != null && !totalBorrowed.isEmpty()) {
		for(UserExpense expense:totalBorrowed) {
			borrowedAmt = borrowedAmt.add(expense.getAmount());
		}
	}
	if(lendedAmt.compareTo(BigDecimal.ZERO) != 0) {
		if((lendedAmt.equals(borrowedAmt)) || (Double.parseDouble(lendedAmt.toString()) < Double.parseDouble(borrowedAmt.toString()))) {
			amount=new BigDecimal(0);
		}
		else {
			amount = lendedAmt.subtract(borrowedAmt);
		}
	}
	else {
		amount=new BigDecimal(0);
	}
	obj.addProperty("overAllLendedAmt", amount);
	return obj;
}

public Date parseDateFromDB(Date dt) throws ParseException {
	Date parsedDate=new SimpleDateFormat("yyyy-MM-dd").parse(dt.toString().substring(0, 10));
	return parsedDate;
}
	
}
