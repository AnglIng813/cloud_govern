package com.casic.cloud.hyperloop.autocoding.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileHelper {

	public static List<File> findAllFile(File file){
		List<File> files = new ArrayList<File>();
		if( file.listFiles().length > 0 ){
			for(File f: file.listFiles()){
				if( f.isFile() ){
					files.add(f);
				}else{
					files.addAll(findAllFile(f));
				}
			}
		}
		return files;
	}
	
	/**
	 * 
	 * @param fileName 文件全路径名
	 * @throws IOException 
	 */
	public static File makeFile(String fileName) throws IOException{
        File f = new File(fileName);
        if(!f.exists()){
        	f.getParentFile().mkdirs();
        	f.createNewFile();
        }
        return f;
	}
	/**
	 * 将 dir中的${package}替换为相应的属性值
	 * @param dir
	 * @param params
	 * @return
	 */
	public static String genFileDir(String dir,Map<String,Object> params){
		String input = dir;
		final String regix = "\\$\\{([^}]*)\\}*";
		Pattern pattern = Pattern.compile(regix);
		Matcher m = pattern.matcher(input);
		while(m.find()){
			String name = m.group(1);
			String value  = String.valueOf(params.get(name));
			input = input.replace(m.group(), value);
		}
		return input;
	}
}
