package io.library.datasource;


import io.library.service.LoggingService;

import java.util.logging.Level;
import java.util.logging.Logger;

public class GlobalDataSource {
    private static IDataSource dataSource;
    private final static Logger logger = LoggingService.getLogger(GlobalDataSource.class);

    private GlobalDataSource() {

    }

    public static void setDataSource(IDataSource dataSourceVal) {

        logger.log(Level.INFO, "Datasource initialized");

        dataSource = dataSourceVal;
    }

    public static IDataSource getDataSource() {
        return dataSource;
    }

}
