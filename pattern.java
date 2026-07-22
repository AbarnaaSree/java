import java.util.*;
public class pattern{
    public static void main(String[] args){
        Scanner sc=new Scanner(System.in);
        int a=sc.nextInt();int num=1;
        for(int i=0;i<a;i++){
            if(i%2==0){
                for(int j=0;j<a;j++){
                    System.out.print(num+j+" ");
                }
                System.out.println();
            }else{
                for(int j=a-1;j>=0;j--){
                    System.out.print(num+j+" ");
                }
                System.out.println();
            }
            num+=a;
        }
    }
}