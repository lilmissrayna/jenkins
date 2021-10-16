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
import com.revature.models.User;
import com.revature.repo.RequestDAO;
import com.revature.repo.UserDAO;

public class EmployeeServiceTest {

	@Mock
	private UserDAO uDao;
	@Mock
	private RequestDAO rDao;

	private EmployeeService eService;

	@Before
	public void setup() {
		uDao = mock(UserDAO.class);
		rDao = mock(RequestDAO.class);
		eService = new EmployeeServiceImpl(uDao, rDao);
	}

	@Test
	public void testCheckIfUserExists() {
		List<User> fakeUserList = new ArrayList<>();
		fakeUserList.add(new User("realusername", "password", "email@email.com"));

		when(uDao.selectAllUsers()).thenReturn(fakeUserList);

		assertTrue(eService.checkIfUsernameExists("realusername"));
		assertFalse(eService.checkIfUsernameExists("fake"));
	}

	@Test
	public void testAuthenticate() {

		User fakeUser = new User("realusername", "password", "email@email.com");

		when(uDao.selectUser("realusername")).thenReturn(fakeUser);

		assertTrue(eService.authenticate("realusername", "password"));
		assertFalse(eService.authenticate("realusername", "badpassword"));
		assertFalse(eService.authenticate("fake", "password"));
		assertFalse(eService.authenticate("fake", "fakepassword"));
	}

	@Test
	public void testLogin() {
		User fakeUser = new User("realusername", "password", "email@email.com");

		when(uDao.selectUser("realusername")).thenReturn(fakeUser);

		User user = eService.login("realusername");

		assertEquals(user.getUsername(), fakeUser.getUsername());
		assertEquals(user.getPassword(), fakeUser.getPassword());
		assertEquals(user.getEmail(), fakeUser.getEmail());
	}

	@Test
	public void testRequestReimbursement() {
		when(rDao.insertRequest("realusername", ReimbursementType.OTHER, 0, "test")).thenReturn(true);
		when(rDao.insertRequest("realusername", ReimbursementType.FOOD, 0, null)).thenReturn(true);
		when(rDao.insertRequest("realusername", ReimbursementType.LODGING, 0, null)).thenReturn(true);
		when(rDao.insertRequest("realusername", ReimbursementType.TRAVEL, 0, null)).thenReturn(true);
		when(rDao.insertRequest("realusername", ReimbursementType.OTHER, 0, null)).thenReturn(false);
		when(rDao.insertRequest("realusername", null, 0, null)).thenReturn(false);
		when(rDao.insertRequest(null, ReimbursementType.OTHER, 0, "test")).thenReturn(false);

		assertTrue(eService.requestReimbursment("realusername", ReimbursementType.OTHER, 0, "test"));
		assertTrue(eService.requestReimbursment("realusername", ReimbursementType.FOOD, 0, null));
		assertTrue(eService.requestReimbursment("realusername", ReimbursementType.LODGING, 0, null));
		assertTrue(eService.requestReimbursment("realusername", ReimbursementType.TRAVEL, 0, null));
		assertFalse(eService.requestReimbursment("realusername", ReimbursementType.OTHER, 0, null));
		assertFalse(eService.requestReimbursment("realusername", null, 0, null));
		assertFalse(eService.requestReimbursment(null, ReimbursementType.OTHER, 0, "test"));
	}

	@Test
	public void testViewPendingRequests() {
		List<Request> fakeRequestList = new ArrayList<>();
		Request fakeRequest = new Request(-1, "realusername", ReimbursementType.FOOD, 0, null, Timestamp.valueOf(LocalDateTime.now()),
				ReimbursementStatus.PENDING);
		fakeRequestList.add(fakeRequest);

		when(rDao.selectRequestByUsernameAndStatus("realusername", ReimbursementStatus.PENDING))
				.thenReturn(fakeRequestList);

		List<Request> requestList = new ArrayList<>();
		requestList = rDao.selectRequestByUsernameAndStatus("realusername", ReimbursementStatus.PENDING);

		assertEquals(requestList.get(0).getUsername(), fakeRequestList.get(0).getUsername());
		assertEquals(requestList.get(0).getId(), fakeRequestList.get(0).getId());
		assertEquals(requestList.get(0).getStatus(), ReimbursementStatus.PENDING);
	}

	@Test
	public void testViewPastRequests() {
		List<Request> fakeRequestList = new ArrayList<>();
		fakeRequestList.add(new Request(1, "realusername", ReimbursementType.FOOD, 0, null, Timestamp.valueOf(LocalDateTime.now()),
				ReimbursementStatus.APPROVED));
		fakeRequestList.add(new Request(2, "realusername", ReimbursementType.LODGING, 0, null, Timestamp.valueOf(LocalDateTime.now()),
				ReimbursementStatus.APPROVED));
		fakeRequestList.add(new Request(3, "realusername", ReimbursementType.FOOD, 0, null, Timestamp.valueOf(LocalDateTime.now()),
				ReimbursementStatus.REJECTED));
		fakeRequestList.add(new Request(4, "realusername", ReimbursementType.OTHER, 0, "test", Timestamp.valueOf(LocalDateTime.now()),
				ReimbursementStatus.REJECTED));
		fakeRequestList.add(new Request(5, "realusername", ReimbursementType.TRAVEL, 0, null, Timestamp.valueOf(LocalDateTime.now()),
				ReimbursementStatus.APPROVED));

		when(rDao.selectPastRequestByUsername("realusername")).thenReturn(fakeRequestList);

		List<Request> requestList = new ArrayList<>();
		requestList = eService.viewPastRequests("realusername");

		assertEquals(requestList.get(0), fakeRequestList.get(0));
		assertEquals(requestList.get(1), fakeRequestList.get(1));
		assertEquals(requestList.get(2), fakeRequestList.get(2));
		assertEquals(requestList.get(3), fakeRequestList.get(3));
		assertEquals(requestList.get(4), fakeRequestList.get(4));
	}
}
	
