

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Collection;

import javax.print.attribute.standard.OutputDeviceAssigned;
import javax.swing.text.html.HTMLDocument.HTMLReader.HiddenAction;
//import defaultP
//import Factor_collection.Factor;
//import Variable_collection.Variable;

public class Algo2 {
	public static  int AddCounter;
	public  static int MulCounter; 

	public Algo2() {}

	/**
	 * give for each Variable the value he have from the query 
	 * @param query
	 * @param vc  Variable_collection
	 * @return the value of the query variable
	 */
	public static String Paint(Query query,Variable_collection vc) {
		ArrayList<Variable_collection.Variable> variables =vc.getVariables();
		for (int i = 1; i < query.getQuery().length-1; i++) {
			String[] line = query.getQuery()[i].split("="); //split the variable from the value for exapmle :C=run -> [c,run]
			Variable_collection.Variable v = Algo.Find_variable_from_char(variables , line[0]);
			v.setColor(line[1]);
		}
		String[] line = query.getQuery()[0].split("="); //split the variable from the value for exapmle :C=run -> [c,run]
		Variable_collection.Variable v = Algo.Find_variable_from_char(variables , line[0]);
		v.setColor("query");
		return line[1];
	}
/**
 * looking for the hidden vars  
 * @param vc 
 * @param query
 * @param algo which algorithm to use 
 * @throws IOException
 */
	public  void Run(Variable_collection vc,Query query, int algo) throws IOException{

		ArrayList<Variable_collection.Variable> back_up = new ArrayList<>();
		ArrayList<Variable_collection.Variable>	hidden =new  ArrayList<Variable_collection.Variable>(); 
		for (Variable_collection.Variable variable : vc.getVariables()) {
			back_up.add(variable);
		}
		remove_variable(vc,query);
		String color = 	 Paint(query, vc);	
		for (Variable_collection.Variable variable : vc.getVariables()) {
			if(variable.getColor().equals("parent")) hidden.add(variable);
		}
		solve (hidden,vc,color,algo);

		//for back up
		for (Variable_collection.Variable variable : vc.getVariables()) {
			variable.setColor(null);
		}

		vc.setVariables(back_up);

		System.out.println();

	}
/**
 * use join and elimination and normalzation in the right order 
 * @param hidden vars
 * @param vc
 * @param color
 * @param algo
 * @throws IOException
 */
	private  void solve(ArrayList<Variable_collection.Variable> hidden, Variable_collection vc, String color, int algo) throws IOException {
		DecimalFormat df1 = new DecimalFormat("#.#####");
		AddCounter =0;
		MulCounter =0;
		if(algo == 2) Collections.sort(hidden);
		if (algo == 3 )  Collections.sort(hidden,new sortByLength() );


		Factor_collection fc = new Factor_collection(vc);

		for (Variable_collection.Variable variable : hidden) {
			//System.out.println("join and elimenetion for  "+ variable.getId());
			ArrayList<Factor_collection.Factor>  dependents = new ArrayList<>();			
			for (Factor_collection.Factor factor : fc.getFactors() ) {//add all the dependents factors to a list and marge them to one factor
				if (factor.getVariables().contains(variable) ) {
					//	System.out.println("factor "+ factor.getId()+" join in to the dependents");
					dependents.add(factor);

				}
			}

			Factor_collection.Factor marg_factor = marge_factors( fc,dependents);
			if(marg_factor.getVariables().size() >1 ) {
				Factor_collection.Factor elimented = marg_factor.elimination(variable);
				fc.getFactors().add(elimented);
			}
			fc.getFactors().removeAll(dependents); 
		}   

		while(fc.getFactors().size()>1) {
			Factor_collection.Factor f1 =fc.getFactors().get(0);
			Factor_collection.Factor f2 =fc.getFactors().get(1);

			//		System.out.println("this is "+f1.getId()+"+ prob "+Arrays.toString(f1.getProbability()));
			Factor_collection.Factor f3 = Factor_collection.join(f1,f2);
			fc.getFactors().add(f3);
			fc.getFactors().remove(f1);
			fc.getFactors().remove(f2);
		}

		double  ans =  normalize(fc.getFactors().get(0),color );
		ans = Double.valueOf(df1.format(ans));
		String output ="";
		output += String.format("%.5f", ans);;
		output += ",";
		output +=  AddCounter;
		output += ",";
		output +=  MulCounter;

		Ex1.Out_list.add(output);

		System.out.println("Add = "+AddCounter);
		System.out.println("mul = "+MulCounter);
		System.out.println("ans = "+ans);


	}


/**
 * Coucolate the right answer from the current factor
 * @param factor to make  coagulate
 * @param color the value of the query variable 
 * @return the answer of the query
 */
	private double normalize(Factor_collection.Factor factor, String color) {
		System.out.println("nirmul");
		factor.print();
		Factor_collection.Factor ans = new Factor_collection.Factor();
		ans.setVariables(factor.getVariables());
		double[] prob = new double[factor.getProbability().length];
		double denominator =0;
		for (int i =0; i<factor.getProbability().length;i++) {
			AddCounter++;
			denominator += factor.getProbability()[i];
		}
		AddCounter--;
		//	System.out.println("deno = "+ denominator);
		for (int i =0; i<factor.getProbability().length;i++) {
			prob[i] = factor.getProbability()[i]/denominator;
		}
		ans.setProbability(prob);
		//	System.out.println("norm prob = ");
		for (int i = 0; i < prob.length; i++) {
			//	System.out.print(prob[i ]+", ");
		}
		int i =0;
		while(!factor.getMatrix()[i][0].equals(color) )
			i++;

		return prob[i];
	}


/**
 * Organize the right order to do the join elimination
 * @param fc
 * @param dependents : factor dependent on a variable  
 * @return the factor after join of all factors that dependent on a var 
 */
	private static Factor_collection.Factor marge_factors(Factor_collection fc, ArrayList<Factor_collection.Factor> dependents) {
		while(dependents.size()>2)
		{
			Factor_collection.Factor min1 = dependents.get(0);
			Factor_collection.Factor min2 = dependents.get(1);
			double minValue  = check_num_of_rows(min1,min2);
			for (int i = 0; i < dependents.size(); i++) {
				for (int j = i+1; j < dependents.size(); j++) {
					Factor_collection.	Factor f1 = dependents.get(i);
					Factor_collection.Factor f2 = dependents.get(j);
					double num_of_rows=check_num_of_rows(f1,f2);
					if(num_of_rows  < minValue  ) {
						minValue =  num_of_rows;
						min1 = f1;
						min2 = f2;
					}		 
					if( num_of_rows == minValue &&  sum_of_ascii(f1,f2)<sum_of_ascii(min1,min2) ) {
						minValue =  num_of_rows;
						min1 = f1;
						min2 = f2;
					}
				}
			}

			//	System.out.println("factors "+ min1.getId()+" and "+ min2.getId()+" the smallest");
			Factor_collection.Factor j = Factor_collection.join(min1,min2);
			dependents.remove(min1);
			dependents.remove(min2);
			fc.getFactors().remove(min1);
			fc.getFactors().remove(min2);

			dependents.add(j);
		}
		Factor_collection.Factor ans = Factor_collection.join(dependents.get(0), dependents.get(1));
		return ans;
	}

