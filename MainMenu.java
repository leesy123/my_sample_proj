package cims;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class MainMenu {
	public static final String URL = "jdbc:oracle:thin:@localhost:1521:orcl";
	public static final String USER_UNIVERSITY = "cims";
	public static final String USER_PASSWD = "oracle";

	public static void main(String[] args) {
		Scanner keyboard = new Scanner(System.in);
		Connection conn = ConnectDB();
		
		// TODO Auto-generated method stub
		while(true) {
			System.out.println("CIMS Console program");
			System.out.println("1. 정보입력");
			System.out.println("2. 정보수정");
			System.out.println("3. 정보삭제");
			System.out.println("4. 검색");
			System.out.println("5. 프로그램 종료");
			
			System.out.print("메뉴를 선택해 주세요 : ");
			
			
			int input = keyboard.nextInt();
			System.out.println("");
			
			if(input == 1) {
				InsertData isd = new InsertData(conn ,keyboard);
				isd.Printmenu();
			}
			else if(input == 2) {
				UpdateData ud = new UpdateData(conn, keyboard);
				ud.Printmenu();
			}
			else if(input == 3) {
				DeleteData dd = new DeleteData(conn, keyboard);
				dd.Printmenu();
			}
			else if(input == 4) {
				Search sc = new Search(conn, keyboard);
				sc.Printmenu();
			}
			else if(input == 5) break;
			
			System.out.println("\n\n");
		}
		
		keyboard.close();
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Connection ConnectDB() {
		Connection conn = null;
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		}
		catch(ClassNotFoundException e) {
			System.err.println("error = " + e.getMessage());
			System.exit(1);
		}
		
		try {
			conn = DriverManager.getConnection(URL, USER_UNIVERSITY, USER_PASSWD);
		}catch(SQLException ex) {
			ex.printStackTrace();
			System.err.println("Cannot get a connection: " + ex.getLocalizedMessage());
			System.err.println("Cannot get a connection: " + ex.getMessage());
			System.exit(1);
		}
		
		return conn;
	}
}
