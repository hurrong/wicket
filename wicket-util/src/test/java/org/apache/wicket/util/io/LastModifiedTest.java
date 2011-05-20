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
package org.apache.wicket.util.io;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.wicket.util.file.Files;
import org.apache.wicket.util.time.Time;
import org.junit.Assert;
import org.junit.Test;

public class LastModifiedTest extends Assert
{
	private static final String FILE = "/some/folder/file.jar";

	@Test
	public void getLocalFileFromUrl()
	{
		File file = Files.getLocalFileFromUrl("file:" + FILE);
		assertEquals(file.getAbsolutePath(), FILE);

		file = Files.getLocalFileFromUrl("jar:file:" + FILE + "!/internal/resource/bla/foo/bar/baz");
		assertEquals(file.getAbsolutePath(), FILE);

		file = Files.getLocalFileFromUrl("http://bla.de");
		assertNull(file);
	}

	@Test
	public void getLastModified() throws IOException
	{
		File file = File.createTempFile("wicket-io-utils-test", "lastmodified");
		assertTrue(file.exists());

		try
		{
			long lm = file.lastModified();

			// it could be the case that the current system does not support last-modified at all
			if (lm != 0)
			{
				final Time expected = Time.millis(lm);
				assertEquals(expected, IOUtils.getLastModified(file));
				assertEquals(expected, Connections.getLastModified(new URL("file:" + file.getAbsolutePath())));
			}
		}
		finally
		{
			Files.remove(file);
		}
	}
}
