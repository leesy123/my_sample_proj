package cims;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

public class Search {
	private Connection conn = null; // Connection object
	private Statement stmt = null;
	private Scanner keyboard = null;
	
	public Search(Connection conn, Scanner keyboard) {
		this.conn = conn;
		this.keyboard = keyboard;
	}
	
	public void Printmenu() {
		while(true) {
			System.out.println("정보검색");
			System.out.println("1. ID 조회하기");
			System.out.println("2. 유저번호 조회");
			System.out.println("3. password 찾기");
			System.out.println("4. 예약정보 검색");
			System.out.println("5. 확인증 조회");
			System.out.println("6. 병원 예약가능시간 조회");
			System.out.println("7. 가게별 방문자수 조회");
			System.out.println("8. 유저 방문기록 조회");
			System.out.println("9. 이전화면");
			
			System.out.print("메뉴를 선택해 주세요 : ");
				
			int input = keyboard.nextInt();
			System.out.println("");
			
			if(input == 1) Search_ID();
			else if(input == 2) Search_Uno();
			else if(input == 3) Search_PW();
			else if(input == 4) Search_Resv();
			else if(input == 5) Search_Conf();
			else if(input == 6) Search_Resv2();
			else if(input == 7) Search_Vis();
			else if(input == 8) Search_Log();
			else if(input == 9) break;
			
			System.out.println("\n");
		}
	}
	
