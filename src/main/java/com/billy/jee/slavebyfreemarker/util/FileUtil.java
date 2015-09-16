package com.billy.jee.slavebyfreemarker.util;

import java.io.File;

public class FileUtil {

	/**
	 * delete a directory
	 *
	 * @param directoryPath
	 */
	public static void deleteDirectory(String directoryPath) {
		File file = new File(directoryPath);
		if (file.exists()) { // 如果不是文件就是目录，这是jdk说的。
			if (file.isFile()) {
				file.delete();
			} else {
				String childPaths[] = file.list();
				for (String string : childPaths) {
					deleteDirectory(file.getAbsolutePath() + "/" + string);
				}
				file.delete();
			}
		}
	}


}
