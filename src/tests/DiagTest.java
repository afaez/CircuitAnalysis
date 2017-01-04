package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import board.Diag;
import board.WireMashine;
import board.netana.NetworkGraph;
import elem.Node;
import elem.resistor.Resistor;
import elem.source.VSource;

public class DiagTest {

	@Test
	public void test() {
		VSource s =  new VSource();
		Resistor r1 = new Resistor(1.);
		Resistor r2 = new Resistor(1.);
		Resistor r3 = new Resistor(1.);
		WireMashine wm = new WireMashine(s);
		wm.connectTo(new Resistor());
		WireMashine[] wms = wm.connectShunt(r1,r2,r3);
		WireMashine.unifyShunt(s, wms);

		System.out.println(Diag.DRAW(s));
		
		System.out.println(new NetworkGraph(s));
		
		Resistor r4 = new Resistor();
		Resistor r5 = new Resistor();
		Resistor r6 = new Resistor();
		Resistor r7 = new Resistor();
		Resistor r8 = new Resistor();
		
		Resistor[] rArr = {r1,r4,r5,r6,r7,r8};
		Resistor series = Resistor.UNITE_SERIES(rArr);
		Resistor  shunt = Resistor.UNITE_SHUNT(rArr);
		//System.out.println(series.getResistant());
		//System.out.println(shunt.getResistant());
	}

}
