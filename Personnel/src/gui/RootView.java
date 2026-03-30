package gui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import personnel.Employe;
import personnel.GestionPersonnel;
import personnel.SauvegardeImpossible;

public class RootView
{
    private Stage stage;
    private GestionPersonnel gestionPersonnel;
    private Employe root;

    public RootView(Stage stage, GestionPersonnel gestionPersonnel, Runnable retour)
    {
        this.stage = stage;
        this.gestionPersonnel = gestionPersonnel;
        this.root = gestionPersonnel.getRoot();
        this.retour = retour;
    }

    private Runnable retour;


    public Scene creerScene()
    {
        return new Scene(creerConteneur(), 800, 600);
    }

    private VBox creerConteneur()
    {
        VBox vbox = new VBox(10);
        vbox.getChildren().add(creerLabelTitre());
        vbox.getChildren().add(creerLabelInfos());
        vbox.getChildren().add(creerLigneNom());
        vbox.getChildren().add(creerLignePrenom());
        vbox.getChildren().add(creerLigneMail());
        vbox.getChildren().add(creerLignePassword());
        vbox.getChildren().add(creerBoutonRetour());
        return vbox;
    }


    private Label creerLabelTitre()
    {
        return new Label("Gerer le compte root");
    }

    private Label creerLabelInfos()
    {
        return new Label("root (super-utilisateur) - mail : " + root.getMail());
    }

    private HBox creerLigneNom()
    {
        HBox hbox = new HBox(10);
        TextField champNom = new TextField(root.getNom());
        Button bouton = new Button("Changer le nom");
        bouton.setOnAction(e -> changerNom(champNom.getText()));
        hbox.getChildren().addAll(new Label("Nom :"), champNom, bouton);
        return hbox;
    }

    private HBox creerLignePrenom()
    {
        HBox hbox = new HBox(10);
        TextField champPrenom = new TextField(root.getPrenom());
        Button bouton = new Button("Changer le prénom");
        bouton.setOnAction(e -> changerPrenom(champPrenom.getText()));
        hbox.getChildren().addAll(new Label("Prénom :"), champPrenom, bouton);
        return hbox;
    }

    private HBox creerLigneMail()
    {
        HBox hbox = new HBox(10);
        TextField champMail = new TextField(root.getMail());
        Button bouton = new Button("Changer le mail");
        bouton.setOnAction(e -> changerMail(champMail.getText()));
        hbox.getChildren().addAll(new Label("Mail :"), champMail, bouton);
        return hbox;
    }

    private HBox creerLignePassword()
    {
        HBox hbox = new HBox(10);
        TextField champPassword = new TextField();
        Button bouton = new Button("Changer le password");
        bouton.setOnAction(e -> changerPassword(champPassword.getText()));
        hbox.getChildren().addAll(new Label("Password :"), champPassword, bouton);
        return hbox;
    }

    private Button creerBoutonRetour()
    {
        Button bouton = new Button("Retour");
        bouton.setOnAction(e -> retour());
        return bouton;
    }


    private void changerNom(String nom)
    {
        try
        {
            root.setNom(nom);
            System.out.println("Nom changé !");
        }
        catch (SauvegardeImpossible e)
        {
            System.out.println(e.getMessage());
        }
    }

    private void changerPrenom(String prenom)
    {
        try
        {
            root.setPrenom(prenom);
            System.out.println("Prénom changé !");
        }
        catch (SauvegardeImpossible e)
        {
            System.out.println(e.getMessage());
        }
    }

    private void changerMail(String mail)
    {
        try
        {
            root.setMail(mail);
            System.out.println("Mail changé !");
        }
        catch (SauvegardeImpossible e)
        {
            System.out.println(e.getMessage());
        }
    }

    private void changerPassword(String password)
    {
        try
        {
            root.setPassword(password);
            System.out.println("Password changé !");
        }
        catch (SauvegardeImpossible e)
        {
            System.out.println(e.getMessage());
        }
    }

    private void retour()
    {
        App app = new App();
        app.start(stage);
    }
}