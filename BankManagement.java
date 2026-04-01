package banking;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;

public class BankManagement {

    static Connection con = DBConnection.getConnection();

    
    public static void createAccount(String name, int pass) {

        try {
            String sql = "INSERT INTO customer(cname, balance, pass_code) VALUES (?, 1000, ?)";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, name);
            ps.setInt(2, pass);

            int x = ps.executeUpdate();

            if (x > 0) {
                System.out.println("Account Created with Balance 1000");
            }

        } catch (Exception e) {
            System.out.println("Error in creating account");
        }
    }


    public static void login(String name, int pass) {

        try {
            String sql = "SELECT * FROM customer WHERE cname=? AND pass_code=?";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, name);
            ps.setInt(2, pass);

            ResultSet rs = ps.executeQuery();

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            if (rs.next()) {

                int ac = rs.getInt("ac_no");

                while (true) {
                    System.out.println("\n1.Deposit");
                    System.out.println("2.Withdraw");
                    System.out.println("3.Transfer");
                    System.out.println("4.Balance");
                    System.out.println("5.Change PIN");
                    System.out.println("6.Logout");

                    int ch = Integer.parseInt(br.readLine());

                    if (ch == 1) {
                        System.out.print("Amount: ");
                        int amt = Integer.parseInt(br.readLine());
                        deposit(ac, amt);

                    } else if (ch == 2) {
                        System.out.print("Amount: ");
                        int amt = Integer.parseInt(br.readLine());
                        withdraw(ac, amt);

                    } else if (ch == 3) {
                        System.out.print("Receiver A/c: ");
                        int r = Integer.parseInt(br.readLine());

                        System.out.print("Amount: ");
                        int amt = Integer.parseInt(br.readLine());

                        transfer(ac, r, amt);

                    } else if (ch == 4) {
                        balance(ac);

                    } else if (ch == 5) {
                        System.out.print("New PIN: ");
                        int newPin = Integer.parseInt(br.readLine());
                        changePin(ac, newPin);

                    } else {
                        break;
                    }
                }

            } else {
                System.out.println("Login Failed");
            }

        } catch (Exception e) {
            System.out.println("Error in login");
        }
    }

    
    public static void deposit(int ac, int amt) {

        if (amt <= 0) {
            System.out.println("Invalid Amount");
            return;
        }

        try {
            String sql = "UPDATE customer SET balance = balance + ? WHERE ac_no=?";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, amt);
            ps.setInt(2, ac);

            ps.executeUpdate();

            System.out.println("Amount Deposited");

        } catch (Exception e) {
            System.out.println("Deposit Failed");
        }
    }

    
    public static void withdraw(int ac, int amt) {

        if (amt <= 0) {
            System.out.println("Invalid Amount");
            return;
        }

        try {
            String check = "SELECT balance FROM customer WHERE ac_no=?";
            PreparedStatement ps = con.prepareStatement(check);

            ps.setInt(1, ac);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int bal = rs.getInt("balance");

                if (bal < amt) {
                    System.out.println("Insufficient Balance");
                    return;
                }
            }

            String sql = "UPDATE customer SET balance = balance - ? WHERE ac_no=?";
            PreparedStatement ps1 = con.prepareStatement(sql);

            ps1.setInt(1, amt);
            ps1.setInt(2, ac);

            ps1.executeUpdate();

            System.out.println("Withdraw Successful");

        } catch (Exception e) {
            System.out.println("Withdraw Failed");
        }
    }

    
    public static void balance(int ac) {

        try {
            String sql = "SELECT balance FROM customer WHERE ac_no=?";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, ac);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("Balance: " + rs.getInt("balance"));
            }

        } catch (Exception e) {
            System.out.println("Error in balance");
        }
    }

    
    public static void transfer(int s, int r, int amt) {

        if (amt <= 0) {
            System.out.println("Invalid Amount");
            return;
        }

        try {
            con.setAutoCommit(false);

            String check = "SELECT balance FROM customer WHERE ac_no=?";
            PreparedStatement ps = con.prepareStatement(check);

            ps.setInt(1, s);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                if (rs.getInt("balance") < amt) {
                    System.out.println("Low Balance");
                    con.rollback();
                    return;
                }
            }

            String d = "UPDATE customer SET balance=balance-? WHERE ac_no=?";
            PreparedStatement ps1 = con.prepareStatement(d);
            ps1.setInt(1, amt);
            ps1.setInt(2, s);
            ps1.executeUpdate();

            String c = "UPDATE customer SET balance=balance+? WHERE ac_no=?";
            PreparedStatement ps2 = con.prepareStatement(c);
            ps2.setInt(1, amt);
            ps2.setInt(2, r);
            ps2.executeUpdate();

            con.commit();
            System.out.println("Transfer Done");

        } catch (Exception e) {
            try {
                con.rollback();
            } catch (Exception ex) {}
            System.out.println("Transfer Failed");
        }
    }

    
    public static void changePin(int ac, int newPin) {

        try {
            String sql = "UPDATE customer SET pass_code=? WHERE ac_no=?";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, newPin);
            ps.setInt(2, ac);

            ps.executeUpdate();

            System.out.println("PIN Updated");

        } catch (Exception e) {
            System.out.println("PIN Change Failed");
        }
    }


}
