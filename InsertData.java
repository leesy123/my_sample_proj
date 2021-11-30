package cims;

import java.sql.*;
import java.util.Random;
import java.util.Scanner;

public class InsertData {
	
	public Connection conn = null;
	public Scanner keyboard = null;
	
	public InsertData(Connection conn, Scanner keyboard) {
		this.conn = conn;
		this.keyboard = keyboard;
	}

	public void Printmenu() {
		while(true) {
			System.out.println("�����Է�");
			System.out.println("1. ȸ������ �߰�");
			System.out.println("2. ������ȯ ���� �߰�");
			System.out.println("3. ���� �����ϱ�");
			System.out.println("4. ����ȭ��");
			
			System.out.print("�޴��� ������ �ּ��� : ");
				
			int input = keyboard.nextInt();
			System.out.println("");
			
			if(input == 1) InsertClient(conn, keyboard);
			else if(input == 2) InsertDisease(conn, keyboard);
			else if(input == 3) InsertReservation(conn, keyboard);
			else if(input == 4) break;
			
			System.out.println("\n");
		}
	}
	
	public void InsertReservation(Connection conn, Scanner keyboard) {
		String user_id = "", unumber = "";
		String rdate = "";
		String hnumber = "";
		int inject_cnt = 1;
		String vnumber = "";
		String rnumber = "";
		String sql = "";
		
		//for eliminating \n in console
		keyboard.nextLine();
		
		System.out.println("��������");
		
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
		
		//��������
		while(true) {
			System.out.print("injection date(ex. 2021-11-01 12:00:00) : ");
			rdate = keyboard.nextLine();
			
			if(rdate.equals("")) continue;
			
			break;
		}
		
		//���� id
		while(true) {
			System.out.print("Hospital number : ");
			hnumber = keyboard.nextLine();
			
			if(hnumber.equals("")) continue;
			
			//��ȿ�� hnumber���� Ȯ��
			try {
				sql = "SELECT * FROM HOSPITAL WHERE HNUMBER = ?";
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setString(1, hnumber); 
				ResultSet rs = ps.executeQuery();
				
				if(!rs.next()) {
					System.out.println("�������� �ʴ� ����id �Դϴ�.");
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
		
		//inject cnt ���
		try {
			sql = "SELECT * FROM CONFIRMATION WHERE UNUMBER = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, unumber); 
			ResultSet rs = ps.executeQuery();
			
			if(!rs.next()) {
				inject_cnt = 1;
			}
			else {
				if(!rs.next()) {
					inject_cnt = 2;
				}
				else {
					System.out.println("2ȸ ��� �����Ͽ� ���̻� ������ �� �����ϴ�.");
					ps.close();
					rs.close();
					return;
				}
			}
			
			ps.close();
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//�ش� ������ ���� ����� ��������� ���� �ӹ��� �� ��ȸ
		try {
			sql = "SELECT V.VNUMBER FROM VACCINE V "
					+ "WHERE V.Hnumber = ? "
					+ "AND V.Expiration_date <= ALL(SELECT Expiration_date FROM VACCINE NV "
					+ "                            WHERE NV.Hnumber = ? "
					+ "                            AND NOT EXISTS (SELECT * FROM RESERVATION NR WHERE NR.Vnumber = NV.Vnumber)) "
					+ "AND NOT EXISTS (SELECT * FROM RESERVATION R WHERE R.Vnumber = V.Vnumber)";
			
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, hnumber); 
			ps.setString(2, hnumber); 
			ResultSet rs = ps.executeQuery();
			
			if(!rs.next()) {
				System.out.println("�ش� ������ �ܿ� ����� �����ϴ�");
				ps.close();
				rs.close();
				return;
			}
			else {
				vnumber = rs.getString(1);
			}
			
			ps.close();
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Rnumber �����ϱ�
		rnumber = "";
		int y = Integer.parseInt(rdate.substring(2, 4));
		int m = Integer.parseInt(rdate.substring(5, 7));
		int d = Integer.parseInt(rdate.substring(8, 10));
		
		rnumber += String.format("%d%d%d", y,m,d);
		rnumber += vnumber.substring(1);
		
		//insert
		sql = "INSERT INTO RESERVATION VALUES(TO_DATE(?, 'yyyy-mm-dd hh24:mi:ss'),?,?,?,?,?)";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, rdate);
			ps.setString(2, hnumber);
			ps.setString(3, rnumber);
			ps.setInt(4, inject_cnt);
			ps.setString(5, unumber);
			ps.setString(6, vnumber);
			
			int count = ps.executeUpdate();
			if(count == 0) {
				System.out.println("error occured when inserting disease data");
			}
			else {
				System.out.println("reservation data inserted(inject count : "+ inject_cnt +")");
			}
			
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void InsertDisease(Connection conn, Scanner keyboard) {
		String user_id = "", disease = "";
		String unumber = "";
		String sql = "";
		String searchQuery = "SELECT * FROM CLIENT WHERE USER_ID = ?";
		String insertQuery = "INSERT INTO UNDERLYING_DISEASE VALUES(?,?)";
		
		//for eliminating \n in console
		keyboard.nextLine();
		
		
		System.out.println("������ȯ �Է�");
		
		//ID input
		while(true){
			System.out.print("ID : ");
			user_id = keyboard.nextLine();
			
			//��ȿ�� user_id���� Ȯ���ϰ� unumberȹ��
			try {
				PreparedStatement ps = conn.prepareStatement(searchQuery);
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
		
		//disease input
		while(true){
			System.out.print("disease : ");
			disease = keyboard.nextLine();
			
			if(disease.equals("")) continue;
			
			//check key constraint
			try {
				sql = "SELECT * FROM UNDERLYING_DISEASE WHERE unumber=? AND disease=?";
				
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setString(1, unumber);
				ps.setString(2, disease);
				ResultSet rs = ps.executeQuery();
				if(!rs.next()) {
					ps.close();
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
		
		//insert data
		try {
			PreparedStatement ps = conn.prepareStatement(insertQuery);
			ps.setString(1, unumber);
			ps.setString(2, disease);
			int count = ps.executeUpdate();
			if(count == 0) {
				System.out.println("error occured when inserting disease data");
			}
			else {
				System.out.println("disease data inserted");
			}
			
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void InsertClient(Connection conn, Scanner keyboard) {
		String sql = "";
		String name = "", phone ="", address="", user_id="", passwd="", sex="";
		String unumber;
		PreparedStatement ps;
		
		//for eliminating \n in console
		keyboard.nextLine();
		
		//ȸ������ �Է�
		System.out.println("ȸ������ �Է�");
		while(name.equals("")){
			System.out.print("name : ");
			name = keyboard.nextLine();
		}
		
		while(true){
			System.out.print("phone number(ex.010-3687-4537) : ");   
			phone = keyboard.nextLine();
			
			if(phone.equals("")) continue;
			
			//check key constraint
			try {
				sql = "SELECT * FROM CLIENT WHERE Phone = ?";
				
				ps = conn.prepareStatement(sql);
				ps.setString(1, phone);
				ResultSet rs = ps.executeQuery();
				if(!rs.next()) {
					ps.close();
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

		while(address.equals("")){
			System.out.print("address : ");
			address = keyboard.nextLine();
		}
		
		while(true){
			System.out.print("ID : ");
			user_id = keyboard.nextLine();
			
			if(user_id.equals("")) continue;
			
			//check key constraint
			try {
				sql = "SELECT * FROM CLIENT WHERE user_id = ?";
				
				ps = conn.prepareStatement(sql);
				ps.setString(1, user_id);
				ResultSet rs = ps.executeQuery();
				if(!rs.next()) {
					ps.close();
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
		
		while(passwd.equals("")){
			System.out.print("Password : ");
			passwd = keyboard.nextLine();
		}
		
		while(true){
			System.out.print("SEX(F or M) : ");
			sex = keyboard.nextLine();
			if(sex.equals("F") || sex.equals("M")) break;
		}
		
		//make a random unumber
		while(true) {
			int randi = RNG(10000,100000);
			unumber = "U" + String.format("%d", randi);
			sql = "SELECT * FROM CLIENT WHERE UNUMBER = ?";
			
			//check key constraint
			try {
				ps = conn.prepareStatement(sql);
				ps.setString(1, unumber);
				ResultSet rs = ps.executeQuery();
				if(!rs.next()) {
					ps.close();
					break;
				}
				ps.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		//DB insert
		sql = "INSERT INTO CLIENT VALUES(?,?,?,?,?,?,?,0,0)";
		
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, name);
			ps.setString(2, phone);
			ps.setString(3, address);
			ps.setString(4, sex);
			ps.setString(5, user_id);
			ps.setString(6, passwd);
			ps.setString(7, unumber);
			
			int count = ps.executeUpdate();
			if(count == 0) {
				System.out.println("error occured when inserting data!");
			}
			else {
				System.out.println("client data inserted!");
			}
			
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	//[min, max)������ ������ ������ �����ϴ� �Լ�
	public int RNG(int min, int max) {
		Random random = new Random();
		double randd = random.nextDouble();
		int randi = (int)(randd * (double)(max-min));
		randi += min;
		
		return randi;
	}
}
