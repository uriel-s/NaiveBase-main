

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;

import javax.sound.sampled.Line;

import org.omg.CORBA.portable.ValueInputStream;


public class CPT {
	private 	String[][] Matrix;
	private  double[] probability;
	private String id;



	public  double[] getProbability() {
		return probability;
	}

	public  String[][] getMatrix() {
		return Matrix;
	}


	public void setMatrix(String[][] matrix) {
		Matrix = matrix;
	}



	public void setProbability(double[] probability) {
		this.probability = probability;
	}



	public String getId() {
		return id;
	}



	public void setId(String id2) {
		this.id = id2;
	}
	
//consturctor
	public CPT(String id2, String[] values, Variable_collection.Variable[] parents, String[] cpt_string) {
		this.id=id2;
		int n = getNumberOfRows(values,parents);
		int m = getNumberOfColoms(parents);
		int RowCounter =0;
		this.Matrix = new String[n][m];
		probability =  new double[n];
		for (int i = 0; i < cpt_string.length; i++) {
			init_rows_to_matrix(cpt_string[i],values,parents,RowCounter);
			init_hidden_row(cpt_string[i],values,parents,RowCounter);
			RowCounter += values.length;
		}

	}


	public CPT() {
		// TODO Auto-generated constructor stub
	}


/**
 * for a row of the cpt in the input file the function add it in the cpt.
 * input the variable data (name , values....)
 */
	public void init_rows_to_matrix(String s,String[]values, Variable_collection.Variable[] parents,int RowCounter) {
      
        int v = 0;
		String[] line= s.split(",");	
		for (int j = 0; j < line.length; j++) {

			if (line[j].charAt(0) == '=') {   //init the values to the matrix
				for( v=0;v<values.length;v++) {
					if(	values[v].equals( line[j].substring(1) ) ) {
						Matrix[RowCounter+v][Matrix[0].length-1] = line[j].substring(1);
						probability[RowCounter+v] =Double.parseDouble( line[j+1]); 
					}
				}
			}
			else if(line[j].charAt(0) != '0' && line[j].charAt(0) != '1') { // init the parents values to the matrix
				for( v=0;v<values.length;v++) {	
                    Matrix[RowCounter+v][j] = line[j];
				}
			}
		}	
}

	/**
	 * for each row of the cpt in the input file the function coucolate the other rows that"hide" from the input and add then in the cpt
	 * input the variable data (name , values....)
	 */
	private void init_hidden_row(String cpt_string,   String[] values, Variable_collection.Variable[] parents, int rowCounter) {

		Matrix[rowCounter + values.length-1][Matrix[0].length-1] = values[values.length-1]; 
		probability[rowCounter+values.length-1] = add_last_var_prob(values,rowCounter);

	}
	
/**
 * intput : the values of variable and  weach row in cpt its found
 * the function look the vlaues and couclate what the last value should be and put it in the right place
 */
	private double add_last_var_prob(String[] values, int RowCounter) {
		double ans =0;
		for(int i =0; i<values.length-1;i++) 
			ans= ans + probability[RowCounter+i];
		return  (1-ans);
	}

	private int getNumberOfColoms(Variable_collection.Variable[] parents) {

		if( parents==null) {
			return 1;	
		}
		return parents.length +1 ;
	}

	private int getNumberOfRows(String[] values, Variable_collection.Variable[] parents) {
		int ans= values.length ;
		if( parents == null) 
			return ans;
		for (int i = 0; i < parents.length; i++) {
			ans *= parents[i].getValues().length;
		}
		//ans+=1;//adding one line for the name of the rows

		return ans ;
	}



	public void print() {
		System.out.println();
		System.out.println("cpt - " +id );
		for (int i = 0; i < Matrix.length; i++) {
			for (int j = 0; j < Matrix[0].length; j++) {

				System.out.print(Matrix[i][j]+" , ");
			}
			System.out.println();
		}
		System.out.println("prop= "+Arrays.toString(probability));


	}



}
