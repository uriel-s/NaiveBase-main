import java.awt.image.AreaAveragingScaleFilter;

import javax.sound.sampled.Line;



	public  class Query{
		private	String[] query ;
        private String var_one;
        private char algo;
		
        
        public String[] getQuery() {
			return query;
		}

		public String getVar_one() {
			return var_one;
		}

		public void setVar_one(String var_one) {
			this.var_one = var_one;
		}

		public char getAlgo() {
			return algo;
		} 
		

		public void setAlgo(char algo) {
			this.algo = algo;
		}

		public void setQuery(String[] query) {
			this.query = query;
		}

		public Query(String str) {
			
			this.algo =str.charAt(str.length()-1);
			str= str.substring(2);
			String[] line =str.split(",|\\|");
			query = new String[line.length];
			for (int i = 0; i < line.length; i++) {
				//String s = line[i].replace("P", "");
			String	s = line[i].replace("(", "");
				s = s.replace(")", "");
				query[i] =s;
			}
			var_one= query[0].substring(0,query[0].indexOf('='));
		}

	}


