/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.userguide.collections;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.jpa.test.BaseEntityManagerFunctionalTestCase;

import org.junit.Test;

import static org.hibernate.userguide.util.TransactionUtil.doInJPA;

/**
 * @author Vlad Mihalcea
 */
public class UnidirectionalBagTest extends BaseEntityManagerFunctionalTestCase {

	@Override
	protected Class<?>[] getAnnotatedClasses() {
		return new Class<?>[] {
				Person.class,
				Phone.class,
		};
	}

	@Test
	public void testLifecycle() {
		doInJPA( this::entityManagerFactory, entityManager -> {
			//tag::collections-unidirectional-bag-lifecycle-example[]
			Person person = new Person( 1L );
			person.getPhones().add( new Phone( 1L, "landline", "028-234-9876" ) );
			person.getPhones().add( new Phone( 2L, "mobile", "072-122-9876" ) );
			entityManager.persist( person );
			//end::collections-unidirectional-bag-lifecycle-example[]
		} );
		doInJPA( this::entityManagerFactory, entityManager -> {
			Person person = entityManager.find( Person.class, 1L );
			person.getPhones().remove( 0 );
		} );
	}

	//tag::collections-unidirectional-bag-example[]
	@Entity(name = "Person")
	public static class Person {

		@Id
		private Long id;
		@OneToMany(cascade = CascadeType.ALL)
		private List<Phone> phones = new ArrayList<>();

		public Person() {
		}

		public Person(Long id) {
			this.id = id;
		}

		public List<Phone> getPhones() {
			return phones;
		}
	}

	@Entity(name = "Phone")
	public static class Phone {

		@Id
		private Long id;

		private String type;

		private String number;

		public Phone() {
		}

		public Phone(Long id, String type, String number) {
			this.id = id;
			this.type = type;
			this.number = number;
		}

		public Long getId() {
			return id;
		}

		public String getType() {
			return type;
		}

		public String getNumber() {
			return number;
		}
	}
	//end::collections-unidirectional-bag-example[]
}