package nc.isi.fragaria_ui.services;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

import java.util.Collection;
import java.util.Map;

import nc.isi.fragaria_adapter_rewrite.entities.AbstractEntity;
import nc.isi.fragaria_adapter_rewrite.entities.EntityMetadata;

import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.services.BeanModelSource;

@SuppressWarnings("rawtypes")
public class BeanModelBuilderImpl implements BeanModelBuilder {
	private final BeanModelSource beanModelSource;
	private final Map<String, BeanModelDefinition> beanModelDefinitions;

	public BeanModelBuilderImpl(BeanModelSource beanModelSource,
			Map<String, BeanModelDefinition> beanModelDefinitions) {
		super();
		this.beanModelSource = beanModelSource;
		this.beanModelDefinitions = beanModelDefinitions;
	}

	@Override
	public <T extends AbstractEntity> BeanModel<T> createDisplayModel(
			Class<T> type, Messages messages, String name) {
		BeanModel<T> beanModel = beanModelSource.createDisplayModel(type,
				messages);
		updateBeanModel(type, name, beanModel);
		return beanModel;
	}

	@SuppressWarnings("unchecked")
	protected <T extends AbstractEntity> void updateBeanModel(Class<T> type,
			String name, BeanModel<T> beanModel) {
		if (name != null) {
			BeanModelDefinition<T> beanModelDefinition = (BeanModelDefinition<T>) beanModelDefinitions
					.get(name);
			checkArgument(beanModelDefinition != null,
					"aucune beanModelDefinition trouvée pour : %s", name);
			checkState(
					beanModelDefinition.beanClass().equals(type),
					"la beanDefinition (type : %s) n'est pas du type attendu (%s)",
					beanModelDefinition.beanClass(), type);
			EntityMetadata entityMetadata = new EntityMetadata(type);
			if (beanModelDefinition.exclude() != null) {
				beanModel.exclude(beanModelDefinition.exclude());
			}
			if (beanModelDefinition.add() != null) {
				for (String property : beanModelDefinition.add()) {
					String dataType = Collection.class
							.isAssignableFrom(entityMetadata
									.propertyType(property)) ? property
							: entityMetadata.propertyType(property)
									.getSimpleName();
					beanModel.add(property).dataType(dataType);
				}
			}
			if (beanModelDefinition.reOrder() != null) {
				beanModel.reorder(beanModelDefinition.reOrder());
			}
		}
	}

	@Override
	public <T extends AbstractEntity> BeanModel<T> createEditModel(
			Class<T> type, Messages messages, String name) {
		BeanModel<T> beanModel = beanModelSource
				.createEditModel(type, messages);
		updateBeanModel(type, name, beanModel);
		return beanModel;
	}

}
