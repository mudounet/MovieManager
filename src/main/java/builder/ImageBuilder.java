/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package builder;

import java.awt.image.BufferedImage;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.MediaListenerAdapter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.mediatool.event.IVideoPictureEvent;
import com.xuggle.xuggler.Global;
import hibernate.TechData;
import java.io.InputStream;
import java.util.ArrayList;
import org.apache.log4j.Logger;

/**
 *
 * @author gmanciet
 */
public class ImageBuilder {
    protected static Logger logger = Logger.getLogger(ImageBuilder.class);
    private String filename;
    private TechData t;

    public ImageBuilder(String filename) throws Exception {
        this.filename = filename;
        t = TechDataBuilder.build(filename);
    }

    private ArrayList<BufferedImage> __extractFrames(int nbOfSamples) {

        int offset = 0;
        long nbOfMilliseconds = t.getPlayTime();
        int samplingPeriod = (int) nbOfMilliseconds / nbOfSamples ;

        logger.debug("Nb of milliseconds: "+nbOfMilliseconds);
        logger.debug("Nb of samples: "+nbOfSamples);
        logger.debug("Sampling period (ms): "+samplingPeriod);

        long time1= System.currentTimeMillis();
        SubImageBuilder b = new SubImageBuilder(t.getPath(), offset, samplingPeriod);
        ArrayList<BufferedImage> list = b.extractFrames();

        long diff = (System.currentTimeMillis() - time1)/1000;
        String result = String.format("%d:%02d:%02d", diff/3600, (diff%3600)/60, (diff%60));
        logger.debug("Extract took "+result+" seconds");
        logger.debug("Speed indicator is "+nbOfMilliseconds/diff);
        logger.debug("Mean time per image: "+diff/nbOfSamples);

        return list;
    }

    public BufferedImage extractFrameAtPercent(long percentage) {
        return extractFramesBeginningAtPercent(1, percentage).get(0);
    }

    public ArrayList<BufferedImage> extractFrames(int number) {
        return extractFramesBeginningAt(number, 0);
    }

    public ArrayList<BufferedImage> extractFramesBeginningAt(int nbOfSamples, long offset) {
       return __extractFrames(nbOfSamples);
    }

    public ArrayList<BufferedImage> extractFramesBeginningAtPercent(int nbOfSamples, long offsetPercent) {
        return null;
    }

    public static InputStream scaleImage(InputStream p_image, int p_width, int p_height) throws Exception {
        /*
     InputStream imageStream = new BufferedInputStream(p_image);
     Image image = (Image) ImageIO.read(imageStream);

     int thumbWidth = p_width;
        int thumbHeight = p_height;

        // Make sure the aspect ratio is maintained, so the image is not skewed
        double thumbRatio = (double)thumbWidth / (double)thumbHeight;
        int imageWidth = image.getWidth(null);
        int imageHeight = image.getHeight(null);
        double imageRatio = (double)imageWidth / (double)imageHeight;
        if (thumbRatio < imageRatio) {
          thumbHeight = (int)(thumbWidth / imageRatio);
        } else {
          thumbWidth = (int)(thumbHeight * imageRatio);
        }

        // Draw the scaled image
        BufferedImage thumbImage = new BufferedImage(thumbWidth,
          thumbHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = thumbImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
          RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.drawImage(image, 0, 0, thumbWidth, thumbHeight, null);

        // Write the scaled image to the outputstream
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
        JPEGEncodeParam param = encoder.
          getDefaultJPEGEncodeParam(thumbImage);
        int quality = 100; // Use between 1 and 100, with 100 being highest quality

        quality = Math.max(0, Math.min(quality, 100));
        param.setQuality((float)quality / 100.0f, false);
        encoder.setJPEGEncodeParam(param);
        encoder.encode(thumbImage);
        String IMAGE_JPG = "JPG";
        ImageIO.write(thumbImage, IMAGE_JPG , out);

        // Read the outputstream into the inputstream for the return value
        ByteArrayInputStream bis = new ByteArrayInputStream(out.toByteArray());
*/
        return null;
    }

