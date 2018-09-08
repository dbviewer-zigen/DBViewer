/*
 * Copyright (c) 2007 - 2009 ZIGEN
 * Eclipse Public License - v 1.0
 * http://www.eclipse.org/legal/epl-v10.html
 */
package zigen.plugin.db.ui.editors.sql;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.IStorageEditorInput;

import zigen.plugin.db.core.IDBConfig;
import zigen.plugin.db.core.rule.SourceDetailInfo;
import zigen.plugin.db.core.rule.SourceErrorInfo;
import zigen.plugin.db.ui.internal.Source;

public class SourceEditorInput implements IStorageEditorInput, IEditorInput {

	private String tooltip;

	private String name;

	private IDBConfig config;;

	private SourceDetailInfo sourceDetailInfo;

	private SourceErrorInfo[] sourceErrorInfos;

	private Source source;

	public SourceEditorInput(IDBConfig config, Source source, SourceDetailInfo sourceDetailInfo, SourceErrorInfo[] sourceErrorInfos) {
		super();
		this.config = config;
		this.source = source;
		this.sourceDetailInfo = sourceDetailInfo;
		this.sourceErrorInfos = sourceErrorInfos;
		this.name = sourceDetailInfo.getName() + "[" + sourceDetailInfo.getType() + "]"; //$NON-NLS-1$ //$NON-NLS-2$
		this.tooltip = sourceDetailInfo.getName() + "[" + sourceDetailInfo.getType() + "]"; //$NON-NLS-1$ //$NON-NLS-2$

	}

	public boolean exists() {
		return false;
	}

	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	public String getName() {
		return this.name;
	}

	public IPersistableElement getPersistable() {
		return null;
	}

	public String getToolTipText() {
		return this.tooltip;
	}

	public Object getAdapter(Class adapter) {
		return Platform.getAdapterManager().getAdapter(this, adapter);
	}

	public boolean equals(Object o) {
		if (o == this)
			return true;

		if (o instanceof SourceEditorInput) {
			SourceEditorInput input = (SourceEditorInput) o;

			if (config.getDbName().equals(input.config.getDbName())) {
				return name.equals(input.getName());
			} else {
				return false;
			}
		}
		return false;
	}

	public int hashCode() {
		return name.hashCode();
	}

	public IDBConfig getConfig() {
		return config;
	}

	public void setToolTipText(String tooltip) {
		this.tooltip = tooltip;
	}

	public SourceErrorInfo[] getSourceErrorInfos() {
		return sourceErrorInfos;
	}

	public SourceDetailInfo getSourceDetailInfo() {
		return sourceDetailInfo;
	}

	public IStorage getStorage() throws CoreException {
		return createStore();
	}

	private IStorage createStore() {
		return new IStorage() {

			public Object getAdapter(Class adapter) {
				return null;
			}

			public boolean isReadOnly() {
				return false;
			}

			public String getName() {
				return null;
			}

			public IPath getFullPath() {
				return null;
			}

			public InputStream getContents() throws CoreException {
				String encoding = org.eclipse.core.resources.ResourcesPlugin.getEncoding();
				byte[] bytes = new byte[0];
				if (sourceDetailInfo != null) {
					try {
						bytes = sourceDetailInfo.getText().getBytes(encoding);
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
						bytes = sourceDetailInfo.getText().getBytes();
					}
				}
				return new ByteArrayInputStream(bytes);
			}

		};
	}


	public Source getSource() {
		return source;
	}
}
