import java.util.*;
public class digit {
    public static void main(String[] args){
        Scanner sc=new Scanner(System.in);
        String s=sc.nextLine();
        int d=sc.nextInt();
        add(s,d);
    }
    public static void add(String s,int d){
        for(int i=0;i<s.length();i++){
            int sum=(s.charAt(i)-'0')+d;
        
        System.out.print(sum);
        }
    }
}
