package net.sf.xmm.moviemanager.commands.guistarters;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import net.sf.xmm.moviemanager.gui.DialogIMDbUpdate;
import net.sf.xmm.moviemanager.models.ModelMovieInfo;
import net.sf.xmm.moviemanager.util.GUIUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MovieManagerCommandDialogIMDB {

	Logger log =  LoggerFactory.getLogger(getClass());
	
	public boolean cancel = false;
	public boolean cancelAll = false;

	String tmpUrlKey;
		
	public synchronized String getIMDBKey(final String movieTitle) throws InterruptedException {
		
		if (cancelAll)
			return null;

		tmpUrlKey = null;
		
		try {
			SwingUtilities.invokeAndWait(new Runnable() {

				public void run() {
					ModelMovieInfo modelInfo = new ModelMovieInfo(false, true);
					modelInfo.model.setTitle(movieTitle);

					DialogIMDbUpdate dialogIMDb = new DialogIMDbUpdate(modelInfo.model, movieTitle, true);
					GUIUtil.showAndWait(dialogIMDb, true);
					
					tmpUrlKey = modelInfo.model.getUrlKey();
					
					cancel = dialogIMDb.getCanceled();
					cancelAll = dialogIMDb.getAborted();
				}
			});
		} catch (InvocationTargetException e) {
			log.error("Exception:" + e.getMessage(), e);
		}
		
		return tmpUrlKey;
	}
}
