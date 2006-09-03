/**
 * TestRunServiceService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package net.jsunit.services;

public interface TestRunServiceService extends javax.xml.rpc.Service {
    public java.lang.String getTestRunServiceAddress();

    public net.jsunit.services.TestRunService getTestRunService() throws javax.xml.rpc.ServiceException;

    public net.jsunit.services.TestRunService getTestRunService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
