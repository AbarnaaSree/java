import java.util.*;
public class set1{
public static void main(String[] args){
        Scanner sc=new Scanner(System.in);
       

        int[][] a = {
            {1,2,3},
            {4,5,6},
            {7,8,9}
        };

        int n = a.length;

        for (int d = 0; d <n+n-1; d++) {
        if(d%2==0){
            int row = Math.min(d, n - 1);
            int col = d - row;

            while (row >= 0 && col < n) {
                System.out.print(a[row][col] + " ");
                row--;
                col++;
            }
        }else{
            int col=Math.min(d, n-1);
            int row=d-col;
            while(row<n&& col>=0){
                System.out.print(a[row][col]+" ");
                row++;
                col--;
            }
        }
        }
    }
}
