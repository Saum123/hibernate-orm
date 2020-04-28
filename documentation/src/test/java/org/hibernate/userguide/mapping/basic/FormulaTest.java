/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.userguide.mapping.basic;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.Formula;
import org.hibernate.jpa.test.BaseEntityManagerFunctionalTestCase;

import org.junit.Test;

import static org.hibernate.testing.transaction.TransactionUtil.doInJPA;
import static org.junit.Assert.assertEquals;

/**
 * @author Vlad Mihalcea
 */
public class FormulaTest extends BaseEntityManagerFunctionalTestCase {

	@Override
	protected Class<?>[] getAnnotatedClasses() {
		return new Class<?>[] {
				Account.class, QuadraticEquation.class, AbsEquation.class, CeilEquation.class,
				FloorEquation.class, ExpEquation.class, LnEquation.class, ModEquation.class, PowerEquation.class
		};
	}

	@Test
	public void testLifecycle() {
		//tag::mapping-column-formula-persistence-example[]
		doInJPA( this::entityManagerFactory, entityManager -> {
			//tag::basic-datetime-temporal-date-persist-example[]
			Account account = new Account( );
			account.setId( 1L );
			account.setCredit( 5000d );
			account.setRate( 1.25 / 100 );
			entityManager.persist( account );
		} );

		doInJPA( this::entityManagerFactory, entityManager -> {
			Account account = entityManager.find( Account.class, 1L );
			assertEquals( Double.valueOf( 62.5d ), account.getInterest());
		} );
		//end::mapping-column-formula-persistence-example[]
	}

	@Test
	public void testLifecycleSQRT() {
		doInJPA( this::entityManagerFactory, entityManager -> {
			QuadraticEquation equation = new QuadraticEquation( );
			equation.setId( 1L );
			equation.setA( 2.0 );
			equation.setB( 5.0 );
			equation.setC( -3.0 );
			entityManager.persist( equation );
		} );

		doInJPA( this::entityManagerFactory, entityManager -> {
			QuadraticEquation equation = entityManager.find( QuadraticEquation.class, 1L );
			assertEquals( Double.valueOf( 0.5 ), equation.getRoot1() );
			assertEquals( Double.valueOf( -3.0 ), equation.getRoot2() );
		} );
	}

	@Test
	public void testLifecycleABS() {
		doInJPA( this::entityManagerFactory, entityManager -> {
			AbsEquation equation = new AbsEquation( );
			equation.setId( 1L );
			equation.setInput( -5.0 );
			entityManager.persist( equation );
		} );

		doInJPA( this::entityManagerFactory, entityManager -> {
			AbsEquation equation = entityManager.find( AbsEquation.class, 1L );
			assertEquals( Double.valueOf( 5.0 ), equation.getOutput() );
		} );
	}

	@Test
	public void testLifecycleCEIL() {
		doInJPA( this::entityManagerFactory, entityManager -> {
			CeilEquation equation = new CeilEquation( );
			equation.setId( 1L );
			equation.setInput( 1.23 );
			entityManager.persist( equation );
		} );

		doInJPA( this::entityManagerFactory, entityManager -> {
			CeilEquation equation = entityManager.find( CeilEquation.class, 1L );
			assertEquals( Double.valueOf( 2.0 ), equation.getOutput() );
		} );
	}

	@Test
	public void testLifecycleFLOOR() {
		doInJPA( this::entityManagerFactory, entityManager -> {
			FloorEquation equation = new FloorEquation( );
			equation.setId( 1L );
			equation.setInput( 1.23 );
			entityManager.persist( equation );
		} );

		doInJPA( this::entityManagerFactory, entityManager -> {
			FloorEquation equation = entityManager.find( FloorEquation.class, 1L );
			assertEquals( Double.valueOf( 1.0 ), equation.getOutput() );
		} );
	}

	@Test
	public void testLifecycleEXP() {
		doInJPA( this::entityManagerFactory, entityManager -> {
			ExpEquation equation = new ExpEquation( );
			equation.setId( 1L );
			equation.setInput( 2.0 );
			entityManager.persist( equation );
		} );

		doInJPA( this::entityManagerFactory, entityManager -> {
			ExpEquation equation = entityManager.find( ExpEquation.class, 1L );
			assertEquals( Double.valueOf( 7.3890560989306504 ), equation.getOutput() );
		} );
	}

	@Test
	public void testLifecycleLN() {
		doInJPA( this::entityManagerFactory, entityManager -> {
			LnEquation equation = new LnEquation( );
			equation.setId( 1L );
			equation.setInput( 2.0 );
			entityManager.persist( equation );
		} );

		doInJPA( this::entityManagerFactory, entityManager -> {
			LnEquation equation = entityManager.find( LnEquation.class, 1L );
			assertEquals( Double.valueOf( 0.6931471805599453 ), equation.getOutput() );
		} );
	}

	@Test
	public void testLifecycleMOD() {
		doInJPA( this::entityManagerFactory, entityManager -> {
			ModEquation equation = new ModEquation( );
			equation.setId( 1L );
			equation.setInput1( 3.0 );
			equation.setInput2( 2.0 );
			entityManager.persist( equation );
		} );

		doInJPA( this::entityManagerFactory, entityManager -> {
			ModEquation equation = entityManager.find( ModEquation.class, 1L );
			assertEquals( Double.valueOf( 1.0 ), equation.getOutput() );
		} );
	}

