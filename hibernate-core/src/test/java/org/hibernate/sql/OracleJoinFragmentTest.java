/*
 *
 *  * Hibernate, Relational Persistence for Idiomatic Java
 *  *
 *  * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 *  * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 *
 */

package org.hibernate.sql;

import java.lang.reflect.Field;

import org.junit.Test;

import static org.junit.Assert.*;

public class OracleJoinFragmentTest {
	@Test
	public void addJoinTest() throws NoSuchFieldException, IllegalAccessException {
		Field afterFromField = OracleJoinFragment.class.getDeclaredField( "afterFrom" );
		Field afterWhereField = OracleJoinFragment.class.getDeclaredField( "afterWhere" );
		afterFromField.setAccessible( true );
		afterWhereField.setAccessible( true );

		OracleJoinFragment ojf = new OracleJoinFragment();
		String tableName = "TABLE_NAME";
		String alias = "tn";
		String[] fkColumns = new String[] {"FK_COLUMN1", "FK_COLUMN2"};
		String[] pkColumns = new String[] {"PK_COLUMN1", "PK_COLUMN2"};
		JoinType joinType = JoinType.FULL_JOIN;
		ojf.addJoin( tableName, alias, fkColumns, pkColumns, joinType );

		StringBuilder afterFrom = (StringBuilder) afterFromField.get( ojf );
		StringBuilder afterWhere = (StringBuilder) afterWhereField.get( ojf );
		assertEquals( ", TABLE_NAME tn", afterFrom.toString() );
		assertEquals( " and FK_COLUMN1(+)=tn.PK_COLUMN1(+) and FK_COLUMN2(+)=tn.PK_COLUMN2(+)", afterWhere.toString() );

		afterFromField.setAccessible( false );
		afterWhereField.setAccessible( false );
	}

	@Test
	public void addJoinTest2DFKColumns() throws NoSuchFieldException, IllegalAccessException {
		Field afterFromField = OracleJoinFragment.class.getDeclaredField( "afterFrom" );
		Field afterWhereField = OracleJoinFragment.class.getDeclaredField( "afterWhere" );
		afterFromField.setAccessible( true );
		afterWhereField.setAccessible( true );

		OracleJoinFragment ojf = new OracleJoinFragment();
		String tableName = "TABLE_NAME";
		String alias = "tn";
		String[][] fkColumns = new String[][] {{"FK_COLUMN_11", "FK_COLUMN_12"}, {"FK_COLUMN_21", "FK_COLUMN_22"}};
		String[] pkColumns = new String[] {"PK_COLUMN1", "PK_COLUMN2"};
		JoinType joinType = JoinType.FULL_JOIN;
		ojf.addJoin( tableName, alias, fkColumns, pkColumns, joinType );

		StringBuilder afterFrom = (StringBuilder) afterFromField.get( ojf );
		StringBuilder afterWhere = (StringBuilder) afterWhereField.get( ojf );
		assertEquals( ", TABLE_NAME tn", afterFrom.toString() );
		assertEquals( "( and FK_COLUMN_11(+)=tn.PK_COLUMN1(+) and FK_COLUMN_12(+)=tn.PK_COLUMN2(+) or  and FK_COLUMN_21(+)=tn.PK_COLUMN1(+) and FK_COLUMN_22(+)=tn.PK_COLUMN2(+))", afterWhere.toString() );

		afterFromField.setAccessible( false );
		afterWhereField.setAccessible( false );
	}

	@Test
	public void addConditionTest() {
		OracleJoinFragment ojf = new OracleJoinFragment();
		String alias = "tn";
		String[] columns = {"COL_1", "COL_2", "COL_3"};
		String condition = " > 1";
		ojf.addCondition(alias, columns, condition);
		assertEquals( " and tn.COL_1 > 1 and tn.COL_2 > 1 and tn.COL_3 > 1", ojf.toWhereFragmentString() );
	}
}