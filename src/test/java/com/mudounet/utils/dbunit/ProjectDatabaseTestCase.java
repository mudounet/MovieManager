package com.mudounet.utils.dbunit;

import com.mudounet.utils.hibernate.HibernateThreadSession;
import com.mudounet.utils.hibernate.HibernateUtils;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import org.dbunit.DatabaseTestCase;
import org.dbunit.DefaultOperationListener;
import org.dbunit.IOperationListener;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.hsqldb.HsqldbDataTypeFactory;
import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ProjectDatabaseTestCase
        extends DatabaseTestCase {

    private static Logger logger = LoggerFactory.getLogger(ProjectDatabaseTestCase.class.getName());
    private static String driver = "hibernate.connection.driver_class";
    private static String url = "hibernate.connection.url";
    private static String username = "hibernate.connection.username";
    private static String password = "hibernate.connection.password";
    protected HibernateThreadSession template;

    public ProjectDatabaseTestCase(String name) {
        super(name);
         template = HibernateUtils.currentSession();
    }

    protected abstract String getDataSetFilename();

//    @Before
//    @Override
//    public void setUp() throws Exception {
//        //super.setUp();
//       
//    }

//    @After
//    @Override
//    protected void tearDown() throws Exception {
//        //HibernateUtils.destroySession();
//        //super.tearDown(); 
//    }

    @Override
    protected IOperationListener getOperationListener() {
        return new DefaultOperationListener() {

            @Override
            public void operationSetUpFinished(
                    IDatabaseConnection connection) {
                // Do not invoke the "super" method to avoid that the connection is closed
            }

            @Override
            public void operationTearDownFinished(
                    IDatabaseConnection connection) {
                // Do not invoke the "super" method to avoid that the connection is closed
            }
        };
    }

    protected IDataSet getDataSet() throws Exception {
        String file = getDataSetFilename();
        if (file == null) {
            return null;
        } else {
            InputStream fileStream = loadFromTestPath(file);
            InputStream dtdStream = TestTools.loadFromClasspath("database.dtd");
            if (dtdStream == null) {
                logger.error("Database-schema loading failed");

                throw new Exception("Database-schema loading failed");
            } else {
                FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
                builder.setMetaDataSetFromDtd(dtdStream);
                return builder.build(fileStream);
            }


        }
    }

    protected IDatabaseConnection getConnection() throws Exception {
        Properties p = new Properties();
        InputStream m = TestTools.loadFromClasspath("hibernate-db.properties");
        p.load(m);
        Class.forName(p.getProperty(driver));
        Connection c =
                DriverManager.getConnection(p.getProperty(url),
                p.getProperty(username),
                p.getProperty(password));
        DatabaseConnection connection = new DatabaseConnection(c);
        DatabaseConfig config = connection.getConfig();
        config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new HsqldbDataTypeFactory());

        return connection;
    }

    private InputStream loadFromTestPath(String file) throws Exception {
        InputStream is = TestTools.loadFromClasspath(file);
        return is;
    }

    public ITable getResults(String query) throws Exception {
        return getConnection().createQueryTable("events", query);
    }

    public int getNbResults(String query) throws Exception {
        return getResults(query).getRowCount();
    }
}