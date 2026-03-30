package banking;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        try {
            while (true) {

                System.out.println("\n1.Create");
                System.out.println("2.Login");
                System.out.println("3.Exit");

                int ch = Integer.parseInt(br.readLine());

                if (ch == 1) {

                    System.out.print("Name: ");
                    String name = br.readLine();

                    System.out.print("Pass: ");
                    int pass = Integer.parseInt(br.readLine());

                    BankManagement.createAccount(name, pass);

                } else if (ch == 2) {

                    System.out.print("Name: ");
                    String name = br.readLine();

                    System.out.print("Pass: ");
                    int pass = Integer.parseInt(br.readLine());

                    BankManagement.login(name, pass);

                } else {
                    break;
                }
            }

        } catch (Exception e) {
            System.out.println("Error");
        }
    }
}