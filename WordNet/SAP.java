import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Digraph;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;

public class SAP {
    
    private final Digraph dg;
    BFSCache vcache, wcache;
   
    private class BFSCache {
	private boolean[] visited;
	private int[] distanceTo;
	private Queue<Integer> recentModified = new LinkedList<>();
	private Queue<Integer> queue = new LinkedList<>();

	private BFSCache(int size) {
	    visited = new boolean[size];
	    distanceTo = new int[size];
	    for(int i = 0; i < size; i++) {
		visited[i] = false;
		distanceTo[i] = -1;
	    }
	}

	private void clear() {
	    while (!recentModified.isEmpty()) {
		int v = recentModified.poll();
		visited[v] = false;
		distanceTo[v] = -1;
	    }
	    queue.clear();
	}
    }

    private int[] findSAP(BFSCache vcache, BFSCache wcache, Set<Integer> set) {
	int[] res;
	BFSCache curCache = vcache;
	BFSCache otherCache = wcache;
	while (!curCache.queue.isEmpty()) {
	    res = helperSAP(curCache, otherCache, set);
	    if (res[0] >= 0) return res;
	    BFSCache tmp = curCache;
	    curCache = otherCache;
	    otherCache = tmp;
	}
	if (!otherCache.queue.isEmpty()) {
	    res = helperSAP(otherCache, curCache, set);
	    if (res[0] >= 0) return res;
	 }
	 vcache.clear();
	 wcache.clear();
	 return new int[]{-1, -1};
    }

    private int[] helperSAP(BFSCache curCache, BFSCache otherCache, Set<Integer> set) {
	 int v = curCache.queue.poll();
	 for (int next : dg.adj(v)) {
	     if (!curCache.visited[next]) {
		 curCache.queue.add(next);
		 curCache.recentModified.add(next);
		 curCache.visited[next] = true;
		 curCache.distanceTo[next] = curCache.distanceTo[v]+1;
		 if (set.contains(next)) { // ancestor is found!                                                                                  
		     int length = curCache.distanceTo[next] + otherCache.distanceTo[next];
		     int ancestor = next;
		     curCache.clear();
		     otherCache.clear();
		     return new int[]{length, ancestor};
		 }
		 set.add(next);
	     }
	 }
	 return new int[]{-1, -1};
     }

     // constructor takes a digraph (not necessarily a DAG)
     public SAP (Digraph G) {
	 if (G == null) throw new IllegalArgumentException();
	 dg = new Digraph(G);
	 vcache = new BFSCache(dg.V());
	 wcache = new BFSCache(dg.V());
     }

     // length of shortest ancestral path between v and w; -1 if no such path
     public int length(int v, int w) {
	 Set<Integer> set = new HashSet<>();
	 int res = initialization(v, w, set);
	 if (res >= 0) return 0;
	 return findSAP(vcache, wcache, set)[0];
     }

     // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
     public int ancestor(int v, int w) {
	 Set<Integer> set = new HashSet<>();
	 int res = initialization(v, w, set);
	 if (res >= 0) return res;
	 return findSAP(vcache, wcache, set)[1];
     }


     // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
     public int length(Iterable<Integer> v, Iterable<Integer> w) {
	 Set<Integer> set = new HashSet<>();
	 int res = initialization(v, w, set);
	 if (res >= 0) return 0;
	 return findSAP(vcache, wcache, set)[0];
     }


     // a common ancestor that participates in shortest ancestral path; -1 if no such path
     public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
	 Set<Integer> set = new HashSet<>();
	 int res = initialization(v, w, set);
	 if (res >= 0) return res;
	 return findSAP(vcache, wcache, set)[1];
     }

     private int initialization(int v, int w, Set<Integer> set) {
	 checkValid(v);
	 checkValid(w);
	 if (v == w) return v;
	 vcache.recentModified.add(v);
	 vcache.queue.add(v);
	 vcache.visited[v] = true;
	 vcache.distanceTo[v] = 0;
	 set.add(v);
	 wcache.recentModified.add(w);
	 wcache.queue.add(w);
	 wcache.visited[w] = true;
	 wcache.distanceTo[w] = 0;
	 set.add(w);
	 return -1;
     }

     private int initialization(Iterable<Integer> v, Iterable<Integer> w, Set<Integer> set) {
	 checkValid(v);
	 checkValid(w);
	 for (int i : v) {
	     vcache.recentModified.add(i);
	     vcache.queue.add(i);
	     vcache.visited[i] = true;
	     vcache.distanceTo[i] = 0;
	     set.add(i);
	 }
	 for (int i : w) {
	     wcache.recentModified.add(i);
	     wcache.queue.add(i);
	     wcache.visited[i] = true;
	     wcache.distanceTo[i] = 0;
	     if (set.contains(i)) return i; // if there is overlap in v and w, then the SAP is 0, and ancestor is the overlapping vertex
	     set.add(i);
	 }
	 return -1;
     }

     private void checkValid(int v) {
	 if (v < 0 || v >= dg.V()) throw new IllegalArgumentException();
     }

     private void checkValid(Iterable<Integer> v) {
	 for (int i : v)
	     if (i < 0 || i >= dg.V()) throw new IllegalArgumentException();
     }

    // do unit testing of this class
    public static void main(String[] args) {
	In in = new In(args[0]);
	Digraph G = new Digraph(in);
	SAP sap = new SAP(G);
	while (!StdIn.isEmpty()) {
	    int v = StdIn.readInt();
	    int w = StdIn.readInt();
	    int length   = sap.length(v, w);
	    int ancestor = sap.ancestor(v, w);
	    StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
	}
    }


}
