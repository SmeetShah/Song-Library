/**
 * @authors Smeet Shah & Akash Patel
 */

package app;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import view.Controller;


public class SongLib extends Application{

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	@Override
	public void start(Stage arg0) throws Exception {
		// TODO Auto-generated method stub
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/app.fxml")); 
		
		AnchorPane root = (AnchorPane)loader.load();
		Controller listController = loader.getController();
		listController.start(arg0); 
		
		
		Scene scene = new Scene(root);
		arg0.setScene(scene);
		arg0.setTitle("Song Library");
		arg0.setResizable(false);
		arg0.show(); 
		
		arg0.setOnCloseRequest(new EventHandler<WindowEvent>() {
		    public void handle(WindowEvent e) {
		    	Controller.saveSongLibraryToFile();
		    	Platform.exit();
		    	System.exit(0);
		    }
		  });
		
		
	}
	
	
}
