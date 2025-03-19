import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Read JSON data from the request
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
        }
        JsonObject json = JsonParser.parseString(sb.toString()).getAsJsonObject();
    }
}


@WebServlet("/signup")
public class SignupServlet extends HttpServlet {

        // Validate user credentials by querying the database
        // Use PasswordUtil to check hashed passwords if applicable
        // ...

    }
}

@WebServlet("/signup")
public class SignupServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        JsonObject json = JsonParser.parseString(sb.toString()).getAsJsonObject();
        String first_name = json.get("first_name").getAsString();
        String last_name = json.get("last_name").getAsString();
        String email = json.get("email").getAsString();
        String password = json.get("password").getAsString();
        boolean agreedToTerms = json.get("agreedToTerms").getAsBoolean();

        if (!agreedToTerms) {
            response.getWriter().write("You must agree to the terms and conditions to sign up.");
            return;
        }

        // Hash the password using BCrypt (see PasswordUtil class)
        String hashedPassword = PasswordUtil.hashPassword(password);

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "INSERT INTO new_users (first_name, last_name, email, password) VALUES (?, ?, ?, ?)")) {
             
             stmt.setString(1, first_name);
             stmt.setString(2, last_name);
             stmt.setString(3, email);
             stmt.setString(4, hashedPassword);
             stmt.executeUpdate();
             
             response.getWriter().write("Signup successful!");
        } catch (SQLException e) {
            response.getWriter().write("Error: " + e.getMessage());
        }
        if (signupSuccessful) {
            // Redirect to home page after successful login
            response.sendRedirect("Homepage.html"); 
        } else {
            // Redirect back to login or show error message
            response.getWriter().write("Invalid credentials. Try again!");
        }
    }
}
