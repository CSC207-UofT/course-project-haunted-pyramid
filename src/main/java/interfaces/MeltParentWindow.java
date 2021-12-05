package interfaces;

import javax.swing.*;

public interface MeltParentWindow {
    void enableFrame();

    void exitFrame();

    MeltParentWindow getParent();

    void refresh();
}
