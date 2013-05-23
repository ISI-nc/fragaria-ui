package nc.isi.fragaria_ui.services;

import java.util.Collection;
import java.util.Map;

import nc.isi.fragaria_adapter_rewrite.dao.SearchQuery;
import nc.isi.fragaria_adapter_rewrite.entities.AbstractEntity;

public interface AutocompleteConfig<T extends AbstractEntity> {
	public int getLimit();
	public Class<T> getType();
	public SearchQuery<T> getSearchQuery(String partial);
	public Map<String,T> getResultsWithLabel(Collection<T> results);
}
