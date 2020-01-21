/**
 * 
 */
package hr.fer.bioinf.bioinf;

import java.util.Set;

/**
 * @author filip
 *
 */
public class DfsState {
	Set<Integer> visited;
	Path current_path;
	
	public DfsState() {
		
	}
	
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
