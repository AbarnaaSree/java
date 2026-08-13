import java.util.*;
public class infoinfo {
    public static long MOD=1_000_000_007L;
    public static void main(String[] args){
    Scanner sc=new Scanner(System.in);
    int n=sc.nextInt();
    int[][] graphs={
        {1},
        {0,2},
        {0,1,3,4},
        {2,4},
        {0}
    };
    long[][] dp=new long[5][3];
    for(int v=0;v<5;v++){
        dp[v][0]=1;
    }
    for(int len=2;len<=n;len++){
        long[][] ndp=new long[5][3];
        for(int u=0;u<5;u++){
            for(int d=0;d<3;d++){
                if(dp[u][d]==0){
                    continue;
                }
                for(int v:graphs[u]){
                    int nd=0;
                    if(v>u){
                        nd=1;
                    }else{
                        nd=2;
                    }
                    if(d==0 || nd!=d){
                    ndp[v][nd]=(ndp[v][nd]+dp[u][d])%MOD;
                    }
                }
            }
        }
        dp=ndp;
    }
    long ans=0;
    for(int v=0;v<5;v++){
        ans=(ans+dp[v][0])%MOD;
        ans=(ans+dp[v][1])%MOD;
        ans=(ans+dp[v][2])%MOD;
    }
    System.out.print(ans);

    }
}
