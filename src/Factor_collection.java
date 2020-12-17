

import java.awt.print.Printable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;


public class Factor_collection {
	private  ArrayList<Factor> factors;
/**
 * constructors
 */
	
	public ArrayList<Factor> getFactors() {
		return factors;
	}


	public void setFactors(ArrayList<Factor> factors) {
		this.factors = factors;
	}


	public Factor_collection(Variable_collection vc) {
		factors = new ArrayList<Factor>();
		for (Variable_collection.Variable v : vc.getVariables()) {
			Factor f = new Factor(v);
			if(f.getVariables().size()>0) {
				factors.add(f);
			   
			}

		}
	}



	public  static class Factor implements Comparable<Factor> {
		private 	String[][] Matrix;
		private  double[] probability;
		private String id;
		private  ArrayList<Variable_collection.Variable>variables;


		public String[][] getMatrix() {
			return Matrix;
		}


		public void setMatrix(String[][] matrix) {
			Matrix = matrix;
		}


		public double[] getProbability() {
			return probability;
		}


		public void setProbability(double[] probability) {
			this.probability = probability;
		}


		public String getId() {
			return id;
		}


		public void setId(String id) {
			this.id = id;
		}

		public int compareTo(Factor other) {
			if(id.charAt(0)<other.getId().charAt(0) )return -1;
			return 1;
			//return timeStarted<o.getTimeStarted()?-1:timeStarted>o.getTimeStarted()?1:doSecodaryOrderSort(o);
		}

		public Factor() {
		}
		
		public Factor(Variable_collection.Variable v) {
			
			this.variables =new ArrayList<Variable_collection.Variable>();
			CPT cpt=v.getCpt(); 
			this.id="";
			this.id +=cpt.getId();

			if (v.getColor() == null || v.getColor().equals("parent") ||v.getColor().equals("query")) { 
				//	this.variables.add(v);
				this.Matrix =  v.getCpt().getMatrix();// need to change to deepcopy
				this.probability = v.getCpt().getProbability();
			}
			else {
				init_matrix_from_cpt(v);
			}
			if(v.getParents() == null) {}
			else {
				for (Variable_collection.Variable parent : v.getParents()) {
					this.variables.add(parent);
				}
			}
			if (v.getColor() == null || v.getColor().equals("parent")||v.getColor().equals("query") ) {
				this.variables.add(v);
			}
			for (Variable_collection.Variable variable : this.variables) {
				if ( !(variable.getColor() == null) && !(variable.getColor().equals("parent")) && !(variable.getColor().equals("query"))  ){
					this.Matrix = reduseMatrix(this.Matrix,this.probability,variable);
				}
			}
		}

/**
 * print a factor (values, matrix,probabilities)
 */
		public void print()
		{

			System.out.println("id = "+id);
			for(int v =0;v<variables.size();v++)
			{ 
				System.out.print(" "+this.variables.get(v).getId()+"     ");
			}
			System.out.println();
			for (int i = 0; i < Matrix.length; i++) {
				for (int j = 0; j < Matrix[0].length; j++) {
					System.out.print(Matrix[i][j]+" , ");					
				}
				System.out.println("   =  "+probability[i]);
			}
			System.out.println("\n");


		}
/**
 *  remove the lines in the cpt of the factor the including this var
 * @param matrix
 * @param probability2
 * @param variable
 * @return the new matrix after rows removing
 */
		private String[][] reduseMatrix(String[][] matrix, double[] probability2, Variable_collection.Variable variable) {
			int m = matrix[0].length-1;
			int n = matrix.length/variable.getValues().length;
			ArrayList<Variable_collection.Variable> new_vars = new ArrayList<>();
			new_vars.addAll(this.variables);
			new_vars.remove(variable);
			String[][] matrix2 = new String[n][m];
			double[] prob= new double[n];
			int rc =0;
			int index = indexOf(variable);
			for (int i = 0; i < matrix.length; i++) {
				if(matrix[i][index].equals(variable.getColor())) {
					for(Variable_collection.Variable vv : new_vars) {
						matrix2[rc][new_vars.indexOf(vv)] =matrix[i][indexOf(vv)];
					}
					prob[rc] =probability2[i];
					rc++;
				}
			}
			this.variables=new_vars;
			this.probability=prob;
			return matrix2;
		}


		
/**
 * making from a variable  a matrix for a factor
 * @param variable  
 */

