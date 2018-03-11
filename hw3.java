package sddd;
import java.awt.Button;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.Format;
import java.text.ParseException;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import com.toedter.calendar.JDateChooser;

public class hw3 extends AbstractFormatter {
	int noOfOpen = 0;
	int noOfOpenUser = 0;
	String MainQueryToExecute = "";
	String MainUserQueryToExecute = "";
	DefaultTableModel reviewTable;
	DefaultTableModel reviewTableUser;
	JTable BusinessReviewTable;
	JTable UserReviewTable;
	JButton btnNewButton;
	JPanel BusinessTablePanel;
	JPanel BusinessTablePanel1;
	public ArrayList<String> selectedSubCat = new ArrayList();
	public ArrayList<String> selectedCat = new ArrayList();
	private JFrame frame;
	DefaultTableModel resultTable;
	DefaultTableModel resultTableuser;
	private JList tag_list;
	String staticquery = "";
	String staticqueryforCat = "";
	JTable BusinessTable;
	JTable BusinessTableuser;
	private int count = 0;
	public HashMap<Integer, String> bIDMap = new HashMap();
	public HashMap<Integer, String> UIDMap = new HashMap();

	/**
	 * Launch the application.
	 */

	private String datePattern = "yyyy-MM-dd";
	private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

	@Override
	public Object stringToValue(String text) throws ParseException {
		return dateFormatter.parseObject(text);
	}

