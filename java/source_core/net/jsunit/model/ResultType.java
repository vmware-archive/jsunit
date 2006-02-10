package net.jsunit.model;

public enum ResultType {
	FAILED_TO_LAUNCH {
		public String getDisplayString() {
			return "failed to launch";
		}
		
		public boolean failedToLaunch() {
			return true;
		}
		protected int getLevel() {
			return 0;
		}
	},
	TIMED_OUT {
		public String getDisplayString() {
			return "timed out";
		}
		
		public boolean timedOut() {
			return true;
		}
		protected int getLevel() {
			return 1;
		}
	},
	EXTERNALLY_SHUT_DOWN {
		public String getDisplayString() {
			return "externally shut down";
		}
		
		public boolean externallyShutDown() {
			return true;
		}
		
		protected int getLevel() {
			return 2;
		}
	},
	ERROR {
		public String getDisplayString() {
			return "error";
		}
		protected int getLevel() {
			return 3;
		}
	}, 
	FAILURE {
		public String getDisplayString() {
			return "failure";
		}		
		protected int getLevel() {
			return 4;
		}
	},
	SUCCESS {
		public String getDisplayString() {
			return "success";
		}
		protected int getLevel() {
			return 5;
		}
	};

	public abstract String getDisplayString();
	
	protected abstract int getLevel();
	
	public final boolean completedTestRun() {
		return !timedOut() && !failedToLaunch() && !externallyShutDown();
	}
	
	public boolean timedOut() {
		return false;
	}

	public boolean failedToLaunch() {
		return false;
	}

	public boolean externallyShutDown() {
		return false;
	}

	public boolean isWorseThan(ResultType other) {
		return getLevel() < other.getLevel();
	}

}
