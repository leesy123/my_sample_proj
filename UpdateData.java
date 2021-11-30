package cims;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class UpdateData {
	public Connection conn = null;
	public Scanner keyboard = null;
	
	public UpdateData(Connection conn, Scanner keyboard) {
		this.conn = conn;
		this.keyboard = keyboard;
	}

	public void Printmenu() {
		while(true) {
			System.out.println("��������");
			System.out.println("1. ȸ������ ����");
			System.out.println("2. ����ȭ��");
			
			System.out.print("�޴��� ������ �ּ��� : ");
				
			int input = keyboard.nextInt();
			System.out.println("");
			
			if(input == 1) {
				UpdateClientInfo(conn, keyboard);
			}
			else if(input == 2) break;
			
			System.out.println("\n");
		}
	}
	
	public void UpdateClientInfo(Connection conn, Scanner keyboard) {
		String user_id, password, name, phone, address, sex;
		String input;
		String sql = "SELECT * FROM CLIENT WHERE USER_ID = ?";
		
		//for eliminating \n in console
		keyboard.nextLine();
		
		System.out.println("ȸ������ ����");
		//ID input
		while(true){
			System.out.print("ID : ");
			user_id = keyboard.nextLine();
			
			//��ȿ�� user_id���� Ȯ���ϰ� unumberȹ��
			try {
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setString(1, user_id); 
				ResultSet rs = ps.executeQuery();
				
				if(!rs.next()) {
					System.out.println("�������� �ʴ� ID �Դϴ�.");
				}
				else {
					name = rs.getString(1);
					phone = rs.getString(2);
					address = rs.getString(3);
					sex = rs.getString(4);
					
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
		
		
		
		//�̸�
		System.out.println("���� ��� �̸� : " + name);
		System.out.print("�� �̸�(������ ����Ϸ��� enter �Է�) : ");
		input = keyboard.nextLine();
		if(!input.equals(""))
			name = input;
		
		//����ȣ
		while(true){
			System.out.println("���� �� : " + phone);
			System.out.print("�� ��(������ ����Ϸ��� enter �Է�) : ");
			input = keyboard.nextLine();
			
			if(input.equals("")) break;
			
			//check key constraint
			try {
				sql = "SELECT * FROM CLIENT WHERE Phone = ?";
				
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setString(1, input);
				ResultSet rs = ps.executeQuery();
				if(!rs.next()) {
					ps.close();
					phone = input;
					break;
				}
				else {
					System.out.println("�̹� �����ϴ� ���Դϴ�");
				}
				
				ps.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		//�ּ�
		System.out.println("���� ��� �ּ� : " + address);
		System.out.print("�� �ּ�(������ ����Ϸ��� enter �Է�) : ");
		input = keyboard.nextLine();
		if(!input.equals(""))
			address = input;
		
		
		//����
		while(true){
			System.out.println("���� ���� : " + sex);
			System.out.print("�� ����(������ ����Ϸ��� enter �Է�) : ");
			input = keyboard.nextLine();
			
			if(input.equals("")) {
				break;
			}
			else if(input.equals("F") || input.equals("M")) {
				sex = input;
				break;
			}
		}
		
		//password
		while(true){
			System.out.print("�� password(������ ����Ϸ��� enter �Է�) : ");
			input = keyboard.nextLine();
			if(input.equals("")) break;
			else {
				password = input;
				break;
			}
		}
		
		//query
		sql = "UPDATE CLIENT C\r\n"
				+ "SET C.name = ?,\r\n"
				+ "    C.phone = ?,\r\n"
				+ "    C.address = ?,\r\n"
				+ "    C.sex = ?,\r\n"
				+ "    C.passwd = ?\r\n"
				+ "WHERE C.user_id = ?";
		
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, name);
			ps.setString(2, phone);
			ps.setString(3, address);
			ps.setString(4, sex);
			ps.setString(5, password);
			ps.setString(6, user_id);
			
			int count = ps.executeUpdate();
			if(count == 0) {
				System.out.println("error occured when inserting data!");
			}
			else {
				System.out.println("client data updated");
			}
			
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
