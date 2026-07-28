import java.util.Scanner;

public class position {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int start = sc.nextInt();
        int end = sc.nextInt();

        if (start % 2 == 0)
            start++;
        else
            start += 2;

        for (int i = start; i < end; i += 2) {
            System.out.print(i);
            if (i + 2 < end)
                System.out.print(",");
        }
    }
}