    protected class SubImageBuilder extends MediaListenerAdapter {

        private String filename;
        private int offset;


        private double samplingPeriod = 5;
        public long MICRO_SECONDS_BETWEEN_FRAMES;
        private long mLastPtsWrite = Global.NO_PTS;
        private ArrayList<BufferedImage> list;

        /**
         * The video stream index, used to ensure we display frames from one
         * and only one video stream from the media container.
         */
        private int mVideoStreamIndex = -1;

        public SubImageBuilder(String filename, int offset, int samplingPeriod) {
            this.filename = filename;
            this.offset = offset;
            this.samplingPeriod = (double)samplingPeriod / 1000;

            init();
        }

        private void init() {
            MICRO_SECONDS_BETWEEN_FRAMES = (long) (Global.DEFAULT_PTS_PER_SECOND * samplingPeriod);
            mLastPtsWrite = Global.NO_PTS;
            this.list = new ArrayList<BufferedImage>();
        }

        /**
         * Takes a media container (file) as the first argument, opens it and
         *  writes some of it's video frames to PNG image files in the
         *  temporary directory.
         *
         * @param args must contain one string which represents a filename
         */
        /** Construct a DecodeAndCaptureFrames which reads and captures
         * frames from a video file.
         *
         * @param filename the name of the media file to read
         */

        public ArrayList<BufferedImage> extractFrames() {
            // create a media reader for processing video

            IMediaReader reader = ToolFactory.makeReader(filename);

            // stipulate that we want BufferedImages created in BGR 24bit color space
            reader.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR);


            // note that DecodeAndCaptureFrames is derived from
            // MediaReader.ListenerAdapter and thus may be added as a listener
            // to the MediaReader. DecodeAndCaptureFrames implements
            // onVideoPicture().

            reader.addListener(this);

            // read out the contents of the media file, note that nothing else
            // happens here.  action happens in the onVideoPicture() method
            // which is called when complete video pictures are extracted from
            // the media source

            logger.debug("Beginning extraction process");
            while (reader.readPacket() == null) {
                do {
                } while (false);
            }
            logger.debug("Ending extraction process");

            return this.list;
        }

        /**
         * Called after a video frame has been decoded from a media stream.
         * Optionally a BufferedImage version of the frame may be passed
         * if the calling {@link IMediaReader} instance was configured to
         * create BufferedImages.
         *
         * This method blocks, so return quickly.
         */
        @Override
        public void onVideoPicture(IVideoPictureEvent event) {
            try {
                // if the stream index does not match the selected stream index,
                // then have a closer look

                if (event.getStreamIndex() != mVideoStreamIndex) {
                    // if the selected video stream id is not yet set, go ahead an
                    // select this lucky video stream

                    if (-1 == mVideoStreamIndex) {
                        mVideoStreamIndex = event.getStreamIndex();
                    } // otherwise return, no need to show frames from this video stream
                    else {
                        return;
                    }
                }

                // if uninitialized, backdate mLastPtsWrite so we get the very
                // first frame

                if (mLastPtsWrite == Global.NO_PTS) {
                    mLastPtsWrite = event.getTimeStamp() - MICRO_SECONDS_BETWEEN_FRAMES;
                }

                // if it's time to write the next frame

                if (event.getTimeStamp() - mLastPtsWrite >= MICRO_SECONDS_BETWEEN_FRAMES) {
                    // Make a temporary file name

                    this.list.add(event.getImage());

                    // indicate file written

                    long seconds = ((long) event.getTimeStamp())
                            / Global.DEFAULT_PTS_PER_SECOND;

                    // update last write time

                    mLastPtsWrite += MICRO_SECONDS_BETWEEN_FRAMES;
                    String result = String.format("[%d:%02d:%02d]", seconds/3600, (seconds%3600)/60, (seconds%60));
                    logger.debug("Extracted frame #"+this.list.size()+" at "+result);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
