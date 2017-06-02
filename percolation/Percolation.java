import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    
    private WeightedQuickUnionUF sites;
    private WeightedQuickUnionUF sites0;
    private boolean[] status; // true: site is open
    private int size; // length and width of the grid
    private int numOpen;

    public Percolation(int n) {                // create n-by-n grid, with all sites blocked
        if (n <= 0) throw new IllegalArgumentException("Invalid grid size!");
        size = n;
        numOpen = 0;
        sites = new WeightedQuickUnionUF(n*n+2); // the last two sites are the virtual top site and virtual bottom site
        sites0 = new WeightedQuickUnionUF(n*n+1);
        status = new boolean[n*n];
    }

    public void open(int row, int col) {    // open site (row, col) if it is not open already
        if (row < 1 || col < 1 || row > size || col > size) throw new IndexOutOfBoundsException("Parameter out of range!");
        if (!isOpen(row, col)) {
            status[(row-1)*size + col-1] = true;
            int index = (row-1)*size + col-1;
            if (row == 1) {
                sites.union(size*size, col-1);
                sites0.union(size*size, col-1);
            }
            if (row == size) sites.union(size*size+1, (row-1)*size+col-1);
            if (hasLeftNeighbor(row, col) && isOpen(row, col-1)) {
                sites.union(index, index-1);
                sites0.union(index, index-1);
            }
            if (hasRightNeighbor(row, col) && isOpen(row, col+1)) {
                sites.union(index, index+1);
                sites0.union(index, index+1);
            }
            if (hasUpNeighbor(row, col) && isOpen(row-1, col)) {
                sites.union(index, index-size);
                sites0.union(index, index-size);
            }
            if (hasDownNeighbor(row, col) && isOpen(row+1, col)) {
                sites.union(index, index+size);
                sites0.union(index, index+size);
            }
            numOpen++;
        }
    }

    private boolean hasLeftNeighbor(int row, int col) {
        if (col == 1) return false;
        return true;
    }

    private boolean hasRightNeighbor(int row, int col) {
        if (col == size) return false;
        return true;
    }

    private boolean hasUpNeighbor(int row, int col) {
        if (row == 1) return false;
        return true;
    }

    private boolean hasDownNeighbor(int row, int col) {
        if (row == size) return false;
        return true;
    }

    public boolean isOpen(int row, int col) {  // is site (row, col) open?
        if (row < 1 || col < 1 || row > size || col > size) throw new IndexOutOfBoundsException("Parameter out of range!");
        return status[(row-1)*size + col-1];
    }

    public boolean isFull(int row, int col) {  // is site (row, col) full?
        if (row < 1 || col < 1 || row > size || col > size) throw new IndexOutOfBoundsException("Parameter out of range!");
        return sites0.connected((row-1)*size+col-1, size*size);
    }

    public int numberOfOpenSites() {       // number of open sites
        return numOpen;
    }

    public boolean percolates() {              // does the system percolate?
        return sites.connected(size*size, size*size+1);
    }
    
    //    public static void main(String[] args)   // test client (optional)
}
