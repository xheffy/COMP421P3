/* COMP 421 project deliverable 3
 * Group 15
 * 
 */

import java.util.*;
import java.sql.*;


public class MainApp {
	public static void main(String args[]) throws SQLException{   

		//Set up driver
		try {
			Driver myDriver = new com.ibm.db2.jcc.DB2Driver();
			DriverManager.registerDriver (myDriver);
		} catch (SQLException ex) {
			   System.out.println("Driver Setup Error");
			   System.exit(1);
		}
		
		//Connect to DB
		String url = "jdbc:db2://comp421.cs.mcgill.ca:50000/cs421";
		Connection conn = null;
		
		try {
			conn = DriverManager.getConnection(url, "cs421g15", "fsdb1517");
		} catch (SQLException sqle) {
			System.out.println("CONNECTION ERROR State: " + sqle.getSQLState());
			System.out.println("CONNECTION ERROR Message: " + sqle.getMessage());
			System.exit(1);
		} 
		
		Statement stmt = conn.createStatement();
		Scanner sc = new Scanner(System.in);
		
		try { 
			boolean end = true; 
			while(end){ 
				int choice = getOptionInput(sc);

				switch (choice) {
					case 1:	queryEmployeeList(conn,stmt);
							break;
					case 2: queryCustomerOrder(conn,stmt, sc);
							break;
					case 3: queryOrderProducts(conn,stmt, sc);
							break;
					case 4: addProductToOrder(conn,stmt,sc);
							break;
					case 5:	deleteProductFromOrder(conn,stmt,sc);
							break;
					case 6: end = false;
							break;
					default: System.out.println("Invalid input. Returning to main menu..."); 
							break;
				} 
			} 
			System.out.println("Quitting application .... ");
			//Disconnect 	
		} finally {
			try {
				sc.close();
				stmt.close();
				conn.close();
			} catch (SQLException e){
				System.out.println("Could not close JDBC connection");
			}
		} 
	} 

	//Shows menu and returns user choice integer.
	static int getOptionInput(Scanner sc){
		System.out.println("------------------------------------------");
		System.out.println("--------------- MAIN MENU ----------------");
		System.out.println("-----1. Show list of employees------------");
		System.out.println("-----2. Show all orders of a customer-----");
		System.out.println("-----3. Show order details ---------------");
		System.out.println("-----4. Add a product to an order---------");
		System.out.println("-----5. Delete a product from an order----");
		System.out.println("-----6. Quit Application------------------");
		System.out.println("------------------------------------------");
		System.out.println("---Please type the number corresponding---");
		System.out.println("---to the action you want to perform------");

		int choice = 0;
		
		if (sc.hasNext()){
			try{
			    choice = Integer.parseInt(sc.nextLine());
			} catch (NumberFormatException e) {
			    System.out.println("You did not input an integer.");
			}
		}
		
		System.out.println("------------------------------------------");
		return choice;
	}

	static void queryEmployeeList(Connection conn, Statement stmt) {
		String empListQ = "SELECT eid, name, role FROM Employees";
		try {
			ResultSet emps = stmt.executeQuery(empListQ);
			ResultSetMetaData md = emps.getMetaData();
			int cols = md.getColumnCount();
			while (emps.next()) {
				for (int i = 1; i <= cols; i++) {
					if (i > 1) System.out.print(",  ");
					String cVal = emps.getString(i);
					System.out.print(cVal + " " + md.getColumnName(i));
				}
				System.out.println("");
			}
		} catch(SQLException e) {
			String message = e.getMessage();
			System.out.println("Message: " + message);
		}
	}

	static void queryCustomerOrder(Connection conn, Statement stmt, Scanner sc) {
		System.out.println("Please enter the email of the customer whose orders you wish to view: ");
		String email = sc.nextLine();
		String custOrderQ = "SELECT * FROM Orders WHERE email = '" + email + "'";
		try {
			ResultSet orders = stmt.executeQuery(custOrderQ);
			ResultSetMetaData md = orders.getMetaData();
			int cols = md.getColumnCount();
			while (orders.next()) {
				for (int i = 1; i <= cols; i++) {
					if (i > 1) System.out.print(",  ");
					String cVal = orders.getString(i);
					System.out.print(cVal + " " + md.getColumnName(i));
				}
				System.out.println("");
			}
		} catch(SQLException e) {
			String message = e.getMessage();
			System.out.println("Message: " + message);
		}
	}

	static void queryOrderProducts(Connection conn, Statement stmt, Scanner sc) {
		System.out.println("Please enter the order no. for the order you wish to view: ");
		int ono = sc.nextInt();
		String ordersQ = "SELECT * FROM Orders WHERE orderNo = " + ono;
		String productsQ = "SELECT P.pid AS id, P.productName AS name, L.quantity AS count FROM Orders O, OrderList L, Products P WHERE O.orderNo = " + ono + " AND O.orderNo = L.orderNo AND L.pid = P.pid";
		try {
			ResultSet orders = stmt.executeQuery(ordersQ);
			ResultSetMetaData md = orders.getMetaData();
			int cols = md.getColumnCount();
			while (orders.next()) {
				for (int i = 1; i <= cols; i++) {
					if (i > 1) System.out.print(",  ");
					String cVal = orders.getString(i);
					System.out.print(cVal + " " + md.getColumnName(i));
				}
				System.out.println("");
			}
			ResultSet products = stmt.executeQuery(productsQ);
			md = products.getMetaData();
			cols = md.getColumnCount();
			while (products.next()) {
				for (int i = 1; i <= cols; i++) {
					if (i > 1) System.out.print(",  ");
					String cVal = products.getString(i);
					System.out.print(cVal + " " + md.getColumnName(i));
				}
				System.out.println("");
			}
			sc.nextLine();
		} catch(SQLException e) {
			String message = e.getMessage();
			System.out.println("Message: " + message);
		}
	}
	
