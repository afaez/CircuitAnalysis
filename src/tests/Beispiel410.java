package tests;


import org.junit.Test;

import board.Diag;
import board.WireMashine;
import board.netana.NetworkGraph;
import elem.resistor.Resistor;
import elem.source.CSource;
import elem.source.VSource;
public class Beispiel410 {
	@Test
	public void test(){
		VSource u = new VSource();
		WireMashine wm = new WireMashine(u);
		wm.connectTo(new Resistor());
		WireMashine[] wms1 = wm.connectShunt(new Resistor(), new Resistor());
		WireMashine[] wms2 = wms1[0].connectShunt(new Resistor(), new Resistor());
		WireMashine[] wms3 = wms2[0].connectShunt(new CSource(), new Resistor(), new Resistor());
		WireMashine wm2 = WireMashine.unifyShunt(new Resistor(), wms1[1], wms2[1],wms3[2]);
		WireMashine.unifyShunt(u,wm2,wms3[0], wms3[1]);
		
		System.out.println(Diag.DRAW(u));
		NetworkGraph ng = new NetworkGraph(u);
		System.out.println((ng).toString());
		
	}
}
