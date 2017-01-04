package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import board.Diag;
import board.Wire;
import board.netana.NetworkGraph;
import elem.Node;
import elem.resistor.Resistor;
import elem.source.CSource;
import elem.source.VSource;

public class auf91 {

	@SuppressWarnings("unused")
	@Test
	public void test() {
		VSource u01 = new VSource(1);
		CSource i03 = new CSource(3);
		Resistor r1 = new Resistor(1);
		Resistor r2 = new Resistor(2);
		Resistor r3 = new Resistor(3);
		Wire.connect(r1, u01);
		Node n1 = new Node(4);
		Node n2 = new Node(4);
		Wire.connect(u01, n1);
		Wire.connect(r2, n1);
		Wire.connect(r3, n1);
		Wire.connect(i03, n1);

		Wire.connect(r1, n2);
		Wire.connect(r2, n2);
		Wire.connect(r3, n2);
		Wire.connect(i03, n2);

		System.out.println(Diag.DRAW(u01));
		NetworkGraph ng = new NetworkGraph(n1);

		System.out.println(ng.toString());
		
		
	}

}
