package gui;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import personnel.*;

public class EditerEmployeView
{
    private Stage stage;
    private GestionPersonnel gestionPersonnel;
    private Ligue ligue;
    private Employe employe;
    private Runnable retour;

    public EditerEmployeView(Stage stage, GestionPersonnel gestionPersonnel, Ligue ligue, Employe employe, Runnable retour)
    {
        this.stage = stage;
        this.gestionPersonnel = gestionPersonnel;
        this.ligue = ligue;
        this.employe = employe;
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
                creerLabel("Editer : " + employe.getNom()),
                creerLigneNom(),
                creerLignePrenom(),
                creerLigneMail(),
                creerLignePassword(),
                creerBoutonAdmin(),
                creerBouton("Supprimer", () -> supprimerEmploye()),
                creerBouton("Retour", () -> retour.run())
        );
        return vbox;
    }

    private HBox creerLigneNom()
    {
        TextField champ = new TextField(employe.getNom());
        HBox hbox = new HBox(10);
        hbox.getChildren().addAll(creerLabel("Nom :"), champ, creerBouton("Changer", () -> changerNom(champ.getText())));
        return hbox;
    }

    private HBox creerLignePrenom()
    {
        TextField champ = new TextField(employe.getPrenom());
        HBox hbox = new HBox(10);
        hbox.getChildren().addAll(creerLabel("Prenom :"), champ, creerBouton("Changer", () -> changerPrenom(champ.getText())));
        return hbox;
    }

    private HBox creerLigneMail()
    {
        TextField champ = new TextField(employe.getMail());
        HBox hbox = new HBox(10);
        hbox.getChildren().addAll(creerLabel("Mail :"), champ, creerBouton("Changer", () -> changerMail(champ.getText())));
        return hbox;
    }

    private HBox creerLignePassword()
    {
        TextField champ = new TextField();
        HBox hbox = new HBox(10);
        hbox.getChildren().addAll(creerLabel("Password :"), champ, creerBouton("Changer", () -> changerPassword(champ.getText())));
        return hbox;
    }

    private Button creerBoutonAdmin()
    {
        String texte = employe.estAdmin(ligue) ? "Retirer admin" : "Nommer administrateur";
        return creerBouton(texte, () -> changerAdmin());
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

    private void changerNom(String nom)
    {
        try { employe.setNom(nom); stage.setScene(creerScene()); }
        catch (SauvegardeImpossible e) { System.out.println(e.getMessage()); }
    }

    private void changerPrenom(String prenom)
    {
        try { employe.setPrenom(prenom); stage.setScene(creerScene()); }
        catch (SauvegardeImpossible e) { System.out.println(e.getMessage()); }
    }

    private void changerMail(String mail)
    {
        try { employe.setMail(mail); stage.setScene(creerScene()); }
        catch (SauvegardeImpossible e) { System.out.println(e.getMessage()); }
    }

    private void changerPassword(String password)
    {
        try { employe.setPassword(password); stage.setScene(creerScene()); }
        catch (SauvegardeImpossible e) { System.out.println(e.getMessage()); }
    }

    private void changerAdmin()
    {
        try
        {
            if (employe.estAdmin(ligue))
                ligue.setAdministrateur(gestionPersonnel.getRoot());
            else
                ligue.setAdministrateur(employe);
            stage.setScene(creerScene());
        }
        catch (SauvegardeImpossible e) { System.out.println(e.getMessage()); }
    }

    private void supprimerEmploye()
    {
        try { employe.remove(); retour.run(); }
        catch (SauvegardeImpossible e) { System.out.println(e.getMessage()); }
    }
}