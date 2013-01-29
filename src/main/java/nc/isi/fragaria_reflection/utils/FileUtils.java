package nc.isi.fragaria_reflection.utils;

public final class FileUtils {
	private FileUtils() {
	}

	public static String removeExtension(String fileName) {
		return fileName.substring(0, fileName.lastIndexOf('.'));
	}

}
