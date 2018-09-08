/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package zigen.plugin.db.ui.views;

import org.eclipse.jface.viewers.DecorationContext;
import org.eclipse.ui.PlatformUI;

public class DecoratingTreeLabelProvider extends ColoringLabelProvider {

	public DecoratingTreeLabelProvider(TreeLabelProvider labelProvider) {
		super(labelProvider, PlatformUI.getWorkbench().getDecoratorManager().getLabelDecorator(), DecorationContext.DEFAULT_CONTEXT);
		labelProvider.addLabelDecorator(new TreeLabelDecorator());
	}
//
//	public DecoratingTreeLabelProvider(TreeLabelProvider labelProvider, boolean errorTick) {
//		this(labelProvider, errorTick, true);
//	}
//
//	public DecoratingTreeLabelProvider(TreeLabelProvider labelProvider, boolean errorTick, boolean flatPackageMode) {
//		super(labelProvider, PlatformUI.getWorkbench().getDecoratorManager().getLabelDecorator(), DecorationContext.DEFAULT_CONTEXT);
////		if (errorTick) {
////			labelProvider.addLabelDecorator(new ProblemsLabelDecorator(null));
////		}
//		setFlatPackageMode(flatPackageMode);
//	}

	/**
	 * Tells the label decorator if the view presents packages flat or hierarchical.
	 * @param enable If set, packages are presented in flat mode.O
	 */
	public void setFlatPackageMode(boolean enable) {
//		if (enable) {
//			setDecorationContext(DecorationContext.DEFAULT_CONTEXT);
//		} else {
//			setDecorationContext(HierarchicalDecorationContext.getContext());
//		}
		setDecorationContext(DecorationContext.DEFAULT_CONTEXT);
	}

}
