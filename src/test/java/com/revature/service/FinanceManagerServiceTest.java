package com.revature.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.revature.models.ReimbursementStatus;
import com.revature.models.ReimbursementType;
import com.revature.models.Request;
import com.revature.repo.RequestDAO;

public class FinanceManagerServiceTest {

	@Mock
	private RequestDAO rDao;

	private FinanceManagerService mService;

	@Before
	public void setup() {
		rDao = mock(RequestDAO.class);

		mService = new FinanceManagerServiceImpl(rDao);
	}

	@Test
	public void testViewAllRequests() {
		List<Request> fakeRequestList = new ArrayList<>();
		fakeRequestList.add(new Request(1, "realusername", ReimbursementType.FOOD, 0, null, Timestamp.valueOf(LocalDateTime.now()),
				ReimbursementStatus.REJECTED));
		fakeRequestList.add(new Request(2, "anotherusername", ReimbursementType.LODGING, 0, null, Timestamp.valueOf(LocalDateTime.now()),
				ReimbursementStatus.PENDING));
		fakeRequestList.add(new Request(3, "someone", ReimbursementType.FOOD, 0, null, Timestamp.valueOf(LocalDateTime.now()),
				ReimbursementStatus.APPROVED));
		fakeRequestList.add(new Request(4, "realusername", ReimbursementType.OTHER, 0, "test", Timestamp.valueOf(LocalDateTime.now()),
				ReimbursementStatus.APPROVED));
		fakeRequestList.add(new Request(5, "realperson", ReimbursementType.TRAVEL, 0, null, Timestamp.valueOf(LocalDateTime.now()),
				ReimbursementStatus.PENDING));

		when(rDao.selectAllRequests()).thenReturn(fakeRequestList);

		List<Request> requestList = new ArrayList<>();
		requestList = mService.viewAllRequests();

		assertEquals(requestList.get(0), fakeRequestList.get(0));
		assertEquals(requestList.get(1), fakeRequestList.get(1));
		assertEquals(requestList.get(2), fakeRequestList.get(2));
		assertEquals(requestList.get(3), fakeRequestList.get(3));
		assertEquals(requestList.get(4), fakeRequestList.get(4));

	}

	@Test
	public void testUpdateRequestStatus() {
		when(rDao.updateRequestStatus(1, ReimbursementStatus.APPROVED)).thenReturn(true);
		when(rDao.updateRequestStatus(1, ReimbursementStatus.REJECTED)).thenReturn(true);
		when(rDao.updateRequestStatus(1, ReimbursementStatus.PENDING)).thenReturn(false);
		when(rDao.updateRequestStatus(1, null)).thenReturn(false);
		when(rDao.updateRequestStatus(-1, ReimbursementStatus.APPROVED)).thenReturn(false);

		assertTrue(mService.updateRequestStatus(1, ReimbursementStatus.APPROVED));
		assertTrue(mService.updateRequestStatus(1, ReimbursementStatus.REJECTED));
		assertFalse(mService.updateRequestStatus(1, ReimbursementStatus.PENDING));
		assertFalse(mService.updateRequestStatus(1, null));
		assertFalse(mService.updateRequestStatus(-1, ReimbursementStatus.APPROVED));
	}
}