	@Test
	public void testLifecyclePOWER() {
		doInJPA( this::entityManagerFactory, entityManager -> {
			PowerEquation equation = new PowerEquation( );
			equation.setId( 1L );
			equation.setBase( 2.0 );
			equation.setExponent( 5.0 );
			entityManager.persist( equation );
		} );

		doInJPA( this::entityManagerFactory, entityManager -> {
			PowerEquation equation = entityManager.find( PowerEquation.class, 1L );
			assertEquals( Double.valueOf( 32.0 ), equation.getOutput() );
		} );
	}

	//tag::mapping-column-formula-example[]
	@Entity(name = "Account")
	public static class Account {

		@Id
		private Long id;

		private Double credit;

		private Double rate;

		@Formula(value = "credit * rate")
		private Double interest;

		//Getters and setters omitted for brevity

		//end::mapping-column-formula-example[]
		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Double getCredit() {
			return credit;
		}

		public void setCredit(Double credit) {
			this.credit = credit;
		}

		public Double getRate() {
			return rate;
		}

		public void setRate(Double rate) {
			this.rate = rate;
		}

		public Double getInterest() {
			return interest;
		}

		public void setInterest(Double interest) {
			this.interest = interest;
		}

		//tag::mapping-column-formula-example[]
	}
	//end::mapping-column-formula-example[]

	@Entity(name = "QuadraticEquation")
	public static class QuadraticEquation{

		@Id
		private Long id;

		private Double a;

		private Double b;

		private Double c;

		@Formula(value = "(((-1 * b) + SQRT((b * b) - (4 * a * c))) / (2 * a))")
		private Double root1;

		@Formula(value = "(((-1 * b) - SQRT((b * b) - (4 * a * c))) / (2 * a))")
		private Double root2;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Double getA() {
			return a;
		}

		public void setA(Double a) {
			this.a = a;
		}

		public Double getB() {
			return b;
		}

		public void setB(Double b) {
			this.b = b;
		}

		public Double getC() {
			return c;
		}

		public void setC(Double c) {
			this.c = c;
		}

		public Double getRoot1() {
			return root1;
		}

		public void setRoot1(Double root1) {
			this.root1 = root1;
		}

		public Double getRoot2() {
			return root2;
		}

		public void setRoot2(Double root2) {
			this.root2 = root2;
		}
	}

	@Entity(name = "AbsEquation")
	public static class AbsEquation{
		@Id
		private Long id;

		private Double input;

		@Formula(value = "(ABS(input))")
		private Double output;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Double getInput() {
			return input;
		}

		public void setInput(Double input) {
			this.input = input;
		}

		public Double getOutput() {
			return output;
		}

		public void setOutput(Double output) {
			this.output = output;
		}
	}

	@Entity(name = "CeilEquation")
	public static class CeilEquation{
		@Id
		private Long id;

		private Double input;

		@Formula(value = "(CEIL(input))")
		private Double output;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Double getInput() {
			return input;
		}

		public void setInput(Double input) {
			this.input = input;
		}

		public Double getOutput() {
			return output;
		}

		public void setOutput(Double output) {
			this.output = output;
		}
	}

	@Entity(name = "FloorEquation")
	public static class FloorEquation{
		@Id
		private Long id;

		private Double input;

		@Formula(value = "(FLOOR(input))")
		private Double output;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Double getInput() {
			return input;
		}

		public void setInput(Double input) {
			this.input = input;
		}

		public Double getOutput() {
			return output;
		}

		public void setOutput(Double output) {
			this.output = output;
		}
	}

	@Entity(name = "ExpEquation")
	public static class ExpEquation{
		@Id
		private Long id;

		private Double input;

		@Formula(value = "(EXP(input))")
		private Double output;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Double getInput() {
			return input;
		}

		public void setInput(Double input) {
			this.input = input;
		}

		public Double getOutput() {
			return output;
		}

		public void setOutput(Double output) {
			this.output = output;
		}
	}

	@Entity(name = "LnEquation")
	public static class LnEquation{
		@Id
		private Long id;

		private Double input;

		@Formula(value = "(LN(input))")
		private Double output;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Double getInput() {
			return input;
		}

		public void setInput(Double input) {
			this.input = input;
		}

		public Double getOutput() {
			return output;
		}

		public void setOutput(Double output) {
			this.output = output;
		}
	}

	@Entity(name = "ModEquation")
	public static class ModEquation{
		@Id
		private Long id;

		private Double input1;

		private Double input2;

		@Formula(value = "(MOD(input1, input2))")
		private Double output;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Double getInput1() {
			return input1;
		}

		public void setInput1(Double input1) {
			this.input1 = input1;
		}

		public Double getInput2() {
			return input2;
		}

		public void setInput2(Double input2) {
			this.input2 = input2;
		}

		public Double getOutput() {
			return output;
		}

		public void setOutput(Double output) {
			this.output = output;
		}
	}

	@Entity(name = "PowerEquation")
	public static class PowerEquation{
		@Id
		private Long id;

		private Double base;

		private Double exponent;

		@Formula(value = "(POWER(base, exponent))")
		private Double output;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Double getBase() {
			return base;
		}

		public void setBase(Double base) {
			this.base = base;
		}

		public Double getExponent() {
			return exponent;
		}

		public void setExponent(Double exponent) {
			this.exponent = exponent;
		}

		public Double getOutput() {
			return output;
		}

		public void setOutput(Double output) {
			this.output = output;
		}
	}

}
