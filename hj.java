import java.util.Scanner;

public class hj{

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();

        n = n + 1;

        char[] binary = new char[32];
        int index = 0;

        // Manual binary conversion
        while (n > 0) {
            int rem = n % 2;
            binary[index++] = (char)(rem + '0');
            
            n = n / 2;
        }

        // Print answer by traversing binary from MSB to LSB,
        // skipping the leading 1.
        boolean firstOneSkipped = false;

        for (int i = index - 1; i >= 0; i--) {

            if (!firstOneSkipped) {
                firstOneSkipped = true; // skip leading 1
                continue;
            }

            if (binary[i] == '0')
                System.out.print("3");
            else
                System.out.print("4");
        }
    }
}