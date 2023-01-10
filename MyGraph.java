import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * My small class graph, with can do topological sorting using dfs.
 */
public class MyGraph {
    /**
     * Param to storage graph in set edge in ich vert.
     */
    private final ArrayList<Set<Integer>> setEdges = new ArrayList<>();
    /**
     * Param where will be storage topological list graph.
     */
    private final ArrayList<Integer> topSort = new ArrayList<>();

    /**
     * Method add new vert in graph.
     */
    public void addVert() {
        setEdges.add(new HashSet<>());
    }

    /**
     * Method add new edge in graph.
     * @param from vert from.
     * @param to vert to.
     */
    public void addEdge(Integer from, Integer to) {
        setEdges.get(from).add(to);
    }

    /**
     * private Method to use dfs with 3 color, only for topological sort.
     * @param vert Current vert of dfs.
     * @param isCycle Boolean information about cycle in graph.
     * @param color Array of all vertices color.
     * @return False if we have cycle in graph, True in others.
     */
    private boolean dfs3Color(Integer vert, Boolean isCycle, ArrayList<Integer> color,
                              boolean isTopSort) {
        color.set(vert, 1);
        for (Integer neigh : setEdges.get(vert)) {
            if (color.get(neigh) == 1) {
                isCycle = true;
                break;
            } else if (color.get(neigh) == 0) {
                dfs3Color(neigh, isCycle, color, isTopSort); // Recursion start.
            }
        }
        color.set(vert, 2);
        if (isTopSort) { // Add only for topSort.
            topSort.add(vert);
        }
        return isCycle;
    }

    /**
     * Method to do topological sorting.
     * @return list of vert in topological sort.
     */
    public ArrayList<Integer> topSort() {
        ArrayList<Integer> color = new ArrayList<>(Collections.nCopies(setEdges.size(), 0));
        boolean isCycle = false;
        for (int i = 0; i < setEdges.size(); ++i) {
            if (color.get(i) == 0 && !isCycle) {
                isCycle = dfs3Color(i, false, color, true);
            }
        }

        /* If graph has cycle topSort impossible to do.*/
        if (isCycle) {
            return null;
        }
        return topSort;
    }

    /**
     * Method to get information is graph with cycle.
     * @return True if graph has cycle, False in others.
     */
    public Boolean isCycle() {
        ArrayList<Integer> color = new ArrayList<>(Collections.nCopies(setEdges.size(), 0));
        boolean isCycle = false;
        for (int i = 0; i < setEdges.size(); ++i) {
            if (color.get(i) == 0 && !isCycle) {
                isCycle = dfs3Color(i, false, color, true);
            }
        }
        return isCycle;
    }
}
