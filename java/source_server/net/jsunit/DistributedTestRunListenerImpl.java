package net.jsunit;

import java.io.File;
import java.util.Date;
import java.util.logging.Logger;

import net.jsunit.configuration.ServerConfiguration;
import net.jsunit.model.DistributedTestRunResult;
import net.jsunit.model.TestRunResult;
import net.jsunit.utility.FileUtility;
import net.jsunit.utility.XmlUtility;

import org.jdom.Element;

public class DistributedTestRunListenerImpl implements DistributedTestRunListener {
    private static final String LOG_PREFIX = "JSTEST-";
    private Logger logger = Logger.getLogger("net.jsunit");
    private File logsDirectory;

    public DistributedTestRunListenerImpl( ServerConfiguration config ) {
        this.logsDirectory = config.getLogsDirectory();
        if( !logsDirectory.exists() ) {
            logsDirectory.mkdir();
        }
    }

    public void notifyRunComplete( DistributedTestRunResult distributedTestRunResult, Date startDate, long millisTaken ) {
        this.saveResult( distributedTestRunResult );
    }


    private void saveResult( DistributedTestRunResult distributedTestRunResult ) {
        TestRunResult[] runResults = distributedTestRunResult.getTestRunResults();

        if( runResults.length > 0 && distributedTestRunResult.getBrowserCount() > 0 ) {
            // get id from <browserResult>
            String id = distributedTestRunResult.getTestRunResults()[0].getBrowserResults()[0].getId();

            // get browserId from <browser>
            String browserId = Integer.toString( distributedTestRunResult.getTestRunResults()[0].getBrowserResults()[0].getBrowser().getId() );

            Element result = distributedTestRunResult.asXml();

            File logFile = new File( logsDirectory + File.separator + LOG_PREFIX + id + "-" + browserId + ".xml" );
            logger.info( "Writing distributed test results to " + logFile );
            FileUtility.write( logFile, XmlUtility.asString( result ) );
        } else {
            logger.warning( "No browser results found to save in distributed test results!" );
        }
    }
}
