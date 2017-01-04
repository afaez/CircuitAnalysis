package elem.source;

import elem.OnePort;
import syms.Sym;
public abstract class Source extends OnePort{
	protected Source() {
		super();
	}
	protected Source(int index) {
		super(index);
	}
	@Override
	public String model() {
		return "Source";
	}
}
