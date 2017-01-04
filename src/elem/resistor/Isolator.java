package elem.resistor;

import mesh.ElementNames;

public class Isolator extends Resistor {
	public Isolator(){
		super(syms.Const.INF);
	}
	@Override
	public String model() {
		return ElementNames.Isolator;
	}
}
