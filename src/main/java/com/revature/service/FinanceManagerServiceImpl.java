package com.revature.service;

import java.util.List;

import com.revature.models.ReimbursementStatus;
import com.revature.models.Request;
import com.revature.repo.RequestDAO;

public class FinanceManagerServiceImpl implements FinanceManagerService {

	private RequestDAO rDao;
	
	public FinanceManagerServiceImpl(RequestDAO rDao) {
		this.rDao = rDao;
	}

	@Override
	public List<Request> viewAllRequests() {
		return rDao.selectAllRequests();
	}

	@Override
	public boolean updateRequestStatus(int id, ReimbursementStatus status) {
		return rDao.updateRequestStatus(id, status);
	}

}
