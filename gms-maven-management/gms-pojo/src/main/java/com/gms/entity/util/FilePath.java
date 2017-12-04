package com.gms.entity.util;

/**
 * 保存在本地的文件modal
 * @author hery
 *
 */
public class FilePath {

	/**
	 * 文件比特流
	 */
	public byte[] fileByte;
	
	/**
	 * 文件名
	 */
	public String fileName;
	
	/**
	 * 保存地址根目录
	 */
	public String fileDirRootPath;
	
	/**
	 * 文件地址url前缀
	 */
	public String filePrefixUrl;
	
	/**
	 * 文件目录相对地址
	 */
	public String fileDirRelativePath;
	
	/**
	 * 文件目录绝对地址
	 */
	public String fileDirPath;
	
	/**
	 * 文件相对地址
	 */
	public String fileRelativePath;
	
	/**
	 * 文件绝对地址
	 */
	public String filePath;
	
	/**
	 * 文件完整路径
	 */
	public String fullUrl;
	
	public String getFullUrl() {
		return fullUrl;
	}

	public void setFullUrl(String fullUrl) {
		this.fullUrl = fullUrl;
	}

	public byte[] getFileByte() {
		return fileByte;
	}

	public void setFileByte(byte[] fileByte) {
		this.fileByte = fileByte;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileDirRootPath() {
		return fileDirRootPath;
	}

	public void setFileDirRootPath(String fileDirRootPath) {
		this.fileDirRootPath = fileDirRootPath;
	}

	public String getFilePrefixUrl() {
		return filePrefixUrl;
	}

	public void setFilePrefixUrl(String filePrefixUrl) {
		this.filePrefixUrl = filePrefixUrl;
	}

	public String getFileDirRelativePath() {
		return fileDirRelativePath;
	}

	public void setFileDirRelativePath(String fileDirRelativePath) {
		this.fileDirRelativePath = fileDirRelativePath;
	}

	public String getFileDirPath() {
		return fileDirPath;
	}

	public void setFileDirPath(String fileDirPath) {
		this.fileDirPath = fileDirPath;
	}

	public String getFileRelativePath() {
		return fileRelativePath;
	}

	public void setFileRelativePath(String fileRelativePath) {
		this.fileRelativePath = fileRelativePath;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	
	
}
