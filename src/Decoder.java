

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class Decoder {
	public   Variable_collection  var_List;
	public  Query[] qur_list;


	/**
	 * Gets data from the file  input.txt.
     And separates the variables from  queries and set them in lits
	 */

	public  Decoder() throws IOException {
		String[] input=	getInput();
		this.var_List= setVariablesList(input[0]);
		this.qur_list = setQueries(input[1]);
		
	}





/**
 * *   
 * @param String  the data from the input.txt
 * @return  an array of Querires
 */
	public Query[] setQueries(String str) {
		String[] lines = str.split("\n"); 
		Query[] queries = new Query[lines.length] ; 
		for (int i = 0; i < lines.length; i++) {
			Query q = new  Query(lines[i]);
			queries[i] =q;	
		}

		return queries;
	}

/**
 * @param out_list : the Strings that coming back from all the Queries 
 * output int file output.txt
 */
	public static void toOutput (ArrayList<String> out_list)throws IOException {
		File file = new File("output.txt");
		file.createNewFile();
		if (!file.canWrite()) throw new IOException("can not be write to");
		if (file.exists()) file.delete();
		FileWriter fileWriter = new FileWriter(file, true);
		for (int i=0;i<out_list.size();i++)
			fileWriter.write(out_list.get(i) + "\n");
		fileWriter.close();
	}


	/**
	 * input String the data of the Variables from the input.txt
	 *  make Variable_collection and return it 
	 */
	public Variable_collection setVariablesList(String str) {

		String [] lines = str.toString().split("Var");
		Variable_collection  vc = new Variable_collection();
		for(int i=0; i<lines.length-2; i++) {
			Variable_collection.Variable v = new Variable_collection.Variable(lines[i+2]); //send every block to Node constructor;
			vc.getVariables().add(v) ;
		}
		return vc;
	}
	/**
     return the input txt in array of two strings .first part the varbiles text, second the qureies.
	 */
	public static String[] getInput() throws IOException {
		//FileReader InputFile = new FileReader("C:\\Users\\USER\\eclipse-workspace1\\NaiveBase\\src\\NaiveBayes\\input.txt");
		FileReader InputFile = new FileReader("input.txt");
		BufferedReader br = new BufferedReader(InputFile);
		StringBuffer stringbuffer = new StringBuffer();
		String line;
		while(br.ready()) {
			line = br.readLine();
			if(!line.isEmpty())
				stringbuffer.append(line+"\n");
		}
		if (!isValiedText(stringbuffer)) throw new RuntimeException(" input doesnt representing a Network!");
		String [] lines = stringbuffer.toString().split("Queries\n");	
		return lines;
	}


	/**
	 *@param str = input.txt
	 *@return boolean true if the String(input txt) is As in the assignment instructions 
	 */
	private static boolean isValiedText(StringBuffer str) {
		//		if (str.indexOf("index") != -1      ||
		//				str.indexOf("Variables") != -1  ||
		//				str.indexOf("Queries") != -1
		//				)return false;
		return true;
	}

}
