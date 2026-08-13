
import java.util.*;

public class inin {

    static final long MOD = 1_000_000_007L;

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();

        // 0 = a
        // 1 = e
        // 2 = i/
        // 3 = o
        // 4 = u

        int[][] graph = {
            {1},          // a -> e
            {0, 2},       // e -> a, i
            {0, 1, 3, 4}, // i -> a, e, o, u
            {2, 4},       // o -> i, u
            {0}           // u -> a
        };

        // direction:
        // 0 = no previous move
        // 1 = increasing
        // 2 = decreasing

        long[][] dp = new long[5][3];

        // For a string of length 1,
        // each vowel can be the starting character.
        for (int v = 0; v < 5; v++) {
            dp[v][0] = 1;
        }

        // Build strings from length 2 to n
        for (int len = 2; len <= n; len++) {

            long[][] ndp = new long[5][3];

            for (int u = 0; u < 5; u++) {

                for (int d = 0; d < 3; d++) {

                    if (dp[u][d] == 0) {
                        continue;
                    }

                    for (int v : graph[u]) {

                        // Determine direction
                        int nd = (v > u) ? 1 : 2;

                        // First move OR direction must alternate
                        if (d == 0 || nd != d) {

                            ndp[v][nd] =
                                (ndp[v][nd] + dp[u][d]) % MOD;
                        }
                    }
                }
            }

            dp = ndp;
        }

        long ans = 0;

        for (int v = 0; v < 5; v++) {
            ans = (ans + dp[v][0]) % MOD;
            ans = (ans + dp[v][1]) % MOD;
            ans = (ans + dp[v][2]) % MOD;
        }

        System.out.println(ans);
    }
}