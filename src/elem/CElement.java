package elem;

import board.Wire;

/**
 * Circuit element
 * @author aminf
 *
 */

public abstract class CElement{
	public final int IN;
	private Wire[] ports;
	protected String name;
	private int index;
	protected CElement(int count){
		IN = count;
		ports = new Wire[IN];
		name = model();
		setIndex();
	}
	protected CElement(int count, int index){
		IN = count;
		ports = new Wire[IN];
		name = model();
		setIndex(index);
	}
	
	public final CElement getConnection(int i){
		if(i<0||i>IN) 
			throw new ArrayIndexOutOfBoundsException("IN" + IN + "\ni" + i);
		if(ports[i]==null){
			//System.out.println("The port" + i + " is empty");
			return null;
		}
		return ports[i].get(this);
	}
	public final void port(Wire w){
		for(int i=0;i<ports.length;i++){
			if(ports[i]==null){
				ports[i] = w;
				return;
			}
		}
		throw new RuntimeException(toString() + " doesnt have any empty ports.");
	}
	public final void port(int i, Wire w) {
		if(i<0||i>IN) 
			throw new ArrayIndexOutOfBoundsException("IN" + IN + "\ni" + i);
		if(ports[i]!=null){
			throw new RuntimeException("Overriding " + this.toString() + "'s port number: " + i);
			//System.out.println("Overriding " + this.toString() + "'s port number: " + i);
		}
		ports[i] = w;
	}
	protected abstract int count();
	protected abstract int count(int i);
	private final void setIndex(){
		index = count();
		name = model() + index();
	}
	private final void setIndex(int index){
		this.index = count (index);
		name = model() + index();
	}
	public abstract String model();
	public String toString(){
		return name;
	}
	public final int index(){
		return index;
	}
}
