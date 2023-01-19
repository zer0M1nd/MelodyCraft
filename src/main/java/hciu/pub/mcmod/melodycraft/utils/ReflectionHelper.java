package hciu.pub.mcmod.melodycraft.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import net.minecraft.block.BlockRedstoneLight;

/**
 * 一个工具类，用于简化处理反射的过程。<br>
 * 反射可以帮助你绕过限制访问protected, private或final成员。<br>
 * 注意：对在编译期间指定了值的final成员（主要是static final常量）无效。<br>
 * <br>
 * 要对一个成员进行反射访问，你需要先获取其srg名称。<br>
 * 获取srg名称的方法可以参考4z的教程的附录C：混淆与反射<br>
 * <br>
 * 反射所带来的时间花费接近正常调用的1000倍，请仅在需要时使用。
 * 
 * @author Herobrine_CZM
 */
public class ReflectionHelper {

	private static Map<MethodDescriptor, Method> methodsCache = new HashMap<>();
	private static Map<FieldDescriptor, Field> fieldsCache = new HashMap<>();
	private static int environment = 0;

	static {
		Class<BlockRedstoneLight> cls = BlockRedstoneLight.class;
		Field f = cls.getDeclaredFields()[0];
		if (f.getName().contains("field")) {
			environment = 1;
		}
	}

	private static class MethodDescriptor {
		Class<?> cls;
		String name;
		Class<?>[] param;

		public MethodDescriptor(Class<?> cls, String name, Class<?>[] param) {
			this.cls = cls;
			this.name = name;
			this.param = param;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((cls == null) ? 0 : cls.hashCode());
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			result = prime * result + Arrays.hashCode(param);
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			MethodDescriptor other = (MethodDescriptor) obj;
			if (cls == null) {
				if (other.cls != null)
					return false;
			} else if (!cls.equals(other.cls))
				return false;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			if (!Arrays.equals(param, other.param))
				return false;
			return true;
		}

	}

	private static class FieldDescriptor {
		Class<?> cls;
		String name;

		public FieldDescriptor(Class<?> cls, String name) {
			this.cls = cls;
			this.name = name;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((cls == null) ? 0 : cls.hashCode());
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			FieldDescriptor other = (FieldDescriptor) obj;
			if (cls == null) {
				if (other.cls != null)
					return false;
			} else if (!cls.equals(other.cls))
				return false;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			return true;
		}

	}

	private static Class<?>[] getParameterTypes(Object[] param) {
		Class<?>[] cls = new Class[param.length];
		for (int i = 0; i < param.length; i++) {
			cls[i] = param[i].getClass();
			if (cls[i] == Integer.class) {
				cls[i] = int.class;
			} else if (cls[i] == Double.class) {
				cls[i] = double.class;
			} else if (cls[i] == Boolean.class) {
				cls[i] = boolean.class;
			} else if (cls[i] == Float.class) {
				cls[i] = float.class;
			} else if (cls[i] == Character.class) {
				cls[i] = char.class;
			} else if (cls[i] == Long.class) {
				cls[i] = long.class;
			} else if (cls[i] == Short.class) {
				cls[i] = short.class;
			} else if (cls[i] == Byte.class) {
				cls[i] = byte.class;
			}
		}
		return cls;
	}

	/**
	 * 通过反射执行一个方法。<br>
	 * 你需要同时提供目标方法的mcp名和srg名，此方法会根据当前所在的环境确定使用哪一个名称。<br>
	 * 默认情况下，此方法会根据你提供的参数列表的对象类型来获取参数的类型列表。<br>
	 * 如果执行方法时有以下两种情况之一：<br>
	 * - 你所提供的参数是该方法参数类型的子类对象<br>
	 * - 该方法接受一个包装类(Integer,Long,Boolean)的对象而非基本类型(int,long,boolean)<br>
	 * 你需要使用
	 * {@link ReflectionHelper#method(Class, String, String, Object, Class[], Object...)}来手动指定参数类型，以找到正确的方法。
	 * 
	 * @param cls    要访问的方法所在的类
	 * @param mcp    该方法的mcp名称
	 * @param srg    该方法的srg名称
	 * @param object 要在哪个对象上执行该方法？如果该方法是static的，可以提供null
	 * @param param  该方法的参数列表
	 * @return 该方法的返回值，如果没有返回值则返回null
	 * @throws ReflectiveOperationException 如果反射中出现了奇怪的错误，请捕获它
	 */
	public static <T> T method(Class<?> cls, String mcp, String srg, Object object, Object... param)
			throws ReflectiveOperationException {
		String name = srg;
		if (environment == 0) {
			name = mcp;
		}
		Class<?>[] pt = getParameterTypes(param);
		MethodDescriptor md = new MethodDescriptor(cls, name, pt);
		Method m = null;
		if (methodsCache.containsKey(md)) {
			m = methodsCache.get(md);
		} else {
			m = cls.getDeclaredMethod(name, pt);
			m.setAccessible(true);
			methodsCache.put(md, m);
		}
		return (T) m.invoke(object, param);
	}

