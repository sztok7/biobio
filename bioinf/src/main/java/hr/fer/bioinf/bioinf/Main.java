package hr.fer.bioinf.bioinf;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {

	public static void main(String[] args) {
		/*if (args.length != 4) {
			System.err.println("Invalid number of arguments.\n");
			System.err.println("<reads.fasta> <contigs.fasta> <overlaps_rr.paf> <overlaps_cr.paf>");
			return;
		}*/
		
		FastaReader rr = new FastaReader(/*args[0]*/"/home/filip/Desktop/biobioinf/EColisynthetic/ecoli_test_reads.fasta");
		System.out.println("Fasta1 finito");
		FastaReader cr = new FastaReader(/*args[1]*/"/home/filip/Desktop/biobioinf/EColisynthetic/ecoli_test_contigs.fasta");
		System.out.println("Fasta2 finito");
		PafReader rro = new PafReader(/*args[2]*/"/home/filip/Desktop/biobioinf/EColisynthetic/overlaps_rr.paf");
		System.out.println("Paf1 finito");
		PafReader cro = new PafReader(/*args[3]*/"/home/filip/Desktop/biobioinf/EColisynthetic/overlaps_cr.paf");
		System.out.println("Paf2 finito");
		
		SeqHandler seq = new SeqHandler();
		System.out.println("SeqHandler made");
		seq.addSequences(rr.getEntries(), Type.READ);
		System.out.println("SeqHan added reads");
		seq.addSequences(cr.getEntries(), Type.CONTIG);
		System.out.println("SeqHan added contigs");
		
		HashMap<Integer, ArrayList<Edge>> graph = new HashMap<Integer, ArrayList<Edge>>();
		graph = mergeGraphs(graph, rro.getGraph());
		System.out.println("Merged rr graph");
		graph = mergeGraphs(graph, cro.getGraph());
		System.out.println("Merged cr graph");
		
		System.out.println(graph.size());
		
		System.out.println("gg");
	}

	private static HashMap<Integer, ArrayList<Edge>> mergeGraphs(HashMap<Integer, ArrayList<Edge>> graph,
			HashMap<Integer, ArrayList<Edge>> graph2) {
		HashMap<Integer, ArrayList<Edge>> new_graph = new HashMap<Integer, ArrayList<Edge>>();
		new_graph.putAll(graph);
		new_graph.putAll(graph2);
		return new_graph;
	}

}
