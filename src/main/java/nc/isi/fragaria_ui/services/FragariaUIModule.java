package nc.isi.fragaria_ui.services;

import java.math.BigDecimal;

import nc.isi.fragaria_adapter_rewrite.entities.AbstractEntity;
import nc.isi.fragaria_adapter_rewrite.entities.Entity;
import nc.isi.fragaria_adapter_rewrite.services.FragariaDomainModule;
import nc.isi.fragaria_reflection.services.ReflectionProvider;
import nc.isi.fragaria_ui.utils.JodaTimeUtil;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.InjectService;
import org.apache.tapestry5.ioc.annotations.SubModule;
import org.apache.tapestry5.ioc.services.ApplicationDefaults;
import org.apache.tapestry5.ioc.services.Coercion;
import org.apache.tapestry5.ioc.services.CoercionTuple;
import org.apache.tapestry5.ioc.services.SymbolProvider;
import org.apache.tapestry5.services.BeanBlockContribution;
import org.apache.tapestry5.services.DisplayBlockContribution;
import org.apache.tapestry5.services.EditBlockContribution;
import org.apache.tapestry5.services.LibraryMapping;
import org.apache.tapestry5.services.ValueEncoderFactory;
import org.apache.tapestry5.services.compatibility.Compatibility;
import org.apache.tapestry5.services.compatibility.Trait;
import org.joda.time.DateTime;
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
			@InjectService("ReflectionProvider") final ReflectionProvider reflectionProvider) {
		Reflections reflections = reflectionProvider.provide();
		for (final Class<? extends Entity> entityClass : reflections
				.getSubTypesOf(AbstractEntity.class)) {
			ValueEncoderFactory valueEncoderFactory = new ValueEncoderFactory() {
				@SuppressWarnings("unchecked")
				public ValueEncoder create(Class type) {
					return new EntityValueEncoder(entityClass);
				}
			};
			configuration.add(entityClass, valueEncoderFactory);
		}

	}

	public static void contributeComponentClassResolver(
			Configuration<LibraryMapping> configuration) {
		configuration.add(new LibraryMapping("fragaria", "nc.isi.fragaria_ui"));
	}

	public static void contributeDefaultDataTypeAnalyzer(
			@SuppressWarnings("rawtypes") MappedConfiguration<Class, String> configuration) {
		configuration.add(DateTime.class, "dateTime");
		configuration.add(BigDecimal.class, "bigDecimal");
	}

	public static void contributeBeanBlockSource(
			Configuration<BeanBlockContribution> configuration) {
		configuration.add(new DisplayBlockContribution("dateTime",
				"fragaria/JodaTimeDisplayBlocks", "dateTime"));
		configuration.add(new EditBlockContribution("dateTime",
				"fragaria/JodaTimePropertyEditBlocks", "dateTime"));
		configuration.add(new DisplayBlockContribution("bigDecimal",
				"fragaria/BigDecimalPropertyDisplayBlocks", "bigDecimal"));
		configuration.add(new EditBlockContribution("bigDecimal",
				"fragaria/BigDecimalPropertyEditBlocks", "bigDecimal"));
	}

	@SuppressWarnings("rawtypes")
	public static void contributeTypeCoercer(
			Configuration<CoercionTuple> configuration) {

		Coercion<java.util.Date, DateTime> toDateTime = new Coercion<java.util.Date, DateTime>() {
			public DateTime coerce(java.util.Date input) {
				return JodaTimeUtil.toDateTime(input);
			}
		};

		configuration.add(new CoercionTuple<java.util.Date, DateTime>(
				java.util.Date.class, DateTime.class, toDateTime));

		Coercion<DateTime, java.util.Date> fromDateTime = new Coercion<DateTime, java.util.Date>() {
			public java.util.Date coerce(DateTime input) {
				return JodaTimeUtil.toJavaDate(input);
			}
		};

		configuration.add(new CoercionTuple<DateTime, java.util.Date>(
				DateTime.class, java.util.Date.class, fromDateTime));
	}

	@Contribute(SymbolProvider.class)
	@ApplicationDefaults
	public static void switchProviderToJQuery(
			MappedConfiguration<String, Object> configuration) {
		configuration.add(SymbolConstants.JAVASCRIPT_INFRASTRUCTURE_PROVIDER,
				"jquery");
	}

	@Contribute(Compatibility.class)
	public static void disableScriptaculous(
			MappedConfiguration<Trait, Boolean> configuration) {
		configuration.add(Trait.SCRIPTACULOUS, false);
		configuration.add(Trait.INITIALIZERS, false);
	}

}
