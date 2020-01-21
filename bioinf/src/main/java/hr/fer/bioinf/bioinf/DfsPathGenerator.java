package hr.fer.bioinf.bioinf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import org.javatuples.Pair;

public class DfsPathGenerator {
	private HashMap<Integer, ArrayList<Edge>> graph = new HashMap<Integer, ArrayList<Edge>>();
	private HashMap<Integer, ArrayList<Edge>> graph_sorted_os = new HashMap<Integer, ArrayList<Edge>>();
	private HashMap<Integer, ArrayList<Edge>> graph_sorted_es = new HashMap<Integer, ArrayList<Edge>>();
	private HashMap<Pair<Integer, Integer>, ArrayList<Path>> paths = new HashMap<Pair<Integer,Integer>, ArrayList<Path>>();
	private SeqHandler seq = new SeqHandler();
	
	public DfsPathGenerator(HashMap<Integer, ArrayList<Edge>> graph, SeqHandler seq) {
		this.graph = graph;
		this.seq = seq;
	}
	
	public void addPath(Path path) {
		Pair<Integer, Integer> key = new Pair<Integer, Integer>(path.vertices.get(0), path.vertices.get(path.vertices.size() - 1));
		ArrayList<Path> containedPaths = new ArrayList<Path>();
		if (paths.containsKey(key)) {
			containedPaths = paths.remove(key);
		}
		containedPaths.add(path);
		paths.put(key, containedPaths);
	}
	
	public HashMap<Pair<Integer, Integer>, ArrayList<Path>> generatePaths() {
		for(int id : seq.getAllByType(Type.CONTIG)) {
			startDfsGreedy(id);
		}
		return paths;
	}
	
	private void startDfsGreedy(int id) {
		DfsState state = new DfsState();
		state.visit(new Edge(id, 0, 0, seq.getLength(id)));
		
		Comparator<Edge> osComparator = new Comparator<Edge>() {
			public int compare(Edge e1, Edge e2) {
				double os1 = e1.getOs();
				double os2 = e2.getOs();
				if (os1 == os2) return 0;
				return os1 > os2 ? 1 : -1;
			}
		};
		
		Comparator<Edge> esComparator = new Comparator<Edge>() {
			public int compare(Edge e1, Edge e2) {
				double es1 = e1.getEs();
				double es2 = e2.getEs();
				if (es1 == es2) return 0;
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
			if (edge.getDest() == id) continue;
			
			ReturnValues ret = dfs(graph_sorted_os, state, edge);
			graph_sorted_os = ret.graph;
			state = ret.state;
			state.clearVisitedExcept(id);
			
			ret = dfs(graph_sorted_es, state, edge);
			graph_sorted_es = ret.graph;
			state = ret.state;
			state.clearVisitedExcept(id);
		}
	}
	
	private ReturnValues dfs(HashMap<Integer, ArrayList<Edge>> graph, DfsState state, Edge from_edge) {
		state.visit(from_edge);
		
		if(seq.getType(from_edge.getDest()) == Type.CONTIG) {
			addPath(state.current_path);
			return new ReturnValues(true, graph, state);
		}
		
		for (Edge edge : graph.get(from_edge.getDest())) {
			ReturnValues r = dfs(graph, state, edge);
			if (!state.been(edge.getDest()) && r.bool) {
				return new ReturnValues(true, graph, state);
			}
		}
		
		return new ReturnValues(false, graph, state);
	}
}
