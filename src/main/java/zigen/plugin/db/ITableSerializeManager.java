/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.runtime.IPath;

import zigen.plugin.db.ui.internal.DataBase;
import zigen.plugin.db.ui.internal.ITable;


public class ITableSerializeManager {

	private IPath path;
	
	public ITableSerializeManager(IPath path) {
		this.path = path;
	}
	
	public String getKey(ITable target){
		return "cache" + File.separator + target.getDataBase().getName() + File.separator + target.getSqlTableName();
	}

	public void save(ITable target) throws IOException {
		String filePath = path.append(getKey(target)).toOSString();
		File f = new File(filePath);
		if(!f.getParentFile().exists()){
			//f.getParentFile().mkdir();
			f.getParentFile().mkdirs();
		}
		SerializeManager.save(new File(filePath), target);;
	}

	public ITable load(ITable target) throws Exception {
		ITable obj = null;
		String filePath = path.append(getKey(target)).toOSString();
		File file = new File(filePath);
		if (file.exists()) {
			try {
				obj = (ITable)SerializeManager.load(file);
			} catch (Exception e) {
				throw e;
			}
		}
		return obj;
	}

	public void delete(ITable target) throws IOException {
		String filePath = path.append(getKey(target)).toOSString();
		File f = new File(filePath);
		if(f.exists()){
			System.out.println(target.getSqlTableName() + "のキャッシュを削除しました。" + f.delete());
		}
		
		if(f.getParentFile().exists() && f.getParentFile().list().length == 0){
			System.out.println(f.getParentFile().getName() + "フォルダを削除しました。" + f.getParentFile().delete());
		}
		
	}
	
	public void delete(DataBase db) throws IOException {
		String dirPath = path.append(db.getName()).toOSString();
		delete(new File(dirPath));				
	}
	
	public void rename(String oldDbName, String newDbName) throws IOException {
		String fromPath = path.append(oldDbName).toOSString();
		File f = new File(fromPath);		
		String toPath = path.append(newDbName).toOSString();
		File t = new File(toPath);		
		if(f.exists() || f.isDirectory()){
			f.renameTo(t);
		}				
	}
	
	private void delete(File file) throws IOException{
		if(file.exists() && file.isFile()){
			file.delete();
		}else if(file.exists() && file.isDirectory()){
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				File f = files[i];
				delete(f);
			}
			file.delete();
		}
	}
}
