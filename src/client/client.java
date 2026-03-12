package client;

import shared.board;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Enumeration;

class client extends JFrame implements ActionListener {
	private int square_clicked = -1;
	private final int delta = 60;
	int origin_x;
	int origin_y;
	JMenuItem menuitem_manage_connections;
	JMenuItem menuitem_new_game;
	board_panel board_panel;

	public client() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JMenuBar menubar = new JMenuBar();

		JMenu menu_game = new JMenu("Game");
		JMenu menu_connect = new JMenu("Connections");
		menubar.add(menu_game);
		menubar.add(menu_connect);

		menuitem_new_game = new JMenuItem("New Game", KeyEvent.VK_N);
		menuitem_manage_connections = new JMenuItem("Manage_connections");

		menuitem_new_game.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		menuitem_new_game.addActionListener(this);
		menuitem_manage_connections.addActionListener(this);

		menu_game.add(menuitem_new_game);
		menu_connect.add(menuitem_manage_connections);

		setJMenuBar(menubar);
		setVisible(true);
	}

	class net_config extends JDialog implements ActionListener {
		private JButton select;
		private JButton cancel;

		public net_config() {
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

	class board_panel extends JPanel implements MouseListener {
		private final int board_panel_size_pixels = 600;
		private board b;

		public board_panel() {
			addMouseListener(this);
			sound_effect s = new sound_effect("effects/board_setup.wav");
			s.play();
			b = new board();
		}

		public Dimension getPreferredSize() {
			return new Dimension(board_panel_size_pixels, board_panel_size_pixels);
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.setColor(Color.gray);
			// center board
			Dimension d = this.getSize();
			origin_x = (d.width - (b.side_size * delta)) / 2;
			origin_y = (d.height - (b.side_size * delta)) / 2;

			// System.out.printf("origin x: %d\norigin y: %d\n", origin_x, origin_y);

			// draw grid
			for (int line = 0; line <= b.side_size; line++) {
				g.drawLine(origin_x, origin_y + line * delta, origin_x + b.side_size * delta,
						origin_y + line * delta);
				g.drawLine(origin_x + line * delta, origin_y, origin_x + line * delta,
						origin_y + b.side_size * delta);
			}
			final int circle_diameter = 2 * delta / 3;

			// draw pieces
			for (int y_index = 0; y_index < b.side_size; y_index++) {
				for (int x_index = 0; x_index < b.side_size; x_index++) {
					int board_array_idx = x_index + y_index * b.side_size;
					if (board_array_idx == square_clicked) {
						g.setColor(Color.green);
						g.fillRect(origin_x + 1 + delta * x_index,
								origin_y + 1 + delta * y_index,
								delta - 1, delta - 1);
					}
					if (b.board[board_array_idx] == b.BLACK_PAWN
							|| b.board[x_index] == b.BLACK_KING)
						g.setColor(Color.BLACK);
					else if (b.board[board_array_idx] == b.RED_PAWN
							|| b.board[x_index] == b.RED_KING)
						g.setColor(Color.RED);
					else
						continue;
					g.fillOval((origin_x - circle_diameter / 2 + delta / 2)
							+ delta * x_index,
							(origin_y - circle_diameter / 2 + delta / 2)
									+ delta * y_index,
							circle_diameter,
							circle_diameter);
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
			if (p.y < b.side_size * delta + origin_y && p.y > origin_y
					&& p.x < b.side_size * delta + origin_x
					&& p.x > origin_x) { // make sure click is within bounds
				square_clicked = ((p.y - origin_y) / delta) * b.side_size
						+ (p.x - origin_x) / delta;
				// System.out.printf("clicked on %d\n", square_clicked);
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

		// public enum click_state {
		// NONE {
		// @Override
		// public click_state next() {
		// return FIRST;
		// if(b. == )
		// }
		// },

		// FIRST {
		// @Override
		// public click_state next() {
		// return NONE;
		// }
		// };

		// public abstract click_state next();
		// }
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if (o == menuitem_new_game) {
			board_panel = new board_panel();
			add(board_panel,
					BorderLayout.CENTER);
			pack();
		} else if (o == menuitem_manage_connections) {
			net_config conf = new net_config();
		}
	}

	public static void main(String[] args) {
		client c = new client();

	}
}
