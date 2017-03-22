/* COMP 421 project deliverable 3
 * Group 15
 * 
 */

import java.util.*;
import java.sql.*;
import java.math.*;
import com.ibm.db2.jcc.*;

public class MainApp {

	public static void main(String args[]) throws SQLException{    
		//Set up driver
		try {
			Driver myDriver = new DB2Driver();
			DriverManager.registerDriver (myDriver);
		} catch (SQLException sqle) {
			System.out.println("Access Error");
			System.exit(1);
		}
		
		//Connect to DB
		Connection conn = DriverManager.getConnection("jdbc:db2://comp421.cs.mcgill.ca:5432/cs421", "cs421g15", "fsdb1517");

		try {
			boolean end = true;
			while(end){
				int choice = getOptionInput();

				switch (choice) {
					case 1:	queryEmployeeList();
							break;
					case 2: queryCustomerOrder();
							break;
					case 3: queryOrderProducts();
							break;
					case 4: addProductToOrder();
							break;
					case 5:	deleteProductFromOrder();
							break;
					case 6: end = false;
							break;
					default: System.out.println("Invalid input. Returning to main menu..."); 
							break;
				}
			}
			System.out.println("Quitting application .... ");
			
		} catch (SQLException sqle){
			sqle.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException sqle){
				sqle.printStackTrace();
			}
		}
	}

	//Shows menu and returns user choice integer.
	static int getOptionInput(){
		System.out.println("------------------------------------------");
		System.out.println("--------------- MAIN MENU ----------------");
		System.out.println("-----1. Show list of employees------------");
		System.out.println("-----2. Show all orders from a user-------");
		System.out.println("-----3. Show order details ---------------");
		System.out.println("-----4. Add a product to an order---------");
		System.out.println("-----5. Delete a product from an order----");
		System.out.println("-----6. Quit Application------------------");
		System.out.println("------------------------------------------");
		System.out.println("---Please type the number corresponding---");
		System.out.println("---to the action you want to perform------");

		Scanner sc = new Scanner(System.in);
		int choice = sc.nextInt();
		sc.close();
		System.out.println("------------------------------------------");
		return choice;
	}

	static void queryEmployeeList(){
	
	}

	static void queryCustomerOrder(){
	
	}

	static void queryOrderProducts(){
		//display Order info (with all order attributes) and products in the Order with all 
		//product attributes (see ER diagram from p2 submission) except inventoryCount 
		//and price
		//uses 2 SQL statements: one to retrieve the order info and one to retrieve the 
		//products list for the order 
	}
	
	static void addProductToOrder(){
		//take as input orderID, then take as input pid
		//uses 2 SQL statements: one to add the product to the order using OrderList 
		//entity, and one to update the orderAmount
	}

	static void deleteProductFromOrder(){
		//take as input orderID, then take as input pid
		//delete product from order using OrderList entity
	}
}
