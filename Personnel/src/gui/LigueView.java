package gui;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import personnel.GestionPersonnel;
import personnel.Ligue;
import personnel.SauvegardeImpossible;

public class LigueView
{
    private Stage stage;
    private GestionPersonnel gestionPersonnel;
    private Runnable retour;

    public LigueView(Stage stage, GestionPersonnel gestionPersonnel, Runnable retour)
    {
        this.stage = stage;
        this.gestionPersonnel = gestionPersonnel;
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
                creerLabel("Gérer les ligues"),
                creerListeLigues(),
                creerLigneAjout(),
                creerBouton("Retour", () -> retour.run())
        );
        return vbox;
    }

    private HBox creerLigneAjout()
    {
        TextField champ = creerChamp("");
        HBox hbox = new HBox(10);
        hbox.getChildren().addAll(
                creerLabel("Nouvelle ligue :"),
                champ,
                creerBouton("Ajouter", () -> ajouterLigue(champ.getText()))
        );
        return hbox;
    }


    private ListView<Ligue> creerListeLigues()
    {
        ListView<Ligue> liste = new ListView<>();
        liste.getItems().addAll(gestionPersonnel.getLigues());
        liste.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2)
                selectionnerLigue(liste.getSelectionModel().getSelectedItem());
        });
        return liste;
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


    private void ajouterLigue(String nom)
    {
        try
        {
            gestionPersonnel.addLigue(nom);
            stage.setScene(creerScene());
        }
        catch (SauvegardeImpossible e)
        {
            System.out.println(e.getMessage());
        }
    }

    private void selectionnerLigue(Ligue ligue)
    {
        if (ligue != null)
            stage.setScene(new EditerLigueView(stage, gestionPersonnel, ligue, () -> stage.setScene(creerScene())).creerScene());
    }
}