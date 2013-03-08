package nc.isi.fragaria_ui.services;

import nc.isi.fragaria_adapter_rewrite.entities.AbstractEntity;

import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.ioc.Messages;

public interface BeanModelBuilder {

	public <T extends AbstractEntity> BeanModel<T> createDisplayModel(
			Class<T> type, Messages messages, String name);

	public <T extends AbstractEntity> BeanModel<T> createEditModel(
			Class<T> type, Messages messages, String name);

}