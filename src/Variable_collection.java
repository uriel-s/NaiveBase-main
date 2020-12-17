

import java.util.ArrayList;
import java.util.Arrays;

//import Factor_collection.Factor;



public class Variable_collection{
	private static ArrayList<Variable> variables;

	//consturctors
	
	public Variable_collection() {
		variables= new ArrayList< Variable>();
	}


	public Variable_collection ( ArrayList<Variable> other) {
		this.variables = new ArrayList<Variable>();
		for (Variable variable : other) {
			Variable nv = new Variable(variable );
			this.variables.add(nv);
		}
	}
	
	
	
	
	
	public static void setVariables(ArrayList<Variable_collection.Variable> back_up) {
		Variable_collection.variables = back_up;
	}

	public ArrayList<Variable_collection.Variable> getVariables() {
		return variables;
	}

	/**
	 * @paramString name of Variable
	 * @return  Variable (null if it not exist)	 
	 * */
	private static Variable strToVar(String s) {
		for (int i = 0; i < variables.size(); i++) {

			if(  s.equals(  variables.get(i).getId()) ){
				return variables.get(i); 
			}
		}
		return null;
	}

	

	public static class Variable implements   Comparable<Variable> {
		private String color; 
		private   String[] values ;
		private  Variable[] parents;
		private	String id;
		private CPT cpt;

//constructors
		
		public Variable(Variable other) {
			this.color = other.getColor();
			this.values =other.values;

			if(other.parents!= null) {
				Variable[] newParents = new Variable[other.parents.length];
				for (int i =0;i< other.getParents().length;i++) {
					newParents[i] = new Variable(	other.getParents()[i]);
				}
				this.parents = newParents;
			}
			
			this.id = other.id;
			int n = other.getCpt().getMatrix().length;
			int m = other.getCpt().getMatrix()[0].length;
			String [][] matrix = new String[n][m];
			double [] prob = new double[n];
			for (int i = 0; i < matrix.length; i++) {
				for (int j = 0; j < matrix[0].length; j++) {
					matrix[i][j] = other.getCpt().getMatrix()[i][j];
				}
				prob[i]=other.getCpt().getProbability()[i];
			}
			CPT cpt = new CPT();
			cpt.setId(other.id);
			cpt.setMatrix(matrix);
			cpt.setProbability(prob);
			this.cpt =cpt;
		}
		
		public Variable(String str) {
			String[] lines = str.split("\n"); //split each row of string
			this.color = null;
			this.id = lines[0].trim();
			this.values = setValues(lines[1]);
			this.parents =setParents(lines[2]);
			this.cpt = setCpt(id, parents,values,lines); 
		}
		
		public String[] setValues( String str) {
			str =str.substring(8);
			String [] values = str.split(",");
			return values;
		}

		private Variable[] setParents(String str) {
			str =str.substring(9);
			String [] str_parents = str.split(",");
			Variable[] p = new Variable[str_parents.length];
			if (str.equals("none") )  return null;
			else{

				for(int i =0;i<p.length;i++)
				{
					Variable v= strToVar(str_parents[i]);
					p[i] = v;
				}
			}			
			return p;
		}   
		//          /\
		//		   /  \
		//	   	  /____\
		//        |  _ |
		//        | | ||
		//        |_|_||
		//////////////////////////


/**
 * @param Variable other
 * Compare Variables by the assci of their names
 */
		public int compareTo(Variable other) {
			if(    id.charAt(0)<other.getId().charAt(0) )return -1;
			return 1;
			//return timeStarted<o.getTimeStarted()?-1:timeStarted>o.getTimeStarted()?1:doSecodaryOrderSort(o);
		}

		public Variable[] getParents() {
			return parents;
		}

		

		public String getColor() {
			return color;
		}

		public void setColor(String color) {
			this.color = color;
		}

		public Variable() {
			// TODO Auto-generated constructor stub
		}

		public CPT setCpt(String id2 ,Variable[]parents , String[] values,String[] lines ) {

			String[] block= copyRangeArray(lines,4);
			CPT cpt = new CPT(id2, values,  parents,block);
			return cpt;

		}


		private String[] copyRangeArray(String[] old_array, int index) {
			String[] new_array= new String[old_array.length-index];
			for (int j = 0; j < new_array.length; j++) {
				new_array[j] =old_array[j+index];
			}
			return new_array;
		}

		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String[] getValues() {
			return values;
		}


		public CPT getCpt() {
			return cpt;
		}


	}

}
