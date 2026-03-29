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
import javax.swing.JDialog;
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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Fenetre principale de l'application Swing.
 *
 * Cette classe reproduit les 8 ecrans de la maquette via un CardLayout,
 * avec une methode dediee pour chaque composant, conteneur et evenement.
 */
public class PersonnelFrame extends JFrame
{
	/** Identifiants des pages affichees dans le CardLayout. */
	private static final String PAGE_LOGIN = "page.login";
	private static final String PAGE_MENU = "page.menu";
	private static final String PAGE_ROOT = "page.root";
	private static final String PAGE_LIGUES = "page.ligues";
	private static final String PAGE_LIGUE_EDIT = "page.ligue.edit";
	private static final String PAGE_EMPLOYES = "page.employes";
	private static final String PAGE_EMPLOYE_DETAIL = "page.employe.detail";
	private static final String PAGE_EMPLOYE_EDIT = "page.employe.edit";

	/** Palette de couleurs inspiree de la maquette noir/vert. */
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
	private Employe connectedUser;

	private JTextField loginUserField;
	private JPasswordField loginPasswordField;
	private JButton loginButton;
	private JLabel loginErrorLabel;

	private JLabel menuIdentityLabel;
	private JButton menuRootButton;
	private JButton menuLiguesButton;
	private JButton menuQuitButton;

	private JTextArea rootInfoArea;
	private JLabel rootPageTitleLabel;
	private JLabel rootPageSubtitleLabel;
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

	/**
	 * Construit la fenetre complete et initialise la navigation multi-pages.
	 */
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

	/**
	 * Configure les parametres globaux de la fenetre (taille, fermeture, position).
	 */
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

	/**
	 * Cree le conteneur principal qui accueille toutes les pages du CardLayout.
	 */
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

	/**
	 * FENETRE 01 : page de connexion root.
	 */
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
		JLabel loginHintLabel = createMutedLabel("Identifiants acceptes: root, mail, nom, nom.prenom");
		loginHintLabel.setAlignmentX(CENTER_ALIGNMENT);
		content.add(loginHintLabel);

		loginPasswordField = createPasswordField();
		content.add(createLabeledInput("MOT DE PASSE", loginPasswordField));

		// Bouton principal qui declenche la verification identifiant/mot de passe.
		loginButton = createPrimaryButton("SE CONNECTER");
		content.add(loginButton);

		loginErrorLabel = createMutedLabel("Identifiants incorrects");
		loginErrorLabel.setForeground(COLOR_DANGER);
		loginErrorLabel.setVisible(false);
		loginErrorLabel.setAlignmentX(CENTER_ALIGNMENT);
		content.add(loginErrorLabel);

		JPanel roles = createSectionPanel();
		roles.setLayout(new BoxLayout(roles, BoxLayout.Y_AXIS));
		JLabel rolesTitle = createMutedLabel("ROLES DISPONIBLES");
		rolesTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		rolesTitle.setHorizontalAlignment(SwingConstants.CENTER);
		roles.add(rolesTitle);
		roles.add(Box.createVerticalStrut(8));
		JLabel roleRoot = createMutedLabel("- root - super-utilisateur");
		roleRoot.setAlignmentX(Component.CENTER_ALIGNMENT);
		roleRoot.setHorizontalAlignment(SwingConstants.CENTER);
		roles.add(roleRoot);
		JLabel roleAdmin = createMutedLabel("- admin - administrateur de ligue");
		roleAdmin.setAlignmentX(Component.CENTER_ALIGNMENT);
		roleAdmin.setHorizontalAlignment(SwingConstants.CENTER);
		roles.add(roleAdmin);
		JLabel roleUser = createMutedLabel("- utilisateur - employe standard");
		roleUser.setAlignmentX(Component.CENTER_ALIGNMENT);
		roleUser.setHorizontalAlignment(SwingConstants.CENTER);
		roles.add(roleUser);
		content.add(roles);

