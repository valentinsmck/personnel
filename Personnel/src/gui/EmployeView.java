package gui;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import personnel.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class EmployeView
{
    private Stage stage;
    private GestionPersonnel gestionPersonnel;
    private Ligue ligue;
    private Runnable retour;

    public EmployeView(Stage stage, GestionPersonnel gestionPersonnel, Ligue ligue, Runnable retour)
    {
        this.stage = stage;
        this.gestionPersonnel = gestionPersonnel;
        this.ligue = ligue;
        this.retour = retour;
    }

    public Scene creerScene()
    {
        return new Scene(creerConteneur(), 800, 600);
    }


    private VBox creerConteneur()
    {
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(
                creerLabel("Gerer les employes de " + ligue.getNom()),
                creerListeEmployes(),
                creerFormulaireAjout(),
                creerBouton("Retour", () -> retour.run())
        );
        return vbox;
    }

    private VBox creerFormulaireAjout()
    {
        TextField champNom = creerChamp("Nom");
        TextField champPrenom = creerChamp("Prenom");
        TextField champMail = creerChamp("Mail");
        TextField champPassword = creerChamp("Password");
        TextField champDateArrivee = creerChamp("Date arrivee (yyyy-mm-dd)");
        TextField champDateDepart = creerChamp("Date depart (yyyy-mm-dd)");

        VBox vbox = new VBox(5);
        vbox.getChildren().addAll(
                creerLabel("Ajouter un employe :"),
                creerLigne("Nom :", champNom),
                creerLigne("Prenom :", champPrenom),
                creerLigne("Mail :", champMail),
                creerLigne("Password :", champPassword),
                creerLigne("Date arrivee :", champDateArrivee),
                creerLigne("Date depart :", champDateDepart),
                creerBouton("Ajouter", () -> ajouterEmploye(
                        champNom.getText(),
                        champPrenom.getText(),
                        champMail.getText(),
                        champPassword.getText(),
                        champDateArrivee.getText(),
                        champDateDepart.getText()
                ))
        );
        return vbox;
    }


    private ListView<Employe> creerListeEmployes()
    {
        ListView<Employe> liste = new ListView<>();
        liste.getItems().addAll(ligue.getEmployes());
        liste.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2)
                selectionnerEmploye(liste.getSelectionModel().getSelectedItem());
        });
        return liste;
    }

    private HBox creerLigne(String labelTexte, TextField champ)
    {
        HBox hbox = new HBox(10);
        hbox.getChildren().addAll(creerLabel(labelTexte), champ);
        return hbox;
    }

    private Button creerBouton(String texte, Runnable action)
    {
        Button bouton = new Button(texte);
        bouton.setOnAction(e -> action.run());
        return bouton;
    }

    private Label creerLabel(String texte)
    {
        return new Label(texte);
    }

    private TextField creerChamp(String prompt)
    {
        TextField champ = new TextField();
        champ.setPromptText(prompt);
        return champ;
    }


    private void ajouterEmploye(String nom, String prenom, String mail, String password, String strDateArrivee, String strDateDepart)
    {
        try
        {
            LocalDate dateArrivee = strDateArrivee.isEmpty() ? null : LocalDate.parse(strDateArrivee);
            LocalDate dateDepart = strDateDepart.isEmpty() ? null : LocalDate.parse(strDateDepart);
            ligue.addEmploye(nom, prenom, mail, password, dateArrivee, dateDepart);
            stage.setScene(creerScene());
        }
        catch (DateTimeParseException e) { System.out.println("Format de date invalide (yyyy-mm-dd)"); }
        catch (DateInvalide e) { System.out.println(e.getMessage()); }
        catch (SauvegardeImpossible e) { System.out.println(e.getMessage()); }
    }

    private void selectionnerEmploye(Employe employe)
    {
        if (employe != null)
            stage.setScene(new EditerEmployeView(stage, gestionPersonnel, ligue, employe, () -> stage.setScene(creerScene())).creerScene());
    }
}