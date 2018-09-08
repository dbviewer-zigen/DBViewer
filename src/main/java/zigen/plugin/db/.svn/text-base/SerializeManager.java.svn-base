/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import zigen.plugin.db.core.XMLManager;

public class SerializeManager {
	
	public static void save(File path, Object value) throws IOException {
		ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
		ClassLoader newLoader = XMLManager.class.getClassLoader();
		Thread.currentThread().setContextClassLoader(newLoader);

		FileOutputStream outFile = null;
		ObjectOutputStream outObject = null;
		try {
			outFile = new FileOutputStream(path); 
			outObject = new ObjectOutputStream(outFile);
			outObject.writeObject(value);
		} finally{
			if(outObject != null) {
				try{
					outObject.close();
				}catch(IOException e){
					;
				}
			}
			if(outFile != null) {
				try{
					outFile.close();
				}catch(IOException e){
					;
				}
			}
		}
		
		
		Thread.currentThread().setContextClassLoader(oldLoader);

	}

	public static Object load(File path) throws Exception {
		Object obj = null;
		ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
		ClassLoader newLoader = SerializeManager.class.getClassLoader();
		Thread.currentThread().setContextClassLoader(newLoader);
		
		FileInputStream inFile = null;
		ObjectInputStream inObject = null;
		try {
			inFile = new FileInputStream(path); 
			inObject = new ObjectInputStream(inFile);
			obj =  inObject.readObject();
		} finally{
			if(inObject != null) {
				try{
					inObject.close();
				}catch(IOException e){
					;
				}
			}
			if(inFile != null) {
				try{
					inFile.close();
				}catch(IOException e){
					;
				}
			}
		}
		Thread.currentThread().setContextClassLoader(oldLoader);

		return obj;
	}

}
