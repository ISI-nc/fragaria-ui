package nc.isi.fragaria_ui.services;

import nc.isi.fragaria_adapter_rewrite.dao.Session;
import nc.isi.fragaria_adapter_rewrite.dao.SessionManager;
import nc.isi.fragaria_adapter_rewrite.entities.AbstractEntity;
import nc.isi.fragaria_adapter_rewrite.entities.Entity;
import nc.isi.fragaria_adapter_rewrite.services.FragariaDomainModule;
import nc.isi.fragaria_reflection.services.ReflectionProvider;

import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.annotations.InjectService;
import org.apache.tapestry5.ioc.annotations.SubModule;
import org.apache.tapestry5.services.LibraryMapping;
import org.apache.tapestry5.services.ValueEncoderFactory;
import org.reflections.Reflections;

@SubModule(FragariaDomainModule.class)
public class FragariaUIModule {

	/**
	 * Contributes {@link ValueEncoderFactory}s for all registered Fragaria
	 * entity classes. Encoding and decoding are based on the id property value
	 * of the entity using type coercion. Hence, if the id can be coerced to a
	 * String and back then the entity can be coerced.
	 * 
	 * => inspired from hibernate module
	 */
	@SuppressWarnings("rawtypes")
	public static void contributeValueEncoderSource(
			MappedConfiguration<Class, ValueEncoderFactory> configuration,
			@InjectService("SessionManager") final SessionManager sessionManager,
			@InjectService("ReflectionProvider") final ReflectionProvider reflectionProvider) {
		final Session session = sessionManager.create();
		Reflections reflections = reflectionProvider.provide();
		for (final Class<? extends Entity> entityClass : reflections
				.getSubTypesOf(AbstractEntity.class)) {
			ValueEncoderFactory valueEncoderFactory = new ValueEncoderFactory() {
				@SuppressWarnings("unchecked")
				public ValueEncoder create(Class type) {
					return new EntityValueEncoder(entityClass, session);
				}
			};
			configuration.add(entityClass, valueEncoderFactory);
		}

	}

	public static void contributeComponentClassResolver(
			Configuration<LibraryMapping> configuration) {
		configuration.add(new LibraryMapping("fragaria", "nc.isi.fragaria_ui"));
	}

}
