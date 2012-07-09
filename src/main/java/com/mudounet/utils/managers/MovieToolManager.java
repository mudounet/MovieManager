/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mudounet.utils.managers;

import com.mudounet.hibernate.Movie;
import com.mudounet.hibernate.movies.others.Snapshot;
import com.mudounet.hibernate.movies.others.MediaInfo;
import com.mudounet.utils.video.VideoPlayerException;
import com.mudounet.utils.video.VlcPlayer;
import com.mudounet.utils.video.VlcPlayerFactory;
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

    private static VlcPlayer createHeadlessPlayer(Movie movie) throws VideoPlayerException {
        VlcPlayer headlessRemotePlayer = VlcPlayerFactory.getHeadlessPlayer();
        headlessRemotePlayer.load(movie.getRealFilename());
        headlessRemotePlayer.play();
        return headlessRemotePlayer;
    }

    public static Movie buildMovie(String path, String title) throws InterruptedException, IOException {
        File file = new File(path);
        return buildMovie(file, title);
    }
    
    public static Movie buildMovie(File file, String title) throws InterruptedException, IOException {
        Movie m = new Movie();
        m.setRealFilename(file.getPath());
        m.setFilename(file.getName());
        m.setMd5(m.getMd5());
        m.setTitle(title);
        m.setSize(file.length());
        m.setModificationDate(file.lastModified());


        return m;
    }
    
    public static MediaInfo getMovieInformations(Movie movie) throws InterruptedException, IOException {
        MediaInfo mediaInfo = new MediaInfo();

     
        VlcPlayer headlessRemotePlayer;
        try {
            headlessRemotePlayer = createHeadlessPlayer(movie);
            
            mediaInfo = headlessRemotePlayer.retrieveMediaInfo();
            headlessRemotePlayer.close();

            
        } catch (VideoPlayerException ex) {
            logger.error("Exception found : "+ex);
        }

        logger.info("Media Infos : ", mediaInfo);

        return mediaInfo;
    }

    /**
     * Generate a set of snapshots from Movie.
     * @param movie
     * @param directory
     * @param nbOfSnapshots
     * @return found snapshots
     * @throws Exception
     */
    public static Set<Snapshot> genSnapshots(Movie movie, File directory, int nbOfSnapshots) throws Exception {
        Set<Snapshot> list = new HashSet<Snapshot>();

        if (!directory.isDirectory()) {
            throw new Exception("Directory doesn't exists");
        }
        if (nbOfSnapshots < 1 || nbOfSnapshots > 9) {
            throw new Exception("Number of snapshots has to be defined between 1 and 9.");
        }

        String prefix = movie.getMd5() + "-";

        VlcPlayer headlessRemotePlayer = createHeadlessPlayer(movie);

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
                } catch (VideoPlayerException ex) {
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
