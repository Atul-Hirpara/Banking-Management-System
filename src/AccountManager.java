package Banking_Management_System.src;

import java.sql.*;
import java.util.*;

public class AccountManager {
    private Connection con;
    private Scanner sc;

    public AccountManager(Connection con , Scanner sc){
        this.con = con;
        this.sc = sc;
    }

    public void credit_money(long account_number) throws SQLException {
        sc.nextLine();
        System.out.print("Enter amount: ");
        double amount = sc.nextDouble();
        sc.nextLine();
        System.out.print("Enter Security PIN: ");
        String security_pin = sc.nextLine();

        try {
            con.setAutoCommit(false);

            if(account_number != 0){
                PreparedStatement st = con.prepareStatement("select * from accounts where account_number=? and security_pin=?");
                st.setLong(1, account_number);
                st.setString(2, security_pin);
                ResultSet rs = st.executeQuery();

                if(rs.next()){
                    String credit_q = "update accounts set balance = balance + ? where account_number = ?";
                    PreparedStatement st1 = con.prepareStatement(credit_q);
                    st1.setDouble(1, amount);
                    st1.setLong(2, account_number);

                    int affectedRow = st1.executeUpdate();

                    if(affectedRow > 0){
                        System.out.println("Rs." +amount+ " credited successfully");
                        con.commit();
                        con.setAutoCommit(true);
                        return;
                    }else{
                        System.out.println("Transaction Failed!");
                        con.rollback();
                        con.setAutoCommit(true);
                    }

                }else{
                    System.out.println("Invalid PIN!");
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        con.setAutoCommit(true);

    }

    public void debit_money(long account_number) throws SQLException {
        sc.nextLine();
        System.out.print("Enter amount: ");
        double amount = sc.nextDouble();
        sc.nextLine();
        System.out.print("Enter Security PIN: ");
        String security_pin = sc.nextLine();

        try {
            con.setAutoCommit(false);
            if(account_number != 0){
                PreparedStatement st = con.prepareStatement("select * from accounts where account_number=? and security_pin=?");
                st.setLong(1, account_number);
                st.setString(2, security_pin);
                ResultSet rs = st.executeQuery();

                if(rs.next()){
                    double curr_balance = rs.getDouble("balance");

                    if(amount <= curr_balance){
                       String debit_q = "update accounts set balance = balance - ? where account_number = ?";
                       PreparedStatement st1 = con.prepareStatement(debit_q);
                       st1.setDouble(1, amount);
                       st1.setLong(2, account_number);

                       int affectedRow = st1.executeUpdate();

                       if(affectedRow > 0){
                            System.out.println("Rs." +amount+ " debited successfully");
                            con.commit();
                            con.setAutoCommit(true);
                            return;
                       }
                       else{
                            System.out.println("Transaction Failed!");
                            con.rollback();
                            con.setAutoCommit(true);

                       }
                    }else{
                        System.out.println("Insufficient Balance");
                    }
                }else{
                    System.out.println("Invalid PIN!");
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        con.setAutoCommit(true);
    }
    
    public void getBalance(long account_number){
        sc.nextLine();
        System.out.print("Enter Security PIN: ");
        String security_pin = sc.nextLine();

        try {
            String q = "select balance from accounts where security_pin = ? and account_number = ?";
            PreparedStatement st = con.prepareStatement(q);

            st.setString(1, security_pin);
            st.setLong(2, account_number);

            ResultSet rs = st.executeQuery();

            if(rs.next()){
                double balance = rs.getDouble("balance");
                System.out.println("Balance: " + balance);
            }else{
                System.out.println("Invalid PIN!");
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void transfer_money(long sender_accno) throws SQLException {
        sc.nextLine();
        System.out.print("Enter Receiver Account Number: ");
        long receiver_accno = sc.nextLong();
        System.out.print("Enter Amount: ");
        double amount = sc.nextDouble();
        sc.nextLine();
        System.out.print("Enter Security PIN: ");
        String security_pin = sc.nextLine();

        try {
            con.setAutoCommit(false);
            if(sender_accno != 0 && receiver_accno != 0){
                PreparedStatement st = con.prepareStatement("select * from accounts where security_pin = ? and account_number = ?");
                st.setString(1, security_pin);
                st.setLong(2, sender_accno);

                ResultSet rs = st.executeQuery();

                if(rs.next()){
                    double curr_balance = rs.getDouble("balance");

                    if(curr_balance >= amount){
                        String debit_q = "update accounts set balance = balance - ? where account_number = ?";
                        String credit_q = "update accounts set balance = balance + ? where account_number = ?";

                        PreparedStatement debit_st = con.prepareStatement(debit_q);
                        debit_st.setDouble(1, amount);
                        debit_st.setLong(2, sender_accno);

                        PreparedStatement credit_st = con.prepareStatement(credit_q);
                        credit_st.setDouble(1, amount);
                        credit_st.setLong(2, receiver_accno);

                        int debit_affected = debit_st.executeUpdate();
                        int credit_affected = credit_st.executeUpdate();

                        if(debit_affected > 0 && credit_affected > 0){
                            System.out.println("Transaction Successfull!");
                            System.out.println("Rs." + amount + " Trasferred Successfull");
                            con.commit();
                            con.setAutoCommit(true);
                            return;
                        }else{
                            System.out.println("Transaction Failed!");
                            con.rollback();
                            con.setAutoCommit(true);
                        }

                    }else{
                        System.out.println("Insufficient balance");
                    }
                }else{
                    System.out.println("Invalid Security PIN!");
                }
            }else{
                System.out.println("Invalid Account Number");
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        con.setAutoCommit(true);
    }


}
