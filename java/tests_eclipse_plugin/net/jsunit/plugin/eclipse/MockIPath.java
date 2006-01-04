package net.jsunit.plugin.eclipse;

import java.io.File;

import org.eclipse.core.runtime.IPath;

public class MockIPath implements IPath {

	private String rawLocation;

	public MockIPath(String rawLocation) {
		this.rawLocation = rawLocation;
	}

	public IPath addFileExtension(String extension) {
		// TODO Auto-generated method stub
		return null;
	}

	public IPath addTrailingSeparator() {
		// TODO Auto-generated method stub
		return null;
	}

	public IPath append(String path) {
		// TODO Auto-generated method stub
		return null;
	}

	public IPath append(IPath path) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getDevice() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getFileExtension() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean hasTrailingSeparator() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isAbsolute() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isPrefixOf(IPath anotherPath) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isRoot() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isUNC() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isValidPath(String path) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isValidSegment(String segment) {
		// TODO Auto-generated method stub
		return false;
	}

	public String lastSegment() {
		// TODO Auto-generated method stub
		return null;
	}

	public IPath makeAbsolute() {
		// TODO Auto-generated method stub
		return null;
	}

	public IPath makeRelative() {
		// TODO Auto-generated method stub
		return null;
	}

	public IPath makeUNC(boolean toUNC) {
		// TODO Auto-generated method stub
		return null;
	}

	public int matchingFirstSegments(IPath anotherPath) {
		// TODO Auto-generated method stub
		return 0;
	}

	public IPath removeFileExtension() {
		// TODO Auto-generated method stub
		return null;
	}

	public IPath removeFirstSegments(int count) {
		// TODO Auto-generated method stub
		return null;
	}

	public IPath removeLastSegments(int count) {
		// TODO Auto-generated method stub
		return null;
	}

	public IPath removeTrailingSeparator() {
		// TODO Auto-generated method stub
		return null;
	}

	public String segment(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	public int segmentCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String[] segments() {
		// TODO Auto-generated method stub
		return null;
	}

	public IPath setDevice(String device) {
		// TODO Auto-generated method stub
		return null;
	}

	public File toFile() {
		// TODO Auto-generated method stub
		return null;
	}

	public String toOSString() {
		// TODO Auto-generated method stub
		return null;
	}

	public String toPortableString() {
		// TODO Auto-generated method stub
		return null;
	}

	public IPath uptoSegment(int count) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object clone() {
		return null;
	}
	
	public String toString() {
		return rawLocation;
	}
}
