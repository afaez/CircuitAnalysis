package elem;

import syms.Sym;

public abstract class OnePort extends CElement{
	//private double v, i;
	protected OnePort() {
		super(2);
	}
	protected OnePort(int index) {
		super(2, index);
	}
	protected Sym getDefaultSym(){
		return Sym.VAR_Index(model(), index());
	}
	
}
