import java.util.*;

public class info2 {

    static final long MOD = 1000000007L;

    // vowels:
    // 0 = a
    // 1 = e
    // 2 = i
    // 3 = o
    // 4 = u

    public static long countStrings(int n) {

        if (n == 1) {
            return 5;
        }

        /*
         * up[v]   = number of strings ending at vowel v
         *           where the LAST transition was increasing.
         *
         * down[v] = number of strings ending at vowel v
         *           where the LAST transition was decreasing.
         */

        long[] up = new long[5];
        long[] down = new long[5];

        /*
         * For the first transition, there is no restriction
         * on direction.
         *
         * We can directly generate all valid transitions.
         */
        for (int from = 0; from < 5; from++) {

            for (int to : next(from)) {

                if (to > from) {
                    up[to]++;
                } else if (to < from) {
                    down[to]++;
                }
            }
        }

        // We already processed length = 2
        for (int len = 3; len <= n; len++) {

            long[] newUp = new long[5];
            long[] newDown = new long[5];

            for (int from = 0; from < 5; from++) {

                /*
                 * If previous direction was DOWN,
                 * current direction MUST be UP.
                 *
                 * from -> to where to > from
                 */
                for (int to : next(from)) {

                    if (to > from) {
                        newUp[to] =
                                (newUp[to] + down[from]) % MOD;
                    }

                    /*
                     * If previous direction was UP,
                     * current direction MUST be DOWN.
                     *
                     * from -> to where to < from
                     */
                    if (to < from) {
                        newDown[to] =
                                (newDown[to] + up[from]) % MOD;
                    }
                }
            }

            up = newUp;
            down = newDown;
        }

        long answer = 0;

        for (int i = 0; i < 5; i++) {
            answer = (answer + up[i]) % MOD;
            answer = (answer + down[i]) % MOD;
        }

        return answer;
    }

    // Directed transition rules
    static int[] next(int vowel) {

        switch (vowel) {

            // a -> e
            case 0:
                return new int[]{1};

            // e -> a, i
            case 1:
                return new int[]{0, 2};

            // i -> a, e, o, u
            case 2:
                return new int[]{0, 1, 3, 4};

            // o -> i, u
            case 3:
                return new int[]{2, 4};

            // u -> a
            case 4:
                return new int[]{0};

            default:
                return new int[]{};
        }
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();

        System.out.println(countStrings(n));

        sc.close();
    }
}