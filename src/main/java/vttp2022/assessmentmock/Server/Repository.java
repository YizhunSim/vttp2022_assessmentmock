package vttp2022.assessmentmock.Server;

import java.io.File;

public class Repository {
  private File repository;

  public Repository(String repo){
    System.out.println("Repository: " +repo);
    this.repository = new File(repo);
  }

  public File[] retrieveFiles(){
    return this.repository.listFiles();
  }

  public String getRepositoryAbsolutePath(String path){
    return this.repository.getAbsolutePath() + path;
  }

  public boolean isPathExists(){
    // System.out.println("Repository - isPathExist: " + this.repository.getAbsolutePath());
    return this.repository.exists();
  }

  public boolean isPathDirectory(){
    // System.out.println("Repository - isPathDirectory: " + this.repository.getAbsolutePath());
    return this.repository.isDirectory();
  }

  public boolean isPathReadable(){
    // System.out.println("Repository - isPathReadable: " + this.repository.getAbsolutePath());
    return this.repository.canRead();
  }
}
