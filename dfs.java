import java.util.*;
public class dfs {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        while(N-- > 0){
            int x=sc.nextInt();
            for(int i=1;i<=9 && i<=x;i++ )
            {
                dfs(i,x);
            }

        }
    }
    public static void dfs(int num,int x)
    {
        if(num>x)
            return;
        System.out.print(num+" ");
        int last=num%10;
        if(last == 9){
            dfs(num*10+8,x);
        }
        else if(last == 0){
            dfs(num*10+1,x);
        }else{
            dfs(num*10+last-1,x);
            dfs(num*10+last+1,x);   
        }
    }
}
