import java.util.*;
public class merge{
    public static void main(String[] args){
        Scanner sc=new Scanner(System.in);
        int a=sc.nextInt();
        int b=sc.nextInt();
        int[] x=new int[a];
        for(int i=0;i<a;i++){
            x[i]=sc.nextInt();  
        }
        int[] y=new int[b];
        for(int i=0;i<b;i++){
            y[i]=sc.nextInt();  
        }
        boolean as=false;
        if(x[0]<x[1]){
            as=true;
        }
        boolean ds=false;
        if(y[0]<y[1]){
            ds=true;
        }
        int l,r;
        if(as){
          l=0;
        }else{
            l=x.length-1;
        }
        if(ds){
            r=0;
        }else{
            r=y.length-1;
        }
        while(l>=0 && l<x.length && r>=0 && r<y.length){
            if(x[l]<y[r]){
                System.out.print(x[l]+" ");
                if(as){
                    l++;
                }else{
                    l--;
                }
            }else{
                System.out.print(y[r]+" ");
                if(ds){
                    r++;
                }else{
                    r--;
                }
            }
        }
        while(l<=0 && l>x.length){
            System.out.print(x[l]+" ");
            if(as){
                l++;
            }else{
                l--;
            }
        }
        while(r<=0 && r>y.length){
            System.out.print(y[r]+" ");
            if(ds){
                r++;
            }else{
                r--;
            }
        }
        
    }
}