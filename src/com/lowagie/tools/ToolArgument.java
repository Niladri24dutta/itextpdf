/*
 * $Id$
 * $Name$
 *
 * Copyright 2005 by Bruno Lowagie.
 *
 * The contents of this file are subject to the Mozilla Public License Version 1.1
 * (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the License.
 *
 * The Original Code is 'iText, a free JAVA-PDF library'.
 *
 * The Initial Developer of the Original Code is Bruno Lowagie. Portions created by
 * the Initial Developer are Copyright (C) 1999, 2000, 2001, 2002 by Bruno Lowagie.
 * All Rights Reserved.
 * Co-Developer of the code is Paulo Soares. Portions created by the Co-Developer
 * are Copyright (C) 2000, 2001, 2002 by Paulo Soares. All Rights Reserved.
 *
 * Contributor(s): all the names of the contributors are added in the source code
 * where applicable.
 *
 * Alternatively, the contents of this file may be used under the terms of the
 * LGPL license (the "GNU LIBRARY GENERAL PUBLIC LICENSE"), in which case the
 * provisions of LGPL are applicable instead of those above.  If you wish to
 * allow use of your version of this file only under the terms of the LGPL
 * License and not to allow others to use your version of this file under
 * the MPL, indicate your decision by deleting the provisions above and
 * replace them with the notice and other provisions required by the LGPL.
 * If you do not delete the provisions above, a recipient may use your version
 * of this file under either the MPL or the GNU LIBRARY GENERAL PUBLIC LICENSE.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the MPL as stated above or under the terms of the GNU
 * Library General Public License as published by the Free Software Foundation;
 * either version 2 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library general Public License for more
 * details.
 *
 * If you didn't download this code from the following link, you should check if
 * you aren't using an obsolete version:
 * http://www.lowagie.com/iText/
 */
package com.lowagie.tools;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;

import com.lowagie.text.Image;

/**
 * This is an argument of one of the tools in the toolbox.
 */
public class ToolArgument implements ActionListener {
	/** reference to the internal frame */
	JInternalFrame f;
	/** describes the argument. */
	private String description;
	/** short name for the argument. */
	private String name;
	/** type of the argument. */
	private String classname;
	/** value of the argument. */
	private String value = null;
	/** Constructs a ToolArgument. */
	public ToolArgument() {}
	/**
	 * Constructs a ToolArgument. 
	 * @param f	the internal frame of the tool
	 * @param name	the name of the argument
	 * @param description	the description of the argument
	 * @param classname		the type of the argument
	 */
	public ToolArgument(JInternalFrame f, String name, String description, String classname) {
		this.f = f;
		this.name = name;
		this.description = description;
		this.classname = classname;
	}
	/**
	 * Give you a String that can be used in a usage description.
	 * @return a String
	 */
	public String getUsage() {
		StringBuffer buf = new StringBuffer("  ");
		buf.append(name);
		buf.append(" -  ");
		buf.append(description);
		buf.append("\n");
		return buf.toString();
	}
	/**
	 * @return Returns the classname.
	 */
	public String getClassname() {
		return classname;
	}
	/**
	 * @param classname The classname to set.
	 */
	public void setClassname(String classname) {
		this.classname = classname;
	}
	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return Returns the value.
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param value The value to set.
	 */
	public void setValue(String value) {
		this.value = value;
	}
	/**
	 * Gets the argument as an object.
	 * @return an object
	 * @throws InstantiationException
	 */
	public Object getArgument() throws InstantiationException {
		if (value == null) return null;
		try {
			if (String.class.getName().equals(classname)) return value;
			if (Image.class.getName().equals(classname)) return Image.getInstance(value);
			if (File.class.getName().equals(classname)) return new File(value);
			if (Color.class.getName().equals(classname)) return Color.decode(value);
		} catch (Exception e) {
			throw new InstantiationException(e.getMessage());
		}
		return value;
	}
	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if (String.class.getName().equals(classname))
			value = JOptionPane.showInputDialog(f, "Enter a value for " + name + ":");
		if (Image.class.getName().equals(classname)) {
			JFileChooser fc = new JFileChooser();
			fc.showOpenDialog(f);
			value = fc.getSelectedFile().getAbsolutePath();
		}
		if (File.class.getName().equals(classname)) {
			JFileChooser fc = new JFileChooser();
			if (name.startsWith("destfile")) {
				fc.showSaveDialog(f);
			}
			else {
				fc.showOpenDialog(f);
			}
			value = fc.getSelectedFile().getAbsolutePath();
		}
		if (Color.class.getName().equals(classname)) {
			Color initialColor = new Color(0xFF, 0xFF, 0xFF);
			if (value != null) initialColor = Color.decode(value);
			Color newColor = JColorChooser.showDialog(f, "Choose Color", initialColor);
			value = "0x" + Integer.toHexString((newColor.getRed() << 16) | (newColor.getGreen() << 8) | (newColor.getBlue() << 0)).toUpperCase();
		}
	}
}
