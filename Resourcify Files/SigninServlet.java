import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.BufferedReader;
import java.io.IOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/signin")
public class SigninServlet extends HttpServlet {
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
        String username = json.get("username").getAsString();
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
                 "INSERT INTO users (username, email, password) VALUES (?, ?, ?)")) {
             
             stmt.setString(1, username);
             stmt.setString(2, email);
             stmt.setString(3, hashedPassword);
             stmt.executeUpdate();
             
             response.getWriter().write("Signup successful!");
        } catch (SQLException e) {
            response.getWriter().write("Error: " + e.getMessage());
        }
    }
}

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
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
        String email = json.get("email").getAsString();
        String password = json.get("password").getAsString();

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "SELECT password FROM users WHERE email = ?")) {
             
             stmt.setString(1, email);
             ResultSet rs = stmt.executeQuery();
             
             if (rs.next()) {
                 String storedHashedPassword = rs.getString("password");
                 if (PasswordUtil.checkPassword(password, storedHashedPassword)) {
                     response.getWriter().write("Login successful!");
                 } else {
                     response.getWriter().write("Invalid email or password.");
                 }
             } else {
                 response.getWriter().write("Invalid email or password.");
             }
        } catch (SQLException e) {
            response.getWriter().write("Error: " + e.getMessage());
        }
    }
}
