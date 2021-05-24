package io.lawrance.datasource;


public class GlobalDataSource {
    private static IDataSource dataSource;

    private GlobalDataSource() {

    }

    public static void setDataSource(IDataSource dataSourceVal) {
        dataSource = dataSourceVal;
    }

    public static IDataSource getDataSource() {
        return dataSource;
    }

}
