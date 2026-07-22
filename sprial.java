public class sprial {
    public static void main(String[] args){
        int[][] a={{1,2,3,4},
                   {12,13,14,5},
                   {11,16,15,6},
                   {10,9,8,7}};
        int rowstart=0,rowend=a.length-1,colstart=0,colend=a[0].length-1;
        while(rowstart<=rowend && colstart<=colend){
            for(int i=colstart;i<=colend;i++){
                System.out.print(a[rowstart][i]+" ");
            }
            rowstart++;
            for(int i=rowstart;i<=rowend;i++){
                System.out.print(a[i][colend]+" ");
            }
            colend--;
            for(int i=colend;i>=colstart;i--){
                System.out.print(a[rowend][i]+" ");
            }
            rowend--;
            for(int i=rowend;i>=rowstart;i--){
                System.out.print(a[i][colstart]+" ");
            }
          colstart++;

        }

    }
}
