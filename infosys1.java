import java.util.*;
public class infosys1 {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        int index=sc.nextInt();
        long maxsum=sc.nextLong();
        System.out.print(call(n,index,maxsum));
    }
    public static int call(int n,int index,long maxsum){
        long low=1;
        long high=maxsum;
        long answer=1;
        while(low<=high){
            long mid=low+(high-low)/2;
            if(check(n,index,maxsum,mid)){
                answer=mid;
                low=mid+1;
            }else{
                high=mid-1;
            }
        }
        return (int)answer;
    }
    public static boolean check(int n,int index, long maxsum,long peak){
        int left=index;
        int right=n-index-1;
        long leftsum=LeftSum(left,peak);
        long rightsum=RightSum(right,peak);
        long total=leftsum+rightsum+peak;
        return total<=maxsum;

    }
    public static long LeftSum(int left,Long peak){
        long sum=0;
        for(int i=1;i<=left;i++){
            long value=peak-i;
            if(value>=1){
                sum+=value;
            }else{
                sum+=1;
            }
        }
        return sum;
    }
    public static long RightSum(int right,Long peak){
        long sum=0;
        for(int i=1;i<=right;i++){
            long value=peak-2L*i;
            if(value>=1){
                sum+=value;
            }else{
                sum+=1;
            }
        }
        return sum;
    }
   
}/*
public class infosys1 {
    public static int maxValue(int n, int index, int maxSum) {
        long low=1;
        long high=maxSum;
        long answer=1;
        while(low<=high){
            long mid=low+(high-low)/2;
            if(call(n,index,maxSum,mid)){
                answer=mid;
                low=mid+1;
            }else{
                high=mid-1;
            }
        }
        return (int)answer;
    }
    public static boolean call(int n,int index,int maxSum,long peak){
        long left=index;
        long right=n-index-1;
        long leftsum=sum(left,peak);
        long rightsum=sum(right,peak);
        long t=leftsum+rightsum+peak;
        return t<=maxSum;
    }
    public static long sum(long count, long peak) {

        long decreasing = Math.min(count, peak - 1);

        long first = peak - 1;
        long last = peak - decreasing;

        long sum = decreasing * (first + last) / 2;

        long remaining = count - decreasing;

        sum += remaining;

        return sum;
    }
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        int index=sc.nextInt();
        int maxsum=sc.nextInt();
        System.out.print(maxValue(n,index,maxsum));
    }
}*/