	/**
	 * 通过反射执行一个方法。<br>
	 * 你需要同时提供目标方法的mcp名和srg名，此方法会根据当前所在的环境确定使用哪一个名称。<br>
	 * 同时，你还要指定该方法的参数列表，以确定能够找到正确的方法。
	 * 
	 * @param cls        要访问的方法所在的类
	 * @param mcp        该方法的mcp名称
	 * @param srg        该方法的srg名称
	 * @param object     要在哪个对象上执行该方法？如果该方法是static的，可以传入null
	 * @param paramTypes 该方法的参数类型列表
	 * @param param      该方法的参数列表
	 * @return 该方法的返回值，如果没有返回值则返回null
	 * @throws ReflectiveOperationException 如果反射中出现了奇怪的错误，请捕获它
	 */
	public static <T> T method(Class<?> cls, String mcp, String srg, Object object, Class<?>[] paramTypes,
			Object... param) throws ReflectiveOperationException {
		String name = srg;
		if (environment == 0) {
			name = mcp;
		}
		Class<?>[] pt = paramTypes;
		MethodDescriptor md = new MethodDescriptor(cls, name, pt);
		Method m = null;
		if (methodsCache.containsKey(md)) {
			m = methodsCache.get(md);
		} else {
			m = cls.getDeclaredMethod(name, pt);
			m.setAccessible(true);
			methodsCache.put(md, m);
		}
		return (T) m.invoke(object, param);
	}

	/**
	 * 通过反射获取一个属性的值。<br>
	 * 你需要同时提供目标属性的mcp名和srg名，此方法会根据当前所在的环境确定使用哪一个名称。<br>
	 * 
	 * @param cls    该属性所在的类
	 * @param mcp    该属性的mcp名称
	 * @param srg    该属性的srg名称
	 * @param object 要获取属性的对象，如果该属性是static的，可以传入null
	 * @return 获取到的属性
	 * @throws ReflectiveOperationException 如果反射中出现了奇怪的错误，请捕获它
	 */
	public static <T> T getField(Class<?> cls, String mcp, String srg, Object object)
			throws ReflectiveOperationException {
		String name = srg;
		if (environment == 0) {
			name = mcp;
		}
		FieldDescriptor md = new FieldDescriptor(cls, name);
		Field f = null;
		if (fieldsCache.containsKey(md)) {
			f = fieldsCache.get(md);
		} else {
			f = cls.getDeclaredField(name);
			f.setAccessible(true);
			fieldsCache.put(md, f);
		}
		return (T) f.get(object);
	}

	/**
	 * 通过反射更改一个属性的值。<br>
	 * 你需要同时提供目标属性的mcp名和srg名，此方法会根据当前所在的环境确定使用哪一个名称。<br>
	 * 再次提醒，对在编译期间指定了值的final成员（主要是static final常量）无效。
	 * 
	 * @param cls    该属性所在的类
	 * @param mcp    该属性的mcp名称
	 * @param srg    该属性的srg名称
	 * @param object 要获取属性的对象，如果该属性是static的，可以传入null
	 * @param value  要设置的属性值
	 * @throws ReflectiveOperationException 如果反射中出现了奇怪的错误，请捕获它
	 */
	public static <T> void setField(Class<?> cls, String mcp, String srg, Object object, Object value)
			throws ReflectiveOperationException {
		String name = srg;
		if (environment == 0) {
			name = mcp;
		}
		FieldDescriptor md = new FieldDescriptor(cls, name);
		Field f = null;
		if (fieldsCache.containsKey(md)) {
			f = fieldsCache.get(md);
		} else {
			f = cls.getDeclaredField(name);
			f.setAccessible(true);
			fieldsCache.put(md, f);
		}
		f.set(object, value);
	}

