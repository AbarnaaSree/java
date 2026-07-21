import java.util.*;

public class Solution {

    public static long maxEnergy(int N, int M, List<List<Integer>> V) {

        // dp[i][j] -> Map(min_so_far -> max_energy)
        List<List<Map<Integer, Long>>> dp = new ArrayList<>();

        for (int i = 0; i < N; i++) {
            List<Map<Integer, Long>> row = new ArrayList<>();
            for (int j = 0; j < M; j++) {
                row.add(new HashMap<>());
            }
            dp.add(row);
        }

        // start cell
        int start = V.get(0).get(0);
        dp.get(0).get(0).put(start, (long) start * start);

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {

                if (i == 0 && j == 0) continue;

                Map<Integer, Long> curr = new HashMap<>();
                int val = V.get(i).get(j);

                // from top
                if (i > 0) {
                    for (Map.Entry<Integer, Long> e : dp.get(i - 1).get(j).entrySet()) {
                        int mn = e.getKey();
                        long energy = e.getValue();

                        int newMin = Math.min(mn, val);
                        long newEnergy = energy + (long) val * newMin;

                        curr.put(newMin,
                                Math.max(curr.getOrDefault(newMin, 0L), newEnergy));
                    }
                }

                // from left
                if (j > 0) {
                    for (Map.Entry<Integer, Long> e : dp.get(i).get(j - 1).entrySet()) {
                        int mn = e.getKey();
                        long energy = e.getValue();

                        int newMin = Math.min(mn, val);
                        long newEnergy = energy + (long) val * newMin;

                        curr.put(newMin,
                                Math.max(curr.getOrDefault(newMin, 0L), newEnergy));
                    }
                }

                // 🔥 PRUNE dominated states
                List<Map.Entry<Integer, Long>> items =
                        new ArrayList<>(curr.entrySet());

                // sort by min_value
                items.sort(Comparator.comparingInt(Map.Entry::getKey));

                Map<Integer, Long> pruned = new HashMap<>();
                long maxEnergySoFar = -1;

                // traverse from large min to small min
                for (int idx = items.size() - 1; idx >= 0; idx--) {
                    int mn = items.get(idx).getKey();
                    long energy = items.get(idx).getValue();

                    if (energy > maxEnergySoFar) {
                        pruned.put(mn, energy);
                        maxEnergySoFar = energy;
                    }
                }

                dp.get(i).set(j, pruned);
            }
        }

        long ans = 0;
        for (long v : dp.get(N - 1).get(M - 1).values()) {
            ans = Math.max(ans, v);
        }

        return ans;
    }
}