		card.add(content, BorderLayout.CENTER);
		return wrapCard(card);
	}

	/**
	 * FENETRE 02 : menu principal avec navigation vers les autres pages.
	 */
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

		// Bouton de navigation vers la page de gestion du compte root.
		menuRootButton = createMenuActionButton("Gerer le compte root", "Modifier les informations du super-utilisateur", COLOR_TEXT);
		// Bouton de navigation vers la page de gestion des ligues.
		menuLiguesButton = createMenuActionButton("Gerer les ligues", "Creer, modifier et supprimer des ligues", COLOR_TEXT);
		// Bouton de sortie (avec confirmation de sauvegarde).
		menuQuitButton = createMenuActionButton("Quitter", "Fermer l'application", COLOR_DANGER);

		content.add(menuRootButton);
		content.add(menuLiguesButton);
		content.add(menuQuitButton);

		card.add(content, BorderLayout.CENTER);
		return wrapCard(card);
	}

	/**
	 * FENETRE 03 : edition du compte root, champ par champ.
	 */
	private JPanel buildRootPage()
	{
		JPanel card = createPageCard();
		JPanel content = createVerticalContainer(12);

		rootPageTitleLabel = createTitleLabel("GERER LE COMPTE ROOT");
		rootPageSubtitleLabel = createMutedLabel("Modification des informations du super-utilisateur");
		content.add(createTitleContainer("FENETRE 03", rootPageTitleLabel, rootPageSubtitleLabel));

		rootInfoArea = createInfoArea();
		content.add(wrapArea(rootInfoArea));

		content.add(createDivider());
		content.add(createMutedLabel("MODIFIER UN CHAMP"));

		rootNomField = createTextField();
		rootPrenomField = createTextField();
		rootMailField = createTextField();
		rootPasswordField = createPasswordField();

		// Chaque bouton applique uniquement le champ de la ligne correspondante.
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

	/**
	 * FENETRE 04 : liste des ligues, avec actions ajouter/editer/retour.
	 */
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
		// Ouvre la page d'edition de la ligue selectionnee dans le tableau.
		liguesEditButton = createNeutralButton("Editer >");
		actions.add(liguesEditButton, BorderLayout.EAST);
		content.add(actions);

		card.add(content, BorderLayout.CENTER);
		return wrapCard(card);
	}

	/**
	 * FENETRE 05 : edition d'une ligue (infos, admin, suppression, renommage).
	 */
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

		// Ouvre la page des employes rattaches a la ligue.
		ligueEmployeesButton = createLinkButton("Voir la liste des employes >");
		// Place le focus sur la zone de renommage.
		ligueRenameButton = createNeutralButton("Renommer la ligue >");
		// Ouvre le selecteur pour changer l'administrateur.
		ligueAdminButton = createWarnButton("Changer l'administrateur >");
		// Supprime la ligue et ses employes apres confirmation.
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

	/**
	 * FENETRE 06 : liste des employes de la ligue courante.
	 */
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
		// Ouvre la fiche detail de l'employe selectionne.
		employesManageButton = createNeutralButton("Gerer >");
		actions.add(employesManageButton, BorderLayout.EAST);
		content.add(actions);

		card.add(content, BorderLayout.CENTER);
		return wrapCard(card);
	}

	/**
	 * FENETRE 07 : fiche detail d'un employe et actions principales.
	 */
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

	/**
	 * FENETRE 08 : edition du compte employe, champ par champ.
	 */
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

	/**
	 * Relie chaque composant interactif a sa methode evenement dediee.
	 */
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

	/**
	 * Valide la connexion depuis la page de login (root/admin/utilisateur).
	 */
	private void onLoginSubmit()
	{
		String identifiant = loginUserField.getText().trim();
		String password = new String(loginPasswordField.getPassword());
		Employe account = findUserByIdentifiant(identifiant);

		if (account == null || !account.checkPassword(password))
		{
			loginErrorLabel.setText("Identifiants incorrects");
			loginErrorLabel.setVisible(true);
			return;
		}

		connectedUser = account;
		initializeContextForConnectedUser();
		loginErrorLabel.setVisible(false);
		loginPasswordField.setText("");
		refreshAllData();
		showPage(PAGE_MENU);
	}

	/**
	 * Navigue vers la page de gestion du compte root.
	 */
	private void onOpenRootPage()
	{
		refreshRootPage();
		showPage(PAGE_ROOT);
	}

	/**
	 * Navigue vers la page listant toutes les ligues.
	 */
	private void onOpenLiguesPage()
	{
		if (!hasLigueAccess())
		{
			showInfo("Aucune ligue associee a votre compte.");
			return;
		}
		refreshLiguesPage();
		showPage(PAGE_LIGUES);
	}

	/**
	 * Demande la fermeture de l'application depuis le menu principal.
	 */
	private void onQuitRequestedFromMenu()
	{
		onCloseRequested();
	}

	/**
	 * Retourne du compte root vers le menu principal.
	 */
	private void onRootBackToMenu()
	{
		showPage(PAGE_MENU);
	}

	/**
	 * Met a jour le nom du root.
	 */
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
			getEditableAccount().setNom(value);
			refreshAllData();
			showInfo("Nom du compte mis a jour.");
		}
		catch (SauvegardeImpossible e)
		{
			showError("Impossible de modifier le nom", e);
		}
	}

	/**
	 * Met a jour le prenom du root.
	 */
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
			getEditableAccount().setPrenom(value);
			refreshAllData();
			showInfo("Prenom du compte mis a jour.");
		}
		catch (SauvegardeImpossible e)
		{
			showError("Impossible de modifier le prenom", e);
		}
	}

	/**
	 * Met a jour le mail du root.
	 */
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
			getEditableAccount().setMail(value);
			refreshAllData();
			showInfo("Mail du compte mis a jour.");
		}
		catch (SauvegardeImpossible e)
		{
			showError("Impossible de modifier le mail", e);
		}
	}

	/**
	 * Met a jour le mot de passe du root (hachage applique en couche metier).
	 */
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
			getEditableAccount().setPassword(value);
			refreshRootPage();
			showInfo("Mot de passe du compte mis a jour.");
		}
		catch (SauvegardeImpossible e)
		{
			showError("Impossible de modifier le mot de passe", e);
		}
	}

	/**
	 * Cree une nouvelle ligue.
	 */
	private void onAddLigue()
	{
		if (!isConnectedRoot())
		{
			showInfo("Seul le compte root peut creer des ligues.");
			return;
		}

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

	/**
	 * Ouvre l'edition de la ligue selectionnee.
	 */
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

	/**
	 * Retourne de la liste des ligues vers le menu principal.
	 */
	private void onBackToMenuFromLigues()
	{
		showPage(PAGE_MENU);
	}

	/**
	 * Ouvre la liste des employes de la ligue en cours.
	 */
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

	/**
	 * Place le curseur dans le champ de renommage de ligue.
	 */
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

	/**
	 * Applique le nouveau nom de la ligue courante.
	 */
	private void onRenameCurrentLigue()
	{
		if (currentLigue == null)
		{
			showInfo("Aucune ligue selectionnee.");
			return;
		}
		if (!canManageCurrentLigueStructure())
		{
			showInfo("Vous n'avez pas les droits pour renommer cette ligue.");
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

	/**
	 * Change l'administrateur de la ligue courante via une liste de choix.
	 */
	private void onChangeCurrentLigueAdmin()
	{
		if (currentLigue == null)
		{
			showInfo("Aucune ligue selectionnee.");
			return;
		}
		if (!canManageCurrentLigueStructure())
		{
			showInfo("Vous n'avez pas les droits pour changer l'administrateur.");
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

	/**
	 * Supprime la ligue courante apres confirmation utilisateur.
	 */
	private void onDeleteCurrentLigue()
	{
		if (currentLigue == null)
		{
			showInfo("Aucune ligue selectionnee.");
			return;
		}
		if (!canManageCurrentLigueStructure())
		{
			showInfo("Vous n'avez pas les droits pour supprimer cette ligue.");
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

	/**
	 * Retourne de l'edition ligue vers la liste des ligues.
	 */
	private void onBackToLiguesFromLigueEdit()
	{
		refreshLiguesPage();
		showPage(PAGE_LIGUES);
	}

	/**
	 * Ajoute un employe dans la ligue courante.
	 */
	private void onAddEmploye()
	{
		if (currentLigue == null)
		{
			showInfo("Aucune ligue selectionnee.");
			return;
		}
		if (!canManageCurrentLigueMembers())
		{
			showInfo("Vous n'avez pas les droits pour ajouter un employe.");
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

	/**
	 * Ouvre la fiche detail de l'employe selectionne.
	 */
	private void onOpenSelectedEmployePage()
	{
		Employe selected = getSelectedEmployeFromTable();
		if (selected == null)
		{
			showInfo("Selectionnez un employe.");
			return;
		}
		if (!canViewEmploye(selected))
		{
			showInfo("Vous pouvez seulement consulter votre propre compte.");
			return;
		}

		currentEmploye = selected;
		refreshEmployeDetailPage();
		refreshEmployeEditPage();
		showPage(PAGE_EMPLOYE_DETAIL);
	}

	/**
	 * Retourne de la liste employes vers l'edition de la ligue.
	 */
	private void onBackToLigueEditFromEmployes()
	{
		refreshLigueEditorPage();
		showPage(PAGE_LIGUE_EDIT);
	}

	/**
	 * Ouvre la page d'edition du compte employe courant.
	 */
	private void onOpenEmployeEditPage()
	{
		if (currentEmploye == null)
		{
			showInfo("Aucun employe selectionne.");
			return;
		}
		if (!canEditEmploye(currentEmploye))
		{
			showInfo("Vous n'avez pas les droits pour modifier ce compte.");
			return;
		}
		refreshEmployeEditPage();
		showPage(PAGE_EMPLOYE_EDIT);
	}

	/**
	 * Supprime l'employe courant apres confirmation.
	 */
	private void onDeleteCurrentEmploye()
	{
		if (currentEmploye == null)
		{
			showInfo("Aucun employe selectionne.");
			return;
		}
		if (!canDeleteEmploye(currentEmploye))
		{
			showInfo("Vous n'avez pas les droits pour supprimer ce compte.");
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

	/**
	 * Retourne de la fiche detail employe vers la liste des employes.
	 */
	private void onBackToEmployesFromEmployeDetail()
	{
		refreshEmployesPage();
		showPage(PAGE_EMPLOYES);
	}

	/**
	 * Met a jour le nom de l'employe courant.
	 */
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

	/**
	 * Met a jour le prenom de l'employe courant.
	 */
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

	/**
	 * Met a jour le mail de l'employe courant.
	 */
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

	/**
	 * Met a jour le mot de passe de l'employe courant.
	 */
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

	/**
	 * Met a jour la date d'arrivee de l'employe courant.
	 */
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

	/**
	 * Met a jour la date de depart de l'employe courant.
	 */
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

	/**
	 * Retourne de l'edition employe vers sa fiche detail.
	 */
	private void onBackToEmployeDetailFromEdit()
	{
		refreshEmployeDetailPage();
		showPage(PAGE_EMPLOYE_DETAIL);
	}

	/**
	 * Gere la fermeture de la fenetre avec confirmation simple Oui/Non.
	 */
	private void onCloseRequested()
	{
		int choice = JOptionPane.showConfirmDialog(
				this,
				"Voulez-vous vraiment quitter ?",
				"Quitter l'application",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE
		);
		if (choice == JOptionPane.YES_OPTION)
			dispose();
	}

	/**
	 * Point unique appele apres une mise a jour d'employe.
	 */
	private void afterEmployeUpdate(String message)
	{
		refreshAllData();
		showInfo(message);
	}

	/**
	 * Verifie qu'un employe est bien selectionne avant une action.
	 */
	private boolean ensureCurrentEmploye()
	{
		if (currentEmploye != null)
			return true;
		showInfo("Aucun employe selectionne.");
		return false;
	}

	/**
	 * Initialise la selection courante juste apres une connexion.
	 */
	private void initializeContextForConnectedUser()
	{
		if (isConnectedRoot())
		{
			currentLigue = null;
			currentEmploye = null;
			return;
		}

		currentLigue = connectedUser.getLigue();
		currentEmploye = connectedUser;
	}

	/**
	 * Retourne le compte actuellement connecte (root par defaut avant login).
	 */
	private Employe getConnectedUser()
	{
		return connectedUser != null ? connectedUser : gestionPersonnel.getRoot();
	}

	/**
	 * Retourne le compte editable sur la page "Gerer mon compte".
	 */
	private Employe getEditableAccount()
	{
		return getConnectedUser();
	}

	/**
	 * Verifie si le compte connecte est root.
	 */
	private boolean isConnectedRoot()
	{
		return getConnectedUser().estRoot();
	}

	/**
	 * Verifie si le compte connecte est admin d'une ligue.
	 */
	private boolean isConnectedAdmin()
	{
		return findAdminLigueFor(getConnectedUser()) != null;
	}

	/**
	 * Indique si l'utilisateur peut voir au moins une ligue.
	 */
	private boolean hasLigueAccess()
	{
		return isConnectedRoot() || getConnectedUser().getLigue() != null;
	}

	/**
	 * Cherche la ligue dont l'employe est administrateur.
	 */
	private Ligue findAdminLigueFor(Employe employe)
	{
		if (employe == null)
			return null;
		for (Ligue ligue : gestionPersonnel.getLigues())
			if (ligue.getAdministrateur() == employe)
				return ligue;
		return null;
	}

	/**
	 * Retourne les ligues visibles par l'utilisateur connecte.
	 */
	private List<Ligue> getVisibleLiguesForConnectedUser()
	{
		if (isConnectedRoot())
			return new ArrayList<Ligue>(gestionPersonnel.getLigues());

		List<Ligue> visible = new ArrayList<Ligue>();
		if (getConnectedUser().getLigue() != null)
			visible.add(getConnectedUser().getLigue());
		return visible;
	}

	/**
	 * Retourne vrai si l'utilisateur peut agir sur la ligue courante.
	 */
	private boolean canAccessCurrentLigue()
	{
		if (currentLigue == null)
			return false;
		if (isConnectedRoot())
			return true;
		return getConnectedUser().getLigue() == currentLigue;
	}

	/**
	 * Gestion de structure (renommer, supprimer ligue, changer admin): root seulement.
	 */
	private boolean canManageCurrentLigueStructure()
	{
		return currentLigue != null && isConnectedRoot();
	}

	/**
	 * Gestion des employes de la ligue : root ou admin de cette ligue.
	 */
	private boolean canManageCurrentLigueMembers()
	{
		if (!canAccessCurrentLigue())
			return false;
		if (isConnectedRoot())
			return true;
		return currentLigue.getAdministrateur() == getConnectedUser();
	}

	/**
	 * Autorisation de consultation d'un employe.
	 */
	private boolean canViewEmploye(Employe employe)
	{
		if (employe == null)
			return false;
		if (isConnectedRoot())
			return true;
		if (employe == getConnectedUser())
			return true;
		return canManageCurrentLigueMembers();
	}

	/**
	 * Autorisation de modification d'un employe.
	 */
	private boolean canEditEmploye(Employe employe)
	{
		if (employe == null)
			return false;
		if (isConnectedRoot())
			return true;
		if (employe == getConnectedUser())
			return true;
		return canManageCurrentLigueMembers();
	}

	/**
	 * Autorisation de suppression d'un employe.
	 */
	private boolean canDeleteEmploye(Employe employe)
	{
		if (employe == null)
			return false;
		if (employe.estRoot() || employe == getConnectedUser())
			return false;
		if (isConnectedRoot())
			return true;
		return canManageCurrentLigueMembers();
	}

	/**
	 * Traduction du role global pour affichage (menu de connexion).
	 */
	private String buildGlobalRoleLabel(Employe employe)
	{
		if (employe.estRoot())
			return "super-utilisateur";
		Ligue adminLigue = findAdminLigueFor(employe);
		if (adminLigue != null)
			return "administrateur - " + adminLigue.getNom();
		return "utilisateur standard";
	}

	/**
	 * Construit le texte HTML des boutons du menu principal.
	 */
	private String buildMenuButtonHtml(String title, String subtitle, Color titleColor)
	{
		return "<html><div style='font-family:monospace;line-height:1.25;'>"
				+ "<span style='font-size:13px;color:" + toHex(titleColor) + ";'><b>" + title + "</b></span><br>"
				+ "<span style='font-size:11px;color:" + toHex(COLOR_MUTED) + ";'>" + subtitle + "</span>"
				+ "</div></html>";
	}

	/**
	 * Recherche un compte a partir de l'identifiant saisi sur l'ecran de connexion.
	 * Identifiants acceptes: root, mail, nom, nom.prenom.
	 */
	private Employe findUserByIdentifiant(String identifiant)
	{
		return gestionPersonnel.findEmployeByIdentifiant(identifiant);
	}

	/**
	 * Ouvre un formulaire modal pour creer un employe.
	 */
	private EmployeDraft askEmployeDraft()
	{
		JTextField nomField = createTextField();
		JTextField prenomField = createTextField();
		JTextField mailField = createTextField();
		JPasswordField passwordField = createPasswordField();
		JTextField dateArriveeField = createTextField();
		JTextField dateDepartField = createTextField();

		JDialog dialog = new JDialog(this, "Ajouter un employe", true);
		dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		JPanel dialogRoot = new JPanel(new BorderLayout());
		dialogRoot.setBackground(COLOR_BG);
		dialogRoot.setBorder(new EmptyBorder(14, 14, 14, 14));

		JPanel card = createPageCard();
		JPanel content = createVerticalContainer(10);
		content.add(createPageHeader("FORMULAIRE", "AJOUTER UN EMPLOYE", "Creation d'un nouveau compte employe"));
		content.add(createLabeledInput("NOM", nomField));
		content.add(createLabeledInput("PRENOM", prenomField));
		content.add(createLabeledInput("MAIL", mailField));
		content.add(createLabeledInput("MOT DE PASSE", passwordField));
		content.add(createLabeledInput("DATE D'ARRIVEE (yyyy-mm-dd)", dateArriveeField));
		content.add(createLabeledInput("DATE DE DEPART (yyyy-mm-dd, optionnel)", dateDepartField));

		JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
		actions.setOpaque(false);
		JButton cancelButton = createGhostButton("Annuler");
		JButton validateButton = createPrimaryButton("Valider");
		actions.add(cancelButton);
		actions.add(validateButton);
		content.add(actions);

		card.add(content, BorderLayout.CENTER);
		dialogRoot.add(card, BorderLayout.CENTER);
		dialog.setContentPane(dialogRoot);
		dialog.setMinimumSize(new Dimension(640, 560));
		dialog.setSize(700, 580);
		dialog.setLocationRelativeTo(this);

		final EmployeDraft[] result = new EmployeDraft[1];
		cancelButton.addActionListener(e -> dialog.dispose());
		validateButton.addActionListener(e ->
		{
			String nom = nomField.getText().trim();
			String prenom = prenomField.getText().trim();
			String mail = mailField.getText().trim();
			String password = new String(passwordField.getPassword()).trim();

			if (nom.isEmpty() || prenom.isEmpty() || mail.isEmpty() || password.isEmpty())
			{
				showInfo("Tous les champs sauf date de depart sont obligatoires.");
				return;
			}

			try
			{
				LocalDate dateArrivee = parseOptionalDate(dateArriveeField.getText().trim());
				LocalDate dateDepart = parseOptionalDate(dateDepartField.getText().trim());
				result[0] = new EmployeDraft(nom, prenom, mail, password, dateArrivee, dateDepart);
				dialog.dispose();
			}
			catch (DateInvalide ex)
			{
				showError("Date invalide", ex);
			}
		});

		dialog.getRootPane().setDefaultButton(validateButton);
		dialog.setVisible(true);
		return result[0];
	}

	/**
	 * Convertit un texte yyyy-mm-dd en LocalDate, vide => null.
	 *
	 * Terme technique : LocalDate est le type Java date sans heure.
	 */
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

	/**
	 * Rafraichit l'ensemble des pages apres une modification metier.
	 */
	private void refreshAllData()
	{
		updateMenuActions();
		updateMenuIdentity();
		refreshRootPage();
		refreshLiguesPage();
		refreshLigueEditorPage();
		refreshEmployesPage();
		refreshEmployeDetailPage();
		refreshEmployeEditPage();
	}

	/**
	 * Met a jour la carte d'identite affichee dans le menu principal.
	 */
	private void updateMenuIdentity()
	{
		Employe user = getConnectedUser();
		String mail = user.getMail() == null || user.getMail().trim().isEmpty() ? "root@system" : user.getMail();
		menuIdentityLabel.setText(user.getNom() + " " + user.getPrenom() + " - " + mail + " - " + buildGlobalRoleLabel(user));
	}

	/**
	 * Met a jour les libelles/actions du menu selon le role connecte.
	 */
	private void updateMenuActions()
	{
		Employe user = getConnectedUser();
		if (user.estRoot())
		{
			menuRootButton.setText(buildMenuButtonHtml("Gerer le compte root", "Modifier les informations du super-utilisateur", COLOR_TEXT));
			menuLiguesButton.setText(buildMenuButtonHtml("Gerer les ligues", "Creer, modifier et supprimer des ligues", COLOR_TEXT));
			menuLiguesButton.setEnabled(true);
			return;
		}

		menuRootButton.setText(buildMenuButtonHtml("Gerer mon compte", "Modifier mes informations personnelles", COLOR_TEXT));
		if (hasLigueAccess())
		{
			String title = isConnectedAdmin() ? "Gerer ma ligue" : "Consulter ma ligue";
			String subtitle = isConnectedAdmin()
					? "Consulter et gerer les employes de ma ligue"
					: "Consulter les employes de ma ligue";
			menuLiguesButton.setText(buildMenuButtonHtml(title, subtitle, COLOR_TEXT));
			menuLiguesButton.setEnabled(true);
		}
		else
		{
			menuLiguesButton.setText(buildMenuButtonHtml("Aucune ligue", "Aucune ligue n'est rattachee a ce compte", COLOR_MUTED));
			menuLiguesButton.setEnabled(false);
		}
	}

	/**
	 * Recharge les donnees visibles sur la page Root.
	 */
	private void refreshRootPage()
	{
		Employe account = getEditableAccount();
		if (account.estRoot())
		{
			rootPageTitleLabel.setText("GERER LE COMPTE ROOT");
			rootPageSubtitleLabel.setText("Modification des informations du super-utilisateur");
		}
		else
		{
			rootPageTitleLabel.setText("GERER MON COMPTE");
			rootPageSubtitleLabel.setText("Modification de mes informations personnelles");
		}

		StringBuilder sb = new StringBuilder();
		sb.append("Nom\n").append(safeValue(account.getNom())).append("\n");
		sb.append("Prenom\n").append(safeValue(account.getPrenom())).append("\n");
		sb.append("Mail\n").append(safeValue(account.getMail())).append("\n");
		sb.append("Password\n********\n");
		rootInfoArea.setText(sb.toString());

		rootNomField.setText("");
		rootPrenomField.setText("");
		rootMailField.setText("");
		rootPasswordField.setText("");
	}

	/**
	 * Recharge la table des ligues et preserve la selection courante.
	 */
	private void refreshLiguesPage()
	{
		Ligue previous = currentLigue;
		ligueRows.clear();
		ligueRows.addAll(getVisibleLiguesForConnectedUser());

		liguesTableModel.setRowCount(0);
		for (Ligue ligue : ligueRows)
		{
			Employe admin = ligue.getAdministrateur();
			String adminName = admin == null ? "- aucun -" : formatEmployeShort(admin);
			liguesTableModel.addRow(new Object[]{ligue.getNom(), adminName, String.valueOf(ligue.getEmployes().size())});
		}

		liguesCountLabel.setText(ligueRows.size() + " ligues enregistrees");
		liguesAddButton.setEnabled(isConnectedRoot());
		liguesEditButton.setEnabled(!ligueRows.isEmpty());

		if (previous != null && ligueRows.contains(previous))
			currentLigue = previous;
		else if (!ligueRows.isEmpty())
			currentLigue = ligueRows.get(0);
		else
			currentLigue = null;

		selectCurrentLigueInTable();
	}

	/**
	 * Recharge la page d'edition de ligue selon la ligue selectionnee.
	 */
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
		ligueEmployeesButton.setEnabled(canAccessCurrentLigue());
		boolean canManageStructure = canManageCurrentLigueStructure();
		ligueRenameButton.setEnabled(canManageStructure);
		ligueRenameValidateButton.setEnabled(canManageStructure);
		ligueAdminButton.setEnabled(canManageStructure);
		ligueDeleteButton.setEnabled(canManageStructure);
		ligueRenameField.setEnabled(canManageStructure);
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

	/**
	 * Active/desactive les commandes de la page ligue.
	 */
	private void setLigueEditEnabled(boolean enabled)
	{
		ligueEmployeesButton.setEnabled(enabled);
		ligueRenameButton.setEnabled(enabled);
		ligueRenameValidateButton.setEnabled(enabled);
		ligueAdminButton.setEnabled(enabled);
		ligueDeleteButton.setEnabled(enabled);
		ligueRenameField.setEnabled(enabled);
	}

	/**
	 * Recharge la table des employes de la ligue courante.
	 */
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

		employesAddButton.setEnabled(canManageCurrentLigueMembers());
		employesManageButton.setEnabled(!employeRows.isEmpty());

		if (currentEmploye != null && !employeRows.contains(currentEmploye))
			currentEmploye = null;
		if (currentEmploye == null && !employeRows.isEmpty())
			currentEmploye = employeRows.get(0);

		selectCurrentEmployeInTable();
	}

	/**
	 * Recharge la fiche detail de l'employe courant.
	 */
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

	/**
	 * Recharge les champs de la page edition employe.
	 */
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

	/**
	 * Active/desactive tous les champs de la page edition employe.
	 */
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

	/**
	 * Recupere la ligue selectionnee dans la table, en tenant compte du tri.
	 */
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

	/**
	 * Recupere l'employe selectionne dans la table, en tenant compte du tri.
	 */
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

	/**
	 * Aligne la selection visuelle de la table ligues sur currentLigue.
	 */
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

	/**
	 * Aligne la selection visuelle de la table employes sur currentEmploye.
	 */
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

	/**
	 * Affiche une page du CardLayout a partir de son identifiant.
	 *
	 * Terme technique : CardLayout est un conteneur de pages superposees,
	 * une seule page est visible a la fois.
	 */
	private void showPage(String pageName)
	{
		pageLayout.show(pageHost, pageName);
	}

	/**
	 * Cree le style de base d'une carte (panneau principal d'une page).
	 */
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

	/**
	 * Centre visuellement la carte et adapte sa largeur a la fenetre.
	 *
	 * Responsive ici signifie :
	 * - largeur maximale sur grand ecran
	 * - reduction progressive sur petit ecran
	 * - bloc toujours centre horizontalement et verticalement
	 */
	private JPanel wrapCard(JPanel card)
	{
		JPanel wrapper = new JPanel(new GridBagLayout());
		wrapper.setOpaque(false);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.NONE;
		gbc.insets = new Insets(14, 14, 14, 14);
		wrapper.add(card, gbc);

		wrapper.addComponentListener(new ComponentAdapter()
		{
			@Override
			public void componentResized(ComponentEvent e)
			{
				int available = Math.max(460, wrapper.getWidth() - 80);
				int targetWidth = Math.min(980, available);
				Dimension pref = card.getPreferredSize();
				if (pref == null || pref.width != targetWidth)
				{
					int height = pref == null ? card.getHeight() : pref.height;
					card.setPreferredSize(new Dimension(targetWidth, Math.max(420, height)));
					wrapper.revalidate();
				}
			}
		});

		Dimension initial = card.getPreferredSize();
		if (initial != null)
			card.setPreferredSize(new Dimension(Math.min(980, Math.max(760, initial.width)), initial.height));
		return wrapper;
	}

	/**
	 * Cree un conteneur vertical qui ajoute un espacement automatique
	 * entre chaque bloc pour un rendu plus propre.
	 */
	private JPanel createVerticalContainer(int gap)
	{
		return new VerticalStackPanel(gap);
	}

	/**
	 * Construit l'entete standard d'une page (numero, titre, sous-titre).
	 */
	private JPanel createPageHeader(String windowLabel, String title, String subtitle)
	{
		return createTitleContainer(windowLabel, createTitleLabel(title), createMutedLabel(subtitle));
	}

	/**
	 * Assemble les labels de titre de page dans un bloc vertical.
	 */
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

	/** Cree un titre principal en couleur accent. */
	private JLabel createTitleLabel(String text)
	{
		JLabel label = new JLabel(text);
		label.setForeground(COLOR_ACCENT);
		label.setFont(FONT_MAIN.deriveFont(Font.BOLD, 26f));
		return label;
	}

	/** Cree un label secondaire (texte discret). */
	private JLabel createMutedLabel(String text)
	{
		JLabel label = new JLabel(text);
		label.setForeground(COLOR_MUTED);
		label.setFont(FONT_MAIN.deriveFont(Font.PLAIN, 13f));
		return label;
	}

	/** Cree un label de contenu principal. */
	private JLabel createTextLabel(String text)
	{
		JLabel label = new JLabel(text);
		label.setForeground(COLOR_TEXT);
		label.setFont(FONT_MAIN.deriveFont(Font.PLAIN, 14f));
		return label;
	}

	/** Cree une ligne label + champ de saisie. */
	private JPanel createLabeledInput(String label, JComponent input)
	{
		JPanel panel = new JPanel(new BorderLayout(0, 5));
		panel.setOpaque(false);
		panel.add(createMutedLabel(label), BorderLayout.NORTH);
		panel.add(input, BorderLayout.CENTER);
		panel.add(Box.createVerticalStrut(4), BorderLayout.SOUTH);
		return panel;
	}

	/** Cree une ligne champ + bouton d'action. */
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

	/** Cree une ligne d'edition complete : titre, champ et bouton valider. */
	private JPanel createLabeledEditRow(String label, JComponent field, JButton actionButton)
	{
		JPanel row = new JPanel(new BorderLayout(0, 5));
		row.setOpaque(false);
		row.add(createMutedLabel(label), BorderLayout.NORTH);
		row.add(createFieldActionRow(field, actionButton, label), BorderLayout.CENTER);
		return row;
	}

	/** Cree un bloc visuel secondaire (encadre sombre). */
	private JPanel createSectionPanel()
	{
		JPanel panel = new JPanel();
		panel.setBackground(COLOR_PANEL);
		panel.setBorder(new CompoundBorder(new LineBorder(COLOR_BORDER), new EmptyBorder(12, 12, 12, 12)));
		return panel;
	}

	/** Cree un champ texte style maquette. */
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

	/** Cree un champ mot de passe style maquette. */
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

	/**
	 * Fabrique de boutons centralisee pour garder un style coherent.
	 */
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

	/** Bouton d'action principal (accent vert). */
	private JButton createPrimaryButton(String text)
	{
		return createButton(text, COLOR_ACCENT, new Color(8, 11, 15), COLOR_ACCENT);
	}

	/** Bouton neutre pour actions standards. */
	private JButton createNeutralButton(String text)
	{
		return createButton(text, new Color(16, 20, 25), COLOR_TEXT, COLOR_BORDER);
	}

	/** Bouton discret (retour/navigation secondaire). */
	private JButton createGhostButton(String text)
	{
		return createButton(text, new Color(15, 18, 22), COLOR_MUTED, COLOR_BORDER);
	}

	/** Bouton de suppression (rouge). */
	private JButton createDangerButton(String text)
	{
		return createButton(text, new Color(34, 12, 16), COLOR_DANGER, COLOR_DANGER);
	}

	/** Bouton d'alerte (jaune) pour action sensible non destructive. */
	private JButton createWarnButton(String text)
	{
		return createButton(text, new Color(34, 28, 12), COLOR_WARN, COLOR_WARN);
	}

	/** Bouton de type lien pour navigation contextuelle. */
	private JButton createLinkButton(String text)
	{
		return createButton(text, new Color(10, 16, 23), COLOR_LINK, COLOR_BORDER);
	}

	/** Cree un bouton menu avec titre + sous-titre en HTML. */
	private JButton createMenuActionButton(String title, String subtitle, Color titleColor)
	{
		JButton button = createNeutralButton(buildMenuButtonHtml(title, subtitle, titleColor));
		button.setHorizontalAlignment(SwingConstants.LEFT);
		button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));
		return button;
	}

	/** Configure un JTable sombre pour les listes ligues/employes. */
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

	/** Encapsule une table dans un scrollpane style maquette. */
	private JScrollPane wrapTable(JTable table)
	{
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBorder(new CompoundBorder(new LineBorder(COLOR_BORDER), new EmptyBorder(0, 0, 0, 0)));
		scrollPane.getViewport().setBackground(COLOR_INPUT);
		return scrollPane;
	}

	/** Cree une zone texte de lecture seule pour afficher les fiches detail. */
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

	/** Encapsule une zone texte dans un scrollpane encadre. */
	private JComponent wrapArea(JTextArea area)
	{
		JScrollPane scrollPane = new JScrollPane(area);
		scrollPane.setBorder(new CompoundBorder(new LineBorder(COLOR_BORDER), new EmptyBorder(0, 0, 0, 0)));
		scrollPane.setPreferredSize(new Dimension(200, 130));
		scrollPane.getViewport().setBackground(COLOR_INPUT);
		return scrollPane;
	}

	/** Place un composant aligne a gauche. */
	private JPanel leftAligned(JComponent component)
	{
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		panel.setOpaque(false);
		panel.add(component);
		return panel;
	}

	/** Cree une ligne separatrice visuelle. */
	private JComponent createDivider()
	{
		JPanel divider = new JPanel();
		divider.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
		divider.setPreferredSize(new Dimension(10, 1));
		divider.setBackground(COLOR_BORDER);
		return divider;
	}

	/** Formate le nom court d'un employe : Nom Prenom. */
	private String formatEmployeShort(Employe employe)
	{
		return safeValue(employe.getNom()) + " " + safeValue(employe.getPrenom());
	}

	/** Construit le libelle de role affiche dans la fiche employe. */
	private String buildRoleLabel(Employe employe)
	{
		if (employe.estRoot())
			return "super-utilisateur";
		if (currentLigue != null && employe.estAdmin(currentLigue))
			return "Admin " + currentLigue.getNom();
		return "Utilisateur standard";
	}

	/** Formate une date, avec tiret quand la valeur est absente. */
	private String formatDate(LocalDate date)
	{
		return date == null ? "-" : date.toString();
	}

	/** Retourne un texte sur, jamais vide (fallback sur -). */
	private String safeValue(String value)
	{
		return value == null || value.trim().isEmpty() ? "-" : value;
	}

	/** Convertit une couleur Java en code hexadecimal CSS (#rrggbb). */
	private String toHex(Color color)
	{
		return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
	}

	/** Affiche une information utilisateur. */
	private void showInfo(String message)
	{
		JOptionPane.showMessageDialog(this, message, "Information", JOptionPane.INFORMATION_MESSAGE);
	}

	/** Affiche une erreur metier/technique. */
	private void showError(String title, Exception exception)
	{
		JOptionPane.showMessageDialog(this, exception.getMessage(), title, JOptionPane.ERROR_MESSAGE);
	}

	/** Lance la fenetre sur le thread Swing. */
	public static void launch(GestionPersonnel gestionPersonnel)
	{
		SwingUtilities.invokeLater(() -> new PersonnelFrame(gestionPersonnel).setVisible(true));
	}

	/** Couple valeur metier + libelle lisible pour les choix d'admin. */
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

	/** Objet temporaire pour transporter les donnees du formulaire d'ajout employe. */
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

	/**
	 * Conteneur vertical avec espacement automatique entre blocs.
	 */
	private static final class VerticalStackPanel extends JPanel
	{
		private final int gap;

		private VerticalStackPanel(int gap)
		{
			super();
			this.gap = gap;
			setOpaque(false);
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		}

		@Override
		public Component add(Component comp)
		{
			if (getComponentCount() > 0)
				super.add(Box.createVerticalStrut(gap));
			return super.add(comp);
		}
	}

	/** TableModel non editable pour garder un tableau purement consultatif. */
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
