import java.util.*;
public class sort{
    public static void main(String[] args){
    Scanner sc=new Scanner(System.in);
    int n=sc.nextInt();
    int[] a=new int[n];
    for(int i=0;i<n;i++){
        a[i]=sc.nextInt();
    }
    int i=0;
    int j=a.length-1;
    while(i<j){
        System.out.print(a[j]+" "+a[i]+" ");
        j--;
        i++;
    }
    if(i==j){
        System.out.print(a[i]);
    }
}
}
