package board.netana;

import java.util.ArrayList;

import elem.CElement;
import elem.resistor.Resistor;
import elem.source.CSource;
import elem.source.VSource;

class TwigAna {
	
	static void ANALYZENETWORK(ArrayList<Twig> twigs){
		for(Twig t : twigs){
			subR(t);
			checkCSource(t);
			checkVSource(t);
		}
		split(twigs);
	}
	private static void split(ArrayList<Twig> twigs){
		ArrayList<Twig> newTwigs =  new ArrayList<Twig>();
		ArrayList<Twig> oldTwigs =  new ArrayList<Twig>();
		for(int i = 0; i < twigs.size(); i++){
			Twig[] st = split(twigs.get(i));
			if(st!=null){
				oldTwigs.add(twigs.get(i));
				for(Twig t : st){
					newTwigs.add(t);
				}
			}
		}
		for(Twig t:oldTwigs){
			twigs.remove(t);
		}
		for(Twig t:newTwigs){
			twigs.add(t);
		}
	}
	private static Twig[] split(Twig t){
		int j = -1;
		Twig[] ts = {t};
		if(t.elements.length==1){
			return ts;
		}
		else if(t.elements.length==2){
			for(CElement[] ces : t.elements){
				if(ces.length==1&& ces[0] instanceof CSource ){
					return ts;
				}
			}
			//No cs has been found in any branch. then we can split the branches up.
			
			for(int i = 0;i< t.elements.length;i++){
				if(t.elements[i].length==1 && t.elements[i][0] instanceof Resistor ){
					j = i;
				}
			}
			
		}
		else if(t.elements.length ==3){
			for(int i = 0;i< t.elements.length;i++){
				if(t.elements[i].length==1 && t.elements[i][0] instanceof Resistor ){
					j = i;
				}
			}
		}
		if(j==-1){
			return ts;
		}
		Twig t1 = new Twig(t.A, t.B, t.elements[j]);
		CElement[][] restArr = new CElement[t.elements.length-1][];
		boolean off = false;
		for(int i = 0; i< t.elements.length;i++){
			if(j==i){
				off = true;
				continue;
			}
			restArr[!off?i:i-1] = t.elements[i];
		}
		Twig t2 = new Twig(t.A,t.B, restArr);
		Twig[] twigs = {t1,t2};
		return twigs;
	}
	
	private static void subR(Twig t) {
		// substitute R series
		for(int index= 0; index < t.elements.length; index++){
			CElement[] ces = t.elements[index];
			int rs = 0;
			for(CElement ce : ces){
				if(ce instanceof Resistor)
					rs++;
			}
			if (rs > 1){
				Resistor[] rArr =  new Resistor[rs];
				CElement[] rest = new CElement[ces.length-rs+1];
				int i =0;
				int j = 1;
				for(CElement ce : ces){
					if(ce instanceof Resistor){
						rArr[i++] = (Resistor)ce;
					}
					else{
						rest[j++] = ce;
					}
				}
				Resistor r = Resistor.UNITE_SERIES(rArr);
				rest[0] = r;
				t.elements[index] =rest;
			}
		}
		// substitute R shunt
		ArrayList<Integer> branches = new ArrayList<>();
		for(int index= 0; index < t.elements.length; index++){
			CElement[] ces = t.elements[index];
			if(ces.length==1){
				if(ces[0] instanceof Resistor){
					branches.add(index);
				}
			}
		}
		if(branches.size()>1){
			CElement[][] elems = new CElement[t.elements.length-branches.size()+1][];
			Resistor[] rArr = new Resistor[branches.size()];
			int rInd = 0;
			int eInd = 1;
			for(int i = 0; i < t.elements.length; i++){
				if(branches.contains(i)){
					rArr[rInd] = (Resistor) t.elements[i][0];
					rInd++;
				}
				else{
					elems[eInd] = t.elements[i];
					eInd++;
				}
			}
			Resistor r = Resistor.UNITE_SHUNT(rArr);
			elems[0] = new CElement[1];
			elems[0][0] = r;
			t.elements = elems;
		}
	}
	
	private static void subV(Twig t){
		for(int index = 0; index<t.elements.length;index++){
			CElement[] cs = t.elements[index];
			boolean hasV = false;
			for(CElement ce : cs){
				if(ce instanceof VSource){
					hasV = true;
					break;
				}
			}
			if(hasV){
				ArrayList<VSource> vs = new ArrayList<VSource>();
				ArrayList<CElement> rest = new ArrayList<CElement>();
				for(CElement ce : cs){
					if(ce instanceof VSource){
						vs.add((VSource) ce);
					}
					else{
						rest.add(ce);
					}
				}
				
			}
		}
	}
	
	private static void checkCSource(Twig t){
		boolean hasCS = false;
		// Checks if the Twig has more than one C Source in one branch.
		for(CElement[] ces: t.elements){
			for(CElement ce : ces){
				if(ce instanceof CSource){
					if(hasCS){
						throw new TwigCompositionException(t, "Twig has more than one c source in one branch.");
					}
					else{
						//CS should be alone.
						if(ces.length!=1){
							throw new TwigCompositionException(t, "Twig has a branch with a CS and another CE.");
						}
						hasCS = true;
					}
				}
			}
		}
		//if It has CS, there should be another branch without any CS.
		if(hasCS){
			if(t.elements.length==1){
				throw new TwigCompositionException(t, "Twig only has one branch with one CS.");
			}
		}
	}
	
	private static void checkVSource(Twig t){
		// Checks if the Twig has V Sources in more than one branch.
		boolean hasVS = false;
		for(CElement[] ces: t.elements){
			boolean foundInMe = false;
			for(CElement ce : ces){
				
				if(ce instanceof VSource){
					if(hasVS){
						throw new TwigCompositionException(t, "Twig has v sources in different branches.");
					}
					foundInMe = true;
					hasVS = true;
				}
				
			}
			//Checks if the branch with he CSource has a resistor wired in series in.
			if(foundInMe){
				if(ces.length==1){
					throw new TwigCompositionException(t, "Twig has a branch with a V Source without any resistor wired in series.");
				}
			}
		}
	}
	
}
