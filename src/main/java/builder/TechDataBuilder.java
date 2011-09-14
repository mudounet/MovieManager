/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package builder;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;
import hibernate.TechData;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import org.apache.log4j.Logger;
import com.mudounet.utils.Md5Generator;

/**
 *
 * @author gmanciet
 */
public class TechDataBuilder {
    protected static Logger logger = Logger.getLogger(TechDataBuilder.class);

    public static TechData build(String filename) throws IOException, FileNotFoundException, NoSuchAlgorithmException, Exception {

        File theFile = new File(filename);

        // copied/modified from Xuggler code below
        // Create a Xuggler container object
        IContainer container = IContainer.make();

        // Open up the container
        if (container.open(filename, IContainer.Type.READ, null) < 0) {
            throw new IllegalArgumentException("could not open file: " + filename);
        }

        if (container.queryStreamMetaData() < 0) {
            throw new IllegalStateException("couldn't query stream meta data for some reason...");
        }

        TechData b = new TechData();
        b.setFilesize((int)container.getFileSize());
        b.setPath(filename);
        
        long duration = container.getDuration() / 1000;
        String result = String.format("[%d:%02d:%02d]", duration/3600, (duration%3600)/60, (duration%60));
        logger.debug("Duration of file "+filename+" is "+result);
        b.setPlayTime(container.getDuration() / 1000);

        b.setMd5Sum(Md5Generator.computeMD5(filename));

        int numStreams = container.getNumStreams();
        // and iterate through the streams to print their meta data
        for (int i = 0; i < numStreams; i++) {
            IStream stream = container.getStream(i);
            IStreamCoder coder = stream.getStreamCoder();

            if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO) {
               
                b.setWidth(coder.getWidth());
                b.setHeight(coder.getHeight());
                logger.debug("Found video stream ("+b.getWidth()+"x"+b.getHeight()+")");
                break;
            }
        }

        return b;
    }
}
