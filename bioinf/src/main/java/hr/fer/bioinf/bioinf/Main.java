package hr.fer.bioinf.bioinf;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {

	public static void main(String[] args) {
		if (args.length != 4) {
			System.err.println("Invalid number of arguments.\n");
			System.err.println("<reads.fasta> <contigs.fasta> <overlaps_rr.paf> <overlaps_cr.paf>");
		}
		
		FastaReader rr = new FastaReader(args[0]);
		FastaReader cr = new FastaReader(args[1]);
		PafReader rro = new PafReader(args[2]);
		PafReader cro = new PafReader(args[3]);
		
		SeqHandler seq = new SeqHandler();
		seq.addSequences(rr.getEntries(), Type.READ);
		seq.addSequences(cr.getEntries(), Type.CONTIG);
		
		HashMap<Integer, ArrayList<Edge>> graph = new HashMap<Integer, ArrayList<Edge>>();
		graph = mergeGraphs(graph, rro.getGraph());
		graph = mergeGraphs(graph, cro.getGraph());
	}

	private static HashMap<Integer, ArrayList<Edge>> mergeGraphs(HashMap<Integer, ArrayList<Edge>> graph,
			HashMap<Integer, ArrayList<Edge>> graph2) {
		// TODO Auto-generated method stub
		return null;
	}

}
