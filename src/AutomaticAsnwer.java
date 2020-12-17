

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;


public class AutomaticAsnwer {


	public AutomaticAsnwer() {

	}

/**
 * @param query
 * @param vc a Variable_collection
 * @return boolean  true in a case that all the information need to solve the query is all ready in the cpt
 *  and there no need to go to the algorthm 
 */
	public static boolean NeedAutomaticAsnwer(Query query, Variable_collection vc) {
		
		boolean need =true;
		Algo.Paint(query, vc);
		Variable_collection.Variable v = Algo.Find_variable_from_char(vc.getVariables(), query.getVar_one());
		if(v.getParents() == null) return false;

		for (Variable_collection.Variable parent : v.getParents()) { 
			if(parent.getColor() == null ) need =  false;
		}
		
		
		for (Variable_collection.Variable child : vc.getVariables()) {
			if (child.getParents() != null  && child.getColor() != null) {
				for (Variable_collection.Variable parent : child.getParents()) {
					if(parent.getId().equals(v.getId() )) need = false;
				}
			}
		}
		
		
		
		
		
		for (Variable_collection.Variable variable : vc.getVariables()) {
			variable.setColor(null);
		}
		
		return need;
	}

	/**
	 * parser the query and return the answer from the cpt
	 * @param input a query and the Variable_collection 
	 *@return double  the probobilty of the query
	 */
	
	public static double AutomaticAsnwer(Query q, Variable_collection vc) throws IOException {
		DecimalFormat df1 = new DecimalFormat("#.#####");

		ArrayList<Variable_collection.Variable> back_up = new ArrayList<>();
		for (Variable_collection.Variable variable : vc.getVariables()) {
			back_up.add(variable);
		}
		vc.setVariables(back_up);


		Algo.Paint(q, vc);

		Variable_collection.Variable variable  = Algo.Find_variable_from_char(vc.getVariables(), q.getVar_one());
		double ans = Algo.locate_in_cpt(variable);
		ans =	Double.valueOf(df1.format(ans));

		String output ="";
		output += String.format("%.5f", ans);;
		output += ",";
		output +=  "0";
		output += ",";
		output +=  "0";
		Ex1.Out_list.add(output);

		System.out.println(ans);

		for (Variable_collection.Variable v : vc.getVariables()) {
			v.setColor(null);
		}

		return 0;


	}






}
