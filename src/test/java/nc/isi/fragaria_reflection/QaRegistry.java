package nc.isi.fragaria_reflection;

import nc.isi.fragaria_reflection.services.FragariaReflectionModule;

import org.apache.tapestry5.ioc.Registry;
import org.apache.tapestry5.ioc.RegistryBuilder;

public enum QaRegistry {
	INSTANCE;

	private final Registry registry = RegistryBuilder
			.buildAndStartupRegistry(FragariaReflectionModule.class);

	public Registry getRegistry() {
		return registry;
	}

}
