package hr.fer.bioinf.bioinf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import org.javatuples.Pair;

/**
 * @author filip
 *
 */
public class DfsPathGenerator {
	private HashMap<Integer, ArrayList<Edge>> graph = new HashMap<Integer, ArrayList<Edge>>();
	private HashMap<Integer, ArrayList<Edge>> graph_sorted_os = new HashMap<Integer, ArrayList<Edge>>();
	private HashMap<Integer, ArrayList<Edge>> graph_sorted_es = new HashMap<Integer, ArrayList<Edge>>();
	private HashMap<Pair<Integer, Integer>, ArrayList<Path>> paths = new HashMap<Pair<Integer, Integer>, ArrayList<Path>>();
	private SeqHandler seq;
	private DfsState state;

	public DfsPathGenerator(HashMap<Integer, ArrayList<Edge>> graph, SeqHandler seq) {
		this.graph.putAll(graph);
		this.seq = seq;
	}

	/**
	 * @return
	 */
	public HashMap<Pair<Integer, Integer>, ArrayList<Path>> generatePaths() {
		for (int id : seq.getAllByType(Type.CONTIG)) {
			if (!graph.containsKey(id))
				continue;
			startDfsGreedy(id);
		}
		return paths;
	}

	private void addPath(Path path) {
		Pair<Integer, Integer> key = new Pair<Integer, Integer>(path.vertices.get(0),
				path.vertices.get(path.vertices.size() - 1));
		ArrayList<Path> containedPaths = new ArrayList<Path>();
		if (paths.containsKey(key)) {
			containedPaths = paths.remove(key);
		}
		containedPaths.add(path);
		paths.put(key, containedPaths);
	}

	private void startDfsGreedy(int id) {
		state = new DfsState();
		state.visit(new Edge(id, 0, 0, seq.getLength(id)));

		Comparator<Edge> osComparator = new Comparator<Edge>() {
			public int compare(Edge e1, Edge e2) {
				double os1 = e1.getOs();
				double os2 = e2.getOs();
				if (os1 == os2)
					return 0;
				return os1 > os2 ? 1 : -1;
			}
		};

		Comparator<Edge> esComparator = new Comparator<Edge>() {
			public int compare(Edge e1, Edge e2) {
				double es1 = e1.getEs();
				double es2 = e2.getEs();
				if (es1 == es2)
					return 0;
				return es1 > es2 ? 1 : -1;
			}
		};

		for (Integer key : graph.keySet()) {
			ArrayList<Edge> edges = graph.get(key);
			Collections.sort(edges, osComparator);
			graph_sorted_os.put(key, edges);
		}

		for (Integer key : graph.keySet()) {
			ArrayList<Edge> edges = graph.get(key);
			Collections.sort(edges, esComparator);
			graph_sorted_es.put(key, edges);
		}

		for (Edge edge : graph.get(id)) {
			if (edge.getDest() == id)
				continue;

			dfsOs(edge);
			state.clearVisitedExcept(id);

			dfsEs(edge);
			state.clearVisitedExcept(id);
		}
	}

	private boolean dfsOs(Edge from_edge) {
		state.visit(from_edge);

		if (seq.getType(from_edge.getDest()) == Type.CONTIG) {
			addPath(state.current_path);
			return true;
		}

		if (!graph.containsKey(from_edge.getDest()))
			return false;
		for (Edge edge : graph.get(from_edge.getDest())) {
			if (!state.been(edge.getDest()) && dfsOs(edge)) {
				return true;
			}
		}

		return false;
	}

	private boolean dfsEs(Edge from_edge) {
		state.visit(from_edge);

		if (seq.getType(from_edge.getDest()) == Type.CONTIG) {
			addPath(state.current_path);
			return true;
		}

		for (Edge edge : graph_sorted_es.get(from_edge.getDest())) {
			if (!state.been(edge.getDest()) && dfsEs(edge)) {
				return true;
			}
		}

		return false;
	}
}
