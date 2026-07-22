import java.util.*;

public class matrix {
    public static void main(String[] args){

        Scanner sc = new Scanner(System.in);

        int a = sc.nextInt();   // Bigger matrix size
        int b = sc.nextInt();   // Smaller matrix size

        int[][] arr = new int[a][a];

        for(int i=0;i<a;i++){
            for(int j=0;j<a;j++){
                arr[i][j] = sc.nextInt();
            }
        }

        int[][] arr1 = new int[b][b];

        for(int i=0;i<b;i++){
            for(int j=0;j<b;j++){
                arr1[i][j] = sc.nextInt();
            }
        }


        HashMap<Integer,Integer> map = new HashMap<>();

        // Count bigger matrix elements
        for(int i=0;i<a;i++){
            for(int j=0;j<a;j++){
                map.put(arr[i][j], map.getOrDefault(arr[i][j],0)+1);
            }
        }


        // Match smaller matrix elements
        for(int i=0;i<b;i++){
            for(int j=0;j<b;j++){

                if(!map.containsKey(arr1[i][j]) || map.get(arr1[i][j]) == 0){
                    System.out.println("FALSE");
                    return;
                }

                map.put(arr1[i][j], map.get(arr1[i][j])-1);
            }
        }

        System.out.println("TRUE");
    }
}