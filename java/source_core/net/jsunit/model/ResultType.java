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
	},
	FAILED_TO_LAUNCH {
		public String getDisplayString() {
			return "failed to launch";
		}
	},
	TIMED_OUT {
		public String getDisplayString() {
			return "timed out";
		}
	};

	public abstract String getDisplayString();
	
}
