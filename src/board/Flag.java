package board;

import java.util.ArrayList;
import java.util.function.Function;

import elem.CElement;

public class Flag<S,T> implements Function<S, T>{
	public Flag(T T0){
		this.t0 = T0;
		
		ces = new ArrayList<BasicStruct>();
	}
	private T t0;
	private ArrayList<BasicStruct> ces;
	@Override
	public T apply(S ce) {
		for(BasicStruct listObj : ces){
			if(listObj.ce == ce){
				return listObj.t;
			}
		}
		ces.add(new BasicStruct(ce, t0));
		return t0;
	}
	public void set(S ce, T t){
		for(BasicStruct listObj : ces){
			if(listObj.ce == ce){
				listObj.t = t;
				return;
			}
		}
		ces.add(new BasicStruct(ce, t));
	}
	public int count(T t){
		int i = 0;
		for(BasicStruct bs : ces){
			if(bs.t==t){
				i++;
			}
		}
		return i;
	}
	class BasicStruct{
		S ce;
		T t;
		BasicStruct(S ce, T t ){
			this.ce= ce;
			this.t = t;
		}
	}
}
