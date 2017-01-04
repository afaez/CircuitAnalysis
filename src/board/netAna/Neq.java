package board.netana;

import java.util.ArrayList;

import elem.Node;
import syms.Const;
import syms.Sym;

class Neq {
	Sym[] eq0Arr;
	Sym eq0;
	private ArrayList<Sym> sList = new ArrayList<Sym>();
	Neq(Node n, Twig[] twigs){
		
		for(int i=0;i< twigs.length;i++){
			Twig t = twigs[i];
			if(t.A != n && t.B != n){
				continue;
			}
			OPorteq opeq = t.eq;
			if(opeq == null)
				throw new RuntimeException("Twig " + i + " has no equation.");
			if(t.A==n){
				sub(opeq.I);
			}
			else if(t.B==n){
				add(opeq.I);
			}
		}
		eq0Arr = sList.toArray(new Sym[sList.size()]);
		createSym();
	}
	public void createSym(){
		eq0 = Const.ZERO;
		for(Sym s : eq0Arr){
			eq0 = Sym.Add(eq0, s);
		}
	}
	public void substitue(Meq[] meqs){
		for(Meq meq: meqs)
			for(int i = 0; i < eq0Arr.length;i++){
				//System.out.println("Subbing " + meq.uiSolvedAfter + " with " + meq.uiEqSolved + " in: " + neq.eq0Arr[i] );
				eq0Arr[i] = eq0Arr[i].substitute(meq.uiSolvedAfter, meq.uiEqSolved);
			}
		createSym();
	}
	private void add(Sym s){
		sList.add(s);
	}
	private void sub(Sym s){
		sList.add(Sym.KehrA(s));
	}
	public String toString(){
		String s = "[";
		for(Sym sym : eq0Arr){
			s += sym.toString() + ", ";
		}
		s = s.substring(0, s.length()-2)+"]";
		return s;
	}
}
