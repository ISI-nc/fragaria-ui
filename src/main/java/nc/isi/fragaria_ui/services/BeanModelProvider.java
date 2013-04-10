package nc.isi.fragaria_ui.services;

import java.util.concurrent.ExecutionException;

import nc.isi.fragaria_adapter_rewrite.entities.AbstractEntity;

import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class BeanModelProvider {
	
	@Inject
	private BeanModelBuilder beanModelBuilder;
	
	@Inject
	private Messages messages;
	
	private final LoadingCache<InnerModelIdentifier, BeanModel<? extends AbstractEntity>> editModelCache = CacheBuilder
			.newBuilder()
			.build(new CacheLoader<InnerModelIdentifier, BeanModel<? extends AbstractEntity>>() {

				@Override
				public BeanModel<? extends AbstractEntity> load(
						InnerModelIdentifier key) throws Exception {
					return (BeanModel<? extends AbstractEntity>) beanModelBuilder.
							createEditModel(key.getType(), messages,key.getModelName());
				}

			});
	
	public BeanModel<? extends AbstractEntity> getModel(Class<? extends AbstractEntity> type,String modelName)
			throws ExecutionException {
			return editModelCache.get(new InnerModelIdentifier(modelName, type));
	}
	
	private class InnerModelIdentifier {
        private final String modelName;
    	private final Class<? extends AbstractEntity> type;

        public InnerModelIdentifier(String modelName,
				Class<? extends AbstractEntity> type) {
			super();
			this.modelName = modelName;
			this.type = type;
		}

		public String getModelName() {
			return modelName;
		}

		public Class<? extends AbstractEntity> getType() {
			return type;
		}

    }
}
