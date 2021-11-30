package cims;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class DeleteData {
	public Connection conn = null;
	public Scanner keyboard = null;
	
	public DeleteData(Connection conn, Scanner keyboard) {
		this.conn = conn;
		this.keyboard = keyboard;
	}
	
	public void Printmenu() {
		while(true) {
			System.out.println("정보삭제");
			System.out.println("1. 회원 탈퇴");
			System.out.println("2. 예약취소");
			System.out.println("3. 기저질환 정보삭제");
			System.out.println("4. 가게정보 삭제");
			System.out.println("5. 이전메뉴로");
			
			System.out.print("메뉴를 선택해 주세요 : ");
				
			int input = keyboard.nextInt();
			System.out.println("");
			
			if(input == 1) {
				DeleteClient(conn, keyboard);
			}
			else if(input == 2) {
				CancelReservation(conn, keyboard);
			}
			else if(input == 3) {
				Delete_dis(conn, keyboard);
			}
			else if(input == 4) {
				Delete_shop(conn, keyboard);
			}
			else if(input == 5) break;
			
			System.out.println("\n");
		}
	}
	
	//기저질환 정보 삭제
	public void Delete_dis(Connection conn, Scanner keyboard) {
		//for eliminating \n in console
		keyboard.nextLine();
		
		String sql = "";
		ResultSet rs = null;
		
		try {
			System.out.println("Enter your User number and diesease name.");
			System.out.print("user number : ");
			String u_number = keyboard.nextLine();
			
			System.out.print("disease : ");
			String disease = keyboard.nextLine();
			
			//입력 정보 확인
			sql = "SELECT *\n"
					+ "FROM UNDERLYING_DISEASE\n"
					+ "WHERE Unumber = '" + u_number + "'\n"
					+ "AND Disease = '" + disease +"'";
			Statement stmt = conn.prepareStatement(sql.toString(),ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			rs = stmt.executeQuery(sql);
			
			int size = -1;
			rs.last();
			size = rs.getRow();
			
			if(size == 0) {
				System.out.println("No results found.");
			}
			else {
				sql = "DELETE FROM UNDERLYING_DISEASE\n"
						+ "WHERE Unumber = '" + u_number + "'";
				
				stmt.execute(sql);
				System.out.print("Successfully deleted.");
			}
			
		}catch(SQLException ex2) {
			System.err.println("sql error = " + ex2.getMessage());
			System.exit(1);
		}
	}	
		
	//가게 정보 삭제
	public void Delete_shop(Connection conn, Scanner keyboard) {
		//for eliminating \n in console
		keyboard.nextLine();
		
		String sql = "";
		ResultSet rs = null;
		
		try {
			System.out.print("Enter shop number : ");
			String shop = keyboard.nextLine();
			
			//입력 정보 확인
			sql = "SELECT *\n"
					+ "FROM SHOP\n"
					+ "WHERE Snumber = '" + shop + "'";
			Statement stmt = conn.prepareStatement(sql.toString(),ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			rs = stmt.executeQuery(sql);
			
			int size = -1;
			rs.last();
			size = rs.getRow();
			
			if(size == 0) {
				System.out.println("No results found.");
			}
			else {
				sql = "DELETE FROM SHOP\n"
						+ "WHERE Snumber = '" + shop + "'";
				
				stmt.execute(sql);
				System.out.print("Successfully deleted.");
			}
			
		}catch(SQLException ex2) {
			System.err.println("sql error = " + ex2.getMessage());
			System.exit(1);
		}
	}	
		
	
	public void CancelReservation(Connection conn, Scanner keyboard) {
		String user_id, password;
		String unumber;
		String input;
		String sql;
		
		//for eliminating \n in console
		keyboard.nextLine();
		
		System.out.println("회원정보 수정");
		//ID input
		while(true){
			System.out.print("ID : ");
			user_id = keyboard.nextLine();
			
			//유효한 user_id인지 확인하고 unumber획득
			try {
				sql = "SELECT * FROM CLIENT WHERE USER_ID = ?";
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setString(1, user_id); 
				ResultSet rs = ps.executeQuery();
				
				if(!rs.next()) {
					System.out.println("존재하지 않는 ID 입니다.");
				}
				else {
					unumber = rs.getString(7);
					ps.close();
					rs.close();
					break;
				}
				
				ps.close();
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//password 확인
		while(true) {
			System.out.print("password : ");
			input = keyboard.nextLine();
			
			sql = "select * from client where user_id = ? and passwd = ?";
			PreparedStatement ps;
			try {
				ps = conn.prepareStatement(sql);
				ps.setString(1, user_id);
				ps.setString(2, input);
				ResultSet rs = ps.executeQuery();
				
				if(!rs.next()) {
					System.out.println("password가 틀렸습니다");
				}
				else {
					password = input;
					ps.close();
					break;
				}
				
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//예약 내역 출력
		sql = "SELECT * FROM RESERVATION WHERE unumber = ?";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, unumber);
			
			ResultSet rs = ps.executeQuery();
			
			if(!rs.next()) {
				System.out.println("예약 내역이 없습니다.");
				return;
			}
			else {
				System.out.println("예약 내역이 확인되었습니다.");
				System.out.println("병원id : " + rs.getString(2));
				System.out.println("접종 회차 : " + rs.getInt(4));
				System.out.println("접종 일시 : " + rs.getDate(1));
			}
			
			rs.close();
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//확인 메세지
		while(true) {
			System.out.print("위 예약을 정말 취소하시겠습니까?(Y/N) ");
			input = keyboard.nextLine();
			
			if(input.equals("Y")) {
				break;
			}
			else if(input.equals("N")) {
				return;
			}
		}

		
		//delete query 수행
		try {
			sql = "DELETE FROM RESERVATION WHERE unumber = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, unumber);
			
			int count = ps.executeUpdate();
			if(count == 0) {
				System.out.println("error occured when deleting data");
			}
			else {
				System.out.println("reservation data deleted");
			}
			
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void DeleteClient(Connection conn, Scanner keyboard) {
		String user_id, password;
		String input;
		String sql;
		
		//for eliminating \n in console
		keyboard.nextLine();
		
		System.out.println("회원정보 수정");
		//ID input
		while(true){
			System.out.print("ID : ");
			user_id = keyboard.nextLine();
			
			//유효한 user_id인지 확인하고 unumber획득
			try {
				sql = "SELECT * FROM CLIENT WHERE USER_ID = ?";
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setString(1, user_id); 
				ResultSet rs = ps.executeQuery();
				
				if(!rs.next()) {
					System.out.println("존재하지 않는 ID 입니다.");
				}
				else {
					ps.close();
					rs.close();
					break;
				}
				
				ps.close();
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//password 확인
		while(true) {
			System.out.print("password : ");
			input = keyboard.nextLine();
			
			sql = "select * from client where user_id = ? and passwd = ?";
			PreparedStatement ps;
			try {
				ps = conn.prepareStatement(sql);
				ps.setString(1, user_id);
				ps.setString(2, input);
				ResultSet rs = ps.executeQuery();
				
				if(!rs.next()) {
					System.out.println("password가 틀렸습니다");
				}
				else {
					password = input;
					ps.close();
					break;
				}
				
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//확인메세지
		while(true) {
			System.out.print("정말 회원정보를 삭제하시겠습니까?(Y/N) : ");
			input = keyboard.nextLine();
			if(input.equals("Y")) {
				break;
			}
			else if(input.equals("N")) {
				System.out.println("탈퇴를 취소합니다");
				return;
			}
		}
		
		//delete query
		sql = "delete from client c where user_id = ?";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, user_id);
			int count = ps.executeUpdate();
			
			if(count == 0) {
				System.out.println("error occured when deleting data");
			}
			else {
				System.out.println("client data deleted");
			}
			
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
