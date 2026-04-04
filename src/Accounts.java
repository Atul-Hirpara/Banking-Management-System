package Banking_Management_System.src;

import java.sql.*;
import java.util.*;



public class Accounts {
    private Connection con;
    private Scanner sc;

    public Accounts(Connection con , Scanner sc){
        this.con = con;
        this.sc = sc;
    }

    public long open_account(String email){
        try {
            if(!account_exist(email)){
                sc.nextLine();
                System.out.print("Enter full name: ");
                String name = sc.nextLine();
                System.out.print("Enter Initial amount: ");
                double balance = sc.nextDouble();
                sc.nextLine();
                System.out.print("Enter Security PIN: ");
                String security_pin = sc.nextLine();

                long account_number = generateAccNumber();  

                String open_q = "insert into accounts (account_number , full_name , email , balance , security_pin) values(? , ? , ? , ? , ?)";

                PreparedStatement st = con.prepareStatement(open_q);
                st.setLong(1, account_number);
                st.setString(2, name);
                st.setString(3, email);
                st.setDouble(4, balance);
                st.setString(5, security_pin);

                int affectedRow = st.executeUpdate();

                if(affectedRow > 0){
                    return account_number;
                }else{
                    throw new RuntimeException("Account Creation Fialed!");
                }

            }
        } catch (Exception e) {
            System.out.println(e);
        }

        throw new RuntimeException("Account Already Exist!");
    }

    public boolean account_exist(String email){
        try {
            String q = "select * from accounts where email=?";
            PreparedStatement st = con.prepareStatement(q);
            st.setString(1, email);

            ResultSet rs = st.executeQuery();

            if(rs.next()){
                return true;
            }

        } catch (Exception e) {
            System.out.println(e);
        }

        return false;
    }

    private long generateAccNumber(){
        try {
            String q = "select account_number from accounts order by account_number desc limit 1";

            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery(q);

            if(rs.next()){
                long last_acc_no = rs.getLong("account_number");
                return last_acc_no + 1;
            }

        } catch (Exception e) {
            System.out.println(e);
        }

        return 10000100;
    }

    public long getAccNumber(String email){
        try {
            String q = "select account_number from accounts where email=?";

            PreparedStatement st = con.prepareStatement(q);

            st.setString(1, email);

            ResultSet rs = st.executeQuery();

            if(rs.next()){
                return rs.getLong("account_number");
            }


        } catch (Exception e) {
            System.out.println(e);
        }

        throw new RuntimeException("Account number does not exist!");

    }

}
