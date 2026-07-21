import java.util.*;
public class subarray {
    public static void main(String[] args){
    Scanner sc=new Scanner(System.in);
    int n=sc.nextInt();
    while(n-- >0){
        int  x=sc.nextInt();
        int[] arr=new int[x];
        for(int i=0;i<x;i++){
            arr[i]=sc.nextInt();
        }
        int sum=0;
        int count=0;
        HashMap<Integer,Integer> map=new HashMap<>();
        for(int i=0;i<x;i++){
            sum+=arr[i];
            if(map.containsKey(sum)){
                count+=map.get(sum);
                
            }
            map.put(sum,map.getOrDefault(sum,0)+1);
        }
        System.out.println(count);
    }
    }
}
