
import java.util.*;
public class freq {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        int[] a=new int[n];
        for(int i=0;i<n;i++){
            a[i]=sc.nextInt();
        }
        int[] x=new int[n];
        int maxi=max(a)+1;
        for(int i=0;i<n;i++){
            int count=1;
            if(a[i]!=maxi){
                for(int j=i+1;j<n;j++){
                    if(a[i]==a[j]){
                        count++;
                        a[j]=maxi;
                    }
                }
                x[i]=count;
            }
        }
        while(max(x)>0){
            int m=max(x);
            for(int i=0;i<n;i++){
                if(x[i]==m){
                    for(int j=0;j<m;j++){
                    System.out.print(a[i]+" ");
                    }
                    x[i]=0;
                }
            }
        }


        
    }
    public static int max(int[] a){
        int m=a[0];
        for(int i=1;i<a.length;i++){
            if(a[i]>m){
                m=a[i];
            }
        }
        return m;
    }
}