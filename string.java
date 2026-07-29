import java.util.Scanner;

public class string {
     static boolean isAlphaNumeric(char ch) {
        return (ch >= 'a' && ch <= 'z') ||
               (ch >= 'A' && ch <= 'Z') ||
               (ch >= '0' && ch <= '9');
    }
    public static void main(String[] args){
        Scanner sc=new Scanner(System.in);
        String a=sc.nextLine();
        int i=a.length()-1;
        int j=0;
        while(i>=0 && j<a.length()){
           if(isAlphaNumeric(a.charAt(i)) && isAlphaNumeric(a.charAt(j))){
            System.out.print(a.charAt(i));
            i--;
            j++;
           }else if(isAlphaNumeric(a.charAt(i)) && !isAlphaNumeric(a.charAt(j))){
            System.out.print(a.charAt(j));
            j++;
           }else if(!isAlphaNumeric(a.charAt(i)) && isAlphaNumeric(a.charAt(j))){
            i--;
           }else{
            System.out.print(a.charAt(j));
            i--;
            j++;
           }

    }
}
}
