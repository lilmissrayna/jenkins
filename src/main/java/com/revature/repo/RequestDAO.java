package com.revature.repo;
import java.util.List;

import com.revature.models.Request;
import com.revature.models.ReimbursementStatus;
import com.revature.models.ReimbursementType;

public interface RequestDAO {

	Request selectRequestById(int id);
	List<Request> selectAllRequests();
	List<Request> selectRequestByUsernameAndStatus(String username, ReimbursementStatus status);
	List<Request> selectPastRequestByUsername(String username);
	boolean updateRequestStatus(int id, ReimbursementStatus status);
	boolean insertRequest(String username, ReimbursementType type, double amount, String description);
	boolean deleteRequest(int id);
}
