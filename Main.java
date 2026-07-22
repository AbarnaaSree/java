import java.util.*;

public class Main {

    static String[] keypad = {
        "",     // 0
        "",     // 1
        "abc",  // 2
        "def",  // 3
        "ghi",  // 4
        "jkl",  // 5
        "mno",  // 6
        "pqrs", // 7
        "tuv",  // 8
        "wxyz"  // 9
    };

    static void solve(int index, int[] digits, String result) {

        // Base case: all digits are processed
        if (index == digits.length) {
            System.out.print(result + " ");
            return;
        }

        // Get letters corresponding to current digit
        String letters = keypad[digits[index]];

        // Try each possible letter
        for (int i = 0; i < letters.length(); i++) {

            solve(index + 1,
                  digits,
                  result + letters.charAt(i));
        }
    }


    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        int T = sc.nextInt();

        while (T-- > 0) {

            int N = sc.nextInt();

            int[] digits = new int[N];

            for (int i = 0; i < N; i++) {
                digits[i] = sc.nextInt();
            }

            solve(0, digits, "");

            System.out.println();
        }

        sc.close();
    }
}