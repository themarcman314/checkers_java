package Client;

import shared.Board;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Enumeration;

class Client extends JFrame implements ActionListener {
	public int squareClicked = -1;
	private final int delta = 60;
	int originX;
	int originY;
	JMenuItem menuItemManageConnections;
	JMenuItem menuItemNewGame;
	BoardPanel boardPanel;

	public Client() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JMenuBar menuBar = new JMenuBar();

		JMenu menuGame = new JMenu("Game");
		JMenu menuConnect = new JMenu("Connections");
		menuBar.add(menuGame);
		menuBar.add(menuConnect);

		menuItemNewGame = new JMenuItem("New Game", KeyEvent.VK_N);
		menuItemManageConnections = new JMenuItem("Manage_connections");

		menuItemNewGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		menuItemNewGame.addActionListener(this);
		menuItemManageConnections.addActionListener(this);

		menuGame.add(menuItemNewGame);
		menuConnect.add(menuItemManageConnections);

		setJMenuBar(menuBar);
		setVisible(true);
	}

	class NetConfig extends JDialog implements ActionListener {
		private JButton select;
		private JButton cancel;

		public NetConfig() {
			setSize(500, 300);
			setLocationRelativeTo(null);
			select = new JButton("Select");
			cancel = new JButton("Cancel");
			select.addActionListener(this);
			cancel.addActionListener(this);
			JPanel p = new JPanel();
			add(p, BorderLayout.SOUTH);
			p.add(select);
			p.add(cancel);
			JPanel container = new JPanel();
			add(container);
			container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

			JLabel text = new JLabel("Enter server IP address:");
			text.setAlignmentX(Component.CENTER_ALIGNMENT); // Keeps it centered
			text.setAlignmentY(Component.CENTER_ALIGNMENT); // Keeps it centered
			container.add(text);
			byte[] addr = { 1, 1, 1, 1 };
			try {
				addr = InetAddress.getLocalHost().getAddress();
			} catch (Exception exception) {
			}

			JSpinner spinner0 = new JSpinner(
					new SpinnerNumberModel(Byte.toUnsignedInt(addr[0]), 0, 255, 1));
			JSpinner spinner1 = new JSpinner(
					new SpinnerNumberModel(Byte.toUnsignedInt(addr[1]), 0, 255, 1));
			JSpinner spinner2 = new JSpinner(
					new SpinnerNumberModel(Byte.toUnsignedInt(addr[2]), 0, 255, 1));
			JSpinner spinner3 = new JSpinner(
					new SpinnerNumberModel(Byte.toUnsignedInt(addr[3]), 0, 255, 1));
			JPanel panel = new JPanel();
			panel.add(spinner0);
			panel.add(new JLabel("."));
			panel.add(spinner1);
			panel.add(new JLabel("."));
			panel.add(spinner2);
			panel.add(new JLabel("."));
			panel.add(spinner3);
			container.add(panel);

			// setsize of dialog
			setVisible(true);

		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if (e.getSource() == select)
				System.out.println("selected ip");
			else if (e.getSource() == cancel) {
				dispose();
			}

		}
	}

	class BoardPanel extends JPanel implements MouseListener {
		private final int boardPanelSizePixels = 600;
		private Board b;

		enum clickState {
			NONE,
			FIRST
		}

		private clickState cState = clickState.NONE;

		public BoardPanel() {
			addMouseListener(this);
			SoundEffect s = new SoundEffect("effects/board_setup.wav");
			s.play();
			b = new Board();
		}

		public boolean isSquareEmpty(int square_num, Board b) {
			if (b.board[square_num] == 0)
				return true;
			return false;
		}

		public Dimension getPreferredSize() {
			return new Dimension(boardPanelSizePixels, boardPanelSizePixels);
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.setColor(Color.gray);
			// center board
			Dimension d = this.getSize();
			originX = (d.width - (b.sideSize * delta)) / 2;
			originY = (d.height - (b.sideSize * delta)) / 2;

			// System.out.printf("origin x: %d\norigin y: %d\n", originX, originY);

			// draw grid
			for (int line = 0; line <= b.sideSize; line++) {
				g.drawLine(originX, originY + line * delta, originX + b.sideSize * delta,
						originY + line * delta);
				g.drawLine(originX + line * delta, originY, originX + line * delta,
						originY + b.sideSize * delta);
			}
			final int circleDiameter = 2 * delta / 3;

			// draw pieces and board squares
			for (int yIndex = 0; yIndex < b.sideSize; yIndex++) {
				for (int xIndex = 0; xIndex < b.sideSize; xIndex++) {
					int boardArrayIdx = xIndex + yIndex * b.sideSize;
					if (yIndex % 2 == 0) {
						if (xIndex % 2 == 0)
							g.setColor(Color.WHITE);
						else {
							g.setColor(Color.GRAY);
						}

					} else {
						if (xIndex % 2 == 1)
							g.setColor(Color.WHITE);
						else {
							g.setColor(Color.GRAY);
						}
					}

					g.fillRect(originX + 1 + delta * xIndex,
							originY + 1 + delta * yIndex,
							delta - 1, delta - 1);

					if (boardArrayIdx == squareClicked
							&& !isSquareEmpty(squareClicked, b)) {
						switch (cState) {
							case clickState.NONE:
								cState = clickState.FIRST;
								g.setColor(Color.GRAY);
								g.drawString("click state is NONE", 50, 50);
								g.setColor(Color.green);
								g.fillRect(originX + 1 + delta * xIndex,
										originY + 1 + delta * yIndex,
										delta - 1, delta - 1);
								break;

							case clickState.FIRST:
								g.setColor(Color.GRAY);
								g.drawString("click state is FIRST", 50, 50);
								cState = clickState.NONE;

								break;
							default:
								break;
						}
					}
					if (b.board[boardArrayIdx] == b.BLACK_PAWN
							|| b.board[xIndex] == b.BLACK_KING)
						g.setColor(Color.BLACK);
					else if (b.board[boardArrayIdx] == b.RED_PAWN
							|| b.board[xIndex] == b.RED_KING)
						g.setColor(Color.RED);
					else
						continue;
					g.fillOval((originX - circleDiameter / 2 + delta / 2)
							+ delta * xIndex,
							(originY - circleDiameter / 2 + delta / 2)
									+ delta * yIndex,
							circleDiameter,
							circleDiameter);
				}
			}
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mousePressed(MouseEvent e) {
			Point p = e.getPoint();
			// System.out.printf("x: %d\ny: %d\n", p.x, p.y);
			if (p.y < b.sideSize * delta + originY && p.y > originY
					&& p.x < b.sideSize * delta + originX
					&& p.x > originX) { // make sure click is within bounds
				squareClicked = ((p.y - originY) / delta) * b.sideSize
						+ (p.x - originX) / delta;
				repaint();
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub

		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if (o == menuItemNewGame) {
			boardPanel = new BoardPanel();
			add(boardPanel,
					BorderLayout.CENTER);
			pack();
		} else if (o == menuItemManageConnections) {
			NetConfig conf = new NetConfig();
		}
	}

	public static void main(String[] args) {
		Client c = new Client();

	}
}
