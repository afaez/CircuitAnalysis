package elem.source;

import mesh.CEI;
import mesh.ElementNames;
import syms.Sym;

public class VSource extends Source {
	private Sym uValue;
	public VSource(){
		uValue = getDefaultSym();
	}
	public VSource(String name){
		this.name = name;
		uValue = Sym.VAR_Name(name);
	}
	public VSource(int index){
		super(index);
		uValue = getDefaultSym();
	}
	@Override
	public String model() {
		return ElementNames.VS;
	}
	@Override
	public int count(int i) {
		return CEI.chooseU(i);
	}
	@Override
	protected int count() {
		return CEI.chooseU();
	}
	public Sym getVoltag(){
		return uValue;
	}
}
