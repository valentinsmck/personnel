package gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import personnel.GestionPersonnel;

public class App extends Application
{
    private Stage stage;
    private GestionPersonnel gestionPersonnel;


    @Override
    public void start(Stage stage)
    {
        this.stage = stage;
        this.gestionPersonnel = GestionPersonnel.getGestionPersonnel();
        stage.setTitle("Gestion du personnel");
        stage.setScene(creerSceneMenuPrincipal());
        stage.show();
    }
    public Scene creerSceneMenuPrincipal()
    {
        return new Scene(creerConteneurMenuPrincipal(), 800, 600);
    }

    private VBox creerConteneurMenuPrincipal()
    {
        VBox vbox = new VBox(10);
        vbox.getChildren().add(creerLabel("Gestion du personnel des ligues"));
        vbox.getChildren().add(creerBoutonRoot());
        vbox.getChildren().add(creerBoutonLigues());
        vbox.getChildren().add(creerBoutonQuitter());
        return vbox;
    }

    private Label creerLabel(String texte)
    {
        return new Label(texte);
    }

    private Button creerBoutonRoot()
    {
        Button bouton = new Button("Gerer le compte root");
        bouton.setOnAction(e -> gererRoot());
        return bouton;
    }

    private Button creerBoutonLigues()
    {
        Button bouton = new Button("Gerer les ligues");
        bouton.setOnAction(e -> gererLigues());
        return bouton;
    }

    private Button creerBoutonQuitter()
    {
        Button bouton = new Button("Quitter");
        bouton.setOnAction(e -> quitter());
        return bouton;
    }

    private void gererRoot()
    {
        RootView rootView = new RootView(stage, gestionPersonnel, () -> retourMenuPrincipal());
        stage.setScene(rootView.creerScene());
    }

    private void retourMenuPrincipal()
    {
        stage.setScene(creerSceneMenuPrincipal());
    }

    private void gererLigues()
    {
        LigueView ligueView = new LigueView(stage, gestionPersonnel, () -> retourMenuPrincipal());
        stage.setScene(ligueView.creerScene());
    }

    private void quitter()
    {
        stage.close();
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}