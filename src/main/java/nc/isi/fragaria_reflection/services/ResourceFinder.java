package nc.isi.fragaria_reflection.services;

import java.io.File;
import java.util.Set;

public interface ResourceFinder {
	/**
	 * Cherche les fichiers correspondant à @param regExp dans les packages
	 * définis par contributeResourceFinder dans le module Tapestry
	 * 
	 * @return
	 */
	Set<File> getResourcesMatching(String regExp);
}
