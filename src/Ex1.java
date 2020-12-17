

import java.awt.Paint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


public class Ex1 {
	public static ArrayList<String> Out_list = new ArrayList<>();

	public static void main(String[] args) throws IOException {
		Decoder d = new Decoder();
		Variable_collection vc = d.var_List;
		Query[] qureis  =d.qur_list;

		for (Query q :qureis) {

			//Variable_collection	vc = new  Variable_collection(VC.getVariables());
			//System.out.println("q = "+ Arrays.toString( q.getQuery()));
//			if(vc.getVariables().size() == q.getQuery().length-1) {// if the answr in the cpt.
//				System.out.println("auto1");
//				AutomaticAsnwer.AutomaticAsnwer(q, vc);
//				continue;
//			}

			boolean need = AutomaticAsnwer.NeedAutomaticAsnwer(q,vc);
			if(need) {
				System.out.println("auto");
				AutomaticAsnwer.AutomaticAsnwer(q, vc);
				continue;
			}
			
			if ((q.getAlgo() == '1'))
			{          
				System.out.println("\n" +q.getAlgo()+"  algo1 run ");
			Algo algo = new Algo();
			algo.solve(q, vc);
			}

			if(q.getAlgo() == '2') {
				System.out.println("\n this is the second" +q.getAlgo()+"  algo");
				Algo2 algo2 = new Algo2();
				algo2.Run(vc, q,2);
			}
			if(q.getAlgo() == '3') {
				System.out.println("\n this is the 3 algo" +q.getAlgo()+"  algo");
				Algo2 algo2 = new Algo2();
				algo2.Run(vc, q,3);
			}

		}
		Decoder.toOutput(Out_list);
	}
}
