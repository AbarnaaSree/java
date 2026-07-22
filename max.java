
import java.util.*;
public class max {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        int[] a=new int[n];
        for(int i=0;i<n;i++){
            a[i]=sc.nextInt();
        }
        int max1=0,max2=0;
        int ele1=-1,ele2=-1;
        int count=1;
        for(int i=1;i<a.length;i++){
            if(a[i]==a[i-1]){
                count++;
            }else{
                if(count>max1){
                    max2=max1;
                    ele2=ele1;
                    max1=count;
                    ele1=a[i-1];
                    
                }else if(count>max2){
                    max2=count;
                    ele2=a[i-1];
                }
                count=1;
            }
        }
        if(count>max1){
            max2=max1;
            ele2=ele1;
            max1=count;
            ele1=a[a.length-1];
        }else if(count>max2){
            max2=count;
            ele2=a[a.length-1];
        }
        System.out.println(ele1 +" "+ele2);
}
}