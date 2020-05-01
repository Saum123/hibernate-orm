/*
 *
 *  * Hibernate, Relational Persistence for Idiomatic Java
 *  *
 *  * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 *  * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 *
 */

package org.hibernate.test.ops;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.Transaction;

import org.hibernate.testing.DialectChecks;
import org.hibernate.testing.RequiresDialectFeature;
import org.junit.Test;

@RequiresDialectFeature(DialectChecks.SupportsNoColumnInsert.class)
public class FunctionalModelTest extends AbstractOperationTestCase {

	@Test
	public void testInsertNewObjects() {
		clearCounts();

		Session s = openSession();
		Transaction tx = s.beginTransaction();
		Node root = new Node("root");
		Node childLevel1 = new Node("childLevel1");
		childLevel1.setDescription( "Child at level 1" );
		root.addChild( childLevel1 );
		s.persist( root );
		tx.commit();
		s.close();

		assertInsertCount( 2 );
		assertUpdateCount( 0 );
		assertDeleteCount( 0 );
		clearCounts();

		s = openSession();
		root = s.get( Node.class, "root" );
		assert (root.getAllChildrenIncludingSelf().size() == 2);
		s.close();

		s = openSession();
		tx = s.beginTransaction();
		s.delete( childLevel1 );
		s.delete( root );
		tx.commit();
		s.close();
	}

	@Test
	public void testUpdateObjects() {
		clearCounts();

		Session s = openSession();
		Transaction tx = s.beginTransaction();
		Node root = new Node("root");
		Node childLevel1 = new Node("childLevel1");
		childLevel1.setDescription( "Child at level 1" );
		root.addChild( childLevel1 );
		s.persist( root );
		tx.commit();
		s.close();

		clearCounts();

		s = openSession();
		tx = s.beginTransaction();
		childLevel1 = s.get( Node.class, "childLevel1" );
		childLevel1.setDescription( "Child at level 1 updated once" );
		s.saveOrUpdate( childLevel1 );
		tx.commit();
		s.close();

		assertInsertCount( 0 );
		assertUpdateCount( 1 );
		assertDeleteCount( 0 );
		clearCounts();

		s = openSession();
		tx = s.beginTransaction();
		childLevel1.setDescription( "Child at level 1 updated twice" );
		s.saveOrUpdate( childLevel1 );
		tx.commit();
		s.close();

		assertInsertCount( 0 );
		assertUpdateCount( 1 );
		assertDeleteCount( 0 );
		clearCounts();

		s = openSession();
		root = (Node) s.get( Node.class, "root" );
		assert (root.getAllChildrenIncludingSelf().size() == 2);
		childLevel1 = s.get( Node.class, "childLevel1" );
		assert (childLevel1.getDescription().equals( "Child at level 1 updated twice" ));
		tx = s.beginTransaction();
		s.delete( childLevel1 );
		s.delete( root );
		tx.commit();
		s.close();
	}

	@Test
	public void testInsertAndUpdateObjects() {
		clearCounts();

		Session s = openSession();
		Transaction tx = s.beginTransaction();
		Node root = new Node("root");
		Node childLevel1 = new Node("childLevel1");
		childLevel1.setDescription( "Child at level 1 updated twice" );
		root.addChild( childLevel1 );
		s.persist( root );
		tx.commit();
		s.close();

		clearCounts();

		childLevel1.setDescription( "Child at level 1 updated thrice" );
		Node childLevel2 = new Node("childLevel2");
		childLevel2.setDescription( "Child at level 2" );
		childLevel1.addChild( childLevel2 );

		s = openSession();
		tx = s.beginTransaction();
		s.saveOrUpdate( root );
		tx.commit();
		s.close();

		assertInsertCount( 1 );
		assertUpdateCount( 1 );
		assertDeleteCount( 0 );
		clearCounts();

		s = openSession();
		root = (Node) s.get( Node.class, "root" );
		assert (root.getAllChildrenIncludingSelf().size() == 3);
		childLevel1 = s.get( Node.class, "childLevel1" );
		assert (childLevel1.getDescription().equals( "Child at level 1 updated thrice" ));
		childLevel2 = s.get( Node.class, "childLevel2" );
		assert (childLevel2.getDescription().equals( "Child at level 2" ));
		tx = s.beginTransaction();
		s.delete( childLevel2 );
		s.delete( childLevel1 );
		s.delete( root );
		tx.commit();
		s.close();
	}

