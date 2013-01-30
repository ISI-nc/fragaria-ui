package nc.isi.fragaria_reflection.utils;

public final class FragariaFileUtils {
	private FragariaFileUtils() {
	}

	public static String removeExtension(String fileName) {
		return fileName.substring(0, fileName.lastIndexOf('.'));
	}

}
