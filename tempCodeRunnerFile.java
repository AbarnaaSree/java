import java.util.*;
public class i {
    public static int getMass(int mass,int depth){
        return Math.min(mass,depth)+mass;
    }
    public static void main(String[] args){
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        int initialMass=sc.nextInt();
        int[] dp=new int[n];
        int[] next=new int[n];
        Arrays.fill(dp,-1);
        for(int i=0;i<n;i++){
            Arrays.fill(next,-1);
            for(int j=0;j<=i;j++){
                int depth=sc.nextInt();
                  if(depth==-1){
                    continue;
                  }
                  if(i==0){
                    next[0]=getMass(initialMass,depth);
                    continue;
                  }
                  int best=-1;
                  if(j>0 && dp[j-1]!=-1){
                    best=getMass(dp[j-1],depth);
                  }
                  if(j<i && dp[j]!=-1){
                    int can=getMass(dp[j],depth);
                    best=Math.max(best,can);
                  }
                  next[j]=best;
            }
            int[] temp=dp;
            dp=next;
            next=temp;
        }
        int answer=0;
        for(int i=0;i<n;i++){
            answer=Math.max(answer,dp[i]);
        }
        System.out.print(answer);

    }
}
