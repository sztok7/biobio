/**
 * 
 */
package hr.fer.bioinf.bioinf;

import java.util.HashSet;
import java.util.Set;

/**
 * @author filip
 *
 */
public class DfsState {
	Set<Integer> visited = new HashSet<Integer>();
	Path current_path = new Path();
	
	public DfsState visit(Edge edge) {
		visited.add(edge.getDest());
		current_path.vertices.add(edge.getDest());
		current_path.extension_lengths.add(edge.getEl());
		current_path.length += edge.getEl();
		return this;
	}
	
	public boolean been(int id) {
		return visited.contains(id);
	}
	
	public void clearVisitedExcept(int id) {
		visited.clear();
		visited.add(id);
	}
}
