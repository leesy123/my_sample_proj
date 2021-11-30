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
			System.out.println("정보수정");
			System.out.println("1. 회원정보 수정");
			System.out.println("2. 이전화면");
			
			System.out.print("메뉴를 선택해 주세요 : ");
				
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
		
		System.out.println("회원정보 수정");
		//ID input
		while(true){
			System.out.print("ID : ");
			user_id = keyboard.nextLine();
			
			//유효한 user_id인지 확인하고 unumber획득
			try {
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setString(1, user_id); 
				ResultSet rs = ps.executeQuery();
				
				if(!rs.next()) {
					System.out.println("존재하지 않는 ID 입니다.");
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
		
		
		
		//이름
		System.out.println("기존 사용 이름 : " + name);
		System.out.print("새 이름(기존값 사용하려면 enter 입력) : ");
		input = keyboard.nextLine();
		if(!input.equals(""))
			name = input;
		
		//폰번호
		while(true){
			System.out.println("기존 값 : " + phone);
			System.out.print("새 값(기존값 사용하려면 enter 입력) : ");
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
					System.out.println("이미 존재하는 값입니다");
				}
				
				ps.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		//주소
		System.out.println("기존 사용 주소 : " + address);
		System.out.print("새 주소(기존값 사용하려면 enter 입력) : ");
		input = keyboard.nextLine();
		if(!input.equals(""))
			address = input;
		
		
		//성별
		while(true){
			System.out.println("기존 성별 : " + sex);
			System.out.print("새 성별(기존값 사용하려면 enter 입력) : ");
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
			System.out.print("새 password(기존값 사용하려면 enter 입력) : ");
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
