/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.utils.managers;

import com.mudounet.hibernate.movies.GenericMovie;
import com.mudounet.hibernate.movies.Movie;
import com.mudounet.hibernate.movies.others.Snapshot;
import com.mudounet.hibernate.movies.others.TechData;
import com.mudounet.utils.video.RemotePlayer;
import com.mudounet.utils.video.RemotePlayerException;
import com.mudounet.utils.video.RemotePlayerFactory;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author isabelle
 */
public class MovieToolManager {

    protected static Logger logger = LoggerFactory.getLogger(MovieToolManager.class.getName());

    private static RemotePlayer createHeadlessPlayer(GenericMovie movie) throws RemotePlayerException {
        RemotePlayer headlessRemotePlayer = RemotePlayerFactory.getHeadlessRemotePlayer();
        headlessRemotePlayer.load(movie.getPath());
        headlessRemotePlayer.play();
        return headlessRemotePlayer;
    }


    public static GenericMovie buildMovie(String path) throws InterruptedException, IOException {
        File file = new File(path);
        return buildMovie(file);
    }
    
    public static GenericMovie buildMovie(File file) throws InterruptedException, IOException {
        Movie m = new Movie();
        m.setPath(file.getPath());
        m.setMd5(m.getMd5());

        return m;
    }
    
    public static TechData getMovieInformations(GenericMovie movie) throws InterruptedException, IOException {
        TechData techData = new TechData();

     
        RemotePlayer headlessRemotePlayer;
        try {
            headlessRemotePlayer = createHeadlessPlayer(movie);
            
            techData = headlessRemotePlayer.retrieveTechData();
            headlessRemotePlayer.close();

            
        } catch (RemotePlayerException ex) {
            logger.error("Exception found : "+ex);
        }

        logger.info("Technical data : ", techData);

        return techData;
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

        if (!directory.isDirectory()) {
            throw new Exception("Directory doesn't exists");
        }
        if (nbOfSnapshots < 1 || nbOfSnapshots > 9) {
            throw new Exception("Number of snapshots has to be defined between 1 and 9.");
        }

        String prefix = movie.getMd5() + "-";

        RemotePlayer headlessRemotePlayer = createHeadlessPlayer(movie);

        long length = headlessRemotePlayer.getLength();

        if (length == -1) {
            return null;
        }

        for (long i = 1; i <= nbOfSnapshots; i++) {
            Snapshot s = new Snapshot();
            s.setPath(directory.getAbsolutePath() + File.separator + prefix + i + ".png");
            s.setTime(length * i / (nbOfSnapshots + 1));

            int nbOfTries = 5;
            boolean success = false;
            for (int retry = 1; retry <= nbOfTries; retry++) {
                try {
                    if (headlessRemotePlayer.takeSnapshot(s.getTime(), s.getPath())) {
                        list.add(s);
                        success = true;
                        break;
                    }
                } catch (RemotePlayerException ex) {
                    logger.warn("Retry " + retry + " / " + nbOfTries);
                    logger.warn("Remote player error : ", ex);
                    headlessRemotePlayer.close();
                    headlessRemotePlayer = createHeadlessPlayer(movie);

                }
            }

            if (!success) {
                logger.error("Thumbnail not generated.");
            }
        }

        headlessRemotePlayer.close();

        return list;
    }
}
