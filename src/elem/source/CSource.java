package elem.source;

import mesh.CEI;
import mesh.ElementNames;
import syms.Sym;

public class CSource extends Source{
	private Sym cValue;
	public CSource(String name){
		this.name = name;
		cValue = Sym.VAR_Name(name);
	}
	public CSource(int index){
		super(index);
		cValue = getDefaultSym();
	}
	public CSource(){
		cValue = getDefaultSym();
	}
	@Override
	public String model() {
		return ElementNames.CS;
	}
	@Override
	protected int count(int i) {
		return CEI.chooseI(i);
	}
	@Override
	protected int count() {
		return CEI.chooseI();
	}
	public Sym getCurrent(){
		return cValue;
	}
	
}
