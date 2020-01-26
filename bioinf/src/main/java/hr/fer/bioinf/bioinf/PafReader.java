package hr.fer.bioinf.bioinf;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.javatuples.Pair;

public class PafReader {
	final double SIMILARITY_CUTOFF_PERCENTAGE = 66.0, OVERHANG_PERCENTAGE = 30.0;
	private ArrayList<PafEntry> entries = new ArrayList<PafEntry>();
	HashMap<Integer, ArrayList<Edge>> graph = new HashMap<Integer, ArrayList<Edge>>();

	public PafReader(String path) {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(path));
			String[] fields, ids = new String[2];
			int[] len = new int[2], start = new int[2], end = new int[2];
			String orientation = "", line;
			while (true) {
				line = reader.readLine();
				if (line == null)
					break;
				fields = line.split("\t");
				for (int i = 0; i < 2; i++) {
					ids[i] = fields[i * 5 + 0];
					len[i] = Integer.parseInt(fields[i * 5 + 1]);
					start[i] = Integer.parseInt(fields[i * 5 + 2]);
					end[i] = Integer.parseInt(fields[i * 5 + 3]);
					if (i == 0)
						orientation = fields[4];
				}
				Integer match = Integer.parseInt(fields[9]), length = Integer.parseInt(fields[10]);
				PafEntry entry = new PafEntry(ids, len, start, end, orientation, (double) (match * 1.0) / length);
				if (!entry.valid())
					continue;
				entries.add(entry);
			}
			reader.close();
			System.out.println("Paf entries: " + entries.size());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public HashMap<Integer, ArrayList<Edge>> getGraph() {
		int[] ol = new int[2], el = new int[2];
		int sumoh, id0, id1;
		for (PafEntry entry : entries) {
			Pair<Integer, Integer> key = entry.key();
			// skip self
			if (key.getValue0() == key.getValue1()) {
				continue;
			}
			double si = entry.getSimilarity();
			// skip not similar enough
			if (si < (SIMILARITY_CUTOFF_PERCENTAGE/100.0)) {			
				continue;
			}

			int ohr0 = entry.getLen()[0] - entry.getEnd()[0];
			int ohr1 = entry.getLen()[1] - entry.getEnd()[1];
			ol[0] = entry.getEnd()[0] - entry.getStart()[0];
			ol[1] = entry.getEnd()[1] - entry.getStart()[1];
			el[0] = ohr1 - ohr0;
			el[1] = entry.getStart()[0] - entry.getStart()[1];
			sumoh = entry.getStart()[1] + ohr0;

			if (sumoh > (OVERHANG_PERCENTAGE/100.0)*ol[0]) {
				continue;
			}

			id0 = entry.getIds()[0];
			id1 = entry.getIds()[1];
			if (entry.getContinuation() == Continuation.CONJUGATED) {
				id1 = reverseId(id1);
			}
			createScoreEdges(id0, id1, ol, sumoh, el, si);
		}
		return graph;
	}

	private void createScoreEdges(int from, int to, int[] ol, int sumoh, int[] el, double si) {
		double os = (ol[0] + ol[1])*si/2.0;
		ArrayList<Edge> list = new ArrayList<Edge>();			
		if (graph.containsKey(from)) {
			list.addAll(graph.get(from));
			list.add(new Edge(to, os, os + el[0]/2.0 - sumoh/2.0, el[0]));
			graph.replace(from, list);
		} else {
			list.add(new Edge(to, os, os + el[0]/2.0 - sumoh/2.0, el[0]));
			graph.put(from, list);
		}
		list = new ArrayList<Edge>();
		if (graph.containsKey(reverseId(to))) {
			list.addAll(graph.get(reverseId(to)));
			list.add(new Edge(reverseId(from), os, os + el[0]/2.0 - sumoh/2.0, el[0]));
			graph.replace(reverseId(to), list);
		} else {
			list.add(new Edge(reverseId(from), os, os + el[0]/2.0 - sumoh/2.0, el[0]));
			graph.put(reverseId(to), list);
		}
	}

	private int reverseId(int id1) {
		return id1 ^ 1;
	}
}
