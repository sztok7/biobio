package hr.fer.bioinf.bioinf;

import java.util.ArrayList;
import java.util.HashMap;

public class ReturnValues {
	public final boolean bool;
	public final HashMap<Integer, ArrayList<Edge>> graph;
	public final DfsState state;
	
	public ReturnValues(boolean bool, HashMap<Integer, ArrayList<Edge>> graph, DfsState state) {
		this.bool = bool;
		this.graph = graph;
		this.state = state;
	}
}
