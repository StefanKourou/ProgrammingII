package core;

import java.util.Scanner;
import java.util.ArrayList;

public class SocialMain {

    ArrayList<String> user = new ArrayList<String>();
    ArrayList<String> password = new ArrayList<String>();
    Scanner in = new Scanner(System.in);
    boolean flag;

    public void createUser() {
        System.out.println("Welcome! Please enter a valid UserName:");
        String x;
        do{
            this.flag = false;
            x = this.in.nextLine();
            for (int i = 0; i < this.user.size(); i++){
                if (x.equals(this.user.get(i))) {
                    this.flag = true;
                    System.err.println("This Username is already in use!");
                    System.out.println("Please choose a different UserName:");
                    break;
                }
            }
        } while(this.flag);
        this.user.add(x);
        createPw(this.user.size()-1);
    }
    
    public void createPw(int i) {
        String pw;
        this.flag = true;
        do {
            System.out.println("Choose a password:");
            pw= this.in.nextLine();
           // System.out.println(x<5?"Your password is really weak!":x<10?"Your password is weak!":flag=false);
            if (pw.length() < 5) {
                System.out.println("Your password is really weak!");;
            } else if (pw.length() < 10) {
                System.out.println("Your password is weak!");
            } else {
                System.out.println("Strong password!");
                this.flag = false;
            }
        } while(this.flag);
        this.password.add(i,pw);
    }

}
