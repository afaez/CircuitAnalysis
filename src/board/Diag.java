package board;

import java.util.ArrayList;

import elem.CElement;

public class Diag{

	public static String DRAW(CElement c){
		String s = "";
		for(CElement e : ARRAY(c)){
			s += e.toString() + " | ";
		}
		if(s.length()!=0){
			s = s.substring(0, s.length() - 4);
		}
		return s;
				
	}
	public static CElement[] ARRAY(CElement c){
		Flag<CElement,Color> f = new Flag<CElement,Color>(Color.white);
		ArrayList<CElement> elemsQueue = new ArrayList<CElement>();
		ArrayList<CElement> elemsArr = new ArrayList<CElement>();
		f.set(c, Color.black);
		elemsQueue.add(c);
		while(!elemsQueue.isEmpty()){
			c = elemsQueue.get(0);
			if(c==null)
				continue;
			elemsArr.add(c);
			for(int i = 0 ; i < c.IN; i++){
				CElement temp = c.getConnection(i);
				if(f.apply(temp)==Color.black) continue;
				f.set(temp, Color.black);
				elemsQueue.add(temp);
			}
			elemsQueue.remove(0);
		}
		return elemsArr.toArray(new CElement[elemsArr.size()]);
	}

}
