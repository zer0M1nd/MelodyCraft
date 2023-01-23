package hciu.pub.mcmod.melodycraft.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {

	private static MessageDigest md5;

	static {
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	private static final char[] CH = "0123456789ABCDEF".toCharArray();

	public static String get(File file) {
		try {
			if (file.exists() && !file.isDirectory()) {
				md5.reset();
				byte[] buf = new byte[1024 * 10];
				FileInputStream in = new FileInputStream(file);
				while (true) {
					int r = in.read(buf);
					if (r == -1) {
						break;
					}
					md5.update(buf, 0, r);
				}
				in.close();
				byte[] val = md5.digest();
				StringBuilder s = new StringBuilder();
				for (byte b : val) {
					int bb = ((int) b + 256) % 256;
					s.append(CH[bb / 16]);
					s.append(CH[bb % 16]);
				}
				return s.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
