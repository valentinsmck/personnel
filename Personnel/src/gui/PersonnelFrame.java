package gui;

import personnel.DateInvalide;
import personnel.Employe;
import personnel.GestionPersonnel;
import personnel.Ligue;
import personnel.SauvegardeImpossible;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Objects;

public class PersonnelFrame extends JFrame
{
	private final GestionPersonnel gestionPersonnel;

	private final DefaultListModel<Ligue> ligueModel = new DefaultListModel<>();
	private final JList<Ligue> ligueList = new JList<>(ligueModel);
	private final DefaultListModel<Employe> employeModel = new DefaultListModel<>();
	private final JList<Employe> employeList = new JList<>(employeModel);
	private final JLabel adminValueLabel = new JLabel("-");

	private final JTextField rootNomField = new JTextField();
	private final JTextField rootPrenomField = new JTextField();
	private final JTextField rootMailField = new JTextField();
	private final JPasswordField rootPasswordField = new JPasswordField();
	private final JTextField rootDateArriveeField = new JTextField();
	private final JTextField rootDateDepartField = new JTextField();

	private JButton addLigueButton;
	private JButton renameLigueButton;
	private JButton deleteLigueButton;
	private JButton refreshLigueButton;

	private JButton addEmployeButton;
	private JButton editEmployeButton;
	private JButton deleteEmployeButton;
	private JButton setAdminButton;
	private JButton refreshEmployeButton;

	private JButton saveRootButton;
	private JButton reloadRootButton;

	public PersonnelFrame(GestionPersonnel gestionPersonnel)
	{
		super("Personnel - Interface Graphique");
		this.gestionPersonnel = gestionPersonnel;
		configureFrame();
		setContentPane(buildMainContainer());
		bindEvents();
		refreshAllData();
	}

	private void configureFrame()
	{
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setSize(1180, 720);
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
		JPanel root = new JPanel(new BorderLayout(12, 12));
		JSplitPane contentSplit = new JSplitPane(
				JSplitPane.HORIZONTAL_SPLIT,
				buildLigueContainer(),
				buildEmployeContainer()
		);
		contentSplit.setResizeWeight(0.35);
		root.add(contentSplit, BorderLayout.CENTER);
		root.add(buildRootContainer(), BorderLayout.SOUTH);
		return root;
	}

	private JPanel buildLigueContainer()
	{
		JPanel panel = new JPanel(new BorderLayout(8, 8));
		panel.add(new JLabel("Ligues"), BorderLayout.NORTH);

		ligueList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		panel.add(new JScrollPane(ligueList), BorderLayout.CENTER);

		JPanel actions = new JPanel(new GridLayout(2, 2, 6, 6));
		addLigueButton = new JButton("Ajouter");
		renameLigueButton = new JButton("Renommer");
		deleteLigueButton = new JButton("Supprimer");
		refreshLigueButton = new JButton("Rafraichir");
		actions.add(addLigueButton);
		actions.add(renameLigueButton);
		actions.add(deleteLigueButton);
		actions.add(refreshLigueButton);
		panel.add(actions, BorderLayout.SOUTH);
		return panel;
	}

	private JPanel buildEmployeContainer()
	{
		JPanel panel = new JPanel(new BorderLayout(8, 8));

		JPanel top = new JPanel(new BorderLayout(8, 8));
		top.add(new JLabel("Employes de la ligue"), BorderLayout.WEST);
		top.add(new JLabel("Administrateur:"), BorderLayout.CENTER);
		top.add(adminValueLabel, BorderLayout.EAST);
		panel.add(top, BorderLayout.NORTH);

		employeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		panel.add(new JScrollPane(employeList), BorderLayout.CENTER);

		JPanel actions = new JPanel(new GridLayout(1, 5, 6, 6));
		addEmployeButton = new JButton("Ajouter");
		editEmployeButton = new JButton("Modifier");
		deleteEmployeButton = new JButton("Supprimer");
		setAdminButton = new JButton("Definir admin");
		refreshEmployeButton = new JButton("Rafraichir");
		actions.add(addEmployeButton);
		actions.add(editEmployeButton);
		actions.add(deleteEmployeButton);
		actions.add(setAdminButton);
		actions.add(refreshEmployeButton);
		panel.add(actions, BorderLayout.SOUTH);
		return panel;
	}

