import java.util.*;

public class swap {

    static int kadane(int[] a) {
        int best = a[0];
        int curr = a[0];

        for (int i = 1; i < a.length; i++) {
            curr = Math.max(a[i], curr + a[i]);
            best = Math.max(best, curr);
        }

        return best;
    }

    static int solve(int[] a, int k) {
        int n = a.length;

        if (k == 0)
            return kadane(a);

        int answer = Integer.MIN_VALUE;

        for (int l = 0; l < n; l++) {

            int sum = 0;
            ArrayList<Integer> insideNeg = new ArrayList<>();
            ArrayList<Integer> outsidePos = new ArrayList<>();

            for (int r = l; r < n; r++) {

                sum += a[r];

                if (a[r] < 0)
                    insideNeg.add(a[r]);

                // collect outside elements
                outsidePos.clear();

                for (int i = 0; i < l; i++) {
                    if (a[i] > 0)
                        outsidePos.add(a[i]);
                }

                for (int i = r + 1; i < n; i++) {
                    if (a[i] > 0)
                        outsidePos.add(a[i]);
                }


                Collections.sort(insideNeg); // most negative first
                outsidePos.sort(Collections.reverseOrder());


                int newSum = sum;

                int swaps = Math.min(k,
                        Math.min(insideNeg.size(), outsidePos.size()));

                for (int i = 0; i < swaps; i++) {

                    int gain = outsidePos.get(i) - insideNeg.get(i);

                    if (gain <= 0)
                        break;

                    newSum += gain;
                }

                answer = Math.max(answer, newSum);
            }
        }

        return answer;
    }


    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();
        int k = sc.nextInt();

        int[] a = new int[n];

        for (int i = 0; i < n; i++)
            a[i] = sc.nextInt();

        System.out.println(solve(a, k));

        sc.close();
    }
}