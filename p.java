import java.util.*;
public class p {

    static void removeUnbalanced(String s) {

        char[] arr = s.toCharArray();

        int open = 0;

        // Remove extra ')'
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == '(') {
                open++;
            } else if (arr[i] == ')') {
                if (open > 0) {
                    open--;
                } else {
                    arr[i] = '#';   // Mark for removal
                }
            }
        }

        int close = 0;

        // Remove extra '('
        for (int i = arr.length - 1; i >= 0; i--) {
            if (arr[i] == ')') {
                close++;
            } else if (arr[i] == '(') {
                if (close > 0) {
                    close--;
                } else {
                    arr[i] = '#';   // Mark for removal
                }
            }
        }

        // Print answer
        for (char ch : arr) {
            if (ch != '#')
                System.out.print(ch);
        }
        System.out.println();
    }

    public static void main(String[] args) {
        removeUnbalanced("((abc)((de))");
        removeUnbalanced("(((ab)");
    }
}