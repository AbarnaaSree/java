import java.util.*;
public class reverse {
    public static void main(String[] args){
        Scanner sc=new Scanner(System.in);
        String a=sc.nextLine();
        revese(0,a);

    }
    public static void revese(int i,String a){
        if(i==a.length()){
            return;
        }
        int start=i;
        while(i<a.length() && a.charAt(i)!=' '){
            i++;
        }
        if(i<a.length()){
            revese(i+1,a);
        }
        for(int j=start;j<i;j++){
            System.out.print(a.charAt(j));
        }
        if(start!=0){
            System.out.print(" ");
        }
    }
}
