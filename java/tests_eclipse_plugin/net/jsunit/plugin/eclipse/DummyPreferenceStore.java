package net.jsunit.plugin.eclipse;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;

public class DummyPreferenceStore implements IPreferenceStore {

	private Map<String, String> map = new HashMap<String, String>();
	
	public void addPropertyChangeListener(IPropertyChangeListener listener) {
	}

	public boolean contains(String name) {
		return map.containsKey(name);
	}

	public void firePropertyChangeEvent(String name, Object oldValue, Object newValue) {		
	}

	public boolean getBoolean(String name) {
		return Boolean.parseBoolean(getString(name));
	}

	public boolean getDefaultBoolean(String name) {
		return false;
	}

	public double getDefaultDouble(String name) {
		return 0;
	}

	public float getDefaultFloat(String name) {

		return 0;
	}

	public int getDefaultInt(String name) {

		return 0;
	}

	public long getDefaultLong(String name) {

		return 0;
	}

	public String getDefaultString(String name) {

		return null;
	}

	public double getDouble(String name) {
		return Double.parseDouble(getString(name));
	}

	public float getFloat(String name) {
		return Float.parseFloat(getString(name));
	}

	public int getInt(String name) {
		return Integer.parseInt(getString(name));
	}

	public long getLong(String name) {
		return Long.parseLong(getString(name));
	}

	public String getString(String name) {
		return map.get(name);
	}

	public boolean isDefault(String name) {

		return false;
	}

	public boolean needsSaving() {

		return false;
	}

	public void putValue(String name, String value) {

		
	}

	public void removePropertyChangeListener(IPropertyChangeListener listener) {

		
	}

	public void setDefault(String name, double value) {

		
	}

	public void setDefault(String name, float value) {

		
	}

	public void setDefault(String name, int value) {

		
	}

	public void setDefault(String name, long value) {

		
	}

	public void setDefault(String name, String defaultObject) {

		
	}

	public void setDefault(String name, boolean value) {

		
	}

	public void setToDefault(String name) {

		
	}

	public void setValue(String name, double value) {
		setValue(name, String.valueOf(value));
	}

	public void setValue(String name, float value) {
		setValue(name, String.valueOf(value));
	}

	public void setValue(String name, int value) {
		setValue(name, String.valueOf(value));		
	}

	public void setValue(String name, long value) {
		setValue(name, String.valueOf(value));		
	}

	public void setValue(String name, String value) {
		map.put(name, value);		
	}

	public void setValue(String name, boolean value) {
		setValue(name, String.valueOf(value));
	}

}