package board;

import elem.CElement;
import elem.Node;
import elem.OnePort;
import syms.Sym;

public class Wire {
	private CElement A,B;
	private Sym U,I;
	
	private Wire(CElement A, CElement B){
		this.A = A;
		this.B = B;
			
	}
	public CElement get(CElement ce){
		if(ce == A) return B;
		return A;
	}
	public static void connect(CElement A, int portA, CElement B, int portB){
		Wire w = new Wire(A, B);
		A.port(portA, w);
		B.port(portB, w);
	}
	public static void connect(CElement from, CElement to){
		Wire w = new Wire(from, to);
		from.port(w);
		to.port(w);
	}
	public static void connect(OnePort op, Node n){
		Wire w = new Wire(op, n);
		op.port(w);
		n.port(w);
	}
	public static void connectSeries(OnePort...ops){
		for(int i = 0; i<ops.length-1; i++){
			connect(ops[i],ops[i+1]);
		}
	}
	
	public static Node connectShunt(OnePort from, OnePort...ops){
		Node n = new Node(ops.length+1);
		connect(from, n);
		for(int i = 1; i < ops.length+1;i++){
			connect(ops[i-1],n);
		}
		return n;
	}
	public static Node UnifyShunt(OnePort to, OnePort...ops){
		Node n = new Node(ops.length+1);
		connect(to,n);
		for(int i = 0; i < ops.length;i++){
			connect(ops[i],n);
		}
		return n;
	}
}
