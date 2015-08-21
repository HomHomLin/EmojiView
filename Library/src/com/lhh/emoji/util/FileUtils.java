package com.lhh.emoji.util;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

public class FileUtils {

	private static final int BUFF_SIZE = 1024; // 1K Byte

	/**
	 * 复制asset文件到SD卡目录，文件大小不能超过1M
	 * 如果文件已經存在将不再复制
	 * 
	 * @param context
	 * @param sourceFilePath 文件名称
	 * @param destFilePath 拷贝到SD卡上的文件名称
	 * @throws java.io.IOException
	 */
	public static void copyAssetFile(Context context, String sourceFilePath,
			String destFilePath) throws IOException {
		File file = new File(destFilePath + File.separator + sourceFilePath);
		if (file != null && file.exists()) {
			return;
		}
		AssetManager assetManager = context.getAssets();
		InputStream in = null;
		OutputStream out = null;
		in = assetManager.open(sourceFilePath);
		File dir = new File(destFilePath);
		if(dir != null && !dir.exists()){
			dir.mkdir();
		}
		File outFile = new File(destFilePath, sourceFilePath);
		out = new FileOutputStream(outFile);
		copyFile(in, out);
		in.close();
		in = null;
		out.flush();
		out.close();
		out = null;

	}

	private static void copyFile(InputStream in, OutputStream out)
			throws IOException {
		byte[] buffer = new byte[1024];
		int read;
		while ((read = in.read(buffer)) != -1) {
			out.write(buffer, 0, read);
		}
	}

	/**
	 * 解压缩一个文件
	 *
	 * @param zipFile
	 *            压缩文件
	 * @param folderPath
	 *            解压缩的目标目录
	 * @throws java.io.IOException
	 *             当解压缩过程出错时抛出
	 */
	public static void upZipFile(File zipFile, String folderPath)
			throws ZipException, IOException {
		File desDir = new File(folderPath);
		if (!desDir.exists()) {
			desDir.mkdirs();
		}
		ZipFile zf = new ZipFile(zipFile);
		for (Enumeration<?> entries = zf.entries(); entries.hasMoreElements();) {
			ZipEntry entry = ((ZipEntry) entries.nextElement());
			InputStream in = zf.getInputStream(entry);
			String str = folderPath + File.separator + entry.getName();
			str = new String(str.getBytes("8859_1"), "GB2312");
			File desFile = new File(str);
			if (!desFile.exists()) {
				File fileParentDir = desFile.getParentFile();
				if (!fileParentDir.exists()) {
					fileParentDir.mkdirs();
				}
				desFile.createNewFile();
			}
			OutputStream out = new FileOutputStream(desFile);
			byte buffer[] = new byte[BUFF_SIZE];
			int realLength;
			while ((realLength = in.read(buffer)) > 0) {
				out.write(buffer, 0, realLength);
			}
			in.close();
			out.close();
		}
		zf.close();
	}
}
