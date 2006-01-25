package net.jsunit.model;

public enum ResultType {
	ERROR {
		public String getDisplayString() {
			return "error";
		}
	}, 
	FAILURE {
		public String getDisplayString() {
			return "failure";
		}		
	},
	SUCCESS {
		public String getDisplayString() {
			return "success";
		}
	};

	public abstract String getDisplayString();
	
}