	@Override
	public String valueToString(Object value) throws ParseException {
		if (value != null) {
			Calendar cal = (Calendar) value;
			return dateFormatter.format(cal.getTime());
		}

		return "";
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					hw3 window = new hw3();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	public hw3() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	int xrr = 0;
	int[] cat1 = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	String[] allCat = { "Active Life", "Arts & Entertainment", "Automotive", "Beauty & Spas", "Cafes", "Car Rental",
			"Convenience Stores", "Dentists", "Department Stores", "Doctors", "Drugstores", "Education",
			"Event Planning & Services", "Flowers & Gifts", "Food", "Grocery", "Hardware Stores", "Health & Medical",
			"Home & Garden", "Home Services", "Hospitals", "Hotels & Travel", "Medical Centers", "Nightlife",
			"Nurseries & Gardening", "Restaurants", "Shopping", "Transportation" };
	JTextField checkinTextField;
	private JTextField starRatingTextField;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField votesTextField;
	private JTextField textField;
	private JTextField textField_1;

	public void updateVar(int statement, int button, JPanel panel) {
		if (statement == -1) {
			cat1[button] = 0;
			xrr -= 1;
			int catInd = 0;
			String[] openCategories = new String[xrr];
			for (int ind = 0; ind < 28; ind++) {
				if (cat1[ind] == 1) {
					openCategories[catInd] = allCat[ind];
					catInd += 1;
				}
			}
			panel.removeAll();
			selectedCat.clear();
			for (String e : openCategories) {
				selectedCat.add(e);
			}
			if (selectedCat.size() == 0) {
				btnNewButton.setEnabled(false);
				updateSubCategory(panel, xrr, openCategories);
				panel.revalidate();
				panel.repaint();
			} else {
				updateSubCategory(panel, xrr, openCategories);
				panel.revalidate();
				panel.repaint();
			}
		} else {
			cat1[button] = 1;
			xrr += 1;
			int catInd = 0;
			String[] openCategories = new String[xrr];
			for (int ind = 0; ind < 28; ind++) {
				if (cat1[ind] == 1) {
					openCategories[catInd] = allCat[ind];
					catInd += 1;
				}
			}
			panel.removeAll();
			selectedCat.clear();
			for (String e : openCategories) {
				selectedCat.add(e);
			}
			updateSubCategory(panel, xrr, openCategories);
			panel.revalidate();
			panel.repaint();
		}

	}

	private void initialize() {

		JTabbedPane jtp = new JTabbedPane();
		int no_of_cat = 0;
		ArrayList<String> categoryName = new ArrayList<String>();
		PreparedStatement stmt = null;
		Connection conn = null;
		try {

			// Establish a connection
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "vardan", "XXXX");
		} catch (Exception e) {
			System.err.println("Got an exception! ");
			System.err.println(e.getMessage());
		}

		// Print the first Scroll box(Categories) starts
		try {
			String var = "SELECT  DISTINCT(CATEGORYNAME) FROM BusinessCategory where CATEGORYNAME in ('Active Life','Arts & Entertainment','Automotive','Car Rental','Cafes','Beauty & Spas','Convenience Stores','Dentists','Doctors','Drugstores','Department Stores','Education','Event Planning & Services','Flowers & Gifts','Food','Health & Medical','Home Services','Home & Garden','Hospitals','Hotels & Travel','Hardware Stores','Grocery','Medical Centers','Nurseries & Gardening','Nightlife','Restaurants','Shopping','Transportation') order by categoryname ";
			stmt = conn.prepareStatement(var);
			ResultSet rs;
			rs = stmt.executeQuery();

			while (rs.next()) {
				String subcat = rs.getString(1);
				categoryName.add(subcat);
				no_of_cat += 1;
			}
			conn.close();
			rs.close();
			stmt.close();

			frame = new JFrame();
			frame.setBounds(100, 100, 1536, 864);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			JPanel mainBusinessPanel = new JPanel();
			mainBusinessPanel.setBorder(new LineBorder(new Color(0, 0, 0), 2));
			mainBusinessPanel.setBounds(712, 16, 643, 343);
			// frame.getContentPane().add(panel_1);
			frame.getContentPane().add(jtp);
			jtp.add(mainBusinessPanel);
			mainBusinessPanel.setLayout(null);

			JPanel categoryPanel = new JPanel();
			categoryPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
			categoryPanel.setBounds(23, 10, 491, 237);
			mainBusinessPanel.add(categoryPanel);
			categoryPanel.setLayout(null);

			resultTable = new DefaultTableModel();
			resultTable.addColumn("Business Name");
			resultTable.addColumn("City");
			resultTable.addColumn("State");
			resultTable.addColumn("Stars");

			JScrollPane scroll2 = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
					JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			scroll2.setBounds(10, 10, 459, 200);
			categoryPanel.add(scroll2);
			JCheckBox[] array = new JCheckBox[no_of_cat];

			JPanel vardan = new JPanel();
			vardan.setLayout(new BoxLayout(vardan, BoxLayout.Y_AXIS));
			int h = 20;
			for (int i = 0; i < no_of_cat; i++) {
				array[i] = new JCheckBox(categoryName.get(i));
				array[i].setBounds(10, h, 150, 20);
				vardan.add(array[i]);
				h += 20;
			}
			scroll2.setViewportView(vardan);

			JPanel subCategoryPanel = new JPanel();
			subCategoryPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
			subCategoryPanel.setBounds(524, 10, 425, 237);
			mainBusinessPanel.add(subCategoryPanel);
			subCategoryPanel.setLayout(null);

			JPanel checkin_panel = new JPanel();
			checkin_panel.setLayout(null);
			checkin_panel.setBorder(new LineBorder(new Color(0, 0, 0)));
			checkin_panel.setBounds(630, 273, 146, 237);
			mainBusinessPanel.add(checkin_panel);

			JComboBox fromDayBox = new JComboBox();
			fromDayBox.setModel(new DefaultComboBoxModel(new String[] { "Select", "Monday", "Tuesday", "Wednesday",
					"Thursday", "Friday", "Saturday", "Sunday" }));
			fromDayBox.setSelectedIndex(0);
			fromDayBox.setBounds(10, 31, 76, 21);
			checkin_panel.add(fromDayBox);

			JLabel lblNewLabel = new JLabel("From");
			lblNewLabel.setBounds(10, 11, 45, 13);
			checkin_panel.add(lblNewLabel);

			JComboBox toDayBox = new JComboBox();
			toDayBox.setModel(new DefaultComboBoxModel(new String[] { "Select", "Monday", "Tuesday", "Wednesday",
					"Thursday", "Friday", "Saturday", "Sunday" }));
			toDayBox.setSelectedIndex(0);
			toDayBox.setBounds(10, 82, 76, 21);
			checkin_panel.add(toDayBox);

			JLabel lblTo = new JLabel("To");
			lblTo.setBounds(10, 62, 45, 13);
			checkin_panel.add(lblTo);

			JLabel lblNoOfCheckins = new JLabel("No. of Checkins");
			lblNoOfCheckins.setBounds(10, 128, 84, 13);
			checkin_panel.add(lblNoOfCheckins);

			JComboBox noCheckinBox = new JComboBox();
			noCheckinBox.setToolTipText("cdc");
			noCheckinBox.setModel(new DefaultComboBoxModel(new String[] {"> < =", ">", "<", "="}));
			noCheckinBox.setSelectedIndex(0);

			noCheckinBox.setBounds(10, 148, 84, 21);
			checkin_panel.add(noCheckinBox);

			checkinTextField = new JTextField();
			checkinTextField.setBounds(10, 191, 96, 19);
			checkin_panel.add(checkinTextField);
			checkinTextField.setColumns(10);

			JComboBox fromHourBox = new JComboBox();
			fromHourBox.setModel(
					new DefaultComboBoxModel(new String[] { "select", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
							"10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));

			fromHourBox.setBounds(96, 31, 40, 21);
			checkin_panel.add(fromHourBox);

			JComboBox toHourBox = new JComboBox();
			toHourBox.setModel(new DefaultComboBoxModel(new String[] { "select", "0", "1", "2", "3", "4", "5", "6", "7",
					"8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));
			toHourBox.setSelectedIndex(0);
			toHourBox.setBounds(96, 82, 40, 21);
			checkin_panel.add(toHourBox);

			JLabel lblHour = new JLabel("Hour");
			lblHour.setBounds(96, 11, 45, 13);
			checkin_panel.add(lblHour);

			JLabel label_2 = new JLabel("Hour");
			label_2.setBounds(96, 62, 45, 13);
			checkin_panel.add(label_2);

			JPanel reviewPanel = new JPanel();
			reviewPanel.setLayout(null);
			reviewPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
			reviewPanel.setBounds(803, 273, 146, 328);
			mainBusinessPanel.add(reviewPanel);

			JLabel label = new JLabel("From");
			label.setBounds(10, 11, 45, 13);
			reviewPanel.add(label);

			JPanel panel_3 = new JPanel();
			panel_3.setBorder(new LineBorder(new Color(0, 0, 0), 2));
			panel_3.setForeground(Color.BLACK);
			jtp.addTab("User", null, panel_3, null);
			jtp.addTab("Business", null, mainBusinessPanel, null);

			JPanel panel_4 = new JPanel();
			panel_4.setBackground(Color.WHITE);
			panel_4.setBounds(73, 21, 613, 307);
			panel_3.add(panel_4);
			panel_4.setLayout(null);

			// Adding the date picker starts
			JDateChooser Member_Since = new JDateChooser();
			Member_Since.setDateFormatString("yyyy-MM-dd");
			Member_Since.setBounds(289, 32, 119, 27);
			panel_4.add(Member_Since);

			// Adding the date picker starts
			JDateChooser fromDate = new JDateChooser();
			fromDate.setDateFormatString("yyyy-MM-dd");
			fromDate.setBounds(10, 25, 119, 27);
			reviewPanel.add(fromDate);

			// adding the date picker ends

			JLabel label_1 = new JLabel("To");
			label_1.setBounds(10, 62, 45, 13);
			reviewPanel.add(label_1);

			JLabel lblStarRating = new JLabel("Star Rating");
			lblStarRating.setBounds(10, 128, 84, 13);
			reviewPanel.add(lblStarRating);

			JComboBox reviewStarbox = new JComboBox();
			reviewStarbox.setModel(new DefaultComboBoxModel(new String[] { ">", "<", "=" }));
			reviewStarbox.setBounds(10, 148, 84, 21);
			reviewPanel.add(reviewStarbox);

			starRatingTextField = new JTextField();
			starRatingTextField.setColumns(10);
			starRatingTextField.setBounds(10, 191, 96, 19);
			reviewPanel.add(starRatingTextField);

			JDateChooser endDate = new JDateChooser();
			endDate.setDateFormatString("yyyy-MM-dd");
			endDate.setBounds(10, 85, 119, 27);
			reviewPanel.add(endDate);

			JComboBox comboBox_10 = new JComboBox();
			comboBox_10.setModel(new DefaultComboBoxModel(new String[] { ">", "<", "=" }));
			comboBox_10.setBounds(10, 248, 84, 21);
			reviewPanel.add(comboBox_10);

			votesTextField = new JTextField();
			votesTextField.setColumns(10);
			votesTextField.setBounds(10, 294, 96, 19);
			reviewPanel.add(votesTextField);

			JLabel lblVotes = new JLabel("Votes");
			lblVotes.setBounds(10, 225, 45, 13);
			reviewPanel.add(lblVotes);

			JLabel lblValues = new JLabel("Values");
			lblValues.setBounds(10, 279, 45, 13);
			reviewPanel.add(lblValues);

			// Button for submission starts
			btnNewButton = new JButton("Search");

			btnNewButton.addActionListener(new ActionListener() {
				Format formatter;
				private String endDateString;
				private String startDateString;
				private String finalQueryBusiness;
				private String fQuery;

				public void actionPerformed(ActionEvent arg0) {
					PreparedStatement stmt = null;
					Connection conn = null;
					try {

						// Establish a connection
						DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
						conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "vardan",
								"XXXX");
					} catch (Exception e) {
						System.err.println("Got an exception! ");
						System.err.println(e.getMessage());
					}
					formatter = new SimpleDateFormat("yyyy-MM-dd");
					// Fetching the start date from button starts
					if (fromDate.getDate() == null) {
						startDateString = "2";
					} else {
						startDateString = (formatter.format(fromDate.getDate()));
					}

					if (endDate.getDate() == null) {
						endDateString = "2";
					} else {
						endDateString = (formatter.format(endDate.getDate()));
					}
					// Fetching the start date from button ends

					// Generating the Review Query Starts
					int greaterOrNotRev = reviewStarbox.getSelectedIndex();
					String valueRev = "";
					if (greaterOrNotRev == 0) {
						valueRev = ">";
					}
					if (greaterOrNotRev == 1) {
						valueRev = "<";
					}
					if (greaterOrNotRev == 2) {
						valueRev = "=";
					}
					int starvalue = 0;
					String starTextBox = starRatingTextField.getText();
					if (!starTextBox.equals("")) {
						try {
							starvalue = Integer.parseInt(starTextBox);
						} catch (Exception e) {
							JOptionPane.showMessageDialog(frame, "Please input integer value in the no of checkin box",
									"A plain message", JOptionPane.PLAIN_MESSAGE);
						}
					} else {
						starvalue = -1;
					}

					int greaterOrNotVotes = comboBox_10.getSelectedIndex();
					String valueVote = "";
					if (greaterOrNotVotes == 0) {
						valueVote = ">";
					}
					if (greaterOrNotVotes == 1) {
						valueVote = "<";
					}
					if (greaterOrNotVotes == 2) {
						valueVote = "=";
					}
					int votevalue = 0;
					String voteTextBox = votesTextField.getText();
					if (!voteTextBox.equals("")) {
						try {
							votevalue = Integer.parseInt(voteTextBox);
						} catch (Exception e) {
							JOptionPane.showMessageDialog(frame, "Please input integer value in the no of checkin box",
									"A plain message", JOptionPane.PLAIN_MESSAGE);
						}
					} else {
						votevalue = -1;
					}

					boolean reviewOrnot = false;



					// Generating the Review query ends

					// Fetching the end date starts
					String endDatestring;
					if (endDate.getDate() == null) {
						endDatestring = "2";
					} else {
						endDatestring = (formatter.format(endDate.getDate()));
					}

					// // Fetching of the days to be fetched ends

					// Getting the no of check in greater or less starts
					int checkinStatus = noCheckinBox.getSelectedIndex();
					// Getting the no of check in greater or less ends

					// Getting the no of star greater or less starts
					int starStatus = reviewStarbox.getSelectedIndex();
					// Getting the no of star greater or less ends

					// Fetching the start time and end time starts
					int startIndexTime = fromHourBox.getSelectedIndex();
					int endIndexTime = toHourBox.getSelectedIndex();
					// Fetching the start time and end time ends

					// Getting the values from the text boxes starts
					String checkinTextBox = checkinTextField.getText();
					int checkinvalue = 0;
					if (!checkinTextBox.equals("")) {
						try {
							checkinvalue = Integer.parseInt(checkinTextBox);
						} catch (Exception e) {
							JOptionPane.showMessageDialog(frame, "Please input integer value in the no of checkin box",
									"A plain message", JOptionPane.PLAIN_MESSAGE);
						}
					} else {
						checkinvalue = 0;
					}

					// Getting the values from the text boxes ends

					// Generation of main query starts
					String value = "";
					int greaterOrNot = noCheckinBox.getSelectedIndex();

					if (greaterOrNot == 1) {
						value = ">";
					}
					if (greaterOrNot == 2) {
						value = "<";
					}
					if (greaterOrNot == 3) {
						value = "=";
					}
					String finalCheckinQuery = "";
					if (startDateString.equals("2") || endDateString.equals("2")) {
					}



					// Generation of main query ends

					// Generating query based on categories starts THIS IS THE MAIN QUERY PART
					// VARDANGUPTA

					int lengthOfcat = selectedCat.size();
					int lenOfSubCat = selectedSubCat.size();
					String categoryQuery = "";

					String catquery = "";
					String revQuery = "";
					String checkinQuery = "";
					if (lengthOfcat == 0) {
						JOptionPane.showMessageDialog(frame, "Please select a category", "A plain message",
								JOptionPane.PLAIN_MESSAGE);
					} else {
						if (lenOfSubCat == 0) {
							catquery = staticqueryforCat + ")";
						} else {
							String dynamic = "";
							for (int ind = 0; ind < lenOfSubCat; ind++) {
								dynamic = dynamic + "'" + selectedSubCat.get(ind) + "'" + ",";
							}
							dynamic = dynamic.replaceAll(",$", "");
							catquery = staticqueryforCat + "and c.SUBCATEGORYNAME in (" + dynamic + "))";
						}

						checkinQuery = finalCheckinQuery + "and businessid in (" + catquery + ")";


						String query1 = "";
						for (int catIndex = 0; catIndex < selectedCat.size(); catIndex++) {
							if(catIndex!=selectedCat.size()-1) {
							query1 = query1 + "(SELECT businessid FROM BusinessCategory WHERE categoryname ="+"'" + selectedCat.get(catIndex) + "'" + ")";
							query1+="AND BusinessId IN";
							}
							else {
								query1 = query1 + "(SELECT businessid FROM BusinessCategory WHERE categoryname ="+"'" + selectedCat.get(catIndex) + "'" + ")";
							}
						}
						
//						String finalQuery1 = "SELECT categoryname FROM BusinessCategory WHERE categoryname IN (" + query1 + ")";
						String finalQuery1=query1;
						String query2 = "";
						String finalQuery2 = "";
						if(selectedSubCat.size()>0) {
						query2="AND BusinessId IN";
						for (int catIndex = 0; catIndex < selectedSubCat.size(); catIndex++) {
							if(catIndex!=selectedSubCat.size()-1) {
							query2 = query2 + "(SELECT businessid FROM BusinessSubCategory WHERE SubCategoryName ="+"'" + selectedSubCat.get(catIndex) + "'" + ")";
							query2+="AND BusinessId IN";
							}
							else {
								query2 = query2 + "(SELECT businessid FROM BusinessSubCategory WHERE SubCategoryName ="+"'" + selectedSubCat.get(catIndex) + "'" + ")";
							}
						}
						finalQuery2=query2;
						}
						

						String[] days = new String[] { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday",
								"Saturday", "Sunday" };
						int startIndexDays = fromDayBox.getSelectedIndex();
						int endIndexDays = toDayBox.getSelectedIndex();
						ArrayList<String> allDays = new ArrayList();
						String daysToString="";
						if (startIndexDays>0) {
						 for (int day = startIndexDays-1; day < 100; day++) {
						 if (days[day % 7] == days[endIndexDays-1]) {
						 allDays.add(days[day % 7]);
						 break;
						 }
						 allDays.add(days[day % 7]);
						 }
						 
						 
						 for(int h=1;h<allDays.size()-1;h++) {
							 daysToString+="'"+allDays.get(h)+"',";
						 }
						 daysToString = daysToString.replaceAll(",$", "");

						}
						
						// Fetching of the days to be fetched ends
						String query3 = "";
						String finalQuery3 = "";
						String tmp="";
						if (fromDayBox.getSelectedIndex() != 0 && toDayBox.getSelectedIndex() != 0
								&& fromHourBox.getSelectedIndex() != 0 && toHourBox.getSelectedIndex() != 0) {
							if (startIndexDays == endIndexDays) {
								query3 = " AND BUSINESSID IN (SELECT BusinessId FROM CheckIN WHERE CheckinDay = "
										+ "'"+ days[startIndexDays - 1] + "'"+ " AND CheckinHour BETWEEN " + (startIndexTime - 1)
										+ " AND " + (endIndexTime - 1) + ")";
							} else {
								int i = startIndexDays - 1, j = endIndexDays - 1;
								
								query3 = "AND BusinessId IN (SELECT BusinessId FROM CheckIN WHERE CheckinDay ="+ "'"+
										 days[startIndexDays - 1] + "'"+ " AND CheckinHour >=" + (startIndexTime - 1) + "\n"
										+ "UNION \n" + "SELECT BusinessId FROM CheckIN WHERE CheckinDay IN "
										+ "("+daysToString+")"+ "\n" + "UNION \n"
										+ "SELECT BusinessId FROM CheckIN WHERE CheckinDay =" + "'"+ days[endIndexDays - 1]
										+ "'"+ " AND  CheckinHour < =" + (endIndexTime - 1) + ")";

							}
							finalQuery3 = query3;
						} else if (fromDayBox.getSelectedIndex() == 0 && toDayBox.getSelectedIndex() == 0
								&& fromHourBox.getSelectedIndex() != 0 && toHourBox.getSelectedIndex() != 0) {
							query3 = " AND BUSINESSID IN (SELECT BusinessId FROM CheckIN WHERE CheckinHour BETWEEN "
									+ (startIndexTime - 1) + " AND " + (endIndexTime - 1) + ")";
							finalQuery3 = query3;
						} else if (fromDayBox.getSelectedIndex() != 0 && toDayBox.getSelectedIndex() != 0
								&& fromHourBox.getSelectedIndex() == 0 && toHourBox.getSelectedIndex() == 0) {
							query3 = " AND BUSINESSID IN (SELECT BusinessId FROM CheckIN WHERE CheckinDay IN "
									+  "("+daysToString+")"+ ")";
							finalQuery3 = query3;
						}
						String query4 = "";
						String finalQuery4 = "";
						if (noCheckinBox.getSelectedIndex() != 0) {
							query4 = value;
							finalQuery4 = " AND BUSINESSID IN (SELECT BusinessId FROM CheckIN WHERE CheckinCount "
									+ query4 + " " + checkinvalue + ")";
						}
						String query5 = "";
						String finalQuery5 = "";
						String currentDate = "2019-12-30";
						if (!startDateString.equals("2") && starvalue == -1 && votevalue == -1) {
							if (endDateString.equals("2")) {
								finalQuery5 = "AND BusinessID IN (SELECT BusinessId FROM Review WHERE RDate BETWEEN "
										+ "'" + startDateString + "'" + " AND " + "'" + currentDate + "'" + ")";
							} else {
								finalQuery5 = "AND BusinessID IN (SELECT BusinessId FROM Review WHERE RDate BETWEEN "
										+ "'" + startDateString + "'" + " AND " + "'" + endDateString + "'" + ")";
							}
						} else if (startDateString.equals("2") && starvalue != -1 && votevalue == -1) {
							finalQuery5 = "AND BusinessID IN (SELECT BusinessId FROM Review WHERE Stars " + valueRev
									+ " " + starvalue + ")";
						} else if (startDateString.equals("2") && starvalue == -1 && votevalue != -1) {
							finalQuery5 = "AND BusinessID IN (SELECT BusinessId FROM Review WHERE (funnyvote+coolvote+usefulvote) "
									+ valueVote + " " + votevalue + ")";
						} else if (!startDateString.equals("2") && starvalue != -1 && votevalue == -1) {
							if (endDateString.equals("2")) {
								finalQuery5 = "AND BusinessID IN (SELECT BusinessId FROM Review WHERE RDate BETWEEN "
										+ "'" + startDateString + "'" + " AND " + "'" + currentDate + "'"
										+ " AND Stars " + valueRev + " " + starvalue + ")";
							} else
								finalQuery5 = "AND BusinessID IN (SELECT BusinessId FROM Review WHERE RDate BETWEEN "
										+ "'" + startDateString + "'" + " AND " + "'" + endDateString + "'"
										+ " AND Stars " + valueRev + " " + starvalue + ")";
						} else if (!startDateString.equals("2") && starvalue == -1 && votevalue != -1) {
							if (endDateString.equals("2")) {
								finalQuery5 = "AND BusinessID IN (SELECT BusinessId FROM Review WHERE RDate BETWEEN "
										+ "'" + startDateString + "'" + " AND " + "'" + currentDate + "'"
										+ " AND (funnyvote+coolvote+usefulvote) " + valueVote + " " + votevalue + ")";
							} else
								finalQuery5 = "AND BusinessID IN (SELECT BusinessId FROM Review WHERE RDate BETWEEN "
										+ "'" + startDateString + "'" + " AND " + "'" + endDateString + "'"
										+ " AND (funnyvote+coolvote+usefulvote) " + valueVote + " " + votevalue + ")";
						} else if (startDateString.equals("2") && starvalue != -1 && votevalue != -1)
							finalQuery5 = "AND BusinessID IN (SELECT BusinessId FROM Review WHERE Stars " + valueRev
									+ " " + starvalue + " AND (funnyvote+coolvote+usefulvote) " + valueVote + " " + votevalue + ")";
						else if (!startDateString.equals("2") && starvalue != -1 && votevalue != -1) {
							if (endDateString.equals("2"))
								finalQuery5 = "AND BusinessID IN (SELECT BusinessId FROM Review WHERE RDate BETWEEN "
										+ "'" + startDateString + "'" + " AND " + "'" + currentDate + "'"
										+ " AND Stars " + valueRev + " " + starvalue + " AND (funnyvote+coolvote+usefulvote) " + valueVote
										+ " " + votevalue + ")";
							else
								finalQuery5 = "AND BusinessID IN (SELECT BusinessId FROM Review WHERE RDate BETWEEN "
										+ "'" + startDateString + "'" + " AND " + "'" + endDateString + "'"
										+ " AND Stars " + valueRev + " " + starvalue + " AND (funnyvote+coolvote+usefulvote) " + valueVote
										+ " " + votevalue + ")";
						}
						fQuery="SELECT DISTINCT b.BName as BName, b.City as City, b.State as State, b.Stars as Stars, b.BusinessId as BusinessId "+
										"FROM BUSINESS b \r\n"+ "WHERE BusinessId IN  ";
						fQuery += finalQuery1 + "\r\n"+ finalQuery2 +"\r\n"+finalQuery3+"\r\n"+finalQuery4+"\r\n"+finalQuery5 ;



						finalQueryBusiness = "";
					}

					// Generating query based on categories ends VARDANGUPTA
					textField.setText("");
					textField.setText(textField.getText() + fQuery); 
					// Clean up the table starts
					if (BusinessTable.getRowCount() > 0) {
						for (int i = BusinessTable.getRowCount() - 1; i > -1; i--) {
							resultTable.removeRow(i);
						}

					}
					// Clean up the table ends
					String[] rowObj = new String[4];
					int i = 0;
					 String FinalQuery = fQuery;
					 try {
					 bIDMap.clear();
					 PreparedStatement stmts = null;
					 Connection conns = null;
					 DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
					 conns = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl",
					 "vardan",
					 "XXXX");
					 stmts = conns.prepareStatement(FinalQuery);
					 ResultSet rs = stmts.executeQuery(FinalQuery);
					 while (rs.next()) {
					 rowObj = new String[] { rs.getString("BName"), rs.getString("City"),
					 rs.getString("State"),
					 rs.getString("Stars") };
					 bIDMap.put(i++, rs.getString("BusinessId"));
					 resultTable.addRow(rowObj);
					 }
					 stmts.close();
					 rs.close();
					 } catch (Exception ex) {
					 System.out.println(ex);
					 }
					
					 openReviewFrame(noOfOpen);
					 noOfOpen++;
					
				}
			});

			// Table for business
			resultTable = new DefaultTableModel();
			resultTable.addColumn("Business Name");
			resultTable.addColumn("City");
			resultTable.addColumn("State");
			resultTable.addColumn("Stars");
			btnNewButton.setBounds(630, 538, 146, 41);
			mainBusinessPanel.add(btnNewButton);

			// Table for users
			resultTableuser = new DefaultTableModel();
			resultTableuser.addColumn("Name");
			resultTableuser.addColumn("Yelping_since");
			resultTableuser.addColumn("Avg. Star");

			// Adding panel to the business page starts
			JPanel BusinessTablePanel = new JPanel();
			BusinessTablePanel.setBorder(new LineBorder(new Color(0, 0, 0), 4, true));
			mainBusinessPanel.add(BusinessTablePanel);
			BusinessTablePanel.setVisible(true);
			BusinessTablePanel.setBounds(983, 10, 332, 699);
			BusinessTablePanel.setLayout(null);
			panel_3.setLayout(null);

			JScrollPane scrollPane_4 = new JScrollPane();
			scrollPane_4.setBounds(10, 10, 300, 680);
			BusinessTablePanel.add(scrollPane_4);
			BusinessTable = new JTable();
			scrollPane_4.setViewportView(BusinessTable);
			BusinessTable.setModel(resultTable);
			BusinessTable.setBorder(new LineBorder(new Color(0, 0, 0)));
			// Adding panel to the business page ends

			// Adding panel to the user page starts
			JPanel BusinessTablePanel2 = new JPanel();
			BusinessTablePanel2.setBorder(new LineBorder(new Color(0, 0, 0), 4, true));
			panel_3.add(BusinessTablePanel2);
			BusinessTablePanel2.setVisible(true);
			BusinessTablePanel2.setBounds(773, 21, 332, 669);
			BusinessTablePanel2.setLayout(null);

			JScrollPane scrollPane_5 = new JScrollPane();
			scrollPane_5.setBounds(10, 10, 300, 649);
			BusinessTablePanel2.add(scrollPane_5);
			BusinessTableuser = new JTable();
			scrollPane_5.setViewportView(BusinessTableuser);
			BusinessTableuser.setModel(resultTableuser);
			BusinessTableuser.setBorder(new LineBorder(new Color(0, 0, 0)));
			// Adding panel to the user page ends

			JComboBox comboBox_3 = new JComboBox();
			comboBox_3.setModel(new DefaultComboBoxModel(new String[] { ">", "<", "=" }));
			comboBox_3.setBounds(150, 90, 95, 21);
			panel_4.add(comboBox_3);

			JComboBox comboBox_4 = new JComboBox();
			comboBox_4.setModel(new DefaultComboBoxModel(new String[] { ">", "<", "=" }));
			comboBox_4.setBounds(150, 138, 95, 21);
			panel_4.add(comboBox_4);

			JComboBox comboBox_8 = new JComboBox();
			comboBox_8.setModel(new DefaultComboBoxModel(new String[] { ">", "<", "=" }));
			comboBox_8.setBounds(150, 189, 95, 21);
			panel_4.add(comboBox_8);

			JComboBox comboBox_9 = new JComboBox();
			comboBox_9.setModel(new DefaultComboBoxModel(new String[] { "AND", "OR" }));
			comboBox_9.setBounds(292, 244, 95, 21);
			panel_4.add(comboBox_9);

			textField_2 = new JTextField(0);
			textField_2.setBounds(425, 91, 96, 19);
			panel_4.add(textField_2);
			textField_2.setColumns(10);

			textField_3 = new JTextField();
			textField_3.setColumns(10);
			textField_3.setBounds(425, 139, 96, 19);
			panel_4.add(textField_3);

			textField_4 = new JTextField();
			textField_4.setColumns(10);
			textField_4.setBounds(425, 190, 96, 19);
			panel_4.add(textField_4);

			JLabel lblNewLabel_1 = new JLabel("Value");
			lblNewLabel_1.setBounds(342, 94, 45, 13);
			panel_4.add(lblNewLabel_1);

			JLabel label_3 = new JLabel("Value");
			label_3.setBounds(342, 142, 45, 13);
			panel_4.add(label_3);

			JLabel label_4 = new JLabel("Value");
			label_4.setBounds(342, 193, 45, 13);
			panel_4.add(label_4);

			JLabel lblNewLabel_2 = new JLabel("Member Since");
			lblNewLabel_2.setBounds(150, 32, 95, 27);
			panel_4.add(lblNewLabel_2);

			JLabel lblReviewCount = new JLabel("Review Count");
			lblReviewCount.setBounds(45, 94, 95, 13);
			panel_4.add(lblReviewCount);

			JLabel lblNoOfFriends = new JLabel("No. Of. Friends");
			lblNoOfFriends.setBounds(45, 142, 95, 13);
			panel_4.add(lblNoOfFriends);

			JLabel lblAvgStar = new JLabel("Avg. Star");
			lblAvgStar.setBounds(45, 193, 75, 13);
			panel_4.add(lblAvgStar);

			JLabel lblSelect = new JLabel("Select");
			lblSelect.setBounds(200, 252, 45, 13);
			panel_4.add(lblSelect);

			// User Button Functionality Starts
			JButton btnNewButton_1 = new JButton("Search");
			btnNewButton_1.setBounds(279, 387, 224, 39);
			panel_3.add(btnNewButton_1);
			
			textField_1 = new JTextField();
			textField_1.setBounds(73, 505, 613, 168);
			panel_3.add(textField_1);
			textField_1.setColumns(10);
			btnNewButton_1.addActionListener(new ActionListener() {
				Format formatter;

				public void actionPerformed(ActionEvent arg0) {

					formatter = new SimpleDateFormat("yyyy-MM");
					// Fetching the start date from button starts
					String MemberSinceValue;
					if (Member_Since.getDate() == null) {
						MemberSinceValue = "2";
					} else {
						MemberSinceValue = (formatter.format(Member_Since.getDate()));
					}

					int greaterOrNotRev = comboBox_3.getSelectedIndex();
					String valueRev = "";
					if (greaterOrNotRev == 0) {
						valueRev = ">";
					}
					if (greaterOrNotRev == 1) {
						valueRev = "<";
					}
					if (greaterOrNotRev == 2) {
						valueRev = "=";
					}
					String checkinTextBoxRev = textField_2.getText();
					int revvalue = 0;

					String revTextBox = textField_2.getText();
					if (!revTextBox.equals("")) {
						try {
							revvalue = Integer.parseInt(checkinTextBoxRev);
						} catch (Exception e) {
							JOptionPane.showMessageDialog(frame, "Please input integer value in the no of checkin box",
									"A plain message", JOptionPane.PLAIN_MESSAGE);
						}
					} else {
						revvalue = -1;
					}

					int greaterOrNotFriends = comboBox_4.getSelectedIndex();
					String valueFriends = "";
					if (greaterOrNotFriends == 0) {
						valueFriends = ">";
					}
					if (greaterOrNotFriends == 1) {
						valueFriends = "<";
					}
					if (greaterOrNotFriends == 2) {
						valueFriends = "=";
					}
					String FriendTextBoxRev = textField_3.getText();

					int FriendValueDD = 0;

					String FriendTextBox = textField_3.getText();
					if (!FriendTextBox.equals("")) {
						try {
							FriendValueDD = Integer.parseInt(FriendTextBoxRev);
						} catch (Exception e) {
							JOptionPane.showMessageDialog(frame, "Please input integer value in the no of checkin box",
									"A plain message", JOptionPane.PLAIN_MESSAGE);
						}
					} else {
						FriendValueDD = -1;
					}

					int greaterOrNotStars = comboBox_8.getSelectedIndex();
					String valueStars = "";
					if (greaterOrNotStars == 0) {
						valueStars = ">";
					}
					if (greaterOrNotStars == 1) {
						valueStars = "<";
					}
					if (greaterOrNotStars == 2) {
						valueStars = "=";
					}
					String StarTextBoxRev = textField_4.getText();

					float StarsValueDD = 0.0f;

					String StarTextBox = textField_4.getText();
					if (!StarTextBox.equals("")) {
						try {
							StarsValueDD = Float.parseFloat(StarTextBoxRev);
						} catch (Exception e) {
							JOptionPane.showMessageDialog(frame, "Please input integer value in the no of checkin box",
									"A plain message", JOptionPane.PLAIN_MESSAGE);
						}
					} else {
						StarsValueDD = -1;
					}

					int AndOrSelector = comboBox_9.getSelectedIndex();
					String AndOrSelectorValue = "";
					if (AndOrSelector == 0) {
						AndOrSelectorValue = "AND";
					}
					if (AndOrSelector == 1) {
						AndOrSelectorValue = "OR";
					}

					// Clean up the table starts
					if (BusinessTableuser.getRowCount() > 0) {
						for (int i = BusinessTableuser.getRowCount() - 1; i > -1; i--) {
							resultTableuser.removeRow(i);
						}
					}

					MainUserQueryToExecute = "SELECT y.userid as userid,y.username as username, y.yelping_since as Yelping_Since,(SELECT avg(stars) from review rx where rx.userid=y.userid) as stars FROM YelpUser y  where y.YELPING_SINCE >="
							+ " " + "'" + MemberSinceValue + "'" + " " + AndOrSelectorValue + "  "
							+ "(SELECT count(f.userid) from friends f where f.userid=y.userid)" + " " + valueFriends
							+ FriendValueDD + " " + AndOrSelectorValue + " "
							+ "(SELECT avg(stars) from review where userid=y.userid)" + valueStars + StarsValueDD;
					textField_1.setText("");
					textField_1.setText(textField_1.getText() + MainUserQueryToExecute);
					
					// Clean up the table ends
					String[] rowObj = new String[3];
					int i = 0;
					String FinalQuery = MainUserQueryToExecute;
					try {
						PreparedStatement stmts = null;
						Connection conns = null;
						DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
						conns = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "vardan",
								"XXXX");
						stmts = conns.prepareStatement(FinalQuery);
						ResultSet rs = stmts.executeQuery(FinalQuery);
						while (rs.next()) {
							rowObj = new String[] { rs.getString("USERNAME"), rs.getString("Yelping_Since"),
									rs.getString("stars") };
							UIDMap.put(i++, rs.getString("userid"));
							resultTableuser.addRow(rowObj);
						}
						stmts.close();
						rs.close();
					} catch (Exception ex) {
						System.out.println(ex);
					}
					openUserReviewFrame(noOfOpenUser);
					noOfOpenUser++;
				}
			});
			// User Button functionality ends
			// Button for submission ends
			btnNewButton.setEnabled(false);
			
			textField = new JTextField();
			textField.setBounds(23, 273, 580, 271);
			mainBusinessPanel.add(textField);
			textField.setColumns(10);

			// Adding of listener to every check box starts
			int i = 0;
			for (i = 0; i < no_of_cat; i++) {
				Object x = array[i];
				int pi = i;
				array[i].addItemListener((ItemListener) new ItemListener() {
					public void itemStateChanged(ItemEvent e) {

						if (((AbstractButton) x).isSelected() == true) {
							btnNewButton.setEnabled(true);
							updateVar(1, pi, subCategoryPanel);
						} else {
							updateVar(-1, pi, subCategoryPanel);
						}
						// Dynamic nature started
					}
					// Dynamic nature ended
				});
			}

		}

		catch (SQLException e) {
			e.printStackTrace();
		}
		// Print the first Scroll box(Categories) ends

	}

	private void updateSubCategory(JPanel panel, int numbers, String[] categ) {
		selectedSubCat.clear();
		ArrayList<String> categoryName = new ArrayList<String>();
		PreparedStatement stmt = null;
		Connection conn = null;
		int noOfSubcat = 0;
		try {

			// Establish a connection
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "vardan", "XXXX");
		} catch (Exception e) {
			System.err.println("Got an exception! ");
			System.err.println(e.getMessage());
		}
		String dynamic = "";
		for (int ind = 0; ind < numbers; ind++) {
			dynamic = dynamic + "'" + categ[ind] + "'" + ",";
		}
		dynamic = dynamic.replaceAll(",$", "");
		staticquery = "select distinct(SUBCATEGORYNAME) from BusinessSubCategory where BUSINESSID in (select b.businessid from businesscategory b where b.CATEGORYNAME in ("
				+ dynamic + ")) order by subcategoryname  ";
		staticqueryforCat = "select distinct(BUSINESSID) from BusinessSubCategory where BUSINESSID in (select b.businessid from businesscategory b,BusinessSubCategory c where b.businessid=c.businessid and b.CATEGORYNAME in ("
				+ dynamic + ") ";
		// and SUBCATEGORYNAME in ()
		try {
			stmt = conn.prepareStatement(staticquery);
			ResultSet rs;
			rs = stmt.executeQuery();

			while (rs.next()) {
				String subcat = rs.getString(1);
				categoryName.add(subcat);
				noOfSubcat += 1;
			}
			conn.close();
			rs.close();
			stmt.close();

			JScrollPane scroll3 = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
					JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			scroll3.setBounds(10, 10, 330, 200);
			panel.add(scroll3);
			JCheckBox[] scroll2val = new JCheckBox[noOfSubcat];
			JPanel vardan2 = new JPanel();
			vardan2.setLayout(new BoxLayout(vardan2, BoxLayout.Y_AXIS));
			int h2 = 20;
			for (int i = 0; i < noOfSubcat; i++) {
				scroll2val[i] = new JCheckBox(categoryName.get(i));
				scroll2val[i].setBounds(10, h2, 150, 20);
				vardan2.add(scroll2val[i]);
				h2 += 20;
			}
			scroll3.setViewportView(vardan2);

			// Adding of listener to every check box starts
			int i = 0;
			for (i = 0; i < noOfSubcat; i++) {
				Object x = scroll2val[i];
				int pi = i;
				scroll2val[i].addItemListener((ItemListener) new ItemListener() {
					public void itemStateChanged(ItemEvent e) {

						if (((AbstractButton) x).isSelected() == true) {
							selectedSubCat.add(((AbstractButton) x).getText());

						} else {
							selectedSubCat.remove(((AbstractButton) x).getText());

						}
						// Dynamic nature started
					}
					// Dynamic nature ended
				});
			}
			// Adding of listener to every check box

		} catch (Exception e) {
		}

	}

	private void openReviewFrame(int n) {

		BusinessTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				ResultSet rs;
				PreparedStatement stmt = null;
				Connection conn = null;
				try {
					DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
					conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "vardan", "XXXX");
				} catch (Exception ex) {
					System.err.println("Got an exception! ");
					System.err.println(ex.getMessage());
				}
				if (e.getClickCount() == 1 && n == noOfOpen - 1) {
					JFrame frame = new JFrame("Business Review");
					JPanel panel = new JPanel();
					panel.setLayout(new FlowLayout());
					JTable target = (JTable) e.getSource();
					int row = target.getSelectedRow();

					BusinessReviewTable = new JTable();
					BusinessReviewTable.setBounds(952, 75, 938, 691);

					reviewTable = new DefaultTableModel();
					BusinessReviewTable.setModel(reviewTable);
					reviewTable.addColumn("Business Name");
					reviewTable.addColumn("Text");
					reviewTable.addColumn("Stars");
					JScrollPane reviewResultPane = new JScrollPane(BusinessReviewTable);
					panel.add(reviewResultPane);

					String[] rowObj = new String[3];
					try {
						CallableStatement call = conn.prepareCall("SELECT * FROM review where businessid=?");
						call.setString(1, bIDMap.get(row));
						rs = call.executeQuery();
						while (rs.next()) {
							rowObj = new String[] { rs.getString("BUSINESSID"), rs.getString("RTEXT"),
									rs.getString("STARS") };
							reviewTable.addRow(rowObj);
						}
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					frame.getContentPane().add(panel);
					frame.setSize(1000, 1000);
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
				}
			}
		});

	}

	private void openUserReviewFrame(int n) {

		BusinessTableuser.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				ResultSet rs;
				PreparedStatement stmt = null;
				Connection conn = null;
				try {

					// Establish a connection
					DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
					conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "vardan", "XXXX");
				} catch (Exception ex) {
					System.err.println("Got an exception! ");
					System.err.println(ex.getMessage());
				}
				if (e.getClickCount() == 1 && n == noOfOpenUser - 1) {
					JFrame frame = new JFrame("User Review");
					JPanel panel = new JPanel();
					panel.setLayout(new FlowLayout());
					JTable target = (JTable) e.getSource();
					int row = target.getSelectedRow();

					UserReviewTable = new JTable();
					UserReviewTable.setBounds(952, 75, 938, 691);

					reviewTableUser = new DefaultTableModel();
					UserReviewTable.setModel(reviewTableUser);
					reviewTableUser.addColumn("User Name");
					reviewTableUser.addColumn("Review Text");
					reviewTableUser.addColumn("Stars");
					JScrollPane reviewResultPane = new JScrollPane(UserReviewTable);
					panel.add(reviewResultPane);

					String[] rowObj = new String[3];
					try {
						CallableStatement call = conn.prepareCall("SELECT * FROM review where userid=?");
						call.setString(1, UIDMap.get(row));
						rs = call.executeQuery();
						while (rs.next()) {
							rowObj = new String[] { rs.getString("USERID"), rs.getString("RTEXT"),
									rs.getString("STARS") };
							reviewTableUser.addRow(rowObj);
						}
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					frame.getContentPane().add(panel);
					frame.setSize(1000, 1000);
					frame.setLocationRelativeTo(null);
					// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame.setVisible(true);
				}
			}
		});

	}
}
