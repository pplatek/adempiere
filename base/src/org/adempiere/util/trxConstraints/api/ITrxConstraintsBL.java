/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) metas GmbH All Rights Reserved.                              *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 *                                                                            *
 * @author Tobias Schoenebrg, metas GmbH                                      *
 *****************************************************************************/
package org.adempiere.util.trxConstraints.api;

import org.adempiere.util.ISingletonService;
import org.compiere.util.DB;

/**
 * Service maintains {@link ITrxConstraints} instances for threads. A new instance is created for a thread at the first
 * time, 'getConstraints' is called. The instance is destroyed when the thread finishes.
 * 
 * @author Tobias Schoeneberg, metas GmbH
 *          <li>FR [ JIRA-73 ] Runtime checking of trx constraints
 *              https://adempiere.atlassian.net/browse/ADEMPIERE-73 
 *              
 * @see DB#getConstraints()
 */
public interface ITrxConstraintsBL extends ISingletonService
{
	/**
	 * Returns the constraints instance for the calling thread.
	 * 
	 * @return
	 */
	public ITrxConstraints getConstraints();

	/**
	 * Returns the constraints instance for the given thread.
	 * 
	 * @param thread
	 * @return
	 */
	public ITrxConstraints getConstraints(Thread thread);

	public void saveConstraints();
	
	public void restoreConstraints();

	/**
	 * 
	 * @param constraints
	 * @return true if the constraints are disabled and shall not be used/enforced
	 */
	boolean isDisabled(ITrxConstraints constraints);
}
