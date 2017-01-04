package board.netana;

import elem.Node;
import syms.Const;
import syms.Sym;

class Meq {
	Sym uiEq;
	Sym uiEqSolved;
	Sym uiSolvedAfter;
	Meq(Twig[] m, Twig[] tree){
		uiEq = Const.ZERO;
		Node last = null;
		for(int i = 0; i < m.length;i++){
			if(last == null){
				uiEq = Sym.Add(uiEq, m[i].eq.ui);
			}
			else{
				uiEq = last==m[i].A?
						Sym.Add(uiEq, m[i].eq.ui):
						Sym.Sub(uiEq, m[i].eq.ui);
			}
			last = m[i].B;
		}
		
		for(Twig tM : m){
			boolean found =false;
			for(Twig tT : tree){
				if(tM==tT){
					found = true;
					break;
				}
			}
			if(found)
				continue;
			uiSolvedAfter = tM.eq.ui;
			uiEqSolved = uiEq.solve(uiSolvedAfter);
		}
	}
	public String toString(){
		String s = "";
		s += this.uiEq.toString() + "=0\n";
		s += uiEqSolved.toString() + "=" + uiSolvedAfter.toString();
		return s;
	}
}
