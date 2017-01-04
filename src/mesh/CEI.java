package mesh;

public class CEI {
	private static boolean[] 
			resist, cSource, vSource, nod;
	private static boolean[] choose(int index, boolean[] arr){
		if(arr == null){
			arr = new boolean[(index+1)*2];
			 arr[index] = true;
			 return arr;
					 
		}
		else if(arr.length-1<index){
			boolean r[] = new boolean[arr.length*2];
			for(int i = 0; i< arr.length;i++){
				r[i] = arr[i];
			}
			r[index] = true;
			return r;
		}
		else{
			if(arr[index]){
				throw new RuntimeException("Index Arr [" + index + "]" +" was already occupied.");
			}
			arr[index] = true;
			return arr;
		}
	}
	private static Object[] choose(boolean[] arr){
		if(arr == null){
			Object[] o= {choose(0, null), 1};
			return o;
		}
		for(int i = 0;i < arr.length; i++){
			if(!arr[i]){
				arr[i] = true;

				Object[] o= {arr, i+1};
				return o;
			}
		}
		Object[] o= {choose(arr.length, arr), arr.length+1};
		return o;
	}
	public static int chooseR(int i){
		i--;
		resist = choose(i, resist);
		return i+1;
	}

	public static int chooseR(){
		Object[] o = choose(resist);
		resist = (boolean[]) o[0];
		return (int) o[1];
	}
	public static int chooseU(int i){
		i--;
		vSource = choose(i, vSource);
		return i+1;
	}

	public static int chooseU(){
		Object[] o = choose(vSource);
		vSource = (boolean[]) o[0];
		return (int) o[1];
	}
	public static int chooseI(int i){
		i--;
		cSource = choose(i, cSource);
		return i+1;
	}

	public static int chooseI(){
		Object[] o = choose(cSource);
		cSource = (boolean[]) o[0];
		return (int) o[1];
	}

	public static int chooseN(int i){
		i--;
		nod = choose(i, nod);
		return i+1;
	}

	public static int chooseN(){
		Object[] o = choose(nod);
		nod = (boolean[]) o[0];
		return (int) o[1];
	}
	
}