		public void init_matrix_from_cpt(Variable_collection.Variable v) {
			int n = getNumberOfRows(v);
			int m = getNumberOfColoms(v);
			int row =0;
			String value = v.getColor();
			String [][] cpt_matrix = v.getCpt().getMatrix();
			String [][] factor_matrix = new String[n][m];
			double[] prob = new double[n];

			for (int i = 0; i < cpt_matrix.length; i++) {
				if(cpt_matrix[i][m-1].equals(value) ) {
					for (int j = 0; j < m; j++) {
						factor_matrix[row][j]= cpt_matrix[i][j];
					}
					prob[row] = v.getCpt().getProbability()[i];
					row++;
				}
			}
			this.Matrix=factor_matrix;
			this.probability= prob;
		}


/**
 * return the number of coloms for a new factor made from a variable v
 * @param v
 * @return
 */
		private int getNumberOfColoms(Variable_collection.Variable v) {
			return v.getCpt().getMatrix()[0].length;
		}

		public ArrayList<Variable_collection.Variable> getVariables() {
			return variables;
		}


		public void setVariables(ArrayList<Variable_collection.Variable> variables) {
			this.variables = variables;
		}

		/**
		 * return the number of rows for a new factor made from a variable v
		 * @param v
		 * @return
		 */
		private int getNumberOfRows(Variable_collection.Variable v) {
			//if ( v.getColor() == null) return v.getCpt().getMatrix().length;
			int num_of_value = v.getValues().length;		
			return (v.getCpt().getMatrix().length/num_of_value) ;//+1;
		}

		//

/**
 * @param v
 * @return   the index of v from a vars list

 */
		public int indexOf(Variable_collection.Variable v) {

			for(int i=0;i<this.getVariables().size();i++) {
				if( this.getVariables().get(i).getId() == v.getId() ) {
					return i;
				}
			}
			return -1;
		}


