package board.netana;

import elem.CElement;
import elem.resistor.Resistor;
import elem.source.CSource;
import elem.source.VSource;
import syms.Sym;

class OPorteq {
	Sym U, I;
	Sym ii, ui;
	int index;
	OPorteq(Twig t, int index){
		this.index = index;
		ui = Sym.VAR_Index("Uzp", index);
		ii = Sym.VAR_Index("Izp", index);
		Sym R = getR(t);
		Sym U0 = getU(t);
		Sym I0 = getI(t);
		if(U0 == null && I0 == null){
			U = Sym.Mul(ii, R);
			I = Sym.Div(ui, R);
		}
		else  if(U0 != null && I0 == null){
			U = Sym.Add(Sym.Mul(ii, R), U0);
			I = Sym.Div(Sym.Sub(ui, U0), R);
		}
		else  if(U0 == null && I0 != null){
			U = Sym.Mul(Sym.Add(ii, I0), R);
			I = Sym.Add(Sym.Div(ui, R), I0);
			
		}
		else  if(U0 != null && I0 != null){
			U = Sym.Sub(Sym.Mul(Sym.Sub(ii, I0), R),U0); //  U = (I-I0)*R - U0
			I = Sym.Add(I0, Sym.Div(Sym.Add(ui,U0),R)); // I = I0 + (U+U0)/R
		}
	}
	public String toString(){
		String s= "";
		s += "U: " + U.toString() + "\n";
		s += "I: " + I.toString() + "\n";
		return s;
	}
	public static Sym getR(Twig t){
		Sym r = null;
		for(CElement[]ces:t.elements){
			for(CElement c:ces){
				if(c instanceof Resistor){
					if(r!=null){
						throw new TwigCompositionException(t, " Twig has invalid number of resistors");
					}
					else r = ((Resistor)c).getR();
				}
				
			}
		}
		if(r==null){
			throw new TwigCompositionException(t,   " Twig has no Resistors ");
		}
		return r;
	}
	public static Sym getU(Twig t){
		Sym u = null;
		for(CElement[]ces:t.elements){
			for(CElement c:ces){
				if(c instanceof VSource){
					if(u!=null){
						throw new TwigCompositionException(t, " Twig has invalid number of Vsources");
					}
					else u = ((VSource)c).getVoltag();
				}
				
			}
		}
		return u;
	}
	public static Sym getI(Twig t){
		Sym i = null;
		for(CElement[]ces:t.elements){
			for(CElement c:ces){
				if(c instanceof CSource){
					if(i!=null){
						throw new TwigCompositionException(t, " Twig has invalid number of CSources");
					}
					else i = ((CSource)c).getCurrent();
				}
				
			}
		}
		return i;
	}
}
