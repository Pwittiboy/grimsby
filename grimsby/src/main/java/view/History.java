package view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import grimsby.networking.util.Constants;
import grimsby.networking.util.MessageUS;

/**
 * The Class History.
 */
public class History extends JFrame {

	/** The content pane. */
	private JPanel contentPane;

	/**
	 * Instantiates a new history. Creates a new frame with displayed history of
	 * the client
	 *
	 * @param queueOfCommands the queue of commands
	 * @param history the history
	 */
	public History(LinkedBlockingQueue<MessageUS> queueOfCommands, String[] history, MainScreen mainScreen) {
		super("All history");
		mainScreen.setViewLogsFalse();
		// setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				e.getWindow().dispose();
				mainScreen.setViewLogsTrue();
			}
		});
		contentPane = new JPanel();
		Design.backgroundColour(contentPane);

		setSize(270, 421);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 640 };
		gbl_contentPane.rowHeights = new int[] { 480 };
		gbl_contentPane.columnWeights = new double[] { 1.0 };
		gbl_contentPane.rowWeights = new double[] { 1.0 };
		contentPane.setLayout(gbl_contentPane);

		JList<String> historyList = new JList<>(history);
		Design.backgroundColourCreateChat(historyList);
		historyList.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				String val = historyList.getSelectedValue();
				if (val != null)
					queueOfCommands.offer(new MessageUS(Constants.LOAD_PARTICULAR_HISTORY, new String[] { val }));
			}
		});

		JScrollPane scrlHistory = new JScrollPane(historyList);

		scrlHistory.setVisible(true);
		scrlHistory.setViewportView(historyList);
		GridBagConstraints gbc_textPane = new GridBagConstraints();
		gbc_textPane.fill = GridBagConstraints.BOTH;
		gbc_textPane.gridx = 0;
		gbc_textPane.gridy = 0;
		contentPane.add(scrlHistory, gbc_textPane);
		this.setVisible(true);
	}

}