import java.util.*;
public class i {
    public static long getMass(long mass,long depth){
        return Math.min(mass,depth)+mass;
    }
    public static void main(String[] args){
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        long initialMass=sc.nextLong();
        long[] dp=new long[n];
        Arrays.fill(dp,-1);
        for(int i=0;i<n;i++){
            long[] newdp=new long[n];
            Arrays.fill(newdp,-1);
            int count=0;
            while(count<=i && sc.hasNextLong()){
                long depth=sc.nextLong();
                if(depth==-1){
                    continue;
                }
                if(i==0){
                    newdp[0]=getMass(initialMass,depth);
                }else{
                    int j=count;
                    if(j>0 && dp[j-1]!=-1){
                        long mass=getMass(dp[j-1],depth);
                        newdp[j]=Math.max(newdp[j],mass);
                    }
                    if(j<i && dp[j]!=-1){
                        long mass=getMass(dp[j],depth);
                        newdp[j]=Math.max(newdp[j],mass);
                    }
                }
                count++;
                }
                dp=newdp;
            }
            long answer=0;
            for(int i=0;i<n;i++){
                answer=Math.max(answer,dp[i]);
            }
            System.out.print(answer);
        }

    }
