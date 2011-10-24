package com.mudounet.utils.video;

import com.sun.jna.NativeLibrary;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;

/**
 * A headless player that sits out of process in a separate VM, feeding one or
 * more players that sit on the other end of the stream.
 * @author gmanciet
 */
public class OutOfProcessHeadlessPlayer extends OutOfProcessPlayer {

    private static final String[] VLC_ARGS = {
        "--intf", "dummy", /* no interface */
        "--vout", "dummy", /* we don't want video (output) */
        "--no-audio", /* we don't want audio (decoding) */
        "--no-video-title-show", /* nor the filename displayed */
        "--no-stats", /* no stats */
        "--no-sub-autodetect-file", /* we don't want subtitles */
        //"--no-inhibit", /* we don't want interfaces */
        "--no-disable-screensaver", /* we don't want interfaces */
        "--no-snapshot-preview", /* no blending in dummy vout */};

    /**
     * Create a new headless player that sits out of process.
     * @throws IOException if something went wrong.
     */
    public OutOfProcessHeadlessPlayer() throws IOException {
        MediaPlayerFactory factory = new MediaPlayerFactory(getPrepareOptions());
        mediaPlayer = factory.newHeadlessMediaPlayer();
    }

    /**
     * Get the special options to pass to vlcj for playing over a headless player.
     * In this case it tells vlcj we're using localhost and playing over HTTP.
     * @return the prepare options as a string array.
     */
    @Override
    public final String[] getPrepareOptions() {
       String[]list = VLC_ARGS;
        return list;
    }

    /**
     * Testing stuff.
     * @param args command line arguments.
     */
    public static void main(String[] args) {
        File nativeDir = new File("lib/native");
        NativeLibrary.addSearchPath("libvlc", nativeDir.getAbsolutePath());
        NativeLibrary.addSearchPath("vlc", nativeDir.getAbsolutePath());
        PrintStream stream = null;
        try {
            stream = new PrintStream(new File("headless-logfile.txt"));
            System.setErr(stream); //Important, MUST redirect err stream
            OutOfProcessHeadlessPlayer player = new OutOfProcessHeadlessPlayer();
            System.err.println("Begin of process");
            player.read();
            System.err.println("End of process");
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        } finally {
            if (stream != null) {
                stream.close();
    }
}
    }
}
