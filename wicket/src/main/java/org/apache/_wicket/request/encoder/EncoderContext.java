/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache._wicket.request.encoder;

import org.apache._wicket.IPage;
import org.apache._wicket.PageParameters;
import org.apache.wicket.RequestListenerInterface;

/**
 * Utility interface for providing and creating new page instances.
 * 
 * @author Matej Knopp
 */
public interface EncoderContext
{
	/**
	 * Returns existing page instance if the page exists.
	 * 
	 * @param pageMapName
	 * @param pageId
	 * @param versionNumber
	 * @return page instance or <code>null</code> if the page does not exist.
	 */
	public IPage getPageInstance(String pageMapName, int pageId, int versionNumber);

	/**
	 * Creates new page instance of page with given class.
	 * 
	 * @param pageMapName
	 * @param pageClass
	 * @param pageParameters
	 * @return new page instance
	 */
	public IPage newPageInstance(String pageMapName, Class<? extends IPage> pageClass,
		PageParameters pageParameters);
	
	/**
	 * @return the namespace for Wicket URLs.
	 */
	public String getNamespace();
	
	/**
	 * @return identifier for non bookmarkable URLs
	 */
	public String getPageIdentifier();
	
	/**
	 * @return identifier for bookmarkable URLs
	 */
	public String getBookmarkableIdentifier();
	
	/**
	 * Returns the listener interface name as string.
	 * 
	 * @param listenerInterface
	 * @return listener interface name as string
	 */
	public String requestListenerInterfaceToString(RequestListenerInterface listenerInterface);
	
	/**
	 * Returns listener interface for the name
	 * 
	 * @param interfaceName
	 * @return listener interface
	 */
	public RequestListenerInterface requestListenerInterfaceFromString(String interfaceName);
}
