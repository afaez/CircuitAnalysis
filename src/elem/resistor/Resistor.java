package elem.resistor;

import elem.OnePort;
import mesh.CEI;
import mesh.ElementNames;
import syms.Sym;

public class Resistor extends OnePort {
	private Sym rValue;
	public Resistor(double r){
		this.rValue= Sym.NUMB(r);
		
	}

	public Resistor(String name){
		this();
		this.name = name;
		rValue = Sym.VAR_Name(name);
	}
	public Resistor(int index){
		super(index);
		rValue = getDefaultSym();
	}
	
	protected Resistor(Sym s){
		this.rValue= s;
	}
	public Resistor(){
		rValue = getDefaultSym();
	}
	public static Resistor UNITE_SERIES(Resistor[] rArr){
		if(rArr.length<1){
			return null;
		}
		Sym s = rArr[0].getResistant();
		for(int i = 1; i< rArr.length; i++){
			s = Sym.Add(s, rArr[i].getResistant());
		}
		Resistor r = new Resistor(s);
		return r;
	}
	public static Resistor UNITE_SHUNT(Resistor[] rArr){
		if(rArr.length<1){
			return null;
		}
		Sym s = Sym.KehrM(rArr[0].getResistant());
		for(int i = 1; i< rArr.length; i++){
			s = Sym.Add(s, Sym.KehrM(rArr[i].getResistant()));
		}
		Resistor r = new Resistor(Sym.KehrM(s));
		return r;
	}
	@Override
	public String model() {
		return ElementNames.Resistor;
	}

	public Sym getR() {
		return rValue;
	}
	@Override
	protected int count() {
		return CEI.chooseR();
	}

	@Override
	protected int count(int i) {
		return CEI.chooseR(i);
	}
	public Sym getResistant(){
		return rValue;
	}
	public String toString(){
		return super.toString()+": "+getResistant();
	}
}
