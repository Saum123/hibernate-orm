/*
 *
 *  * Hibernate, Relational Persistence for Idiomatic Java
 *  *
 *  * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 *  * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 *
 */

package org.hibernate.boot;

import org.hibernate.boot.jaxb.internal.CacheableFileXmlSource;
import org.hibernate.boot.jaxb.spi.Binding;

import org.junit.Before;
import org.junit.Test;

import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MetadataSourcesTest {
	private CacheableFileXmlSource cacheableFileXmlSource;
	private Binding binding;

	@Before
	public void setup() {
		cacheableFileXmlSource = mock( CacheableFileXmlSource.class );
		binding = mock( Binding.class );
		MockitoAnnotations.initMocks( this );
	}

	@Test
	public void addCacheableFileNewTest() {
		MetadataSources metadataSources = new MetadataSources();
		when( cacheableFileXmlSource.doBind( any() ) ).thenReturn( binding );
		metadataSources.addCacheableFileNew( cacheableFileXmlSource );
		assert ( metadataSources.getXmlBindings().get( 0 ).equals( binding ) );
	}
}