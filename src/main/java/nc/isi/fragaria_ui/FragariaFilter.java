package nc.isi.fragaria_ui;

import javax.servlet.ServletException;

import nc.isi.fragaria_adapter_rewrite.utils.DefaultRegistry;

import org.apache.tapestry5.TapestryFilter;
import org.apache.tapestry5.ioc.Registry;

public class FragariaFilter extends TapestryFilter {

	@Override
	protected void init(Registry registry) throws ServletException {
		DefaultRegistry.setInstance(registry);
	}

}
