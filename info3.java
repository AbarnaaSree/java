import java.util.*;

public class info3 {

    static long getMass(long mass, long depth) {
        return mass + Math.min(mass, depth);
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();
        long initialMass = sc.nextLong();

        // dp[j] = maximum mass reaching position j
        long[] dp = new long[n];
        Arrays.fill(dp, -1);

        for (int i = 0; i < n; i++) {

            long[] newDp = new long[n];
            Arrays.fill(newDp, -1);

            int count = 0;

            // Read enough values for this row.
            // Ignore -1 padding values.
            while (count <= i && sc.hasNextLong()) {

                long depth = sc.nextLong();

                if (depth == -1) {
                    continue;
                }

                if (i == 0) {
                    // First cell
                    newDp[0] = getMass(initialMass, depth);
                } 
                else {

                    // Position in this row
                    int j = count;

                    // From upper-left
                    if (j > 0 && dp[j - 1] != -1) {

                        long mass = getMass(dp[j - 1], depth);

                        newDp[j] = Math.max(newDp[j], mass);
                    }

                    // From upper-right
                    if (j < i && dp[j] != -1) {

                        long mass = getMass(dp[j], depth);

                        newDp[j] = Math.max(newDp[j], mass);
                    }
                }

                count++;
            }

            dp = newDp;
        }

        long answer = 0;

        for (int j = 0; j < n; j++) {
            answer = Math.max(answer, dp[j]);
        }

        System.out.println(answer);

        sc.close();
    }
}