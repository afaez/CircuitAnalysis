package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import elem.*;
import elem.source.*;
import elem.resistor.*;
import board.*;
import board.netana.NetworkGraph;
public class Beispiel415 {

	@Test
	public void test() {
		Node n = setup();
		System.out.println(Diag.DRAW(n));

		NetworkGraph ng = new NetworkGraph(n);
		System.out.println((ng).toString());
		
	}
	public Node setup(){
		Resistor r1 = new Resistor(1);
		WireMashine wm1 = new WireMashine(r1);
		WireMashine[] wm23 = wm1.connectShunt(new Resistor(2), new Resistor(3));
		WireMashine[] wm4667 = wm23[0].connectShunt(new VSource(4), new CSource(6), new Resistor(6), new Resistor(7));
		wm4667[2].connectTo(new VSource(6));
		wm4667[0].connectTo(new Resistor(4));
		WireMashine wm8  = WireMashine.unifyShunt(new Resistor(8), wm4667[0], wm4667[3]);
		//wm8.connectTo(new Resistor(9));
		Resistor r5 = new Resistor(5);
		CSource i5 = new CSource(5);
		WireMashine wm5 = new WireMashine(r5);
		wm5  = WireMashine.unifyShunt(i5, wm5, wm23[1], wm8);
		Node n1 = new Node(i5, r5, r1, wm4667[1].last(), wm4667[2].last());
		return n1;
		
	}

}
