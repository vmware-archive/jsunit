package net.jsunit.action;

import com.opensymphony.xwork.Action;

public class IndexAction implements Action {

    private int referencedJsFileFieldCount;

    public String execute() throws Exception {
        return SUCCESS;
    }

    public int getReferencedJsFileFieldCount() {
        return referencedJsFileFieldCount;
    }

    public void setReferencedJsFileFieldCount(int referencedJsFileFieldCount) {
        this.referencedJsFileFieldCount = referencedJsFileFieldCount;
    }

}
