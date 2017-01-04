package elem;

import board.Wire;
import mesh.CEI;
import mesh.ElementNames;

public class Node extends CElement{
	public Node(int n) {
		super(n);
	}
	public Node(CElement... ces){
		super(ces.length);
		for(int i = 0; i < ces.length;i ++){
			boolean foundEmpty = false;
			for(int j = 0; j< ces[i].IN&&!foundEmpty; j++){
				if(ces[i].getConnection(j)==null){
					Wire.connect(ces[i], j, this, i);
					foundEmpty = true;
				}
			}
			if(!foundEmpty)
				throw new RuntimeException(ces[i].toString()+" has no empty Port.");
		}
	}
	@Override
	public String model() {
		return ElementNames.Node;
	}



	@Override
	public int count() {
		return CEI.chooseN();
	}
	@Override
	public int count(int i) {
		return CEI.chooseI(i);
	}

	

}
