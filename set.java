public class set{
    public static void main(String[] args) {

        int[] nums = {2,4,6,2,5};

        int prev2 = 0; // dp[i-2]
        int prev1 = 0; // dp[i-1]


        for(int num : nums) {

            int pick = num + prev2;

            int notPick = prev1;

            int current = Math.max(pick, notPick);


            prev2 = prev1;
            prev1 = current;
        }


        System.out.println(prev1);
    }
}