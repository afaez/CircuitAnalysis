package board.netana;

import java.util.ArrayList;
import java.util.List;

import board.Color;
import board.Diag;
import board.Flag;
import elem.CElement;
import elem.Node;
import syms.Sym;

public class NetworkGraph {
	private final Twig[] twigs;
	private final Neq[] neqs;
	private final Meq[] meqs;
	private final Node[] nodes;
	private final Twig[] tree;
	private final Twig[][] maschen;
	private ArrayList<Node> nList;
	private ArrayList<Twig> tList;
	private ArrayList<CElement> currentTwig;
	public NetworkGraph(CElement ce) {
		this(findNode(ce));
	}
	public NetworkGraph(Node n){
		
		setupTwigsAndNodes(n);
		this.nodes = nList.toArray(new Node[nList.size()]);
		this.twigs = tList.toArray(new Twig[tList.size()]);
		
		setupOPEquations();
		
		tree = completeTree();
		
		neqs = new Neq[nodes.length-1];
		setupNodesEquations();
		
		maschen = new Twig[twigs.length-(neqs.length)][];
		meqs =  new Meq[maschen.length];
		setupMaschen();
		for(Neq neq : neqs){
			neq.substitue(meqs);
		}
		
	}
	private void setupTwigsAndNodes(Node n){
		currentTwig = new ArrayList<CElement>();
		nList = new ArrayList<Node>();
		tList = new ArrayList<Twig>();
		Flag<CElement,Color> f = new Flag<CElement,Color>(Color.white);
		visit(n,f);
		optimize();
		TwigAna.ANALYZENETWORK(tList);
	}
	private void setupOPEquations(){
		for(int i = 0; i <  twigs.length;i++){
			twigs[i].setEQ(new OPorteq(twigs[i],i));
		}
		
	}
	private void setupNodesEquations(){
		//boolean jumpFirst = false;
		for(int i = 0; i< neqs.length;i++){
			neqs[i] = new Neq(nodes[i], twigs); 
		}
	}
	private void setupMaschen(){

		int index = 0;
		for(Twig t : twigs){
			boolean contains = false;
			for(Twig treeT : tree){
				if(t == treeT){
					contains = true;
					break;
				}
			}
			if(contains){
				continue;
			}
			maschen[index++] = pathInTree(t);
		}
		for(int i = 0;i < maschen.length;i++){
			meqs[i] = new Meq(maschen[i],tree);
		}
	}
	//MASCHE
	private Twig[] pathInTree(Twig t){
		
		Flag<Twig,Integer> f = new Flag<Twig,Integer>(0);
		visit(t.B,t.A, f);
		Twig[] masche = new Twig[f.count(2)+1];
		masche[masche.length-1] = t;
		int index = 0;
		for(int i = 0 ;i < tree.length;i++){
			if(f.apply(tree[i]) == 2){
				masche[index] = tree[i];
				index++;
			}
		}
		return masche;
	}
	//MASCHE
	private boolean visit(Node searching, Node visiting, Flag<Twig,Integer>f){
		
		for(Twig treeT : tree){
			if(f.apply(treeT)!=0)
				continue;
			if(treeT.A == visiting||treeT.B == visiting){
				if(treeT.A == searching||treeT.B == searching){
					f.set(treeT, 2);
					return true;
				}
				else{
					f.set(treeT, 1);
					Node B = treeT.A == visiting?treeT.B:treeT.A;
					if(visit(searching, B,f)){
						f.set(treeT, 2);
						return true;
					}
				}
			}
		}
		return false;
	}
	//TREE
	private Twig[] completeTree() {
		Flag<Node, Boolean> f = new Flag<Node, Boolean>(false);
		ArrayList<Twig>tree =new ArrayList<Twig>();
		for(Twig t : twigs){
			if(!(f.apply(t.A)&&f.apply(t.B))){
				tree.add(t);
				f.set(t.A, true);
				f.set(t.B, true);
			}
		}
		return tree.toArray(new Twig[tree.size()]);
	}
	//Constructor
	private static Node findNode(CElement ce) {
		for(CElement e : Diag.ARRAY(ce)){
			if(e instanceof Node){
				return (Node) e;
			}
		
		}
		throw new RuntimeException("No Node in this Network");
	}
	//Combine twigs
	private void optimize() {
		for(Twig t0 : tList){
			ArrayList<Twig> uni = new ArrayList<Twig>();
			uni.add(t0);
			for(Twig t1 : tList){
				if(t0==t1)continue;
				if(sameSource(t0,t1)){
					uni.add(t1);
				}
			}
			if(uni.size()>1){
				//System.out.println("unifying: " + uni.toString());
				unify(uni);
				optimize();
				return;
			}
			
		}
		
	}
	//Combine twigs
	private boolean sameSource(Twig t0, Twig t1){
		if(t1.A!=t0.A){
			if(t0.A!=t1.B){
				return false;
			}
			else
				t1.switchNodes();
		}
		if(t1.B!=t0.B){
			return false;
		}
		return true;
	}
	//
	private void visit(CElement u, Flag<CElement,Color> f){
		f.set(u, Color.gray);
		currentTwig.add(u);
		for(int i = 0; i < u.IN; i++){
			CElement v = u.getConnection(i);
			if(v instanceof Node){
				if(currentTwig.get(0)!= v){
					Node first  =  (Node) currentTwig.get(0);
					if(f.apply(first) == Color.black){
						continue;
					}
					currentTwig.remove(0);
					Twig t = new Twig(first, (Node) v, currentTwig.toArray(new CElement[currentTwig.size()]));
					tList.add(t);
					currentTwig  = new ArrayList<CElement>();
					
					if(f.apply(v)!=Color.white){
						currentTwig .add(first);
					}
				}
			}
			if(f.apply(v)==Color.white){
				visit(v,f);
				if(u instanceof Node){
					currentTwig  = new ArrayList<CElement>();
					currentTwig.add(u);
				}
			}
		}
		f.set(u, Color.black);
		if(u instanceof Node)
			nList.add((Node) u);
	}
	private void unify( ArrayList<Twig> twigs){
		if(twigs.size()<=1)return;
		Twig t0 = twigs.get(0);
		int twigsCount = 0;
		for(Twig t : twigs){
			if(!sameSource(t0,t)){
				return;
			}
			twigsCount += t.elements.length;
		}
		CElement[][] ces = new CElement[twigsCount][];
		int j = 0;
		for(int i = 0; i<twigsCount; i++){
			for(int inner = 0 ; inner < twigs.get(j).elements.length; inner++){
				ces[i] = twigs.get(j).elements[inner];
			}
			j++;
		}
		Twig newTwig = new Twig(t0.A, t0.B, ces);
		this.tList.removeAll(twigs);
		tList.add(newTwig);
	}
	/*public NetworkGraph unifyTwigs(Twig...twigs){
		if(twigs.length<=1)return null;
		Twig t0 = twigs[0];
		int twigsCount = 0;
		ArrayList<Twig> returnTwigList = new ArrayList<Twig>();
		for(Twig t : this.twigs){
			returnTwigList.add(t);
		}
		for(Twig t : twigs){
			boolean found = false;
			for(Twig tLocal : this.twigs){
				if(tLocal==t){
					found = true;
					returnTwigList.remove(tLocal);
					break;
				}
			}
			
			if(!found){
				return null;
			}
			if(!sameSource(t,t0)){
				return null;
			}
			twigsCount += t.elements.length;
		}
		if(this.twigs.length-returnTwigList.size()!=twigs.length){
			return null;
		}
		CElement[][] ces = new CElement[twigsCount][];
		int j = 0;
		for(int i = 0; i<twigsCount; i++){
			for(int inner = 0 ; inner < twigs[j].elements.length; inner++){
				ces[i] = twigs[j].elements[inner];
			}
			j++;
		}
		Twig newTwig = new Twig(t0.A, t0.B, ces);
		returnTwigList.add(newTwig);
		Twig[] newTArray = returnTwigList.toArray(new Twig[returnTwigList.size()]);
		return null;//new NetworkGraph(newTArray, this.nodes);
	}*/
	public Twig[] getTwigs(){
		return twigs;
	}
	public Twig getContaining(String ce){
		for(Twig t : twigs){
			if(t.has(ce)) return t;
		}
		return null;
	}
	public Twig getContaining(CElement ce){
		for(Twig t : twigs){
			if(t.has(ce)) return t;
		}
		return null;
	}
	public String toString(){
		String s = nodes.length+ " nodes, " + twigs.length + " twigs:\n";
		for(int i = 0 ; i <  twigs.length; i++){
			s += "twig "+ i + ": \n" + twigs[i].toString() + "\n";
			s += twigs[i].eq.toString() + "\n";
		}
		s+= "Tree: \n";
		for(Twig t : tree){
			s +=  t.toString() + "\n";
		}
		s+= "Nodes :\n";
		for(int i=0;i< nodes.length;i++){
			s+= "node " + i + ": " + nodes[i].toString() + "\n";
			s+= neqs.length<=i?"null\n":neqs[i].toString() + "\n";
		}
		s+="Maschen :\n";
		for(int i=0;i< maschen.length;i++){
			s+= "masche " + i + ": ";
			for(Twig t:maschen[i]){
				s+= t.toString() + "| ";
			}
			s+="\n";
			s+=meqs[i].toString();
			s+="\n";
		}
		s +="\nMatrix Gleichung:\n\n";
		s+= Gleichung();
		return s;
	}
	public String Gleichung(){
		String s = "";
		for(int i =0;i< neqs.length;i++){
			s += neqs[i].eq0.getSoP()+ " = 0 \n";
		}
		return s;
	}
}