	/**
	 * Connects lines  that have the  this variable  (but other values ) to one line
	 * @param variable
	 * @return the factor without the variable
	 */
		public Factor elimination(Variable_collection.Variable v) {

			Factor factor = new Factor();
			ArrayList<Variable_collection.Variable> variables =  new ArrayList<>();
			for (Variable_collection.Variable vr : this.getVariables()) {
				variables.add(vr);
			}
			int v_index = this.indexOf(v);
			System.out.println(this.getId() + " before elimenation");
			this.print();
			System.out.println("destroy  "+ v.getId());
			variables.remove(v_index);
			Collections.sort(variables);
			String[][] matrix = build_matrix(variables);
			factor.setVariables(variables);
			factor.setMatrix(matrix);
			factor.setId(this.getId());
			double [] prob =reduce_probability(factor,this,v); 
			factor.setProbability(prob);
			return factor;

		}

/**
 * Connects probabilities that have the  this variable (but other values ) to one line
* @param variable
 * @param dst
 * @param src
 * @return the probabilities without the variable
 */
		private double[] reduce_probability(Factor dst,Factor src, Variable_collection.Variable v) {
			String[][] matrix = dst.getMatrix();
			double[]ans = new double[matrix.length];

			for(int i=0; i<matrix.length;i++) {
				HashMap<String,String> help = new HashMap<String,String>();
				for(int j = 0;j< dst.getVariables().size();j++)	{

					String id = dst.getVariables().get(j).getId();
					String color = matrix[i][j];
					if (!id.equals(v.getId()))  help.put(id, color);	
				}
				
				double p =  sum_of_prob_by_var2(help,src);//get_probability_from_factor2(help,f1);
				ans[i] = p;
			}

			return ans;
		}

		
/**
 * adding all the lines probabilies with the right value to one 
 * @param help hash map : key = variable id , value = current value
 * @param factor
 * @return new probability of line in the cpt factor
 */
		private double sum_of_prob_by_var2(HashMap<String, String> help, Factor factor) {
			double sum =0;
			for (int i =0 ;i< factor.getMatrix().length;i++) {
				boolean goodLine =true;
				for (Variable_collection.Variable variable : factor.getVariables() ) {
					int colom = factor.indexOf(variable);
					if(colom != -1 && help.containsKey(variable.getId()) ) {
						String key = variable.getId();
						if(!help.get( key ).equals(factor.getMatrix()[i][colom]) ) goodLine =false;
					}
				}
				if(goodLine) {
					Algo2.AddCounter++;
					sum  = sum + factor.getProbability()[i];	
				}
			}
			Algo2.AddCounter--;
			return sum;
		}

	}
/**
 * join two factors to one factor
 * @param f1
 * @param f2
 * @return
 */
	public static Factor join(Factor f1, Factor f2) {

		Factor  factor = new Factor();
		String name = f1.getId()+f2.getId();
		factor.setId(name);
		Set<Variable_collection.Variable> new_set = new HashSet<Variable_collection.Variable>();
		new_set.addAll(f1.getVariables());
		new_set.addAll(f2.getVariables());
		ArrayList<Variable_collection.Variable> variables = new ArrayList<Variable_collection.Variable>();
		variables.addAll(new_set);
		Collections.sort(variables);
		factor.setVariables(variables);
		String[][] matrix = build_matrix(variables);        
		factor.setMatrix(matrix);
		double[] prob = build_probability(factor,f1,f2);
		factor.setProbability(prob);

		return factor;
	}

/*
 * 
 */
	private static double[] build_probability(Factor factor, Factor f1, Factor f2) {

		double[]ans = new double[factor.Matrix.length];

		for(int i=0; i<factor.getMatrix().length;i++) {
			HashMap<String,String> help = new HashMap<String,String>();
			for(int j = 0; j<factor.getVariables().size();j++)	{
				String id = factor.getVariables().get(j).getId();
				String color = factor.getMatrix()[i][j];
				help.put(id, color);	
			}
			double p1 =  get_probability_from_factor2(help,f1);
			double p2 =  get_probability_from_factor2(help,f2);
			Algo2.MulCounter++;
			ans[i] = p1*p2;
		}
		return ans;
	}


	/**
	 * return the probability of one line in the factor
	 * @param help
	 * @param f
	 * @return
	 */
	private static double get_probability_from_factor2(HashMap<String, String> help, Factor f) {

		for (int line = 0; line < f.getMatrix().length; line++) {
			boolean rightLine =true;
			for(Variable_collection.Variable variable :f.getVariables()   ) {
				int colom = f.indexOf(variable);
				if(colom != -1) {
					if( !(f.getMatrix()[line][colom].equals(help.get(variable.getId()))  )) 
						rightLine=false;
				}
			}
			if  (rightLine ==  true) {
				return f.getProbability()[line];
			}
		}
		return -1;
	}



/**
 * Bullied a matrix from variables and thier values
 * @param variables
 * @return matrix
 */
	private static String[][] build_matrix(ArrayList<Variable_collection.Variable> variables) {
		Variable_collection.Variable v = variables.get(0);
		String[][] matrix = new String[v.getValues().length][1];
		for (int i = 0; i < matrix.length; i++) {
			matrix[i][0] = v.getValues()[i];
		}
		if (variables.size() == 1) {
			return matrix;
		}

		for (int variable = 1; variable < variables.size(); variable++) {
			int RC=0;//row counter
			v = variables.get(variable);
			int n =matrix.length*v.getValues().length;
			int m = matrix[0].length+1;
			String[][] matrix2 = new String[n][m];		
			for(String value : v.getValues() ) {
				for (int i = 0; i < matrix.length; i++) {
					for (int j = 0; j < matrix[0].length; j++) {
						matrix2[RC+i][j]=matrix[i][j];
					}
					matrix2[RC+i][m-1] = value;
				}	
				RC+= matrix.length;
			}
			matrix= matrix2;
		}
		return matrix;
	}

}