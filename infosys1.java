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
}
