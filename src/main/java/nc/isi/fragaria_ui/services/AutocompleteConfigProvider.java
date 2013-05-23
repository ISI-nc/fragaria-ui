package nc.isi.fragaria_ui.services;



import java.util.Map;


public class AutocompleteConfigProvider {
	Map<String,AutocompleteConfig> autocompleteConfigurations;
	
	public AutocompleteConfigProvider(Map<String, AutocompleteConfig> autocompleteConfigurations) {
		this.autocompleteConfigurations = autocompleteConfigurations;
	}
	
	public AutocompleteConfig getAutocompleteConfig(String key){
		return autocompleteConfigurations.get(key);
	}
}
