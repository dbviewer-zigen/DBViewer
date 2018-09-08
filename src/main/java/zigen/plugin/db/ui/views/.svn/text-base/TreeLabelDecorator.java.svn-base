package zigen.plugin.db.ui.views;

import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import zigen.plugin.db.ui.internal.Source;

public class TreeLabelDecorator implements ILabelDecorator {
	private Image decImage;

	public Image decorateImage(Image image, Object element) {

//		if(element instanceof ITable){
//			ITable table = (ITable)element;
//			if(!table.isExpanded()){
//				decImage = new Image(Display.getCurrent(), image, SWT.IMAGE_DISABLE);
//				return decImage;
//			}
//		}else
		if(element instanceof Source){
			Source src = (Source)element;
			if(!src.canOpen()){
				decImage = new Image(Display.getCurrent(), image, SWT.IMAGE_DISABLE);
				return decImage;
			}
		}
		return image;
	}

	public String decorateText(String text, Object element) {
		return null;
	}

	public void addListener(ILabelProviderListener listener) {
	}

	public void dispose() {
		if(!decImage.isDisposed()){
			decImage.dispose();
		}

	}

	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	public void removeListener(ILabelProviderListener listener) {
	}

}
