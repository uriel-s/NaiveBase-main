

import java.io.IOException;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;


public class Algo {
	public  int AddCounter;
	public int MulCounter; 
	
	public Algo() {}

	/**
	 *  giving for each Variable from the Variable_collection the current  value from the query (null if not on the query)
	 * @param query
	 * @param vc Variable_collection
	 */
	public static void Paint(Query query,Variable_collection vc) {
		ArrayList<Variable_collection.Variable> variables =vc.getVariables();
		for (int i = 0; i < query.getQuery().length-1; i++) {
			//	System.out.println("line before split :"+query.getQuery()[i]);
			String[] line = query.getQuery()[i].split("="); //split the variable from the value for exapmle :C=run -> [c,run]
			Variable_collection.Variable v = Find_variable_from_char(variables , line[0]);
			v.setColor(line[1]);
		}
	}

	/**
	 * input a name of a variable and  return right the Variable from a list 
	 * @param variables list 
	 * @param s variable name
	 * @return variable 
	 */
	public static Variable_collection.Variable Find_variable_from_char(ArrayList<Variable_collection.Variable> variables, String s) {
		for (int i = 0; i < variables.size(); i++) {
			Variable_collection.Variable v = variables.get(i);
			if ( v.getId().equals(s) ) return v;	 		
		}
		return null;
	}



	/**
	 * 
	 * @param variables list
	 * @return a Variable from the list  that dosnt have any color
	 */
	public static Variable_collection.Variable Non_color_var(ArrayList<Variable_collection.Variable> variables) {
		for (int i = 0; i < variables.size(); i++) {
			if (variables.get(i).getColor() == null) return variables.get(i);
		}
		return null;
	}

	/**
	 * Recursive function  for everey variable that not in the mention in the query
	 * make a couclae for every values it have . and add it to the answer.  
	 * @param variables list
	 * @return double the answer of the query
	 */
	public double 	Find_Probability ( ArrayList<Variable_collection.Variable> variables ) {
		DecimalFormat df1 = new DecimalFormat("#0.#####");
		double Answer =0;
		Variable_collection.Variable  v = Non_color_var(variables);
		
		if (v == null) {// if all the vars are painted
			return find_Variable_in_matrix(variables);
		}
		
		for (String value : v.getValues()) {
			v.setColor(value);
			Answer += 	Find_Probability (variables);			
			AddCounter++;
			v.setColor(null);
		}

		AddCounter--;
		return  Answer;//Double.valueOf(df1.format(Answer));
	}





	/**
	 *  root function . call  paint, findProbapility function. and normaltion.
	 *  put the query asnwer in the list of all aswers
	 * @param query
	 * @param vc
	 */

	public void solve(Query query,Variable_collection vc) throws IOException {
		Paint(query, vc);

		AddCounter =0;
		MulCounter =0;
		ArrayList<Variable_collection.Variable> variables =   vc.getVariables();
		DecimalFormat df1 = new DecimalFormat("#.#####");
		String c = query.getVar_one();
		String color =Find_variable_from_char(variables, c).getColor();
		double numerator  =0;// מונה
		double denominator  = 0;//מכנה

		Variable_collection.Variable first_var = Find_variable_from_char(variables, query.getVar_one());
		numerator  = Find_Probability(variables);// 	find_Variable_in_matrix(v);
		for (String value : first_var.getValues()) {
			if(!color.equals(value)) {	
				System.out.println("value for nirmul = "+ value);
				AddCounter++;
				Paint(query, vc);
				first_var.setColor(value);
				denominator  +=    	Find_Probability(variables);
			}
		}
		
		double ans = Double.valueOf(df1.format(numerator/(denominator+numerator)));
		System.out.println(ans);
		String output ="";
		output += String.format("%.5f", ans);
		output += ",";
		output +=  AddCounter;
		output += ",";
		output +=  MulCounter;
		Ex1.Out_list.add(output);
	}



	/**
	 *  locate in the cpt the right lines of that fit the values of the vars a
	 * @param variables
	 * @return double sum of the probabilities that fit the variables values
	 */
	private double find_Variable_in_matrix(ArrayList<Variable_collection.Variable> variables) {
		DecimalFormat df1 = new DecimalFormat("#0.#####");
		double ans= 1;
		System.out.println();
		for (Variable_collection.Variable v : variables) {
			MulCounter++;
			CPT cpt = v.getCpt();
			int k = 0;
			int i =0;
			String color = v.getColor();
			if(v.getParents() == null) {
				while(i < v.getValues().length  && !color.equals( v.getValues()[i])   )  
					i++;
				ans*= cpt.getProbability()[i];
			}
			else {
				ans*= locate_in_cpt(v);}
		}
		MulCounter--;
		return ans;     //Double.valueOf(df1.format( ans));
	}




	public static String[] list_of_values(String[] line) {
		String[] ans = new String[line.length-1];
		for (int i = 0; i < ans.length; i++) {
			ans[i] = line[i];
		}
		return ans;
	}

	/**
	 * locate the probability of given Variable in the cpt 
	 * @param v
	 * @return double probability
	 */
	public  static double locate_in_cpt(Variable_collection.Variable v){ 

		int i =0;
		int k =0;

		String[] color_of_parents = new String[v.getParents().length];
		for (int j = 0; j < color_of_parents.length; j++) {
			color_of_parents[j] = v.getParents()[j].getColor();
		}

		while(!Arrays.deepEquals(color_of_parents, list_of_values( v.getCpt().getMatrix()[i])  )  )//&&  i < v.getValues().length ) 
		{
			i++ ;
		}
		while(k < v.getValues().length  && !v.getColor().equals( v.getValues()[k])       )  
			k++;

		i+=k;
		return  	v.getCpt().getProbability()[i];
	} 


}
