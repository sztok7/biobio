/**
 * 
 */
package hr.fer.bioinf.bioinf;

/**
 * @author filip
 *
 */
public class Edge {
	private int dest, el;
	private double os, es;
	
	public Edge(int dest, double os, double es, int el) {
		this.dest = dest;
		this.os = os;
		this.es = es;
		this.el = el;
	}

	public int getDest() {
		return dest;
	}

	public void setDest(int dest) {
		this.dest = dest;
	}

	public int getEl() {
		return el;
	}

	public void setEl(int el) {
		this.el = el;
	}

	public double getOs() {
		return os;
	}

	public void setOs(double os) {
		this.os = os;
	}

	public double getEs() {
		return es;
	}

	public void setEs(double es) {
		this.es = es;
	}
}
