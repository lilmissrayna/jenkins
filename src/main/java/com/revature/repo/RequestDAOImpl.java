package com.revature.repo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.revature.models.ReimbursementStatus;
import com.revature.models.ReimbursementType;
import com.revature.models.Request;
import com.revature.util.ConnectionFactory;

public class RequestDAOImpl implements RequestDAO {

	@Override
	public Request selectRequestById(int id) {

		String sql = "SELECT * FROM reimbursement WHERE id = ?";
		Request selectedRequestById = new Request();
		try (Connection conn = ConnectionFactory.getConnection()) {

			PreparedStatement ps = conn.prepareStatement(sql);

			ps.setInt(1, id);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				selectedRequestById = new Request(rs.getInt("id"), rs.getString("user_username_fk"),
						ReimbursementType.valueOf(rs.getString("reimbursement_type")), rs.getDouble("amount"),
						rs.getString("description"), rs.getTimestamp("time_of_request"),
						ReimbursementStatus.valueOf(rs.getString("status")));

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return selectedRequestById;

	}

	@Override
	public List<Request> selectAllRequests() {

		String sql = "SELECT * FROM reimbursement";
		List<Request> allRequests = new ArrayList<Request>();
		try (Connection conn = ConnectionFactory.getConnection()) {

			PreparedStatement ps = conn.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				allRequests.add(new Request(rs.getInt("id"), rs.getString("user_username_fk"),
						ReimbursementType.valueOf(rs.getString("reimbursement_type")), rs.getDouble("amount"),
						rs.getString("description"), rs.getTimestamp("time_of_request"),
						ReimbursementStatus.valueOf(rs.getString("status"))));

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return allRequests;
	}

	@Override
	public List<Request> selectRequestByUsernameAndStatus(String username, ReimbursementStatus status) {
		String sql = "SELECT * FROM reimbursement WHERE user_username_fk = ? AND status = ?::reimbursement_status";
		List<Request> selectedRequestByUsernameAndStatus = new ArrayList<Request>();
		try (Connection conn = ConnectionFactory.getConnection()) {

			PreparedStatement ps = conn.prepareStatement(sql);

			ps.setString(1, username);
			ps.setString(2, status.name());

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				selectedRequestByUsernameAndStatus.add(new Request(rs.getInt("id"), rs.getString("user_username_fk"),
						ReimbursementType.valueOf(rs.getString("reimbursement_type")), rs.getDouble("amount"),
						rs.getString("desciption"), rs.getTimestamp("time_of_request"),
						ReimbursementStatus.valueOf(rs.getString("status"))));
 
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return selectedRequestByUsernameAndStatus;
	}

	@Override
	public List<Request> selectPastRequestByUsername(String username) {
		String sql = "SELECT * FROM reimbursement WHERE user_username_fk = ? AND (status = ?::reimbursement_status OR status = ?::reimbursement_status)";
		List<Request> selectedPastRequestByUsername = new ArrayList<Request>();
		try (Connection conn = ConnectionFactory.getConnection()) {
			PreparedStatement ps = conn.prepareStatement(sql);

			ps.setString(1, username);
			ps.setString(2, ReimbursementStatus.APPROVED.name());
			ps.setString(3, ReimbursementStatus.REJECTED.name());
			
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				selectedPastRequestByUsername.add(new Request(rs.getInt("id"), rs.getString("user_username_fk"),
						ReimbursementType.valueOf(rs.getString("reimbursement_type")), rs.getDouble("amount"),
						rs.getString("description"), rs.getTimestamp("time_of_request"),
						ReimbursementStatus.valueOf(rs.getString("status"))));

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return selectedPastRequestByUsername;
	}

	@Override
	public boolean updateRequestStatus(int id, ReimbursementStatus status) {
		boolean success = false;
		String sql = "UPDATE reimbursement SET status = ?::reimbursement_status WHERE id = ?;";
		try (Connection conn = ConnectionFactory.getConnection()) {
			PreparedStatement updateStatus = conn.prepareStatement(sql);
			updateStatus.setString(1, status.name());
			updateStatus.setInt(2, id);
			updateStatus.execute();

			sql = "SELECT * FROM reimbursement WHERE id = ? AND status = ?::reimbursement_status";
			PreparedStatement confirmUpdate = conn.prepareStatement(sql);
			confirmUpdate.setInt(1, id);
			confirmUpdate.setString(2, status.name());
			success = confirmUpdate.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return success;
	}

	@Override
	public boolean insertRequest(String username, ReimbursementType type, double amount, String description) {

		boolean success = false;
		Timestamp timeOfRequest = Timestamp.valueOf(LocalDateTime.now());
		try (Connection conn = ConnectionFactory.getConnection()) {

			String sql = "INSERT INTO reimbursement(user_username_fk,reimbursement_type,amount,desciption,time_of_request,status) "
					+ "VALUES( ?, ?::reimbursement_types, ?, ?, ?, ?::reimbursement_status );";
			PreparedStatement insertStatement = conn.prepareStatement(sql);
			insertStatement.setString(1, username);
			insertStatement.setString(2, type.name());
			insertStatement.setDouble(3, amount);
			insertStatement.setString(4, description);
			insertStatement.setTimestamp(5, timeOfRequest);
			insertStatement.setString(6, ReimbursementStatus.PENDING.name());
			insertStatement.execute();

			sql = "SELECT * FROM reimbursement WHERE user_username_fk = ? AND time_of_request = ?;";
			PreparedStatement confirmInsert = conn.prepareStatement(sql);
			confirmInsert.setString(1, username);
			confirmInsert.setTimestamp(2, timeOfRequest);
			success = confirmInsert.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return success;
	}

	@Override
	public boolean deleteRequest(int id) {
		boolean success = false;
		String sql = "DELETE FROM reimbursement WHERE id =?;";
		try (Connection conn = ConnectionFactory.getConnection()) {
			PreparedStatement updateStatus = conn.prepareStatement(sql);
			updateStatus.setInt(1, id);
			updateStatus.execute();

			sql = "SELECT * FROM reimbursement WHERE id = ?;";
			PreparedStatement confirmDelete = conn.prepareStatement(sql);
			confirmDelete.setInt(1, id);
			success = !(confirmDelete.execute());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return success;
	}

}