	//이름과 번호를 이용한 ID 검색
	public void Search_ID() {
		//for eliminating \n in console
		keyboard.nextLine();
		
		String sql = "";
		ResultSet rs = null;
		
		try {
			System.out.println("Enter your name and phone number.");
			System.out.print("name : ");
			String name = keyboard.nextLine();
			
			System.out.print("phone : ");
			String phone = keyboard.nextLine();
			
			sql = "SELECT User_id\n"
					+ "FROM CLIENT\n"
					+ "WHERE Name = '" + name + "'\n"
					+ "AND Phone = '" + phone + "'";
			stmt = conn.prepareStatement(sql.toString(),ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			rs = stmt.executeQuery(sql);
			
			int size = -1;
			rs.last();
			size = rs.getRow();
			rs.beforeFirst();
			
			if(size == 0) {
				System.out.println("No results found.");
			}
			else {
				System.out.print("Your ID is ");
				
				while(rs.next()) {
					String id = rs.getString(1);
					System.out.println(id);
				}
			}
			
		}catch(SQLException ex2) {
			System.err.println("sql error = " + ex2.getMessage());
			System.exit(1);
		}
	}
	
	//이름과 번호를 이용한 유저 번호 검색
	public void Search_Uno() {
		//for eliminating \n in console
		keyboard.nextLine();
		
		String sql = "";
		ResultSet rs = null;
		
		try {
			System.out.println("Enter your name and phone number.");
			System.out.print("name : ");
			String name = keyboard.nextLine();
			
			System.out.print("phone : ");
			String phone = keyboard.nextLine();
			
			sql = "SELECT Unumber\n"
					+ "FROM CLIENT\n"
					+ "WHERE Name = '" + name + "'\n"
					+ "AND Phone = '" + phone + "'";
			stmt = conn.prepareStatement(sql.toString(),ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			rs = stmt.executeQuery(sql);
			
			int size = -1;
			rs.last();
			size = rs.getRow();
			rs.beforeFirst();
			
			if(size == 0) {
				System.out.println("No results found.");
			}
			else {
				System.out.print("Your User number is ");
				
				while(rs.next()) {
					String u_number = rs.getString(1);
					System.out.println(u_number);
				}
			}
			
		}catch(SQLException ex2) {
			System.err.println("sql error = " + ex2.getMessage());
			System.exit(1);
		}
	}
	
	//이름, 번호, id를 이용해 password 검색
	public void Search_PW() {
		//for eliminating \n in console
		keyboard.nextLine();
		
		String sql = "";
		ResultSet rs = null;
		
		try {
			System.out.println("Enter your name, phone number and ID.");
			System.out.print("name : ");
			String name = keyboard.nextLine();
			
			System.out.print("phone : ");
			String phone = keyboard.nextLine();
			
			System.out.print("id : ");
			String id = keyboard.nextLine();
			
			sql = "SELECT Passwd\n"
					+ "FROM CLIENT\n"
					+ "WHERE Name = '" + name + "'\n"
					+ "AND Phone = '" + phone + "'\n"
					+ "AND User_id = '" + id + "'";
			stmt = conn.prepareStatement(sql.toString(),ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			rs = stmt.executeQuery(sql);
			
			int size = -1;
			rs.last();
			size = rs.getRow();
			rs.beforeFirst();
			
			if(size == 0) {
				System.out.println("No results found.");
			} 
			else {
				System.out.print("Your Password is ");
				
				while(rs.next()) {
					String passwd = rs.getString(1);
					System.out.println(passwd);
				}
			}
			
		}catch(SQLException ex2) {
			System.err.println("sql error = " + ex2.getMessage());
			System.exit(1);
		}
	}

	//유저 번호를 이용해 예약 정보 검색
	public void Search_Resv() {
		//for eliminating \n in console
		keyboard.nextLine();
		
		String sql = "";
		ResultSet rs = null;
		
		try {	
			System.out.println("Enter your User number.");
			System.out.print("user number : ");
			String u_number = keyboard.nextLine();
			
			sql = "SELECT r.Rnumber, r.Rdate, h.Name, v.Name\n"
					+ "FROM RESERVATION r, HOSPITAL h, CLIENT c, VACCINE v\n"
					+ "WHERE c.Unumber = r.Unumber\n"
					+ "AND r.Hnumber = h.Hnumber\n"
					+ "AND r.Vnumber = v.Vnumber\n"
					+ "AND c.Unumber = '" + u_number +"'";

			stmt = conn.prepareStatement(sql.toString(),ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

			rs = stmt.executeQuery(sql);
			
			int size = -1;
			rs.last();
			size = rs.getRow();
			rs.beforeFirst();
			
			if(size == 0) {
				System.out.println("No results found.");
			} 
			else {
				System.out.println("Reservation number |     Date     |           Hospital           |        Vaccine");
				System.out.println("------------------------------------------------------------------------------------------");
				
				while(rs.next()) {
					String r_number = rs.getString(1);
					java.sql.Date date = rs.getDate(2);
					String hospital = rs.getString(3);
					String vaccine = rs.getString(4);
					
					java.util.Date rDate = new java.util.Date(date.getTime());
					System.out.println(r_number + "         |  " + new SimpleDateFormat("yyyy-MM-dd").format(rDate) + "  | " + hospital + " | " + vaccine);
				}
			}
			
		}catch(SQLException ex2) {
			System.err.println("sql error = " + ex2.getMessage());
			System.exit(1);
		}
	}

	//유저 번호를 이용해 확인증 검색
	public void Search_Conf() {
		//for eliminating \n in console
		keyboard.nextLine();
		
		String sql = "";
		ResultSet rs = null;
		
		try {	
			System.out.println("Enter your User number.");
			System.out.print("user number : ");
			String u_number = keyboard.nextLine();
			
			sql = "SELECT c.Name, f.Cnumber, f.Inject_cnt, f.Inject_date\n"
					+ "FROM CLIENT c, CONFIRMATION f\n"
					+ "WHERE c.Unumber = f.Unumber\n"
					+ "AND c.Unumber = '" + u_number +"'";

			stmt = conn.prepareStatement(sql.toString(),ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

			rs = stmt.executeQuery(sql);
			
			int size = -1;
			rs.last();
			size = rs.getRow();
			rs.beforeFirst();
			
			if(size == 0) {
				System.out.println("No results found.");
			} 
			else {
				System.out.println("Name | Confirmation No | Inject Count | Inject Date");
				System.out.println("------------------------------------------------------");
				
				while(rs.next()) {
					String name = rs.getString(1);
					String c_number = rs.getString(2);
					String cnt = rs.getString(3);
					java.sql.Date date = rs.getDate(4);
					
					java.util.Date cDate = new java.util.Date(date.getTime());
					System.out.println(name + " |  " + c_number + "    |     " + cnt + "       | " + new SimpleDateFormat("yyyy-MM-dd").format(cDate));
				}
			}
			
		}catch(SQLException ex2) {
			System.err.println("sql error = " + ex2.getMessage());
			System.exit(1);
		}
	}

	//다음 날짜를 구하는 메소드
	String nextDate(String date) throws ParseException {
		  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		  Calendar c = Calendar.getInstance();
		  java.util.Date d = sdf.parse(date);
		  
		  c.setTime(d);
		  c.add(Calendar.DATE,1);
		  String date1 = sdf.format(c.getTime());
		  return date1;
	}	
	
	//병원 예약 가능 시간 조회
	public void Search_Resv2() {
		//for eliminating \n in console
		keyboard.nextLine();
		
		String sql = "";
		ResultSet rs = null;
		
		try {
			System.out.println("Enter hospital name and date.");
			System.out.print("hospital name : ");
			String hospital = keyboard.nextLine();
			
			System.out.print("date(yyyy-MM-dd) : ");
			String date1 = keyboard.nextLine();
			String date2 = nextDate(date1);
			
			ArrayList<String> rTime = new ArrayList<>();
			
			rTime.add("09:00");
			rTime.add("10:00");
			rTime.add("11:00");
			rTime.add("12:00");
			rTime.add("13:00");
			rTime.add("14:00");
			rTime.add("15:00");
			rTime.add("16:00");
			rTime.add("17:00");
			
			sql = "SELECT r.Rdate\n"
					+ "FROM RESERVATION r, HOSPITAL h\n"
					+ "WHERE r.Hnumber = h.Hnumber\n"
					+ "AND h.Name = '" + hospital + "'\n"
					+ "AND r.Rdate > '" + date1 + "'\n"
					+ "AND r.Rdate < '" + date2 + "'";

			stmt = conn.prepareStatement(sql.toString(),ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

			rs = stmt.executeQuery(sql);
			
			int size = -1;
			rs.last();
			size = rs.getRow();
			rs.beforeFirst();
			
			System.out.println("Selected Date : " + date1);
			System.out.println("Available Time");
			System.out.println("------------------");
			
			if(size == 0) {
				
				for(String atime : rTime) {
					System.out.println(atime);
				}
			} 
			else {
				while(rs.next()) {
					java.sql.Date date = rs.getDate(1);
					java.util.Date rDate = new java.util.Date(date.getTime());
					
					String atime = new SimpleDateFormat("HH:mm").format(rDate);
					rTime.remove(atime);
				}
				
				for(String atime : rTime) {
					System.out.println(atime);
				}
			}
			
		}catch(SQLException ex2) {
			System.err.println("sql error = " + ex2.getMessage());
			System.exit(1);
		}catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//가게별 방문자 수 검색
	public void Search_Vis() {
		//for eliminating \n in console
		keyboard.nextLine();
		
		String sql = "";
		ResultSet rs = null;
		
		try {	
			System.out.print("Enter Shop name : ");
			String s_name = keyboard.nextLine();
			
			sql = "SELECT S.NAME AS SHOP_NAME, COUNT(*) AS VISITOR\n"
					+ "FROM SHOP S, CLIENT C, VISIT_LOG V\n"
					+ "WHERE V.SNUMBER = S.SNUMBER AND V.UNUMBER = C.UNUMBER\n"
					+ "AND S.NAME = '" + s_name + "'\n"
					+ "GROUP BY S.NAME";
				stmt = conn.prepareStatement(sql.toString(),ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
				rs = stmt.executeQuery(sql);
			
			int size = -1;
			rs.last();
			size = rs.getRow();
			rs.beforeFirst();
			
			System.out.println("Shop Name | Visitor");
			System.out.println("--------------------");
			
			while(rs.next()) {
				String name = rs.getString(1);
				int visitor = rs.getInt(2);
				
				System.out.println(name + " |  " + visitor);
			}
			
		}catch(SQLException ex2) {
			System.err.println("sql error = " + ex2.getMessage());
			System.exit(1);
		}
	}
	

}
