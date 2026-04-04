package Banking_Management_System.src;
import java.sql.*;
import java.util.*;


public class BankingApp {

    private static final String url = "jdbc:mysql://localhost:3306/banking_system";
    private static final String user = "root";
    private static final String pass = "atul123@";

    public static void main(String[] args) throws Exception {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection( url , user , pass);

            Scanner sc = new Scanner(System.in);

            User user = new User(con , sc);
            Accounts acc = new Accounts(con, sc);
            AccountManager am = new AccountManager(con, sc);
            
            String email;
            long account_number;

            while (true) {
                System.out.println();
                System.out.println("*** WELCOME TO BANKING SYSTEM ***");
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.print("Enter your choice: ");
                int choice1 = sc.nextInt();

                switch (choice1) {
                    case 1:
                        user.register();
                        break;

                    case 2:
                        email = user.login();
                        if(email!=null){
                            System.out.println();
                            System.out.println("User Logged In!");
                            if(!acc.account_exist(email)){
                                System.out.println();
                                System.out.println("1. Open a new Bank Account");
                                System.out.println("2. Exit");
                                System.out.print("Enter your choice: ");
                                int temp = sc.nextInt();
                                if(temp == 1) {
                                    account_number = acc.open_account(email);
                                    System.out.println("Account Created Successfully");
                                    System.out.println("Your Account Number is: " + account_number);
                                }else{
                                    break;
                                }

                            }
                            
                            account_number = acc.getAccNumber(email);
                            int choice2 = 0;
                            while (choice2 != 5) {
                                System.out.println();
                                System.out.println("1. Debit Money");
                                System.out.println("2. Credit Money");
                                System.out.println("3. Transfer Money");
                                System.out.println("4. Check Balance");
                                System.out.println("5. Log Out");
                                System.out.print("Enter your choice: ");
                                choice2 = sc.nextInt();

                                switch (choice2) {
                                    case 1:
                                        am.debit_money(account_number);
                                        break;

                                    case 2:
                                        am.credit_money(account_number);
                                        break;

                                    case 3:
                                        am.transfer_money(account_number);
                                        break;

                                    case 4:
                                        am.getBalance(account_number);
                                        break;

                                    case 5:
                                        break;

                                    default:
                                        System.out.println("Enter Valid Choice!");
                                        break;
                                }
                            }
                        
                        }else{
                            System.out.println("Incorrect Email or Password!");
                        }

                        break;
                        
                    case 3:
                        System.out.println("THANK YOU FOR USING BANKING SYSTEM!!!");
                        System.out.println("Exiting System!");
                        return;

                    default:
                        System.out.println("Enter Valid Choice");
                        break;
                }
            }


        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