	/**
	 * 
	 * @param f1
	 * @param f2
	 * @return the sum of the first char of the variables in two factors
	 */
	private static int sum_of_ascii(Factor_collection.Factor f1, Factor_collection.Factor f2) {
		Set<Variable_collection.Variable> new_set = new HashSet<Variable_collection.Variable>();
		new_set.addAll(f1.getVariables());
		new_set.addAll(f2.getVariables());
		ArrayList<Variable_collection.Variable> variables = new ArrayList<Variable_collection.Variable>();
		int sum =0;
		for (Variable_collection.Variable variable : variables) {
			sum =+ variable.getId().charAt(0);
		}

		return sum;	
	}
/**
 * 
 * @param f1
 * @param f2
 * @return the sum of lines that will be if you make join to this factors factors
 */
	private static double check_num_of_rows(Factor_collection.Factor f1,Factor_collection. Factor f2) {
		Set<Variable_collection.Variable> new_set = new HashSet<Variable_collection.Variable>();
		new_set.addAll(f1.getVariables());
		new_set.addAll(f2.getVariables());
		ArrayList<Variable_collection.Variable> variables = new ArrayList<Variable_collection.Variable>();		
		variables.addAll(new_set);
		int sum =1;
		for (Variable_collection.Variable variable : variables) {
			sum = sum*variable.getValues().length;
		}
		return sum;
	}



/**
 * remove variables that not in the query or not parent ancestor 
 * @param vc
 * @param query
 */
	public static void  remove_variable(Variable_collection vc,Query query){
		contact(query, vc);
		ArrayList<Variable_collection.Variable> new_variables_list = new ArrayList<>();
		for (Variable_collection.Variable variable : vc.getVariables()) {
			if (variable.getColor()  != null) {
				new_variables_list.add(variable);
			}
			else {
			}
		}

		vc.setVariables(new_variables_list);


	}


/**
 * contact and contact_parents are two function that give a value for each variable in the query , or give a value "parent if needed
 * @param query
 * @param vc
 */
	public static  void contact(Query query,Variable_collection vc) {
		ArrayList<Variable_collection.Variable> variables =vc.getVariables();
		for (int i = 0; i < query.getQuery().length-1; i++) {
			String[] line = query.getQuery()[i].split("="); //split the variable from the value for exapmle :C=run -> [c,run]
			Variable_collection.Variable v = Algo.Find_variable_from_char(variables , line[0]);
			v.setColor(line[1]);
			if( v.getParents()!= null) {
				for (Variable_collection.Variable parent : v.getParents()) {
					contact_parents(parent);
				} 
			}
		}
	}

	/**
 * contact and contact_parents are two function that give a value for each variable in the query , or give a value "parent if needed
	 * @param query
	 * @param vc
	 */
	public static   void contact_parents(Variable_collection.Variable v) {
		v.setColor("parent");
		if( v.getParents()!= null)
			for (Variable_collection.Variable parent : v.getParents()) {
				contact_parents(parent);
			}
	}



}
