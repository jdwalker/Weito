package weito;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileAccess {

	private List<String> fileLocs;
	private final String fileformat;
	private final boolean recursive;

	public List<String> getFilesFromDir(String folderLocation) {
		String fl = folderLocation;
		fileLocs = new ArrayList<String>();
		try {
			File file = new File(fl);
			if( file.exists() ) {
				fileLocs = recursiveGetFilesFromDir( file );
			}
		} catch (Exception e) {
			return fileLocs;
		}
		return fileLocs;
	}

	private List<String> recursiveGetFilesFromDir(
			File dir) throws Exception {
		for( File file : dir.listFiles() ) {
			if( file.isDirectory() && recursive ) {
				recursiveGetFilesFromDir( file);
			} else if( file.isFile() && fileHasExtension(file) ) {
				fileLocs.add( file.getAbsolutePath() );
			}
		}
		return fileLocs;
	}

	private boolean fileHasExtension(File file) {	
		String name = file.getName();
		return name.substring( name.lastIndexOf('.') + 1 ).equalsIgnoreCase(fileformat);
	}

	public FileAccess(String threeletterfileformat, boolean recursiveFileSearch) {
		this.fileformat = threeletterfileformat;
		this.recursive = recursiveFileSearch;
	}

}
