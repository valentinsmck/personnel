package gui;

import personnel.DateInvalide;
import personnel.Employe;
import personnel.GestionPersonnel;
import personnel.Ligue;
import personnel.SauvegardeImpossible;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PersonnelFrame extends JFrame
{
	private static final String PAGE_LOGIN = "page.login";
	private static final String PAGE_MENU = "page.menu";
	private static final String PAGE_ROOT = "page.root";
	private static final String PAGE_LIGUES = "page.ligues";
	private static final String PAGE_LIGUE_EDIT = "page.ligue.edit";
	private static final String PAGE_EMPLOYES = "page.employes";
	private static final String PAGE_EMPLOYE_DETAIL = "page.employe.detail";
	private static final String PAGE_EMPLOYE_EDIT = "page.employe.edit";

	private static final Color COLOR_BG = new Color(3, 6, 9);
	private static final Color COLOR_CARD = new Color(14, 16, 20);
	private static final Color COLOR_PANEL = new Color(8, 10, 13);
	private static final Color COLOR_INPUT = new Color(5, 7, 10);
	private static final Color COLOR_BORDER = new Color(34, 39, 47);
	private static final Color COLOR_TEXT = new Color(210, 217, 224);
	private static final Color COLOR_MUTED = new Color(108, 118, 129);
	private static final Color COLOR_ACCENT = new Color(25, 243, 162);
	private static final Color COLOR_LINK = new Color(47, 167, 255);
	private static final Color COLOR_WARN = new Color(248, 189, 61);
	private static final Color COLOR_DANGER = new Color(246, 76, 94);

	private static final Font FONT_MAIN = new Font(Font.MONOSPACED, Font.PLAIN, 14);

	private final GestionPersonnel gestionPersonnel;
	private final CardLayout pageLayout = new CardLayout();
	private final JPanel pageHost = new JPanel(pageLayout);

	private final List<Ligue> ligueRows = new ArrayList<>();
	private final List<Employe> employeRows = new ArrayList<>();
	private Ligue currentLigue;
	private Employe currentEmploye;

	private JTextField loginUserField;
	private JPasswordField loginPasswordField;
	private JButton loginButton;
	private JLabel loginErrorLabel;

	private JLabel menuIdentityLabel;
	private JButton menuRootButton;
	private JButton menuLiguesButton;
	private JButton menuQuitButton;

	private JTextArea rootInfoArea;
	private JTextField rootNomField;
	private JTextField rootPrenomField;
	private JTextField rootMailField;
	private JPasswordField rootPasswordField;
	private JButton rootNomButton;
	private JButton rootPrenomButton;
	private JButton rootMailButton;
	private JButton rootPasswordButton;
	private JButton rootBackButton;

	private JLabel liguesCountLabel;
	private JTable liguesTable;
	private DefaultTableModel liguesTableModel;
	private JButton liguesAddButton;
	private JButton liguesEditButton;
	private JButton liguesBackButton;

	private JLabel ligueEditTitleLabel;
	private JLabel ligueEditSubtitleLabel;
	private JTextArea ligueInfoArea;
	private JTextField ligueRenameField;
	private JButton ligueEmployeesButton;
	private JButton ligueRenameButton;
	private JButton ligueRenameValidateButton;
	private JButton ligueAdminButton;
	private JButton ligueDeleteButton;
	private JButton ligueEditBackButton;

	private JLabel employesTitleLabel;
	private JLabel employesCountLabel;
	private JTable employesTable;
	private DefaultTableModel employesTableModel;
	private JButton employesAddButton;
	private JButton employesManageButton;
	private JButton employesBackButton;

	private JLabel employeDetailTitleLabel;
	private JLabel employeDetailSubtitleLabel;
	private JTextArea employeDetailInfoArea;
	private JButton employeOpenEditButton;
	private JButton employeDeleteButton;
	private JButton employeDetailBackButton;

	private JLabel employeEditTitleLabel;
	private JTextField employeEditNomField;
	private JTextField employeEditPrenomField;
	private JTextField employeEditMailField;
	private JPasswordField employeEditPasswordField;
	private JTextField employeEditDateArriveeField;
	private JTextField employeEditDateDepartField;
	private JButton employeEditNomButton;
	private JButton employeEditPrenomButton;
	private JButton employeEditMailButton;
	private JButton employeEditPasswordButton;
	private JButton employeEditDateArriveeButton;
	private JButton employeEditDateDepartButton;
	private JButton employeEditBackButton;

	public PersonnelFrame(GestionPersonnel gestionPersonnel)
	{
		super("GESTIONPERSONNEL");
		this.gestionPersonnel = gestionPersonnel;
		configureFrame();
		setContentPane(buildMainContainer());
		bindEvents();
		refreshAllData();
		showPage(PAGE_LOGIN);
	}

	private void configureFrame()
	{
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setMinimumSize(new Dimension(1050, 720));
		setSize(1180, 760);
		setLocationRelativeTo(null);
		addWindowListener(new java.awt.event.WindowAdapter()
		{
			@Override
			public void windowClosing(java.awt.event.WindowEvent e)
			{
				onCloseRequested();
			}
		});
	}

	private JPanel buildMainContainer()
	{
		JPanel root = new JPanel(new GridBagLayout());
		root.setBackground(COLOR_BG);

		pageHost.setOpaque(false);
		pageHost.add(buildLoginPage(), PAGE_LOGIN);
		pageHost.add(buildMenuPage(), PAGE_MENU);
		pageHost.add(buildRootPage(), PAGE_ROOT);
		pageHost.add(buildLiguesPage(), PAGE_LIGUES);
		pageHost.add(buildLigueEditPage(), PAGE_LIGUE_EDIT);
		pageHost.add(buildEmployesPage(), PAGE_EMPLOYES);
		pageHost.add(buildEmployeDetailPage(), PAGE_EMPLOYE_DETAIL);
		pageHost.add(buildEmployeEditPage(), PAGE_EMPLOYE_EDIT);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(30, 42, 30, 42);
		root.add(pageHost, gbc);
		return root;
	}

	private JPanel buildLoginPage()
	{
		JPanel card = createPageCard();
		JPanel content = createVerticalContainer(14);
		content.add(createPageHeader("FENETRE 01", "CONNEXION", "Authentification requise"));

		JLabel appLabel = new JLabel("GESTIONPERSONNEL", SwingConstants.CENTER);
		appLabel.setFont(FONT_MAIN.deriveFont(Font.BOLD, 22f));
		appLabel.setForeground(COLOR_ACCENT);
		appLabel.setAlignmentX(CENTER_ALIGNMENT);
		content.add(appLabel);

		loginUserField = createTextField();
		loginUserField.setText("root");
		content.add(createLabeledInput("IDENTIFIANT", loginUserField));

		loginPasswordField = createPasswordField();
		content.add(createLabeledInput("MOT DE PASSE", loginPasswordField));

		loginButton = createPrimaryButton("SE CONNECTER");
		content.add(loginButton);

		loginErrorLabel = createMutedLabel("Identifiants incorrects");
		loginErrorLabel.setForeground(COLOR_DANGER);
		loginErrorLabel.setVisible(false);
		loginErrorLabel.setAlignmentX(CENTER_ALIGNMENT);
		content.add(loginErrorLabel);

		JPanel roles = createSectionPanel();
		roles.setLayout(new BoxLayout(roles, BoxLayout.Y_AXIS));
		roles.add(createMutedLabel("ROLES DISPONIBLES"));
		roles.add(Box.createVerticalStrut(8));
		roles.add(createMutedLabel("- root - super-utilisateur"));
		roles.add(createMutedLabel("- admin - administrateur de ligue"));
		content.add(roles);

		card.add(content, BorderLayout.CENTER);
		return wrapCard(card);
	}

	private JPanel buildMenuPage()
	{
		JPanel card = createPageCard();
		JPanel content = createVerticalContainer(12);
		content.add(createPageHeader("FENETRE 02", "MENU PRINCIPAL", "Connecte en tant que root - super-utilisateur"));

		JPanel identityPanel = createSectionPanel();
		identityPanel.setLayout(new BorderLayout(10, 0));

		JLabel avatar = new JLabel("R", SwingConstants.CENTER);
		avatar.setOpaque(true);
		avatar.setBackground(new Color(8, 66, 48));
		avatar.setForeground(COLOR_ACCENT);
		avatar.setBorder(new CompoundBorder(new LineBorder(new Color(9, 95, 66)), new EmptyBorder(10, 12, 10, 12)));
		identityPanel.add(avatar, BorderLayout.WEST);

		menuIdentityLabel = createTextLabel("root@system - super-utilisateur");
		identityPanel.add(menuIdentityLabel, BorderLayout.CENTER);

		JLabel connected = new JLabel("CONNECTE", SwingConstants.CENTER);
		connected.setOpaque(true);
		connected.setBackground(new Color(8, 66, 48));
		connected.setForeground(COLOR_ACCENT);
		connected.setBorder(new CompoundBorder(new LineBorder(new Color(9, 95, 66)), new EmptyBorder(5, 10, 5, 10)));
		identityPanel.add(connected, BorderLayout.EAST);

		content.add(identityPanel);

		menuRootButton = createMenuActionButton("Gerer le compte root", "Modifier les informations du super-utilisateur", COLOR_TEXT);
		menuLiguesButton = createMenuActionButton("Gerer les ligues", "Creer, modifier et supprimer des ligues", COLOR_TEXT);
		menuQuitButton = createMenuActionButton("Quitter", "Fermer l'application", COLOR_DANGER);

		content.add(menuRootButton);
		content.add(menuLiguesButton);
		content.add(menuQuitButton);

		card.add(content, BorderLayout.CENTER);
		return wrapCard(card);
	}

	private JPanel buildRootPage()
	{
		JPanel card = createPageCard();
		JPanel content = createVerticalContainer(12);
		content.add(createPageHeader("FENETRE 03", "GERER LE COMPTE ROOT", "Modification des informations du super-utilisateur"));

		rootInfoArea = createInfoArea();
		content.add(wrapArea(rootInfoArea));

		content.add(createDivider());
		content.add(createMutedLabel("MODIFIER UN CHAMP"));

		rootNomField = createTextField();
		rootPrenomField = createTextField();
		rootMailField = createTextField();
		rootPasswordField = createPasswordField();

		rootNomButton = createNeutralButton("Modifier");
		rootPrenomButton = createNeutralButton("Modifier");
		rootMailButton = createNeutralButton("Modifier");
		rootPasswordButton = createNeutralButton("Modifier");

		content.add(createFieldActionRow(rootNomField, rootNomButton, "Nouveau nom..."));
		content.add(createFieldActionRow(rootPrenomField, rootPrenomButton, "Nouveau prenom..."));
		content.add(createFieldActionRow(rootMailField, rootMailButton, "Nouveau mail..."));
		content.add(createFieldActionRow(rootPasswordField, rootPasswordButton, "Nouveau mot de passe..."));

		content.add(createDivider());
		rootBackButton = createGhostButton("<- Retour");
		content.add(leftAligned(rootBackButton));

		card.add(content, BorderLayout.CENTER);
		return wrapCard(card);
	}

	private JPanel buildLiguesPage()
	{
		JPanel card = createPageCard();
		JPanel content = createVerticalContainer(12);
		content.add(createPageHeader("FENETRE 04", "LIGUES", ""));

		JPanel topBar = new JPanel(new BorderLayout(10, 0));
		topBar.setOpaque(false);
		liguesCountLabel = createMutedLabel("0 ligues enregistrees");
		topBar.add(liguesCountLabel, BorderLayout.WEST);
		liguesAddButton = createPrimaryButton("+ Ajouter");
		topBar.add(liguesAddButton, BorderLayout.EAST);
		content.add(topBar);

		liguesTableModel = new ReadOnlyTableModel(new String[]{"NOM", "ADMINISTRATEUR", "NB"});
		liguesTable = createDarkTable(liguesTableModel);
		liguesTable.setRowSorter(new TableRowSorter<DefaultTableModel>(liguesTableModel));
		content.add(wrapTable(liguesTable));

		JPanel actions = new JPanel(new BorderLayout(8, 0));
		actions.setOpaque(false);
		liguesBackButton = createGhostButton("<- Retour");
		actions.add(liguesBackButton, BorderLayout.WEST);
		liguesEditButton = createNeutralButton("Editer >");
		actions.add(liguesEditButton, BorderLayout.EAST);
		content.add(actions);

		card.add(content, BorderLayout.CENTER);
		return wrapCard(card);
	}

	private JPanel buildLigueEditPage()
	{
		JPanel card = createPageCard();
		JPanel content = createVerticalContainer(12);

		ligueEditTitleLabel = createTitleLabel("EDITER -");
		ligueEditSubtitleLabel = createMutedLabel("Gestion de la ligue selectionnee");
		content.add(createTitleContainer("FENETRE 05", ligueEditTitleLabel, ligueEditSubtitleLabel));

		ligueInfoArea = createInfoArea();
		content.add(wrapArea(ligueInfoArea));

		content.add(createDivider());
		content.add(createMutedLabel("ACTIONS"));

		ligueEmployeesButton = createLinkButton("Voir la liste des employes >");
		ligueRenameButton = createNeutralButton("Renommer la ligue >");
		ligueAdminButton = createWarnButton("Changer l'administrateur >");
		ligueDeleteButton = createDangerButton("Supprimer la ligue");

		content.add(ligueEmployeesButton);
		content.add(ligueRenameButton);
		content.add(ligueAdminButton);

		JPanel bottomActions = new JPanel(new BorderLayout(8, 0));
		bottomActions.setOpaque(false);
		bottomActions.add(ligueDeleteButton, BorderLayout.WEST);
		ligueEditBackButton = createGhostButton("<- Retour");
		bottomActions.add(ligueEditBackButton, BorderLayout.EAST);
		content.add(bottomActions);

		content.add(createDivider());
		content.add(createMutedLabel("RENOMMER"));
		ligueRenameField = createTextField();
		ligueRenameField.setToolTipText("Nouveau nom de la ligue");
		ligueRenameValidateButton = createNeutralButton("Valider");
		content.add(createFieldActionRow(ligueRenameField, ligueRenameValidateButton, "Nouveau nom de la ligue..."));

		card.add(content, BorderLayout.CENTER);
		return wrapCard(card);
	}

	private JPanel buildEmployesPage()
	{
		JPanel card = createPageCard();
		JPanel content = createVerticalContainer(12);

		employesTitleLabel = createTitleLabel("EMPLOYES");
		employesCountLabel = createMutedLabel("0 employe");
		content.add(createTitleContainer("FENETRE 06", employesTitleLabel, employesCountLabel));

		JPanel topBar = new JPanel(new BorderLayout(10, 0));
		topBar.setOpaque(false);
		topBar.add(Box.createHorizontalStrut(1), BorderLayout.WEST);
		employesAddButton = createPrimaryButton("+ Ajouter");
		topBar.add(employesAddButton, BorderLayout.EAST);
		content.add(topBar);

		employesTableModel = new ReadOnlyTableModel(new String[]{"NOM", "MAIL", "ARRIVEE", "DEPART"});
		employesTable = createDarkTable(employesTableModel);
		employesTable.setRowSorter(new TableRowSorter<DefaultTableModel>(employesTableModel));
		content.add(wrapTable(employesTable));

		JPanel actions = new JPanel(new BorderLayout(8, 0));
		actions.setOpaque(false);
		employesBackButton = createGhostButton("<- Retour");
		actions.add(employesBackButton, BorderLayout.WEST);
		employesManageButton = createNeutralButton("Gerer >");
		actions.add(employesManageButton, BorderLayout.EAST);
		content.add(actions);

		card.add(content, BorderLayout.CENTER);
		return wrapCard(card);
	}

	private JPanel buildEmployeDetailPage()
	{
		JPanel card = createPageCard();
		JPanel content = createVerticalContainer(12);

		employeDetailTitleLabel = createTitleLabel("GERER");
		employeDetailSubtitleLabel = createMutedLabel("Ligue - Role");
		content.add(createTitleContainer("FENETRE 07", employeDetailTitleLabel, employeDetailSubtitleLabel));

		employeDetailInfoArea = createInfoArea();
		content.add(wrapArea(employeDetailInfoArea));

		content.add(createDivider());

		employeOpenEditButton = createNeutralButton("Modifier le compte");
		employeDeleteButton = createDangerButton("Supprimer l'employe");
		employeDetailBackButton = createGhostButton("<- Retour");

		content.add(employeOpenEditButton);

		JPanel bottom = new JPanel(new BorderLayout(8, 0));
		bottom.setOpaque(false);
		bottom.add(employeDeleteButton, BorderLayout.WEST);
		bottom.add(employeDetailBackButton, BorderLayout.EAST);
		content.add(bottom);

		card.add(content, BorderLayout.CENTER);
		return wrapCard(card);
	}

	private JPanel buildEmployeEditPage()
	{
		JPanel card = createPageCard();
		JPanel content = createVerticalContainer(10);

		employeEditTitleLabel = createTitleLabel("MODIFIER LE COMPTE");
		content.add(createTitleContainer("FENETRE 08", employeEditTitleLabel, createMutedLabel("Modification des informations de l'employe")));

		employeEditNomField = createTextField();
		employeEditPrenomField = createTextField();
		employeEditMailField = createTextField();
		employeEditPasswordField = createPasswordField();
		employeEditDateArriveeField = createTextField();
		employeEditDateDepartField = createTextField();

		employeEditNomButton = createNeutralButton("Valider");
		employeEditPrenomButton = createNeutralButton("Valider");
		employeEditMailButton = createNeutralButton("Valider");
		employeEditPasswordButton = createNeutralButton("Valider");
		employeEditDateArriveeButton = createNeutralButton("Valider");
		employeEditDateDepartButton = createNeutralButton("Valider");

		content.add(createLabeledEditRow("NOM", employeEditNomField, employeEditNomButton));
		content.add(createLabeledEditRow("PRENOM", employeEditPrenomField, employeEditPrenomButton));
		content.add(createLabeledEditRow("MAIL", employeEditMailField, employeEditMailButton));
		content.add(createLabeledEditRow("MOT DE PASSE", employeEditPasswordField, employeEditPasswordButton));
		content.add(createLabeledEditRow("DATE D'ARRIVEE", employeEditDateArriveeField, employeEditDateArriveeButton));
		content.add(createLabeledEditRow("DATE DE DEPART (optionnel)", employeEditDateDepartField, employeEditDateDepartButton));

		content.add(createDivider());
		employeEditBackButton = createGhostButton("<- Retour");
		content.add(leftAligned(employeEditBackButton));

		card.add(content, BorderLayout.CENTER);
		return wrapCard(card);
	}

	private void bindEvents()
	{
		loginButton.addActionListener(e -> onLoginSubmit());
		loginPasswordField.addActionListener(e -> onLoginSubmit());

		menuRootButton.addActionListener(e -> onOpenRootPage());
		menuLiguesButton.addActionListener(e -> onOpenLiguesPage());
		menuQuitButton.addActionListener(e -> onQuitRequestedFromMenu());

		rootNomButton.addActionListener(e -> onRootUpdateNom());
		rootPrenomButton.addActionListener(e -> onRootUpdatePrenom());
		rootMailButton.addActionListener(e -> onRootUpdateMail());
		rootPasswordButton.addActionListener(e -> onRootUpdatePassword());
		rootBackButton.addActionListener(e -> onRootBackToMenu());

		liguesAddButton.addActionListener(e -> onAddLigue());
		liguesEditButton.addActionListener(e -> onOpenSelectedLiguePage());
		liguesBackButton.addActionListener(e -> onBackToMenuFromLigues());
		liguesTable.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if (e.getClickCount() == 2)
					onOpenSelectedLiguePage();
			}
		});

		ligueEmployeesButton.addActionListener(e -> onOpenEmployesPage());
		ligueAdminButton.addActionListener(e -> onChangeCurrentLigueAdmin());
		ligueDeleteButton.addActionListener(e -> onDeleteCurrentLigue());
		ligueEditBackButton.addActionListener(e -> onBackToLiguesFromLigueEdit());
		ligueRenameButton.addActionListener(e -> onStartRenameCurrentLigue());
		ligueRenameValidateButton.addActionListener(e -> onRenameCurrentLigue());

		employesAddButton.addActionListener(e -> onAddEmploye());
		employesManageButton.addActionListener(e -> onOpenSelectedEmployePage());
		employesBackButton.addActionListener(e -> onBackToLigueEditFromEmployes());
		employesTable.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if (e.getClickCount() == 2)
					onOpenSelectedEmployePage();
			}
		});

		employeOpenEditButton.addActionListener(e -> onOpenEmployeEditPage());
		employeDeleteButton.addActionListener(e -> onDeleteCurrentEmploye());
		employeDetailBackButton.addActionListener(e -> onBackToEmployesFromEmployeDetail());

		employeEditNomButton.addActionListener(e -> onUpdateEmployeNom());
		employeEditPrenomButton.addActionListener(e -> onUpdateEmployePrenom());
		employeEditMailButton.addActionListener(e -> onUpdateEmployeMail());
		employeEditPasswordButton.addActionListener(e -> onUpdateEmployePassword());
		employeEditDateArriveeButton.addActionListener(e -> onUpdateEmployeDateArrivee());
		employeEditDateDepartButton.addActionListener(e -> onUpdateEmployeDateDepart());
		employeEditBackButton.addActionListener(e -> onBackToEmployeDetailFromEdit());
	}

	private void onLoginSubmit()
	{
		String identifiant = loginUserField.getText().trim();
		String password = new String(loginPasswordField.getPassword());
		Employe root = gestionPersonnel.getRoot();

		if (!"root".equalsIgnoreCase(identifiant) || !root.checkPassword(password))
		{
			loginErrorLabel.setText("Identifiants incorrects");
			loginErrorLabel.setVisible(true);
			return;
		}

		loginErrorLabel.setVisible(false);
		loginPasswordField.setText("");
		refreshAllData();
		showPage(PAGE_MENU);
	}

	private void onOpenRootPage()
	{
		refreshRootPage();
		showPage(PAGE_ROOT);
	}

	private void onOpenLiguesPage()
	{
		refreshLiguesPage();
		showPage(PAGE_LIGUES);
	}

	private void onQuitRequestedFromMenu()
	{
		onCloseRequested();
	}

	private void onRootBackToMenu()
	{
		showPage(PAGE_MENU);
	}

	private void onRootUpdateNom()
	{
		String value = rootNomField.getText().trim();
		if (value.isEmpty())
		{
			showInfo("Le nom ne peut pas etre vide.");
			return;
		}
		try
		{
			gestionPersonnel.getRoot().setNom(value);
			refreshAllData();
			showInfo("Nom root mis a jour.");
		}
		catch (SauvegardeImpossible e)
		{
			showError("Impossible de modifier le nom", e);
		}
	}

	private void onRootUpdatePrenom()
	{
		String value = rootPrenomField.getText().trim();
		if (value.isEmpty())
		{
			showInfo("Le prenom ne peut pas etre vide.");
			return;
		}
		try
		{
			gestionPersonnel.getRoot().setPrenom(value);
			refreshAllData();
			showInfo("Prenom root mis a jour.");
		}
		catch (SauvegardeImpossible e)
		{
			showError("Impossible de modifier le prenom", e);
		}
	}

	private void onRootUpdateMail()
	{
		String value = rootMailField.getText().trim();
		if (value.isEmpty())
		{
			showInfo("Le mail ne peut pas etre vide.");
			return;
		}
		try
		{
			gestionPersonnel.getRoot().setMail(value);
			refreshAllData();
			showInfo("Mail root mis a jour.");
		}
		catch (SauvegardeImpossible e)
		{
			showError("Impossible de modifier le mail", e);
		}
	}

	private void onRootUpdatePassword()
	{
		String value = new String(rootPasswordField.getPassword()).trim();
		if (value.isEmpty())
		{
			showInfo("Le mot de passe ne peut pas etre vide.");
			return;
		}
		try
		{
			gestionPersonnel.getRoot().setPassword(value);
			refreshRootPage();
			showInfo("Mot de passe root mis a jour.");
		}
		catch (SauvegardeImpossible e)
		{
			showError("Impossible de modifier le mot de passe", e);
		}
	}

	private void onAddLigue()
	{
		String nom = JOptionPane.showInputDialog(this, "Nom de la ligue :", "Ajouter une ligue", JOptionPane.PLAIN_MESSAGE);
		if (nom == null || nom.trim().isEmpty())
			return;
		try
		{
			gestionPersonnel.addLigue(nom.trim());
			refreshLiguesPage();
		}
		catch (SauvegardeImpossible e)
		{
			showError("Impossible d'ajouter la ligue", e);
		}
	}

	private void onOpenSelectedLiguePage()
	{
		Ligue selected = getSelectedLigueFromTable();
		if (selected == null)
		{
			showInfo("Selectionnez une ligue a editer.");
			return;
		}
		currentLigue = selected;
		refreshLigueEditorPage();
		refreshEmployesPage();
		showPage(PAGE_LIGUE_EDIT);
	}

	private void onBackToMenuFromLigues()
	{
		showPage(PAGE_MENU);
	}

	private void onOpenEmployesPage()
	{
		if (currentLigue == null)
		{
			showInfo("Aucune ligue selectionnee.");
			return;
		}
		refreshEmployesPage();
		showPage(PAGE_EMPLOYES);
	}

	private void onStartRenameCurrentLigue()
	{
		if (currentLigue == null)
		{
			showInfo("Aucune ligue selectionnee.");
			return;
		}

		ligueRenameField.requestFocusInWindow();
		ligueRenameField.selectAll();
	}

	private void onRenameCurrentLigue()
	{
		if (currentLigue == null)
		{
			showInfo("Aucune ligue selectionnee.");
			return;
		}

		String nouveauNom = ligueRenameField.getText().trim();
		if (nouveauNom.isEmpty())
		{
			showInfo("Saisissez un nouveau nom.");
			return;
		}

		try
		{
			currentLigue.setNom(nouveauNom);
			refreshAllData();
			showInfo("Ligue renommee.");
		}
		catch (SauvegardeImpossible e)
		{
			showError("Impossible de renommer la ligue", e);
		}
	}

	private void onChangeCurrentLigueAdmin()
	{
		if (currentLigue == null)
		{
			showInfo("Aucune ligue selectionnee.");
			return;
		}

		List<Employe> candidats = new ArrayList<Employe>(currentLigue.getEmployes());
		if (candidats.isEmpty())
		{
			showInfo("Cette ligue n'a pas d'employe.");
			return;
		}

		EmployeChoice[] choices = new EmployeChoice[candidats.size()];
		EmployeChoice defaultChoice = null;
		for (int i = 0; i < candidats.size(); i++)
		{
			Employe employe = candidats.get(i);
			choices[i] = new EmployeChoice(employe, formatEmployeShort(employe));
			if (Objects.equals(currentLigue.getAdministrateur(), employe))
				defaultChoice = choices[i];
		}

		Object selected = JOptionPane.showInputDialog(
				this,
				"Choisissez le nouvel administrateur :",
				"Changer l'administrateur",
				JOptionPane.PLAIN_MESSAGE,
				null,
				choices,
				defaultChoice
		);

		if (!(selected instanceof EmployeChoice))
			return;

		try
		{
			currentLigue.setAdministrateur(((EmployeChoice) selected).employe);
			refreshAllData();
			showInfo("Administrateur mis a jour.");
		}
		catch (SauvegardeImpossible e)
		{
			showError("Impossible de changer l'administrateur", e);
		}
	}

	private void onDeleteCurrentLigue()
	{
		if (currentLigue == null)
		{
			showInfo("Aucune ligue selectionnee.");
			return;
		}

		int confirm = JOptionPane.showConfirmDialog(
				this,
				"Supprimer la ligue et tous ses employes ?",
				"Confirmation",
				JOptionPane.YES_NO_OPTION
		);
		if (confirm != JOptionPane.YES_OPTION)
			return;

		try
		{
			currentLigue.remove();
			currentLigue = null;
			currentEmploye = null;
			refreshAllData();
			showPage(PAGE_LIGUES);
		}
		catch (SauvegardeImpossible e)
		{
			showError("Impossible de supprimer la ligue", e);
		}
	}

	private void onBackToLiguesFromLigueEdit()
	{
		refreshLiguesPage();
		showPage(PAGE_LIGUES);
	}

	private void onAddEmploye()
	{
		if (currentLigue == null)
		{
			showInfo("Aucune ligue selectionnee.");
			return;
		}

		EmployeDraft draft = askEmployeDraft();
		if (draft == null)
			return;

		try
		{
			currentLigue.addEmploye(draft.nom, draft.prenom, draft.mail, draft.password, draft.dateArrivee, draft.dateDepart);
			refreshAllData();
		}
		catch (DateInvalide | SauvegardeImpossible e)
		{
			showError("Impossible d'ajouter l'employe", e);
		}
	}

	private void onOpenSelectedEmployePage()
	{
		Employe selected = getSelectedEmployeFromTable();
		if (selected == null)
		{
			showInfo("Selectionnez un employe.");
			return;
		}
		currentEmploye = selected;
		refreshEmployeDetailPage();
		refreshEmployeEditPage();
		showPage(PAGE_EMPLOYE_DETAIL);
	}

	private void onBackToLigueEditFromEmployes()
	{
		refreshLigueEditorPage();
		showPage(PAGE_LIGUE_EDIT);
	}

	private void onOpenEmployeEditPage()
	{
		if (currentEmploye == null)
		{
			showInfo("Aucun employe selectionne.");
			return;
		}
		refreshEmployeEditPage();
		showPage(PAGE_EMPLOYE_EDIT);
	}

	private void onDeleteCurrentEmploye()
	{
		if (currentEmploye == null)
		{
			showInfo("Aucun employe selectionne.");
			return;
		}

		int confirm = JOptionPane.showConfirmDialog(
				this,
				"Supprimer cet employe ?",
				"Confirmation",
				JOptionPane.YES_NO_OPTION
		);
		if (confirm != JOptionPane.YES_OPTION)
			return;

		try
		{
			currentEmploye.remove();
			currentEmploye = null;
			refreshAllData();
			showPage(PAGE_EMPLOYES);
		}
		catch (SauvegardeImpossible e)
		{
			showError("Impossible de supprimer l'employe", e);
		}
	}

	private void onBackToEmployesFromEmployeDetail()
	{
		refreshEmployesPage();
		showPage(PAGE_EMPLOYES);
	}

	private void onUpdateEmployeNom()
	{
		if (!ensureCurrentEmploye())
			return;
		String value = employeEditNomField.getText().trim();
		if (value.isEmpty())
		{
			showInfo("Le nom ne peut pas etre vide.");
			return;
		}
		try
		{
			currentEmploye.setNom(value);
			afterEmployeUpdate("Nom mis a jour.");
		}
		catch (SauvegardeImpossible e)
		{
			showError("Impossible de modifier le nom", e);
		}
	}

	private void onUpdateEmployePrenom()
	{
		if (!ensureCurrentEmploye())
			return;
		String value = employeEditPrenomField.getText().trim();
		if (value.isEmpty())
		{
			showInfo("Le prenom ne peut pas etre vide.");
			return;
		}
		try
		{
			currentEmploye.setPrenom(value);
			afterEmployeUpdate("Prenom mis a jour.");
		}
		catch (SauvegardeImpossible e)
		{
			showError("Impossible de modifier le prenom", e);
		}
	}

	private void onUpdateEmployeMail()
	{
		if (!ensureCurrentEmploye())
			return;
		String value = employeEditMailField.getText().trim();
		if (value.isEmpty())
		{
			showInfo("Le mail ne peut pas etre vide.");
			return;
		}
		try
		{
			currentEmploye.setMail(value);
			afterEmployeUpdate("Mail mis a jour.");
		}
		catch (SauvegardeImpossible e)
		{
			showError("Impossible de modifier le mail", e);
		}
	}

	private void onUpdateEmployePassword()
	{
		if (!ensureCurrentEmploye())
			return;
		String value = new String(employeEditPasswordField.getPassword()).trim();
		if (value.isEmpty())
		{
			showInfo("Le mot de passe ne peut pas etre vide.");
			return;
		}
		try
		{
			currentEmploye.setPassword(value);
			employeEditPasswordField.setText("");
			afterEmployeUpdate("Mot de passe mis a jour.");
		}
		catch (SauvegardeImpossible e)
		{
			showError("Impossible de modifier le mot de passe", e);
		}
	}

	private void onUpdateEmployeDateArrivee()
	{
		if (!ensureCurrentEmploye())
			return;
		try
		{
			LocalDate value = parseOptionalDate(employeEditDateArriveeField.getText().trim());
			currentEmploye.setDateArrivee(value);
			afterEmployeUpdate("Date d'arrivee mise a jour.");
		}
		catch (DateInvalide | SauvegardeImpossible e)
		{
			showError("Impossible de modifier la date d'arrivee", e);
		}
	}

	private void onUpdateEmployeDateDepart()
	{
		if (!ensureCurrentEmploye())
			return;
		try
		{
			LocalDate value = parseOptionalDate(employeEditDateDepartField.getText().trim());
			currentEmploye.setDateDepart(value);
			afterEmployeUpdate("Date de depart mise a jour.");
		}
		catch (DateInvalide | SauvegardeImpossible e)
		{
			showError("Impossible de modifier la date de depart", e);
		}
	}

	private void onBackToEmployeDetailFromEdit()
	{
		refreshEmployeDetailPage();
		showPage(PAGE_EMPLOYE_DETAIL);
	}

	private void onCloseRequested()
	{
		String[] options = {"Sauvegarder et quitter", "Quitter sans sauvegarder", "Annuler"};
		int choice = JOptionPane.showOptionDialog(
				this,
				"Voulez-vous sauvegarder avant de quitter ?",
				"Confirmation",
				JOptionPane.DEFAULT_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,
				options,
				options[0]
		);

		if (choice == 0)
		{
			try
			{
				gestionPersonnel.sauvegarder();
				dispose();
			}
			catch (SauvegardeImpossible e)
			{
				showError("Sauvegarde impossible", e);
			}
		}
		else if (choice == 1)
		{
			dispose();
		}
	}

	private void afterEmployeUpdate(String message)
	{
		refreshAllData();
		showInfo(message);
	}

	private boolean ensureCurrentEmploye()
	{
		if (currentEmploye != null)
			return true;
		showInfo("Aucun employe selectionne.");
		return false;
	}

	private EmployeDraft askEmployeDraft()
	{
		JTextField nomField = createTextField();
		JTextField prenomField = createTextField();
		JTextField mailField = createTextField();
		JPasswordField passwordField = createPasswordField();
		JTextField dateArriveeField = createTextField();
		JTextField dateDepartField = createTextField();

		JPanel form = new JPanel(new GridLayout(6, 2, 6, 6));
		form.setBackground(COLOR_CARD);
		form.add(createMutedLabel("Nom"));
		form.add(nomField);
		form.add(createMutedLabel("Prenom"));
		form.add(prenomField);
		form.add(createMutedLabel("Mail"));
		form.add(mailField);
		form.add(createMutedLabel("Mot de passe"));
		form.add(passwordField);
		form.add(createMutedLabel("Date d'arrivee (yyyy-mm-dd)"));
		form.add(dateArriveeField);
		form.add(createMutedLabel("Date de depart (yyyy-mm-dd, optionnel)"));
		form.add(dateDepartField);

		int result = JOptionPane.showConfirmDialog(
				this,
				form,
				"Ajouter un employe",
				JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE
		);
		if (result != JOptionPane.OK_OPTION)
			return null;

		String nom = nomField.getText().trim();
		String prenom = prenomField.getText().trim();
		String mail = mailField.getText().trim();
		String password = new String(passwordField.getPassword()).trim();

		if (nom.isEmpty() || prenom.isEmpty() || mail.isEmpty() || password.isEmpty())
		{
			showInfo("Tous les champs sauf date de depart sont obligatoires.");
			return null;
		}

		try
		{
			LocalDate dateArrivee = parseOptionalDate(dateArriveeField.getText().trim());
			LocalDate dateDepart = parseOptionalDate(dateDepartField.getText().trim());
			return new EmployeDraft(nom, prenom, mail, password, dateArrivee, dateDepart);
		}
		catch (DateInvalide e)
		{
			showError("Date invalide", e);
			return null;
		}
	}

	private LocalDate parseOptionalDate(String value) throws DateInvalide
	{
		if (value == null || value.isEmpty())
			return null;
		try
		{
			return LocalDate.parse(value);
		}
		catch (DateTimeParseException e)
		{
			throw new DateInvalide("Format de date invalide. Utilisez yyyy-mm-dd.");
		}
	}

	private void refreshAllData()
	{
		updateMenuIdentity();
		refreshRootPage();
		refreshLiguesPage();
		refreshLigueEditorPage();
		refreshEmployesPage();
		refreshEmployeDetailPage();
		refreshEmployeEditPage();
	}

	private void updateMenuIdentity()
	{
		Employe root = gestionPersonnel.getRoot();
		String mail = root.getMail() == null || root.getMail().trim().isEmpty() ? "root@system" : root.getMail();
		menuIdentityLabel.setText(root.getNom() + " " + root.getPrenom() + " - " + mail);
	}

	private void refreshRootPage()
	{
		Employe root = gestionPersonnel.getRoot();
		StringBuilder sb = new StringBuilder();
		sb.append("Nom\n").append(safeValue(root.getNom())).append("\n");
		sb.append("Prenom\n").append(safeValue(root.getPrenom())).append("\n");
		sb.append("Mail\n").append(safeValue(root.getMail())).append("\n");
		sb.append("Password\n********\n");
		rootInfoArea.setText(sb.toString());

		rootNomField.setText("");
		rootPrenomField.setText("");
		rootMailField.setText("");
		rootPasswordField.setText("");
	}

	private void refreshLiguesPage()
	{
		Ligue previous = currentLigue;
		ligueRows.clear();
		ligueRows.addAll(new ArrayList<Ligue>(gestionPersonnel.getLigues()));

		liguesTableModel.setRowCount(0);
		for (Ligue ligue : ligueRows)
		{
			Employe admin = ligue.getAdministrateur();
			String adminName = admin == null ? "- aucun -" : formatEmployeShort(admin);
			liguesTableModel.addRow(new Object[]{ligue.getNom(), adminName, String.valueOf(ligue.getEmployes().size())});
		}

		liguesCountLabel.setText(ligueRows.size() + " ligues enregistrees");

		if (previous != null && ligueRows.contains(previous))
			currentLigue = previous;
		else if (!ligueRows.isEmpty())
			currentLigue = ligueRows.get(0);
		else
			currentLigue = null;

		selectCurrentLigueInTable();
	}

	private void refreshLigueEditorPage()
	{
		if (currentLigue == null)
		{
			ligueEditTitleLabel.setText("EDITER - AUCUNE LIGUE");
			ligueEditSubtitleLabel.setText("Selectionnez d'abord une ligue");
			ligueInfoArea.setText("Nom\n-\nAdministrateur\n-\nEmployes\n-");
			ligueRenameField.setText("");
			setLigueEditEnabled(false);
			return;
		}

		setLigueEditEnabled(true);
		ligueEditTitleLabel.setText("EDITER - " + currentLigue.getNom().toUpperCase());
		ligueEditSubtitleLabel.setText("Gestion de la ligue selectionnee");

		Employe admin = currentLigue.getAdministrateur();
		String adminText = admin == null ? "- aucun -" : formatEmployeShort(admin);
		StringBuilder sb = new StringBuilder();
		sb.append("Nom\n").append(currentLigue.getNom()).append("\n");
		sb.append("Administrateur\n").append(adminText).append("\n");
		sb.append("Employes\n").append(currentLigue.getEmployes().size()).append(" membres");
		ligueInfoArea.setText(sb.toString());
	}

	private void setLigueEditEnabled(boolean enabled)
	{
		ligueEmployeesButton.setEnabled(enabled);
		ligueRenameButton.setEnabled(enabled);
		ligueRenameValidateButton.setEnabled(enabled);
		ligueAdminButton.setEnabled(enabled);
		ligueDeleteButton.setEnabled(enabled);
		ligueRenameField.setEnabled(enabled);
	}

	private void refreshEmployesPage()
	{
		employeRows.clear();
		employesTableModel.setRowCount(0);

		if (currentLigue == null)
		{
			employesTitleLabel.setText("EMPLOYES - AUCUNE LIGUE");
			employesCountLabel.setText("0 employe");
			employesAddButton.setEnabled(false);
			employesManageButton.setEnabled(false);
			currentEmploye = null;
			return;
		}

		employesTitleLabel.setText("EMPLOYES - " + currentLigue.getNom().toUpperCase());
		employeRows.addAll(new ArrayList<Employe>(currentLigue.getEmployes()));
		employesCountLabel.setText(employeRows.size() + " employes");

		for (Employe employe : employeRows)
		{
			String nomCell = formatEmployeShort(employe);
			if (employe.estAdmin(currentLigue))
				nomCell = nomCell + " (admin)";
			employesTableModel.addRow(new Object[]{
					nomCell,
					safeValue(employe.getMail()),
					formatDate(employe.getDateArrivee()),
					formatDate(employe.getDateDepart())
			});
		}

		employesAddButton.setEnabled(true);
		employesManageButton.setEnabled(!employeRows.isEmpty());

		if (currentEmploye != null && !employeRows.contains(currentEmploye))
			currentEmploye = null;
		if (currentEmploye == null && !employeRows.isEmpty())
			currentEmploye = employeRows.get(0);

		selectCurrentEmployeInTable();
	}

	private void refreshEmployeDetailPage()
	{
		if (currentEmploye == null)
		{
			employeDetailTitleLabel.setText("GERER - AUCUN EMPLOYE");
			employeDetailSubtitleLabel.setText("Selectionnez un employe");
			employeDetailInfoArea.setText("Nom\n-\nPrenom\n-\nMail\n-\nArrivee\n-\nDepart\n-\nRole\n-");
			employeOpenEditButton.setEnabled(false);
			employeDeleteButton.setEnabled(false);
			return;
		}

		employeDetailTitleLabel.setText("GERER - " + currentEmploye.getNom().toUpperCase() + " " + currentEmploye.getPrenom().toUpperCase());
		employeDetailSubtitleLabel.setText(currentLigue == null ? "Ligue" : currentLigue.getNom() + " - " + buildRoleLabel(currentEmploye));

		StringBuilder sb = new StringBuilder();
		sb.append("Nom\n").append(safeValue(currentEmploye.getNom())).append("\n");
		sb.append("Prenom\n").append(safeValue(currentEmploye.getPrenom())).append("\n");
		sb.append("Mail\n").append(safeValue(currentEmploye.getMail())).append("\n");
		sb.append("Arrivee\n").append(formatDate(currentEmploye.getDateArrivee())).append("\n");
		sb.append("Depart\n").append(formatDate(currentEmploye.getDateDepart())).append("\n");
		sb.append("Role\n").append(buildRoleLabel(currentEmploye));
		employeDetailInfoArea.setText(sb.toString());

		employeOpenEditButton.setEnabled(true);
		employeDeleteButton.setEnabled(!currentEmploye.estRoot());
	}

	private void refreshEmployeEditPage()
	{
		if (currentEmploye == null)
		{
			employeEditTitleLabel.setText("MODIFIER LE COMPTE - AUCUN EMPLOYE");
			setEmployeEditEnabled(false);
			employeEditNomField.setText("");
			employeEditPrenomField.setText("");
			employeEditMailField.setText("");
			employeEditPasswordField.setText("");
			employeEditDateArriveeField.setText("");
			employeEditDateDepartField.setText("");
			return;
		}

		employeEditTitleLabel.setText("MODIFIER LE COMPTE - " + currentEmploye.getNom().toUpperCase() + " " + currentEmploye.getPrenom().toUpperCase());
		setEmployeEditEnabled(true);
		employeEditNomField.setText(safeValue(currentEmploye.getNom()));
		employeEditPrenomField.setText(safeValue(currentEmploye.getPrenom()));
		employeEditMailField.setText(safeValue(currentEmploye.getMail()));
		employeEditPasswordField.setText("");
		employeEditDateArriveeField.setText(currentEmploye.getDateArrivee() == null ? "" : currentEmploye.getDateArrivee().toString());
		employeEditDateDepartField.setText(currentEmploye.getDateDepart() == null ? "" : currentEmploye.getDateDepart().toString());
	}

	private void setEmployeEditEnabled(boolean enabled)
	{
		employeEditNomField.setEnabled(enabled);
		employeEditPrenomField.setEnabled(enabled);
		employeEditMailField.setEnabled(enabled);
		employeEditPasswordField.setEnabled(enabled);
		employeEditDateArriveeField.setEnabled(enabled);
		employeEditDateDepartField.setEnabled(enabled);
		employeEditNomButton.setEnabled(enabled);
		employeEditPrenomButton.setEnabled(enabled);
		employeEditMailButton.setEnabled(enabled);
		employeEditPasswordButton.setEnabled(enabled);
		employeEditDateArriveeButton.setEnabled(enabled);
		employeEditDateDepartButton.setEnabled(enabled);
	}

	private Ligue getSelectedLigueFromTable()
	{
		int viewRow = liguesTable.getSelectedRow();
		if (viewRow < 0)
			return null;
		int modelRow = liguesTable.convertRowIndexToModel(viewRow);
		if (modelRow < 0 || modelRow >= ligueRows.size())
			return null;
		return ligueRows.get(modelRow);
	}

	private Employe getSelectedEmployeFromTable()
	{
		int viewRow = employesTable.getSelectedRow();
		if (viewRow < 0)
			return null;
		int modelRow = employesTable.convertRowIndexToModel(viewRow);
		if (modelRow < 0 || modelRow >= employeRows.size())
			return null;
		return employeRows.get(modelRow);
	}

	private void selectCurrentLigueInTable()
	{
		if (currentLigue == null)
		{
			liguesTable.clearSelection();
			return;
		}
		for (int i = 0; i < ligueRows.size(); i++)
		{
			if (Objects.equals(currentLigue, ligueRows.get(i)))
			{
				int view = liguesTable.convertRowIndexToView(i);
				liguesTable.getSelectionModel().setSelectionInterval(view, view);
				return;
			}
		}
		liguesTable.clearSelection();
	}

	private void selectCurrentEmployeInTable()
	{
		if (currentEmploye == null)
		{
			employesTable.clearSelection();
			return;
		}
		for (int i = 0; i < employeRows.size(); i++)
		{
			if (Objects.equals(currentEmploye, employeRows.get(i)))
			{
				int view = employesTable.convertRowIndexToView(i);
				employesTable.getSelectionModel().setSelectionInterval(view, view);
				return;
			}
		}
		employesTable.clearSelection();
	}

	private void showPage(String pageName)
	{
		pageLayout.show(pageHost, pageName);
	}

	private JPanel createPageCard()
	{
		JPanel card = new JPanel(new BorderLayout());
		card.setBackground(COLOR_CARD);
		card.setBorder(new CompoundBorder(
				new LineBorder(COLOR_BORDER, 1, true),
				new EmptyBorder(24, 30, 24, 30)
		));
		return card;
	}

	private JPanel wrapCard(JPanel card)
	{
		JPanel wrapper = new JPanel(new GridBagLayout());
		wrapper.setOpaque(false);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		wrapper.add(card, gbc);

		GridBagConstraints spacer = new GridBagConstraints();
		spacer.gridx = 0;
		spacer.gridy = 1;
		spacer.weightx = 1;
		spacer.weighty = 1;
		JPanel empty = new JPanel();
		empty.setOpaque(false);
		wrapper.add(empty, spacer);
		return wrapper;
	}

	private JPanel createVerticalContainer(int gap)
	{
		JPanel container = new JPanel();
		container.setOpaque(false);
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
		container.putClientProperty("gap", gap);
		return container;
	}

	private JPanel createPageHeader(String windowLabel, String title, String subtitle)
	{
		return createTitleContainer(windowLabel, createTitleLabel(title), createMutedLabel(subtitle));
	}

	private JPanel createTitleContainer(String windowLabel, JLabel title, JLabel subtitle)
	{
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		JLabel window = createMutedLabel(windowLabel);
		window.setFont(FONT_MAIN.deriveFont(Font.PLAIN, 12f));
		panel.add(window);
		panel.add(Box.createVerticalStrut(4));
		panel.add(title);
		panel.add(Box.createVerticalStrut(3));
		panel.add(subtitle);
		panel.add(Box.createVerticalStrut(10));
		return panel;
	}

	private JLabel createTitleLabel(String text)
	{
		JLabel label = new JLabel(text);
		label.setForeground(COLOR_ACCENT);
		label.setFont(FONT_MAIN.deriveFont(Font.BOLD, 26f));
		return label;
	}

	private JLabel createMutedLabel(String text)
	{
		JLabel label = new JLabel(text);
		label.setForeground(COLOR_MUTED);
		label.setFont(FONT_MAIN.deriveFont(Font.PLAIN, 13f));
		return label;
	}

	private JLabel createTextLabel(String text)
	{
		JLabel label = new JLabel(text);
		label.setForeground(COLOR_TEXT);
		label.setFont(FONT_MAIN.deriveFont(Font.PLAIN, 14f));
		return label;
	}

	private JPanel createLabeledInput(String label, JComponent input)
	{
		JPanel panel = new JPanel(new BorderLayout(0, 5));
		panel.setOpaque(false);
		panel.add(createMutedLabel(label), BorderLayout.NORTH);
		panel.add(input, BorderLayout.CENTER);
		panel.add(Box.createVerticalStrut(4), BorderLayout.SOUTH);
		return panel;
	}

	private JPanel createFieldActionRow(JComponent field, JButton actionButton, String placeholder)
	{
		if (field instanceof JTextField)
			((JTextField) field).setToolTipText(placeholder);
		if (field instanceof JPasswordField)
			((JPasswordField) field).setToolTipText(placeholder);

		JPanel row = new JPanel(new BorderLayout(8, 0));
		row.setOpaque(false);
		row.add(field, BorderLayout.CENTER);
		row.add(actionButton, BorderLayout.EAST);
		row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
		return row;
	}

	private JPanel createLabeledEditRow(String label, JComponent field, JButton actionButton)
	{
		JPanel row = new JPanel(new BorderLayout(0, 5));
		row.setOpaque(false);
		row.add(createMutedLabel(label), BorderLayout.NORTH);
		row.add(createFieldActionRow(field, actionButton, label), BorderLayout.CENTER);
		return row;
	}

	private JPanel createSectionPanel()
	{
		JPanel panel = new JPanel();
		panel.setBackground(COLOR_PANEL);
		panel.setBorder(new CompoundBorder(new LineBorder(COLOR_BORDER), new EmptyBorder(12, 12, 12, 12)));
		return panel;
	}

	private JTextField createTextField()
	{
		JTextField field = new JTextField();
		field.setBackground(COLOR_INPUT);
		field.setForeground(COLOR_TEXT);
		field.setCaretColor(COLOR_TEXT);
		field.setFont(FONT_MAIN.deriveFont(Font.PLAIN, 14f));
		field.setBorder(new CompoundBorder(new LineBorder(COLOR_BORDER), new EmptyBorder(8, 10, 8, 10)));
		return field;
	}

	private JPasswordField createPasswordField()
	{
		JPasswordField field = new JPasswordField();
		field.setBackground(COLOR_INPUT);
		field.setForeground(COLOR_TEXT);
		field.setCaretColor(COLOR_TEXT);
		field.setFont(FONT_MAIN.deriveFont(Font.PLAIN, 14f));
		field.setBorder(new CompoundBorder(new LineBorder(COLOR_BORDER), new EmptyBorder(8, 10, 8, 10)));
		return field;
	}

	private JButton createButton(String text, Color bg, Color fg, Color border)
	{
		JButton button = new JButton(text);
		button.setFont(FONT_MAIN.deriveFont(Font.BOLD, 13f));
		button.setFocusPainted(false);
		button.setBackground(bg);
		button.setForeground(fg);
		button.setBorder(new CompoundBorder(new LineBorder(border), new EmptyBorder(8, 12, 8, 12)));
		button.setOpaque(true);
		button.setContentAreaFilled(true);
		button.setAlignmentX(LEFT_ALIGNMENT);
		return button;
	}

	private JButton createPrimaryButton(String text)
	{
		return createButton(text, COLOR_ACCENT, new Color(8, 11, 15), COLOR_ACCENT);
	}

	private JButton createNeutralButton(String text)
	{
		return createButton(text, new Color(16, 20, 25), COLOR_TEXT, COLOR_BORDER);
	}

	private JButton createGhostButton(String text)
	{
		return createButton(text, new Color(15, 18, 22), COLOR_MUTED, COLOR_BORDER);
	}

	private JButton createDangerButton(String text)
	{
		return createButton(text, new Color(34, 12, 16), COLOR_DANGER, COLOR_DANGER);
	}

	private JButton createWarnButton(String text)
	{
		return createButton(text, new Color(34, 28, 12), COLOR_WARN, COLOR_WARN);
	}

	private JButton createLinkButton(String text)
	{
		return createButton(text, new Color(10, 16, 23), COLOR_LINK, COLOR_BORDER);
	}

	private JButton createMenuActionButton(String title, String subtitle, Color titleColor)
	{
		String html = "<html><div style='font-family:monospace;line-height:1.25;'>"
				+ "<span style='font-size:13px;color:" + toHex(titleColor) + ";'><b>" + title + "</b></span><br>"
				+ "<span style='font-size:11px;color:" + toHex(COLOR_MUTED) + ";'>" + subtitle + "</span>"
				+ "</div></html>";
		JButton button = createNeutralButton(html);
		button.setHorizontalAlignment(SwingConstants.LEFT);
		button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));
		return button;
	}

	private JTable createDarkTable(DefaultTableModel model)
	{
		JTable table = new JTable(model);
		table.setFont(FONT_MAIN.deriveFont(Font.PLAIN, 13f));
		table.setBackground(COLOR_INPUT);
		table.setForeground(COLOR_TEXT);
		table.setGridColor(COLOR_BORDER);
		table.setRowHeight(34);
		table.setSelectionBackground(new Color(20, 60, 48));
		table.setSelectionForeground(COLOR_TEXT);
		table.setFillsViewportHeight(true);
		table.setBorder(new LineBorder(COLOR_BORDER));

		JTableHeader header = table.getTableHeader();
		header.setFont(FONT_MAIN.deriveFont(Font.BOLD, 12f));
		header.setBackground(new Color(10, 13, 17));
		header.setForeground(COLOR_MUTED);
		header.setReorderingAllowed(false);
		return table;
	}

	private JScrollPane wrapTable(JTable table)
	{
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBorder(new CompoundBorder(new LineBorder(COLOR_BORDER), new EmptyBorder(0, 0, 0, 0)));
		scrollPane.getViewport().setBackground(COLOR_INPUT);
		return scrollPane;
	}

	private JTextArea createInfoArea()
	{
		JTextArea area = new JTextArea();
		area.setEditable(false);
		area.setLineWrap(true);
		area.setWrapStyleWord(true);
		area.setBackground(COLOR_INPUT);
		area.setForeground(COLOR_TEXT);
		area.setFont(FONT_MAIN.deriveFont(Font.PLAIN, 14f));
		area.setBorder(new EmptyBorder(8, 10, 8, 10));
		return area;
	}

	private JComponent wrapArea(JTextArea area)
	{
		JScrollPane scrollPane = new JScrollPane(area);
		scrollPane.setBorder(new CompoundBorder(new LineBorder(COLOR_BORDER), new EmptyBorder(0, 0, 0, 0)));
		scrollPane.setPreferredSize(new Dimension(200, 130));
		scrollPane.getViewport().setBackground(COLOR_INPUT);
		return scrollPane;
	}

	private JPanel leftAligned(JComponent component)
	{
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		panel.setOpaque(false);
		panel.add(component);
		return panel;
	}

	private JComponent createDivider()
	{
		JPanel divider = new JPanel();
		divider.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
		divider.setPreferredSize(new Dimension(10, 1));
		divider.setBackground(COLOR_BORDER);
		return divider;
	}

	private String formatEmployeShort(Employe employe)
	{
		return safeValue(employe.getNom()) + " " + safeValue(employe.getPrenom());
	}

	private String buildRoleLabel(Employe employe)
	{
		if (employe.estRoot())
			return "super-utilisateur";
		if (currentLigue != null && employe.estAdmin(currentLigue))
			return "Admin " + currentLigue.getNom();
		return "Employe";
	}

	private String formatDate(LocalDate date)
	{
		return date == null ? "-" : date.toString();
	}

	private String safeValue(String value)
	{
		return value == null || value.trim().isEmpty() ? "-" : value;
	}

	private String toHex(Color color)
	{
		return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
	}

	private void showInfo(String message)
	{
		JOptionPane.showMessageDialog(this, message, "Information", JOptionPane.INFORMATION_MESSAGE);
	}

	private void showError(String title, Exception exception)
	{
		JOptionPane.showMessageDialog(this, exception.getMessage(), title, JOptionPane.ERROR_MESSAGE);
	}

	public static void launch(GestionPersonnel gestionPersonnel)
	{
		SwingUtilities.invokeLater(() -> new PersonnelFrame(gestionPersonnel).setVisible(true));
	}

	private static final class EmployeChoice
	{
		private final Employe employe;
		private final String label;

		private EmployeChoice(Employe employe, String label)
		{
			this.employe = employe;
			this.label = label;
		}

		@Override
		public String toString()
		{
			return label;
		}
	}

	private static final class EmployeDraft
	{
		private final String nom;
		private final String prenom;
		private final String mail;
		private final String password;
		private final LocalDate dateArrivee;
		private final LocalDate dateDepart;

		private EmployeDraft(String nom, String prenom, String mail, String password, LocalDate dateArrivee, LocalDate dateDepart)
		{
			this.nom = nom;
			this.prenom = prenom;
			this.mail = mail;
			this.password = password;
			this.dateArrivee = dateArrivee;
			this.dateDepart = dateDepart;
		}
	}

	private static class ReadOnlyTableModel extends DefaultTableModel
	{
		private ReadOnlyTableModel(String[] columns)
		{
			super(columns, 0);
		}

		@Override
		public boolean isCellEditable(int row, int column)
		{
			return false;
		}
	}
}