	/**
	 * 获取一个包下的所有类 <br/>
	 * 抄的代码：https://blog.csdn.net/qq_30285985/java/article/details/102988013
	 * 
	 * @param pack
	 * @return
	 */
	public static Set<Class<?>> getClasses(String pack) {

		// 第一个class类的集合
		Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
		// 是否循环迭代
		boolean recursive = true;
		// 获取包的名字 并进行替换
		String packageName = pack;
		String packageDirName = packageName.replace('.', '/');
		// 定义一个枚举的集合 并进行循环来处理这个目录下的things
		Enumeration<URL> dirs;
		try {
			dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
			// 循环迭代下去
			while (dirs.hasMoreElements()) {
				// 获取下一个元素
				URL url = dirs.nextElement();
				// 得到协议的名称
				String protocol = url.getProtocol();
				// 如果是以文件的形式保存在服务器上
				if ("file".equals(protocol)) {
					// 获取包的物理路径
					String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
					// 以文件的方式扫描整个包下的文件 并添加到集合中
					findClassesInPackageByFile(packageName, filePath, recursive, classes);
				} else if ("jar".equals(protocol)) {
					// 如果是jar包文件
					// 定义一个JarFile
					System.out.println("jar类型的扫描");
					JarFile jar;
					try {
						// 获取jar
						jar = ((JarURLConnection) url.openConnection()).getJarFile();
						// 从此jar包 得到一个枚举类
						Enumeration<JarEntry> entries = jar.entries();
						findClassesInPackageByJar(packageName, entries, packageDirName, recursive, classes);
					} catch (IOException e) {
						// log.error("在扫描用户定义视图时从jar包获取文件出错");
						e.printStackTrace();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return classes;
	}

	private static void findClassesInPackageByFile(String packageName, String packagePath, final boolean recursive,
			Set<Class<?>> classes) {
		// 获取此包的目录 建立一个File
		File dir = new File(packagePath);
		// 如果不存在或者 也不是目录就直接返回
		if (!dir.exists() || !dir.isDirectory()) {
			// log.warn("用户定义包名 " + packageName + " 下没有任何文件");
			return;
		}
		// 如果存在 就获取包下的所有文件 包括目录
		File[] dirfiles = dir.listFiles(new FileFilter() {
			// 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
			@Override
			public boolean accept(File file) {
				return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));
			}
		});
		// 循环所有文件
		for (File file : dirfiles) {
			// 如果是目录 则继续扫描
			if (file.isDirectory()) {
				findClassesInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive,
						classes);
			} else {
				// 如果是java类文件 去掉后面的.class 只留下类名
				String className = file.getName().substring(0, file.getName().length() - 6);
				try {
					// 添加到集合中去
					// classes.add(Class.forName(packageName + '.' +
					// className));
					// 经过回复同学的提醒，这里用forName有一些不好，会触发static方法，没有使用classLoader的load干净
					classes.add(
							Thread.currentThread().getContextClassLoader().loadClass(packageName + '.' + className));
				} catch (ClassNotFoundException e) {
					// log.error("添加用户自定义视图类错误 找不到此类的.class文件");
					e.printStackTrace();
				}
			}
		}
	}

	private static void findClassesInPackageByJar(String packageName, Enumeration<JarEntry> entries,
			String packageDirName, final boolean recursive, Set<Class<?>> classes) {
		// 同样的进行循环迭代
		while (entries.hasMoreElements()) {
			// 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
			JarEntry entry = entries.nextElement();
			String name = entry.getName();
			// 如果是以/开头的
			if (name.charAt(0) == '/') {
				// 获取后面的字符串
				name = name.substring(1);
			}
			// 如果前半部分和定义的包名相同
			if (name.startsWith(packageDirName)) {
				int idx = name.lastIndexOf('/');
				// 如果以"/"结尾 是一个包
				if (idx != -1) {
					// 获取包名 把"/"替换成"."
					packageName = name.substring(0, idx).replace('/', '.');
				}
				// 如果可以迭代下去 并且是一个包
				if ((idx != -1) || recursive) {
					// 如果是一个.class文件 而且不是目录
					if (name.endsWith(".class") && !entry.isDirectory()) {
						// 去掉后面的".class" 获取真正的类名
						String className = name.substring(packageName.length() + 1, name.length() - 6);
						try {
							// 添加到classes
							classes.add(Class.forName(packageName + '.' + className));
						} catch (ClassNotFoundException e) {
							// .error("添加用户自定义视图类错误 找不到此类的.class文件");
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
}
