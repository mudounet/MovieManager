package com.mudounet.utils.video;

import com.mudounet.utils.Utils;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicSliderUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The control panel for displaying the video.
 * @author gmanciet
 */
public class VideoControlPanel extends JPanel {

    protected static Logger logger = LoggerFactory.getLogger(VideoControlPanel.class.getName());
    private static final long serialVersionUID = 1L;
    private JButton play;
    private JButton pause;
    private JButton stop;
    private JButton mute;
    private JSlider positionSlider;
    private Canvas videoArea;
    private List<RemotePlayer> mediaPlayers;
    private ScheduledExecutorService executorService;
    private boolean pauseCheck;
    private String videoPath;

    /**
     * Create a new video control panel.
     */
    public VideoControlPanel() {

        executorService = Executors.newSingleThreadScheduledExecutor();
        play = buildButton("images/play.png", "images/play-pressed.png");

        play.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    playVideo();
                } catch (RemotePlayerException ex) {
                    logger.error("Remote player error : " , ex);
                }
            }
        });

        pause = buildButton("images/pause.png", "images/pause-pressed.png");

        pause.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    pauseVideo();
                } catch (RemotePlayerException ex) {
                    logger.error("Remote player error : " , ex);
                }
            }
        });

        stop = buildButton("images/stop.png", "images/stop-pressed.png");

        stop.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    stopVideo();
                } catch (RemotePlayerException ex) {
                    logger.error("Remote player error : " , ex);
                }
                positionSlider.setValue(0);
            }
        });

        mute = buildButton("images/volume-high.png", null);
        mute.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    setMute(!getMute());
                } catch (RemotePlayerException ex) {
                    logger.error("Remote player error : " , ex);
                }
            }
        });
        positionSlider = new JSlider(0, 1000);
        positionSlider.setEnabled(false);
        positionSlider.setValue(0);
        positionSlider.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                for (RemotePlayer mediaPlayer : mediaPlayers) {
                    try {
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.pause();
                            pauseCheck = false;
                        } else {
                            pauseCheck = true;
                        }
                    } catch (RemotePlayerException ex) {
                        logger.error("Remote player error : " , ex);
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                for (RemotePlayer mediaPlayer : mediaPlayers) {
                    try {
                        mediaPlayer.setTime((long) ((positionSlider.getValue() / (double) 1000) * mediaPlayer.getLength()));
                        if (!pauseCheck) {
                            mediaPlayer.play();
                        }
                    } catch (RemotePlayerException ex) {
                        logger.error("Remote player error : " , ex);
                    }
                }
            }
        });

        try {
            positionSlider.setUI(new BasicSliderUI(positionSlider) {

                @Override
                protected void scrollDueToClickInTrack(int direction) {
                    // this is the default behaviour, let's comment that out
                    //scrollByBlock(direction);

                    int value = positionSlider.getValue();

                    if (positionSlider.getOrientation() == JSlider.HORIZONTAL) {
                        value = this.valueForXPosition(positionSlider.getMousePosition().x);
                    } else if (positionSlider.getOrientation() == JSlider.VERTICAL) {
                        value = this.valueForYPosition(positionSlider.getMousePosition().y);
                    }
                    positionSlider.setValue(value);
                }
            });

        } catch (Exception ex) {
            //UI issue, cannot do a lot and don't want to break program...
        }

        videoArea = new Canvas();
        videoArea.setBackground(Color.BLACK);
        videoArea.setMinimumSize(new Dimension(20, 20));
        videoArea.setPreferredSize(new Dimension(100, 100));
        setLayout(new BorderLayout());
        add(videoArea, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BorderLayout());
        JPanel sliderPanel = new JPanel();
        sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.X_AXIS));
        sliderPanel.add(positionSlider);
        controlPanel.add(sliderPanel, BorderLayout.SOUTH);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(play);
        buttonPanel.add(pause);
        buttonPanel.add(stop);
        buttonPanel.add(mute);
        controlPanel.add(buttonPanel, BorderLayout.NORTH);
        add(controlPanel, BorderLayout.NORTH);
        mediaPlayers = new ArrayList<RemotePlayer>();
        videoArea.addHierarchyListener(new HierarchyListener() {

            @Override
            public void hierarchyChanged(HierarchyEvent e) {
                if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) > 0 && videoArea.isShowing()) {
                    new Thread() {

                        @Override
                        public void run() {
                            RemotePlayer player = RemotePlayerFactory.getEmbeddedRemotePlayer(videoArea);
                            mediaPlayers.add(0, player);
                            if (videoPath != null) {
                                try {
                                    player.load(videoPath);
                                } catch (RemotePlayerException ex) {
                                    logger.error("Remote player error : " , ex);
                                }
                            }
                            play.setEnabled(true);
                            pause.setEnabled(true);
                            stop.setEnabled(true);
                            mute.setEnabled(true);
                            positionSlider.setEnabled(true);
                        }
                    }.start();
                    videoArea.removeHierarchyListener(this);
                }
            }
        });
    }

    private JButton buildButton(String normalIcon, String pressedIcon) {


        JButton button = new JButton(Utils.getImageIcon(normalIcon));
        if (!(pressedIcon == null)) {
            button.setPressedIcon(Utils.getImageIcon(pressedIcon));
        }
        button.setBorderPainted(false);
        button.setRolloverEnabled(false);
        button.setEnabled(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        return button;
    }

    /**
     * Load the given video to be controlled via this panel.
     * @param videoPath the video path to load.
     * @throws RemotePlayerException  
     */
    public void loadVideo(String videoPath) throws RemotePlayerException {
        this.videoPath = videoPath;
        for (RemotePlayer mediaPlayer : mediaPlayers) {
            mediaPlayer.load(videoPath);
        }
    }

    /**
     * Play the loaded video.
     * @throws RemotePlayerException 
     */
    public void playVideo() throws RemotePlayerException {
        for (int i = 0; i < mediaPlayers.size(); i++) {
            final RemotePlayer mediaPlayer = mediaPlayers.get(i);
            if (i > 0) {
                mediaPlayer.setMute(true);
            }
            mediaPlayer.play();
            executorService.scheduleAtFixedRate(new Runnable() {

                @Override
                public void run() {
                    try {
                        if (mediaPlayer.isPlaying()) {
                            SwingUtilities.invokeLater(new Runnable() {

                                @Override
                                public void run() {
                                    try {
                                        int timeVal = (int) ((mediaPlayer.getTime() / (double) mediaPlayer.getLength()) * 1000);
                                        positionSlider.setValue(timeVal);
                                    } catch (RemotePlayerException ex) {
                                        logger.error("Remote player error : " , ex);
                                    }
                                }
                            });
                        }
                    } catch (RemotePlayerException ex) {
                        logger.error("Remote player error : " , ex);
                    }
                }
            }, 0, 500, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * Get the current time of the video.
     * @return the current time of the video.
     * @throws RemotePlayerException  
     */
    public long getTime() throws RemotePlayerException {
        return mediaPlayers.get(0).getTime();
    }

    /**
     * Set the current time of the video.
     * @param time the current time of the video.
     * @throws RemotePlayerException  
     */
    public void setTime(long time) throws RemotePlayerException {
        for (RemotePlayer mediaPlayer : mediaPlayers) {
            mediaPlayer.setTime(time);
        }
    }

    /**
     * Pause the currently playing video.
     * @throws RemotePlayerException 
     */
    public void pauseVideo() throws RemotePlayerException {
        for (RemotePlayer mediaPlayer : mediaPlayers) {
            mediaPlayer.pause();
        }
    }

    /**
     * Stop the currently playing video.
     * @throws RemotePlayerException 
     */
    public void stopVideo() throws RemotePlayerException {
        for (RemotePlayer mediaPlayer : mediaPlayers) {
            mediaPlayer.stop();
        }
    }

    /**
     * Set whether the video is muted.
     * @param muteState true to mute, false to unmute.
     * @throws RemotePlayerException  
     */
    public void setMute(boolean muteState) throws RemotePlayerException {
        mediaPlayers.get(0).setMute(muteState);
        if (getMute()) {
            mute.setIcon(Utils.getImageIcon("images/volume-low.png"));
        } else {
            mute.setIcon(Utils.getImageIcon("images/volume-high.png"));
        }
    }

    /**
     * Determine if this video is muted.
     * @return true if muted, false if not.
     * @throws RemotePlayerException  
     */
    public boolean getMute() throws RemotePlayerException {
        return mediaPlayers.get(0).getMute();
    }

    /**
     * Close down all the players controlled via this control panel and stop
     * the external VM's / remote players it controls.
     * @throws RemotePlayerException 
     */
    public void close() throws RemotePlayerException {
        for (RemotePlayer mediaPlayer : mediaPlayers) {
            mediaPlayer.close();
            executorService.shutdownNow();
        }
    }

    public void takeSnapshot(long time, String path) throws RemotePlayerException {
        mediaPlayers.get(0).takeSnapshot(time, path);
    }

    /**
     * Try and stop and clear up if we haven't already.
     * @throws Throwable if something goes wrong.
     */
    @Override
    protected void finalize() throws Throwable {
        stopVideo();
        super.finalize();
        close();
    }

    /**
     * Just for testing.
     * @param args 
     */
    public static void main(String[] args) {
        try {
//            JFrame frame = new JFrame();
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            frame.setSize(300, 300);
//            VideoControlPanel panel = new VideoControlPanel();
//            frame.setLayout(new BorderLayout());
//            frame.add(panel, BorderLayout.CENTER);
//            frame.setVisible(true);
//            frame.setVisible(true);

//            panel.loadVideo("src/test/resources/sample_video.flv");

            //panel.playVideo();
            RemotePlayer headlessRemotePlayer = RemotePlayerFactory.getHeadlessRemotePlayer();

            headlessRemotePlayer.load("src/test/resources/sample_video.flv");
            headlessRemotePlayer.play();

            long length = headlessRemotePlayer.getLength();
            logger.info("Taking snapshot");

            long snapshots = 9;
            for (long i = 1; i <= snapshots; i++) {
                logger.debug("Snapshot index : "+i+" / "+snapshots);
                headlessRemotePlayer.takeSnapshot(length * i / (snapshots + 1), "test-" + i + ".png");
            }

            logger.info("All snapshots taken");
            headlessRemotePlayer.close();
        } catch (RemotePlayerException ex) {
            logger.error("Remote player error : " , ex);
        }

    }
}
