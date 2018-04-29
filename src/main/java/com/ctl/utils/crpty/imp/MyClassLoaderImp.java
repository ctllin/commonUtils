package com.ctl.utils.crpty.imp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import com.ctl.utils.crpty.dao.MyClassLoaderDao;
import org.apache.log4j.Logger;


@SuppressWarnings("rawtypes")
public class MyClassLoaderImp extends ClassLoader    implements MyClassLoaderDao {
	public static Logger logger=Logger.getLogger(MyClassLoaderImp.class);
	private static String myClasspath = new String("");
	private static Hashtable<String, Class<?>> loadClassHashTable = new Hashtable<String, Class<?>>();
	private static Hashtable<String, Long> loadClassTime = new Hashtable<String, Long>();
	public MyClassLoaderImp() {

	}

	/** */
	/**
	 * create a classloader and specify a classpath.
	 *
	 * @param myClasspath
	 *            the specified classpath name.
	 */
	public MyClassLoaderImp(String myClasspath) {
		if (!myClasspath.endsWith("\\")) {
			myClasspath = myClasspath + "\\";
		}
		MyClassLoaderImp.myClasspath = myClasspath;
	}

	/** */
	/**
	 * set the classpath
	 *
	 * @param myClasspath
	 *            the specified classpath name
	 */

	public void SetmyClasspath(String myClasspath) {
		if (!myClasspath.endsWith("\\")) {
			myClasspath = myClasspath + "\\";
		}
		MyClassLoaderImp.myClasspath = myClasspath;
	}

	/** */
	/**
	 * Loads the class with the specified binary name. This method searches for
	 * classes in the same manner as the loadClass(String, boolean) method.
	 * Invoking this method is equivalent to invoking {loadClass(name,false)}.
	 *
	 * @param className
	 *            The binary name of the class.
	 *
	 * @return The resulting <tt>Class</tt> object.
	 *
	 * @throws ClassNotFoundException
	 *             If the class was not found.
	 */

	@SuppressWarnings("unchecked")
	public Class loadClass(String className) throws ClassNotFoundException {
		return loadClass(className, false);
	}

	/** */
	/**
	 * Loads the class with the specified binary name. The default
	 * implementation of this method searches for classes in the following
	 * order:
	 *
	 * Invoke {findLoadedClass(String)} to check if the class has already been
	 * loaded.
	 *
	 * Invoke {findSystemClass(String)} to load the system class.
	 *
	 * Invoke the {findClass(String)} method to find the class.
	 *
	 * If the class was found using the above steps, and the resolve flag is
	 * true, this method will then invoke the {resolveClass(Class)} method on
	 * the resulting Class object.
	 *
	 * @param name
	 *            The binary name of the class.
	 *
	 * @param resolve
	 *            If true then resolve the class.
	 *
	 * @return The resulting Class object.
	 *
	 * @throws ClassNotFoundException
	 *             If the class could not be found.
	 */

	@SuppressWarnings("unchecked")
	public  Class loadClass(String name, boolean resolve)
			throws ClassNotFoundException {

		try {
			Class foundClass = findLoadedClass(name);

			// check if the class has already been loaded.
			if (foundClass != null) {
				return foundClass;
			}

			// if the class is systemClass, load the system class by system
			if (name.startsWith("java.")) {
				foundClass = findSystemClass(name);
				loadClassHashTable.put(name, foundClass);
				return foundClass;
			}

			// invoke the findClass() method to load the class
			try {
				foundClass = findClass(name);
			} catch (Exception fnfe) {
			}

			if (resolve && (foundClass != null)) {
				resolveClass(foundClass);
			}
			return foundClass;
		} catch (Exception e) {
			throw new ClassNotFoundException(e.toString());
		}
	}

	/** */
	/**
	 * Finds the class with the specified binary name.The default implementation
	 * throws a ClassNotFoundException.
	 *
	 * @param className
	 *            The binary name of the class.
	 *
	 * @return The resulting Class object.
	 *
	 * @throws ClassNotFoundException
	 *             If the class could not be found.
	 */

