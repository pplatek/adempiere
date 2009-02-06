/******************************************************************************
 * Copyright (C) 2008 Low Heng Sin                                            *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 *****************************************************************************/
package org.adempiere.webui.util;

import java.io.Serializable;
import java.util.Properties;
import java.util.logging.Logger;

import org.compiere.model.I_AD_Preference;
import org.compiere.model.MPreference;
import org.compiere.model.Query;
import org.compiere.util.Env;
import org.compiere.util.Language;

/**
 * 
 * @author hengsin
 *
 */
public final class UserPreference implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/** Language			*/
	public static final String 	P_LANGUAGE = 		"Language";
	private static final String DEFAULT_LANGUAGE = 	Language.getName
		(System.getProperty("user.language") + "_" + System.getProperty("user.country"));
	/** Role */
	public static final String P_ROLE = "Role";
	private static final String DEFAULT_ROLE = "";
	/** Client Name */
	public static final String P_CLIENT = "Client";
	private static final String DEFAULT_CLIENT = "";
	/** Org Name */
	public static final String P_ORG = "Organization";
	private static final String DEFAULT_ORG = "";
	/** Warehouse Name */
	public static final String P_WAREHOUSE = "Warehouse";
	private static final String DEFAULT_WAREHOUSE = "";

	/** Ini Properties */
	private static final String[] PROPERTIES = new String[] { 
		P_LANGUAGE,
		P_ROLE, 
		P_CLIENT,
		P_ORG, 
		P_WAREHOUSE };
	/** Ini Property Values */
	private static final String[] VALUES = new String[] { 
		DEFAULT_LANGUAGE,
		DEFAULT_ROLE,
		DEFAULT_CLIENT, 
		DEFAULT_ORG, 
		DEFAULT_WAREHOUSE };

	/** Container for Properties */
	private Properties props = new Properties();

	private int m_AD_User_ID;

	/** Logger */
	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger(UserPreference.class.getName());

	/**
	 * save user preference
	 */
	public void savePreference() {
		if (m_AD_User_ID > 0) {
			Query query = new Query(Env.getCtx(), I_AD_Preference.Table_Name, "AD_User_ID = ? AND Attribute = ? AND AD_Window_ID Is NULL", null);
			for (int i = 0; i < PROPERTIES.length; i++) {
				String attribute = PROPERTIES[i];
				String value = props.getProperty(attribute);

				MPreference preference = query.setParameters(new Object[]{m_AD_User_ID, attribute}).first(); 
				if (preference == null) {
					preference = new MPreference(Env.getCtx(), 0, null);				
					preference.setAD_User_ID(m_AD_User_ID);
					preference.setAttribute(attribute);
				}
				preference.setValue(value);
				preference.saveEx();
			}
		}
	}

	/**
	 * load user preference
	 * @param AD_User_ID
	 */
	public void loadPreference(int AD_User_ID) {
		if (AD_User_ID > 0) {
			m_AD_User_ID = AD_User_ID;
			props = new Properties();

			Query query = new Query(Env.getCtx(), I_AD_Preference.Table_Name, "AD_User_ID = ? AND Attribute = ? AND AD_Window_ID Is NULL", null);

			for (int i = 0; i < PROPERTIES.length; i++) {
				String attribute = PROPERTIES[i];
				String value = VALUES[i];

				MPreference preference = query.setParameters(new Object[]{m_AD_User_ID, attribute}).first();
				if (preference != null) {
					value = preference.getValue();
				} 
				
				props.setProperty(attribute, value);
			}
		}
	}

	/**
	 * delete all user preference
	 */
	public void deletePreference() {
		if (m_AD_User_ID > 0) {
			props = new Properties();

			Query query = new Query(Env.getCtx(), I_AD_Preference.Table_Name, "AD_User_ID = ? AND Attribute = ? AND AD_Window_ID Is NULL", null);
			for (int i = 0; i < PROPERTIES.length; i++) {
				String attribute = PROPERTIES[i];

				MPreference preference = query.setParameters(new Object[]{m_AD_User_ID, attribute}).first();
				if (preference != null) {
					preference.deleteEx(true);
				}
			}
		}
	}

	/***************************************************************************
	 * Set Property
	 * 
	 * @param key
	 *            Key
	 * @param value
	 *            Value
	 */
	public void setProperty(String key, String value) {
		if (props == null)
			props = new Properties();
		if (value == null)
			props.setProperty(key, "");
		else
			props.setProperty(key, value);
	} // setProperty

	/**
	 * Set Property
	 * 
	 * @param key
	 *            Key
	 * @param value
	 *            Value
	 */
	public void setProperty(String key, boolean value) {
		setProperty(key, value ? "Y" : "N");
	} // setProperty

	/**
	 * Set Property
	 * 
	 * @param key
	 *            Key
	 * @param value
	 *            Value
	 */
	public void setProperty(String key, int value) {
		setProperty(key, String.valueOf(value));
	} // setProperty

	/**
	 * Get Propery
	 * 
	 * @param key
	 *            Key
	 * @return Value
	 */
	public String getProperty(String key) {
		if (key == null)
			return "";

		String value = props.getProperty(key, "");
		if (value == null || value.length() == 0)
			return "";
		return value;
	} // getProperty

	/**
	 * Get Propery as Boolean
	 * 
	 * @param key
	 *            Key
	 * @return Value
	 */
	public boolean isPropertyBool(String key) {
		return getProperty(key).equals("Y");
	} // getProperty
}