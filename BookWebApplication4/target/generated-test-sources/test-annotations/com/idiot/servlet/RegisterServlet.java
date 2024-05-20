package com.idiot.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/reg/register")
public class RegisterServlet extends HttpServlet {
	//private static final String query = "INSERT INTO BOOKDATA(BOOKNAME, BOOKEDITION, BOOKPRICE) VALUES(?,?,?)";

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// Set content type and character encoding
		res.setContentType("text/html;charset=UTF-8");
		System.out.println("posts");
		// Get PrintWriter
		PrintWriter pw = res.getWriter();
		System.out.println("reg page error");
		// Get the book info from request parameters
		String bookName = req.getParameter("bookName");
		String bookEdition = req.getParameter("bookEdition");
		float bookPrice;
		try {
			bookPrice = Float.parseFloat(req.getParameter("bookPrice"));
		} catch (NumberFormatException e) {
			pw.println("<h2>Invalid book price</h2>");
			pw.println("<a href='home.html'>Home</a>");
			return;
		}

		// Load JDBC driver
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException cnf) {
			pw.println("<h2>Database driver not found</h2>");
			cnf.printStackTrace();
			return;
		}

		// Generate the connection and execute the query
		try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/book", "root", "root")){
			
			String query = "insert into bookdata values(?,?,?)";
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, bookName);
			ps.setString(2, bookEdition);
			ps.setFloat(3, bookPrice);

			int count = ps.executeUpdate();

			if (count == 1) {
				pw.println("<h2>Record Is Registered Successfully</h2>");
			} else {
				pw.println("<h2>Record not Registered Successfully</h2>");
			}

		} catch (SQLException se) {
			se.printStackTrace(pw);
			pw.println("<h2>" + se.getMessage() + "</h2>");
		}

		// Add links to navigate back home or to the book list
		pw.println("<a href='home.html'>Home</a>");
		pw.println("<br>");
		pw.println("<a href='bookList'>Book List</a>");
	}
}