	static void addProductToOrder(Connection conn, Statement stmt,Scanner sc){
		System.out.println("To which order do you wish to add products?");
		System.out.println("Input the orderNo:");
		int orderNo = sc.nextInt();
		System.out.println("Which product do you wish to add to this order?");
		System.out.println("Input the pid:");
		int pid = sc.nextInt();
		System.out.println("How many such products do you wish to add to the given order?");
		System.out.println("Input the quantity:");
		int quantity = sc.nextInt();
		sc.nextLine();
		try{ 
			if (quantity<1)
			{
				System.out.println("The specified quantity must be greater than 1.");
			}
			else
			{
				String check = "UPDATE OrderList SET quantity = quantity+" + quantity + " WHERE orderNo = " + orderNo + " AND pid = " + pid;
				int code = stmt.executeUpdate(check);
				if (code == 0)
				{
					String findOrder = "SELECT orderNo FROM Orders WHERE orderNo=" + orderNo;
					ResultSet foundOrder_count = stmt.executeQuery(findOrder);
					int foundOrder = 0;
					while(foundOrder_count.next())
					{
						foundOrder++;
					}
					String findProduct = "SELECT pid FROM Products WHERE pid=" + pid;
					ResultSet foundProduct_count = stmt.executeQuery(findProduct);
					int foundProduct = 0;
					while(foundProduct_count.next())
					{
						foundProduct++;
					}
					if(foundOrder == 0)
					{
						System.out.println("This orderNo does not exist.");
					}
					else if(foundProduct == 0)
					{
						System.out.println("This pid does not exist.");
					}
					else
					{
						System.out.println("No record exists for given orderNo and pid in OrderList table.");
						System.out.println("New record created.");
						String insertProduct = "INSERT INTO OrderList VALUES(" + orderNo + "," + pid +"," + quantity + ")"; 
						stmt.executeUpdate(insertProduct);
						String updateOrderAmount = "UPDATE Orders SET orderAmount = orderAmount + (SELECT price FROM Products WHERE pid = " + pid + ")*" + quantity + " WHERE orderNo = " + orderNo;
						stmt.executeUpdate(updateOrderAmount);
				 	    System.out.println("Product was added to given order and the order amount was increased accordingly.");
					}
				}
				else
				{
					System.out.println("Record for given orderNo and pid already exists in OrderList table.");
					System.out.println("Quantity was incremented.");
					String updateOrderAmount = "UPDATE Orders SET orderAmount = orderAmount + (SELECT price FROM Products WHERE pid = " + pid + ")*" + quantity + " WHERE Orders.orderNo = " + orderNo;
					stmt.executeUpdate(updateOrderAmount);
			 	    System.out.println("Product was added to given order and the order amount was increased accordingly.");
				}
			}
		}
		catch(SQLException e)
		{
			String message = e.getMessage(); // Get SQLMESSAGE includes SQLSTATE and SQLCODE
            System.out.println("Message: " + message);
		}
	}

	static void deleteProductFromOrder(Connection conn, Statement stmt,Scanner sc){
		System.out.println("From which order do you wish to delete products?");
		System.out.println("Input the orderNo:");
		int orderNo = sc.nextInt();
		System.out.println("Which product do you wish to delete from this order?");
		System.out.println("Input the pid:");
		int pid = sc.nextInt();
		sc.nextLine();
		try{
			String findOrderList = "SELECT orderNo FROM OrderList WHERE orderNo=" + orderNo + " AND pid=" + pid;
			ResultSet foundOrderList_count = stmt.executeQuery(findOrderList);
			int foundOrderList = 0;
			while(foundOrderList_count.next())
			{
				foundOrderList++;
			}
		    if (foundOrderList==0)
		    {
	            System.out.println("No record exists for given orderNo and pid in OrderList table.");
		    	System.out.println("Deletion of product from order failed.");
		    }
		    else
		    {
			    String updateOrderAmount = "UPDATE Orders SET orderAmount=orderAmount-(SELECT price FROM Products WHERE pid=" + pid + ")*(SELECT quantity FROM OrderList WHERE orderNo=" + orderNo + " AND pid=" + pid + ") WHERE orderNo= " + orderNo;
			    stmt.executeUpdate(updateOrderAmount);
			    String deleteSQL = "DELETE FROM OrderList WHERE orderNo = " + orderNo + " AND pid = " + pid;
			    stmt.executeUpdate(deleteSQL);
		 	    System.out.println("Product was deleted from given order and the order amount was decreased accordingly.");
		    }
		}
		catch(SQLException e)
		{
			String message = e.getMessage(); // Get SQLMESSAGE includes SQLSTATE and SQLCODE
            System.out.println("Message: " + message);
		}
	}
}
