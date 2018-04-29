package com.ctl.utils.crpty.dao;

import java.io.IOException;

@SuppressWarnings("rawtypes")
public interface MyClassLoaderDao {
	public void SetmyClasspath(String myClasspath);

	public Class loadClass(String className) throws ClassNotFoundException;

	public Class loadClass(String name, boolean resolve)
			throws ClassNotFoundException;

	public Class findClass(String className);

	public byte[] loadClassData(String className) throws IOException;

	public Class loadClass(byte[] classData, String className)
			throws ClassNotFoundException;

	public Class loadClass(String className, String jarName)
			throws ClassNotFoundException;

	public Class reload(String fileName);

	public String searchFile(String classpath, String fileName);;
}
