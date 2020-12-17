

import java.util.Comparator;


public class sortByLength implements Comparator<Variable_collection.Variable> {

	@Override
	public int compare(Variable_collection.Variable v1, Variable_collection.Variable v2) {
		
	CPT	cpt1= v1.getCpt();
	CPT	cpt2= v2.getCpt();
		
	return (cpt1.getMatrix().length +cpt1.getMatrix()[0].length) -
			(cpt2.getMatrix().length +cpt2.getMatrix()[0].length);
			
	
	}

}
