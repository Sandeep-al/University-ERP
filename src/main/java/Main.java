import static javax.swing.SwingUtilities.invokeLater;
import edu.univ.erp.ui.*;

public class Main {
    public static void main(String[] args) {
        invokeLater(() -> new LoginWindow().setVisible(true));
    }
}