	@SuppressWarnings("unchecked")
	public Class findClass(String className) {

		byte[] classData = null;
		try {
			classData = loadClassData(className);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (classData == null) {
			return null;
		}

		Class c = null;
		if(className.endsWith("Dao")||className.endsWith("Bean")||className.equalsIgnoreCase("ClassLoader")||className.equalsIgnoreCase("MyClassLoaderImp")){//如果是接口类或是JavaBean那么不用自定义的方式加载（先解密，后加载）而是直接加载
			try {
				c = Class.forName(className);
			} catch (ClassNotFoundException e) {
				logger.info("Class.forName("+className+")失败");
			}
		}else{
			c = defineClass(className, classData, 0, classData.length);
		}
		MyClassLoaderImp.loadClassHashTable.put(className, c);
		return c;
	}

	/** */
	/**
	 * Loads the classData with the specified binary name. This method searches
	 * for classes in the specified classpath as
	 * searchFile(myClasspath,className) method.
	 *
	 * @param name
	 *            The binary name of the class
	 *
	 * @return The resulting the classData of the class object by byte[]
	 *
	 * @throws IOException
	 *             if have some failed or interrupted I/O operations.
	 */

	public byte[] loadClassData(String className) throws IOException {

		String filePath = searchFile(myClasspath, className + ".class");

		if (!(filePath == null || filePath == "")) {

			FileInputStream inFile = new FileInputStream(filePath);
			byte[] classData = new byte[inFile.available()];
			inFile.read(classData);
			inFile.close();
			loadClassTime.put(className, new File(filePath).lastModified());
			return classData;
		} else {

			filePath = searchFile(myClasspath, className + ".java");
			if (!(filePath == null || filePath == "")) {
				Runtime.getRuntime().exec("javac " + filePath);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return loadClassData(className);
			} else {
				logger.info("Haven't found the file， and fail to read the classData！");
				return null;
			}
		}
	}

	/** */
	/**
	 * Loads the class with the specified binary name.The default implementation
	 * throws a ClassNotFoundException.
	 *
	 * @param classData
	 *            The data of the class.
	 * @param className
	 *            The binary name of the class.
	 *
	 * @return The resulting Class object.
	 *
	 * @throws ClassNotFoundException
	 *             If the class could not be found.
	 */

	public Class loadClass(byte[] classData, String className)
			throws ClassNotFoundException {
		Class c = defineClass(className, classData, 0, classData.length);
		loadClassHashTable.put(className, c);
		return c;
	}

	/** */
	/**
	 * Loads the class with the specified binary name.The default implementation
	 * throws a ClassNotFoundException.
	 *
	 * @param className
	 *            The binary name of the class.
	 * @param jarName
	 *            The binary name of the jar that search the class from it.
	 *
	 * @return The resulting Class object.
	 *
	 * @throws ClassNotFoundException
	 *             If the class could not be found.
	 */


	public Class loadClass(String className, String jarName)
			throws ClassNotFoundException {

		String jarPath = searchFile(myClasspath, jarName + ".jar");
		JarInputStream in = null;

		if (!(jarPath == null || jarPath == "")) {

			try {
				in = new JarInputStream(new FileInputStream(jarPath));
				JarEntry entry;
				while ((entry = in.getNextJarEntry()) != null) {
					String outFileName = entry.getName().substring(
							entry.getName().lastIndexOf("/") + 1,
							entry.getName().length());
					if (outFileName.equals(className + ".class")) {
						if (entry.getSize() == -1) {
							System.err.println("error ： can't read the file！");
							return null;
						}
						byte[] classData = new byte[(int) entry.getSize()];
						in.read(classData);
						return loadClass(classData, className);
					}
				}
				logger.info("Haven't found the file " + className
						+ " in " + jarName + ".jar.");
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			logger.info("Haven't found the jarFile: " + jarName
					+ ".jar.");
			return null;
		}
		return null;
	}

	/** */
	/**
	 * Reloads the class with the specified binary name. Needn't have to restart
	 * JVM then reload the class.
	 *
	 * @param className
	 *            The binary name of the class need to reload .
	 *
	 * @return The resulting Class object.
	 *
	 * @throws ClassNotFoundException
	 *             If the class was not found.
	 */

	public Class reload(String fileName) {

		String filePath = searchFile(myClasspath, fileName + ".class");
		Long a = new File(filePath).lastModified();

		if (!a.equals(loadClassTime.get(fileName))) {
			loadClassHashTable.remove(fileName);
			loadClassTime.remove(fileName);
			try {
				MyClassLoaderImp mc2 = new MyClassLoaderImp(myClasspath);
				mc2.loadClass(fileName);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			logger.info("The class is the newest version , needn't reloading.");
		}
		return null;
	}

	/** */
	/**
	 * search the file with the specified binary name. Needn't have to restart
	 * JVM then reload the class.
	 *
	 * @param classpath
	 *            the specified path where we search.
	 * @param fileName
	 *            The binary name of the file that want to search.
	 *
	 * @return The resulting file path.
	 */

	public String searchFile(String classpath, String fileName) {

		String cut = fileName.substring(fileName.lastIndexOf('.'), fileName
				.length());
		String path = fileName.substring(0, fileName.lastIndexOf('.')).replace(
				'.', '/')
				+ cut;

		File f = new File(classpath + path);
		if (f.isFile()) {
			return f.getPath();
		} else {
			String objects[] = new File(classpath).list();
			for (int i = 0; i < objects.length; i++) {
				if (new File(classpath + File.separator + objects[i])
						.isDirectory()) {
					String s = searchFile(classpath + objects[i]
							+ File.separator, fileName);
					if (s == null || s == "") {
						continue;
					} else {
						return s;
					}
				}
			}
		}
		return null;
	};

}