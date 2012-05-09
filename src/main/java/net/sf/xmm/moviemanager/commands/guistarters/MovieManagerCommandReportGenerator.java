package net.sf.xmm.moviemanager.commands.guistarters;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import net.sf.xmm.moviemanager.MovieManager;
import net.sf.xmm.moviemanager.gui.DialogReportGenerator;

import org.slf4j.LoggerFactory;

public class MovieManagerCommandReportGenerator implements ActionListener {

	protected org.slf4j.Logger log = LoggerFactory.getLogger(getClass());
	
    /**
     * Executes the command.
     **/
    protected static void execute() {
        DialogReportGenerator dialogPrint = new DialogReportGenerator(MovieManager.getDialog());
    }

    /**
     * Invoked when an action occurs.
     **/
    public void actionPerformed(ActionEvent event) {
        log.debug("ActionPerformed: " + event.getActionCommand());
        execute();
    }
}
