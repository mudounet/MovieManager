/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.utils.managers;

import com.mudounet.hibernate.movies.GenericMovie;
import com.mudounet.hibernate.movies.others.Snapshot;
import com.mudounet.hibernate.movies.others.TechData;
import com.mudounet.utils.video.RemotePlayer;
import com.mudounet.utils.video.RemotePlayerFactory;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.apache.log4j.Logger;

/**
 *
 * @author isabelle
 */
public class MovieToolManager {

    protected static Logger logger = Logger.getLogger(MovieToolManager.class.getName());

    public static TechData getMovieInformations(GenericMovie movie) throws InterruptedException, IOException {
        TechData techData = new TechData();

        File file = new File(movie.getPath());
        techData.setSize(file.length());

        logger.info(techData);

        return null;
    }

    /**
     * Generate a set of snapshots from Movie.
     * @param movie
     * @param directory
     * @param nbOfSnapshots
     * @return found snapshots
     * @throws Exception
     */
    public static Set<Snapshot> genSnapshots(GenericMovie movie, File directory, int nbOfSnapshots) throws Exception {
        Set<Snapshot> list = new HashSet<Snapshot>();

        if(!directory.isDirectory()) {
            throw new Exception("Directory doesn't exists");
        }
        if(nbOfSnapshots < 1 || nbOfSnapshots > 9) {
            throw new Exception("Number of snapshots has to be defined between 1 and 9.");
        }

        String prefix = movie.getMd5()+"-";

                RemotePlayer headlessRemotePlayer = RemotePlayerFactory.getHeadlessRemotePlayer();
                headlessRemotePlayer.load("src/test/resources/sample_video.flv");
                headlessRemotePlayer.play();

                long length = headlessRemotePlayer.getLength();

                for (long i = 1; i <= nbOfSnapshots; i++) {
                    Snapshot s = new Snapshot();
            s.setPath(directory.getAbsolutePath()+File.separator+prefix + i + ".png");
                    s.setTime(length * i / (nbOfSnapshots + 1));

            if(headlessRemotePlayer.takeSnapshot(s.getTime(), s.getPath())) {
                        list.add(s);
                    }
            else {
                logger.error(s.getPath()+" is not generated.");
                }
            }
        
        return list;
    }
                }
