package Banking_Management_System.src;

import java.sql.*;
import java.util.*;

public class User {
    private Connection con;
    private Scanner sc;

    public User(Connection con , Scanner sc){
        this.con = con;
        this.sc = sc;
    }

    public void register(){

        try {
            sc.nextLine();
            System.out.print("Full name: ");
            String full_name = sc.nextLine();
            System.out.print("Email: ");
            String email = sc.nextLine();
            System.out.print("Password: ");
            String pass = sc.nextLine();

            if(user_exist(email)){
                System.out.println("User Already Exist for this email address");
                return;
            }

            String register_q = "insert into user (full_name , email , password) values(? , ? , ?)";

            PreparedStatement st = con.prepareStatement(register_q);
            st.setString(1, full_name);
            st.setString(2, email);
            st.setString(3, pass);

            int affectedRow = st.executeUpdate();

            if(affectedRow > 0){
                System.out.println("Registration Successfull!");
            }else{
                System.out.println("Registration Failed!");
            }
            
        } catch (Exception e) {
            System.out.println(e);
        }
    
    }

    public String login(){

        sc.nextLine();
        System.out.print("Email: ");
        String email = sc.nextLine();
        System.out.print("Password: ");
        String pass = sc.nextLine();

        try {
            String login_q = "select * from user where email=? and password=?";

            PreparedStatement st = con.prepareStatement(login_q);

            st.setString(1, email);
            st.setString(2, pass);

            ResultSet rt = st.executeQuery();

            if(rt.next()){
                return email;
            }
            else{
                return null;
            }
            
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public boolean user_exist(String email){

        try {
            String q = "select * from user where email = ?";
            PreparedStatement st = con.prepareStatement(q);
            st.setString(1, email);

            ResultSet rt = st.executeQuery();

            if(rt.next()){
                return true;
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

}
