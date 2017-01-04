package board;

import elem.CElement;
import elem.Node;
import elem.OnePort;

public class WireMashine {
	private OnePort last;
	public WireMashine(OnePort start){
		last = start;
	}
	public void connectTo(OnePort next){
		if(last == null){
			System.out.println("WireMashien seems done. But has been called to connect to " + next.toString());
			return;
		}
		Wire.connect(last, next);
		last = next;
	}
	public WireMashine[] connectShunt(OnePort...ops){
		if(last == null){
			System.out.println("WireMashien seems done. But has been called to connect to Something.");
			return null;
		}
		WireMashine[] wms = new WireMashine[ops.length];
		for(int i = 0 ; i< wms.length; i++){
			wms[i] = new WireMashine(ops[i]);
		}
		Wire.connectShunt(last, ops);
		last=null;
		return wms;
	}
	public static WireMashine unifyShunt(OnePort to, WireMashine... wms){
		WireMashine wm = new WireMashine(to);
		OnePort[] ops= new OnePort[wms.length];
		for(int i = 0; i < wms.length; i++){
			ops[i] = wms[i].last;
		}
		Wire.UnifyShunt(to, ops);
		return wm;
	}
	public CElement last() {
		return last;
	}
}
