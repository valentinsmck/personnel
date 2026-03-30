package gui;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import personnel.GestionPersonnel;
import personnel.Ligue;
import personnel.SauvegardeImpossible;

public class EditerLigueView
{
    private Stage stage;
    private GestionPersonnel gestionPersonnel;
    private Ligue ligue;
    private Runnable retour;

    public EditerLigueView(Stage stage, GestionPersonnel gestionPersonnel, Ligue ligue, Runnable retour)
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
                creerLabel("Editer : " + ligue.getNom()),
                creerLigneNom(),
                creerBouton("Gerer les employes", () -> gererEmployes()),
                creerBouton("Supprimer", () -> supprimerLigue()),
                creerBouton("Retour", () -> retour.run())
        );
        return vbox;
    }

    private HBox creerLigneNom()
    {
        TextField champ = creerChamp(ligue.getNom());
        HBox hbox = new HBox(10);
        hbox.getChildren().addAll(
                creerLabel("Nom :"),
                champ,
                creerBouton("Renommer", () -> renommer(champ.getText()))
        );
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

    private TextField creerChamp(String valeur)
    {
        return new TextField(valeur);
    }

    private void renommer(String nom)
    {
        try
        {
            ligue.setNom(nom);
            stage.setScene(creerScene());
        }
        catch (SauvegardeImpossible e)
        {
            System.out.println(e.getMessage());
        }
    }

    private void supprimerLigue()
    {
        try
        {
            ligue.remove();
            retour.run();
        }
        catch (SauvegardeImpossible e)
        {
            System.out.println(e.getMessage());
        }
    }



    private void gererEmployes()
    {
        EmployeView employeView = new EmployeView(stage, gestionPersonnel, ligue, () -> stage.setScene(creerScene()));
        stage.setScene(employeView.creerScene());
    }
}