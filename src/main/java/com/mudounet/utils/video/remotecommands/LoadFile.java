/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.utils.video.remotecommands;

/**
 *
 * @author gmanciet
 */
public class LoadFile extends Command {
    private static final long serialVersionUID = 1L;
    private String filePath;
    private boolean result;

    public LoadFile(String path) {
        this.filePath = path;
    }

    /**
     * Get the value of result
     *
     * @return the value of result
     */
    public boolean isResult() {
        return result;
    }

    /**
     * Set the value of result
     *
     * @param result new value of result
     */
    public void setResult(boolean result) {
        this.result = result;
    }


    /**
     * Get the value of filePath
     *
     * @return the value of filePath
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * Set the value of filePath
     *
     * @param filePath new value of filePath
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String toString() {
        return "LoadFile{" + "filePath=" + filePath + '}';
    }


}