	@Test
	public void testInsertAndDeleteObjects() {
		clearCounts();

		Session s = openSession();
		Transaction tx = s.beginTransaction();
		Node root = new Node("root");
		Node childLevel1 = new Node("childLevel1");
		childLevel1.setDescription( "Child at level 1 updated thrice" );
		Node childLevel2 = new Node("childLevel2");
		childLevel2.setDescription( "Child at level 2" );
		childLevel1.addChild( childLevel2 );
		root.addChild( childLevel1 );
		s.persist( root );
		tx.commit();
		s.close();

		clearCounts();

		s = openSession();
		tx = s.beginTransaction();
		Node newChildLevel2 = new Node("newChildLevel2");
		newChildLevel2.setDescription( "New child at level 2" );
		Set<Node> childSet = new HashSet<>();
		childLevel1.setChildren( childSet );
		childLevel1.addChild( newChildLevel2 );
		s.delete( childLevel2 );
		s.saveOrUpdate( root );
		tx.commit();
		s.close();

		assertInsertCount( 1 );
		assertUpdateCount( 0 );
		assertDeleteCount( 1 );
		clearCounts();

		s = openSession();
		root = (Node) s.get( Node.class, "root" );
		assert (root.getAllChildrenIncludingSelf().size() == 3);
		childLevel1 = s.get( Node.class, "childLevel1" );
		assert (childLevel1.getDescription().equals( "Child at level 1 updated thrice" ));
		newChildLevel2 = s.get( Node.class, "newChildLevel2" );
		assert (newChildLevel2.getDescription().equals( "New child at level 2" ));
		tx = s.beginTransaction();
		s.delete( newChildLevel2 );
		s.delete( childLevel1 );
		s.delete( root );
		tx.commit();
		s.close();
	}

	@Test
	public void testUpdateAndDeleteObjects() {
		clearCounts();

		Session s = openSession();
		Transaction tx = s.beginTransaction();
		Node root = new Node("root");
		Node childLevel1 = new Node("childLevel1");
		childLevel1.setDescription( "Child at level 1 updated thrice" );
		Node newChildLevel2 = new Node("newChildLevel2");
		newChildLevel2.setDescription( "New child at level 2" );
		childLevel1.addChild( newChildLevel2 );
		root.addChild( childLevel1 );
		s.persist( root );
		tx.commit();
		s.close();

		clearCounts();

		s = openSession();
		tx = s.beginTransaction();
		childLevel1.setDescription( "Child at level 1 updated four times" );
		Set<Node> childSet = new HashSet<>();
		childLevel1.setChildren( childSet );
		s.delete( newChildLevel2 );
		s.saveOrUpdate( root );
		tx.commit();
		s.close();

		assertInsertCount( 0 );
		assertUpdateCount( 1 );
		assertDeleteCount( 1 );
		clearCounts();

		s = openSession();
		root = (Node) s.get( Node.class, "root" );
		assert (root.getAllChildrenIncludingSelf().size() == 2);
		childLevel1 = s.get( Node.class, "childLevel1" );
		assert (childLevel1.getDescription().equals( "Child at level 1 updated four times" ));
		tx = s.beginTransaction();
		s.delete( childLevel1 );
		s.delete( root );
		tx.commit();
		s.close();
	}

	@Test
	public void testDeleteObjects() {
		clearCounts();

		Session s = openSession();
		Transaction tx = s.beginTransaction();
		Node root = new Node("root");
		Node childLevel1 = new Node("childLevel1");
		childLevel1.setDescription( "Child at level 1 updated four times" );
		root.addChild( childLevel1 );
		s.persist( root );
		tx.commit();
		s.close();

		clearCounts();

		s = openSession();
		tx = s.beginTransaction();
		Set<Node> childSet = new HashSet<>();
		root.setChildren( childSet );
		s.delete( childLevel1 );
		s.saveOrUpdate( root );
		tx.commit();
		s.close();

		assertInsertCount( 0 );
		assertUpdateCount( 0 );
		assertDeleteCount( 1 );
		clearCounts();

		s = openSession();
		root = (Node) s.get( Node.class, "root" );
		assert (root.getAllChildrenIncludingSelf().size() == 1);
		tx = s.beginTransaction();
		s.delete( root );
		tx.commit();
		s.close();
	}
}