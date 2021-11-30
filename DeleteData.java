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
			System.out.println("��������");
			System.out.println("1. ȸ�� Ż��");
			System.out.println("2. �������");
			System.out.println("3. ������ȯ ��������");
			System.out.println("4. �������� ����");
			System.out.println("5. �����޴���");
			
			System.out.print("�޴��� ������ �ּ��� : ");
				
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
	
	//������ȯ ���� ����
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
			
			//�Է� ���� Ȯ��
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
		
	//���� ���� ����
	public void Delete_shop(Connection conn, Scanner keyboard) {
		//for eliminating \n in console
		keyboard.nextLine();
		
		String sql = "";
		ResultSet rs = null;
		
		try {
			System.out.print("Enter shop number : ");
			String shop = keyboard.nextLine();
			
			//�Է� ���� Ȯ��
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
		
		System.out.println("ȸ������ ����");
		//ID input
		while(true){
			System.out.print("ID : ");
			user_id = keyboard.nextLine();
			
			//��ȿ�� user_id���� Ȯ���ϰ� unumberȹ��
			try {
				sql = "SELECT * FROM CLIENT WHERE USER_ID = ?";
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setString(1, user_id); 
				ResultSet rs = ps.executeQuery();
				
				if(!rs.next()) {
					System.out.println("�������� �ʴ� ID �Դϴ�.");
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
		
		//password Ȯ��
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
					System.out.println("password�� Ʋ�Ƚ��ϴ�");
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
		
		//���� ���� ���
		sql = "SELECT * FROM RESERVATION WHERE unumber = ?";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, unumber);
			
			ResultSet rs = ps.executeQuery();
			
			if(!rs.next()) {
				System.out.println("���� ������ �����ϴ�.");
				return;
			}
			else {
				System.out.println("���� ������ Ȯ�εǾ����ϴ�.");
				System.out.println("����id : " + rs.getString(2));
				System.out.println("���� ȸ�� : " + rs.getInt(4));
				System.out.println("���� �Ͻ� : " + rs.getDate(1));
			}
			
			rs.close();
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Ȯ�� �޼���
		while(true) {
			System.out.print("�� ������ ���� ����Ͻðڽ��ϱ�?(Y/N) ");
			input = keyboard.nextLine();
			
			if(input.equals("Y")) {
				break;
			}
			else if(input.equals("N")) {
				return;
			}
		}

		
		//delete query ����
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
		
		System.out.println("ȸ������ ����");
		//ID input
		while(true){
			System.out.print("ID : ");
			user_id = keyboard.nextLine();
			
			//��ȿ�� user_id���� Ȯ���ϰ� unumberȹ��
			try {
				sql = "SELECT * FROM CLIENT WHERE USER_ID = ?";
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setString(1, user_id); 
				ResultSet rs = ps.executeQuery();
				
				if(!rs.next()) {
					System.out.println("�������� �ʴ� ID �Դϴ�.");
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
		
		//password Ȯ��
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
					System.out.println("password�� Ʋ�Ƚ��ϴ�");
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
		
		//Ȯ�θ޼���
		while(true) {
			System.out.print("���� ȸ�������� �����Ͻðڽ��ϱ�?(Y/N) : ");
			input = keyboard.nextLine();
			if(input.equals("Y")) {
				break;
			}
			else if(input.equals("N")) {
				System.out.println("Ż�� ����մϴ�");
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
