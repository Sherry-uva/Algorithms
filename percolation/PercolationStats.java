import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    
    private double[] threshold;
    private int size;
    private int numTrials;

    public PercolationStats(int n, int trials) {    // perform trials independent experiments on an n-by-n grid
        if (n <= 0 || trials <= 0) throw new IllegalArgumentException("Invalid parameter range!");
        size = n;
        numTrials = trials;
        threshold = new double[numTrials];
        Percolation perc;
        int[] seq;
        int count;
        for (int i = 0; i < numTrials; i++) {
            count = 0;
            perc = new Percolation(size);
            seq = StdRandom.permutation(size*size);
            while (!perc.percolates()) {
                perc.open(seq[count]/size+1, seq[count] % size+1);
                count++;
            }
            threshold[i] = ((double) count)/(size*size);
        }
    }
    public double mean() {                          // sample mean of percolation threshold
        return StdStats.mean(threshold);
    }

    public double stddev() {                       // sample standard deviation of percolation threshold
        return StdStats.stddev(threshold);
    }

    public double confidenceLo() {                  // low  endpoint of 95% confidence interval
        return mean()-1.96*stddev()/Math.sqrt(numTrials);
    }

    public double confidenceHi() {                  // high endpoint of 95% confidence interval
        return mean()+1.96*stddev()/Math.sqrt(numTrials);
    }

    public static void main(String[] args) {       // test client (described below)
        int num = Integer.parseInt(args[0]);
        int trial = Integer.parseInt(args[1]);
        PercolationStats ps = new PercolationStats(num, trial);

        String confidence = ps.confidenceLo() + ", " + ps.confidenceHi();
        StdOut.println("mean                    = " + ps.mean());
        StdOut.println("stddev                  = " + ps.stddev());
        StdOut.println("95% confidence interval = " + confidence);
    }
}
