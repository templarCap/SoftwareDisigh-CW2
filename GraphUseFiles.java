import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.DataFormatException;

public class GraphUseFiles {
    public ArrayList<Set<Integer>> setEdges = new ArrayList<>();
    private final ArrayList<Integer> topSort = new ArrayList<>();

    public void addVert() {
        setEdges.add(new HashSet<>());
    }

    public void addEdge(Integer from, Integer to) {
        setEdges.get(from).add(to);
    }

    private boolean dfs3Color(Integer vert, Boolean isCycle, ArrayList<Integer> color) {
        color.set(vert, 1);
        for (Integer neigh : setEdges.get(vert)) {
            if (color.get(neigh) == 1) {
                isCycle = true;
            } else if (color.get(neigh) == 0) {
                dfs3Color(neigh, isCycle, color);
            }
        }
        color.set(vert, 2);
        topSort.add(vert);
        return isCycle;
    }

    public ArrayList<Integer> topSort() throws DataFormatException {
        ArrayList<Integer> color = new ArrayList<>();
        for (int i = 0; i < setEdges.size(); ++i) {
            color.add(0);
        }
        boolean isCycle = false;
        for (int i = 0; i < setEdges.size(); ++i) {
            if (color.get(i) == 0 && !isCycle) {
                isCycle = dfs3Color(i, false, color);
            }
        }
        if (isCycle) {
            throw new DataFormatException("This graph have cycle.");
        }
        return topSort;
    }
}
