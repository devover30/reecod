package info.devram.reecod.data;


public class DashboardService implements DashboardDataSource.RemoteResponseListener {

    //private static final String TAG = "DashboardService";

    private static volatile DashboardService instance;
    private final DashboardDataSource dataSource;

    private DashboardService(DashboardDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static DashboardService getInstance(DashboardDataSource dataSource) {
        if (instance == null) {
            instance = new DashboardService(dataSource);
        }
        return instance;
    }

}
