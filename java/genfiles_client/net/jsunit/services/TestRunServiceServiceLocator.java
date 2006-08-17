/**
 * TestRunServiceServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package net.jsunit.services;

public class TestRunServiceServiceLocator extends org.apache.axis.client.Service implements net.jsunit.services.TestRunServiceService {

    public TestRunServiceServiceLocator() {
    }


    public TestRunServiceServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public TestRunServiceServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for TestRunService
    private java.lang.String TestRunService_address = "https://services.jsunit.net/services/TestRunService";

    public java.lang.String getTestRunServiceAddress() {
        return TestRunService_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String TestRunServiceWSDDServiceName = "TestRunService";

    public java.lang.String getTestRunServiceWSDDServiceName() {
        return TestRunServiceWSDDServiceName;
    }

    public void setTestRunServiceWSDDServiceName(java.lang.String name) {
        TestRunServiceWSDDServiceName = name;
    }

    public net.jsunit.services.TestRunService getTestRunService() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(TestRunService_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getTestRunService(endpoint);
    }

    public net.jsunit.services.TestRunService getTestRunService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            net.jsunit.services.TestRunServiceSoapBindingStub _stub = new net.jsunit.services.TestRunServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getTestRunServiceWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setTestRunServiceEndpointAddress(java.lang.String address) {
        TestRunService_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (net.jsunit.services.TestRunService.class.isAssignableFrom(serviceEndpointInterface)) {
                net.jsunit.services.TestRunServiceSoapBindingStub _stub = new net.jsunit.services.TestRunServiceSoapBindingStub(new java.net.URL(TestRunService_address), this);
                _stub.setPortName(getTestRunServiceWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("TestRunService".equals(inputPortName)) {
            return getTestRunService();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("https://services.jsunit.net/services", "TestRunServiceService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("https://services.jsunit.net/services", "TestRunService"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("TestRunService".equals(portName)) {
            setTestRunServiceEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
