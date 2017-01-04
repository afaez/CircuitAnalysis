package board.netana;

import elem.CElement;
import elem.Node;
import elem.resistor.Resistor;

class Twig{
	CElement[][] elements;
 	Node A;
 	Node B;
 	OPorteq eq;
	Twig(Node A, Node B, CElement[] elems){
		this.A = A;
		this.B = B;
		elements = new CElement[1][];
		elements[0] = elems;
	}
	Twig(Node A, Node B, CElement[][] elemsArray){
		this.A = A;
		this.B = B;
		elements = elemsArray;
		
	}
	public void switchNodes() {
		Node temp = B;
		B = A;
		A = temp;
	}
	public void setEQ(OPorteq eq){
		this.eq = eq;
	}
	public boolean has(String CE){
		for(CElement[] ces : elements){
			for(CElement ce: ces){
				if(ce.toString().equals(CE)){
					return true;
				}
			}
		}
		return false;
	}
	public boolean has(CElement CE){
		for(CElement[] ces : elements){
			for(CElement ce: ces){
				if(ce.equals(CE)){
					return true;
				}
			}
		}
		return false;
	}
	public String toString(){
		String s = "";
		s += A.toString() + " ";
		boolean first = true;
		for(CElement[] ces : elements ){
			if(!first)
				s+="|";
			first = false;
			s+="(";
			for(CElement ce : ces){
				/*if(ce instanceof Resistor){
					s += ce.toString() + ": " + ((Resistor)ce).getResistant() + " ";
				}
				else*/{
					s += ce.toString() + " ";
				}
			}
			s+=") ";
		}
		s += B.toString() + " ";
		return s;
	}
	public CElement[][] getElements(){
		return elements;
	}
}