import java.util.*;
public class leftsum {
    public static void main(String[] args){
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        int[] arr=new int[n];
        int total=0;
        for(int i=0;i<n;i++){
            arr[i]=sc.nextInt();
            total+=arr[i];
        }
        int leftsum=0;
        int rightsum=0;
        int index=-1;
        for(int i=0;i<n;i++){
            rightsum=total-leftsum-arr[i];
            if(leftsum==rightsum){
                index=i;
                break;
            }
            leftsum+=arr[i];
        }
        System.out.println(index);
    }
}
