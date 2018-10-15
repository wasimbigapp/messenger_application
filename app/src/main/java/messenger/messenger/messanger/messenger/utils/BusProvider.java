package messenger.messenger.messanger.messenger.utils;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Created by bedprakash.r on 09/01/18.
 */

public class BusProvider {

    private static Bus uiBus = new Bus(ThreadEnforcer.MAIN);

    private BusProvider() {

    }

    public static Bus getUiBus() {
        return uiBus;
    }
}
