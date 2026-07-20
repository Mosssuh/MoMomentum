package mv.mossuh.momomentum.API;

import mv.mossuh.momomentum.MANAGERS.MomentumManager;

public class MomentumAPI {
    private static MomentumManager manager = new MomentumManager();

    public static MomentumManager getManager() {
        return manager;
    }
}