	private JPanel buildRootContainer()
	{
		JPanel panel = new JPanel(new BorderLayout(8, 8));
		panel.add(new JLabel("Gestion du root"), BorderLayout.NORTH);

		JPanel form = new JPanel(new GridLayout(2, 6, 6, 6));
		form.add(new JLabel("Nom"));
		form.add(new JLabel("Prenom"));
		form.add(new JLabel("Mail"));
		form.add(new JLabel("Password"));
		form.add(new JLabel("Date arrivee (yyyy-mm-dd)"));
		form.add(new JLabel("Date depart (yyyy-mm-dd)"));
		form.add(rootNomField);
		form.add(rootPrenomField);
		form.add(rootMailField);
		form.add(rootPasswordField);
		form.add(rootDateArriveeField);
		form.add(rootDateDepartField);
		panel.add(form, BorderLayout.CENTER);

		JPanel actions = new JPanel(new GridLayout(1, 2, 6, 6));
		saveRootButton = new JButton("Enregistrer root");
		reloadRootButton = new JButton("Recharger");
		actions.add(saveRootButton);
		actions.add(reloadRootButton);
		panel.add(actions, BorderLayout.SOUTH);
		return panel;
	}

	private void bindEvents()
	{
		addLigueButton.addActionListener(e -> onAddLigue());
		renameLigueButton.addActionListener(e -> onRenameLigue());
		deleteLigueButton.addActionListener(e -> onDeleteLigue());
		refreshLigueButton.addActionListener(e -> refreshLigues());

		ligueList.addListSelectionListener(e ->
		{
			if (!e.getValueIsAdjusting())
				onLigueSelectionChanged();
		});

		addEmployeButton.addActionListener(e -> onAddEmploye());
		editEmployeButton.addActionListener(e -> onEditEmploye());
		deleteEmployeButton.addActionListener(e -> onDeleteEmploye());
		setAdminButton.addActionListener(e -> onSetAdministrateur());
		refreshEmployeButton.addActionListener(e -> refreshEmployesForSelectedLigue());

		saveRootButton.addActionListener(e -> onSaveRoot());
		reloadRootButton.addActionListener(e -> refreshRootForm());
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

	private void onAddLigue()
	{
		String nom = JOptionPane.showInputDialog(this, "Nom de la ligue :", "Ajouter une ligue", JOptionPane.PLAIN_MESSAGE);
		if (nom == null || nom.trim().isEmpty())
			return;

		try
		{
			gestionPersonnel.addLigue(nom.trim());
			refreshLigues();
		}
		catch (SauvegardeImpossible e)
		{
			showError("Impossible d'ajouter la ligue", e);
		}
	}

	private void onRenameLigue()
	{
		Ligue ligue = ligueList.getSelectedValue();
		if (ligue == null)
		{
			showInfo("Selectionnez d'abord une ligue.");
			return;
		}

		String nouveauNom = JOptionPane.showInputDialog(this, "Nouveau nom :", ligue.getNom());
		if (nouveauNom == null || nouveauNom.trim().isEmpty())
			return;

		try
		{
			ligue.setNom(nouveauNom.trim());
			refreshLigues();
		}
		catch (SauvegardeImpossible e)
		{
			showError("Impossible de renommer la ligue", e);
		}
	}

	private void onDeleteLigue()
	{
		Ligue ligue = ligueList.getSelectedValue();
		if (ligue == null)
		{
			showInfo("Selectionnez d'abord une ligue.");
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
			ligue.remove();
			refreshLigues();
		}
		catch (SauvegardeImpossible e)
		{
			showError("Impossible de supprimer la ligue", e);
		}
	}

	private void onLigueSelectionChanged()
	{
		refreshEmployesForSelectedLigue();
	}

	private void onAddEmploye()
	{
		Ligue ligue = ligueList.getSelectedValue();
		if (ligue == null)
		{
			showInfo("Selectionnez d'abord une ligue.");
			return;
		}

		EmployeFormData data = askEmployeForm(null);
		if (data == null)
			return;

		try
		{
			ligue.addEmploye(data.nom, data.prenom, data.mail, data.password, data.dateArrivee, data.dateDepart);
			refreshEmployesForSelectedLigue();
		}
		catch (DateInvalide | SauvegardeImpossible e)
		{
			showError("Impossible d'ajouter l'employe", e);
		}
	}

	private void onEditEmploye()
	{
		Employe employe = employeList.getSelectedValue();
		if (employe == null)
		{
			showInfo("Selectionnez d'abord un employe.");
			return;
		}

		EmployeFormData data = askEmployeForm(employe);
		if (data == null)
			return;

		try
		{
			employe.setNom(data.nom);
			employe.setPrenom(data.prenom);
			employe.setMail(data.mail);
			if (!data.password.isEmpty())
				employe.setPassword(data.password);
			applyDates(employe, data.dateArrivee, data.dateDepart);
			refreshEmployesForSelectedLigue();
			refreshRootForm();
		}
		catch (DateInvalide | SauvegardeImpossible e)
		{
			showError("Impossible de modifier l'employe", e);
		}
	}

	private void onDeleteEmploye()
	{
		Employe employe = employeList.getSelectedValue();
		if (employe == null)
		{
			showInfo("Selectionnez d'abord un employe.");
			return;
		}

		int confirm = JOptionPane.showConfirmDialog(
				this,
				"Supprimer l'employe selectionne ?",
				"Confirmation",
				JOptionPane.YES_NO_OPTION
		);
		if (confirm != JOptionPane.YES_OPTION)
			return;

		try
		{
			employe.remove();
			refreshEmployesForSelectedLigue();
			refreshRootForm();
		}
		catch (SauvegardeImpossible e)
		{
			showError("Impossible de supprimer l'employe", e);
		}
	}

	private void onSetAdministrateur()
	{
		Ligue ligue = ligueList.getSelectedValue();
		Employe employe = employeList.getSelectedValue();
		if (ligue == null || employe == null)
		{
			showInfo("Selectionnez une ligue puis un employe.");
			return;
		}

		try
		{
			ligue.setAdministrateur(employe);
			refreshEmployesForSelectedLigue();
		}
		catch (SauvegardeImpossible e)
		{
			showError("Impossible de changer l'administrateur", e);
		}
	}

	private void onSaveRoot()
	{
		Employe root = gestionPersonnel.getRoot();
		if (root == null)
		{
			showInfo("Aucun root charge.");
			return;
		}

		try
		{
			root.setNom(rootNomField.getText().trim());
			root.setPrenom(rootPrenomField.getText().trim());
			root.setMail(rootMailField.getText().trim());
			String newPassword = new String(rootPasswordField.getPassword());
			if (!newPassword.isEmpty())
				root.setPassword(newPassword);

			LocalDate dateArrivee = parseOptionalDate(rootDateArriveeField.getText().trim());
			LocalDate dateDepart = parseOptionalDate(rootDateDepartField.getText().trim());
			applyDates(root, dateArrivee, dateDepart);
			refreshRootForm();
			showInfo("Compte root mis a jour.");
		}
		catch (DateInvalide | SauvegardeImpossible e)
		{
			showError("Impossible de mettre a jour le root", e);
		}
	}

	private void applyDates(Employe employe, LocalDate newDateArrivee, LocalDate newDateDepart) throws DateInvalide, SauvegardeImpossible
	{
		boolean arriveeChanged = !Objects.equals(newDateArrivee, employe.getDateArrivee());
		boolean departChanged = !Objects.equals(newDateDepart, employe.getDateDepart());
		if (!arriveeChanged && !departChanged)
			return;

		if (newDateArrivee != null && newDateDepart != null && newDateDepart.isBefore(newDateArrivee))
			throw new DateInvalide("La date de depart ne peut pas etre avant la date d'arrivee.");

		if (arriveeChanged && departChanged)
			employe.setDateDepart(null);

		if (arriveeChanged)
			employe.setDateArrivee(newDateArrivee);
		if (departChanged)
			employe.setDateDepart(newDateDepart);
	}

	private EmployeFormData askEmployeForm(Employe employe)
	{
		JTextField nomField = new JTextField(employe != null ? employe.getNom() : "");
		JTextField prenomField = new JTextField(employe != null ? employe.getPrenom() : "");
		JTextField mailField = new JTextField(employe != null ? employe.getMail() : "");
		JPasswordField passwordField = new JPasswordField();
		JTextField dateArriveeField = new JTextField(employe != null && employe.getDateArrivee() != null ? employe.getDateArrivee().toString() : "");
		JTextField dateDepartField = new JTextField(employe != null && employe.getDateDepart() != null ? employe.getDateDepart().toString() : "");

		JPanel form = new JPanel(new GridLayout(6, 2, 6, 6));
		form.add(new JLabel("Nom"));
		form.add(nomField);
		form.add(new JLabel("Prenom"));
		form.add(prenomField);
		form.add(new JLabel("Mail"));
		form.add(mailField);
		form.add(new JLabel(employe == null ? "Password" : "Nouveau password (vide = conserver)"));
		form.add(passwordField);
		form.add(new JLabel("Date arrivee (yyyy-mm-dd)"));
		form.add(dateArriveeField);
		form.add(new JLabel("Date depart (yyyy-mm-dd)"));
		form.add(dateDepartField);

		int result = JOptionPane.showConfirmDialog(
				this,
				form,
				employe == null ? "Ajouter un employe" : "Modifier un employe",
				JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE
		);
		if (result != JOptionPane.OK_OPTION)
			return null;

		try
		{
			LocalDate dateArrivee = parseOptionalDate(dateArriveeField.getText().trim());
			LocalDate dateDepart = parseOptionalDate(dateDepartField.getText().trim());
			return new EmployeFormData(
					nomField.getText().trim(),
					prenomField.getText().trim(),
					mailField.getText().trim(),
					new String(passwordField.getPassword()),
					dateArrivee,
					dateDepart
			);
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
		refreshLigues();
		refreshEmployesForSelectedLigue();
		refreshRootForm();
	}

	private void refreshLigues()
	{
		Ligue selected = ligueList.getSelectedValue();
		ligueModel.clear();
		for (Ligue ligue : gestionPersonnel.getLigues())
			ligueModel.addElement(ligue);

		if (selected != null)
			ligueList.setSelectedValue(selected, true);
		if (ligueList.getSelectedIndex() < 0 && !ligueModel.isEmpty())
			ligueList.setSelectedIndex(0);
	}

	private void refreshEmployesForSelectedLigue()
	{
		employeModel.clear();
		Ligue ligue = ligueList.getSelectedValue();
		if (ligue == null)
		{
			adminValueLabel.setText("-");
			return;
		}

		for (Employe employe : new ArrayList<>(ligue.getEmployes()))
			employeModel.addElement(employe);

		Employe admin = ligue.getAdministrateur();
		adminValueLabel.setText(admin != null ? admin.getNom() + " " + admin.getPrenom() : "-");
	}

	private void refreshRootForm()
	{
		Employe root = gestionPersonnel.getRoot();
		if (root == null)
			return;

		rootNomField.setText(root.getNom());
		rootPrenomField.setText(root.getPrenom());
		rootMailField.setText(root.getMail());
		rootPasswordField.setText("");
		rootDateArriveeField.setText(root.getDateArrivee() != null ? root.getDateArrivee().toString() : "");
		rootDateDepartField.setText(root.getDateDepart() != null ? root.getDateDepart().toString() : "");
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

	private static final class EmployeFormData
	{
		private final String nom;
		private final String prenom;
		private final String mail;
		private final String password;
		private final LocalDate dateArrivee;
		private final LocalDate dateDepart;

		private EmployeFormData(String nom, String prenom, String mail, String password, LocalDate dateArrivee, LocalDate dateDepart)
		{
			this.nom = nom;
			this.prenom = prenom;
			this.mail = mail;
			this.password = password;
			this.dateArrivee = dateArrivee;
			this.dateDepart = dateDepart;
		}
	}
}
