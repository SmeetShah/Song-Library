/**
 * @authors Smeet Shah & Akash Patel
 */

package view;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


import app.Song;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class Controller{

    @FXML
    private Button addSong;

    @FXML
    private Button editSong;

    @FXML
    private Button deleteSong;
    
    @FXML
    private Button confirmAddButton;

    @FXML
    private Button cancelAddButton;

    @FXML
    private Button confirmEditButton;

    @FXML
    private Button cancelEditButton;

    @FXML
    private TextField songNameTextField;

    @FXML
    private TextField artistNameTextField;

    @FXML
    private TextField albumNameTextField;

    @FXML
    private TextField albumYearTextField;
    
    @FXML
    private ListView<String> songListView = new ListView<String>();
    
    private ObservableList<String> songObsList = FXCollections.observableArrayList();
    
    @FXML
    public static ArrayList<Song> songArrList = new ArrayList<Song>();
    
    public void start(Stage stage) {
    	songListView.setItems(songObsList);
    	songListView.getSelectionModel().select(0); 
    }
    
    @FXML
	public void initialize() {

		if(checkFileStatus()) {	
			songListView.setItems(songObsList);
			loadSongLibraryToProgram();	
		}	

	}
    
    @FXML
    void showDetailsOfSelectedItem(MouseEvent event) {
    	if(songListView.getSelectionModel().isEmpty())return;
    	int indexToShow = songListView.getSelectionModel().getSelectedIndex();
    	editableTextFields(false);
    	showSongDetails(indexToShow);
    }
    
    @FXML
    public void sceneHandlerForAddEdit(ActionEvent event) {
    	Button temp = (Button)event.getSource();
    	
    	if(temp == addSong) {
    		setPromptText();
    		editableTextFields(true);
    		visibilityOfButtonsForAdd(true);
    	}else if (temp == editSong) {
    		if(songListView.getSelectionModel().isEmpty())return;
    		int indexToShow = songListView.getSelectionModel().getSelectedIndex();
        	showSongDetails(indexToShow);
    		editableTextFields(true);
    		visibilityOfButtonsForEdit(true);
    	}
    	
    }
    
    @FXML 
    void deleteSongFromList(ActionEvent event) {
    	
    	if(songArrList.size() <= 0)return;
    	
    	int indexToShow = songListView.getSelectionModel().getSelectedIndex();
    	showSongDetails(indexToShow);
    	//Song songToDelete = songArrList.get(indexToShow);
    	
    	Alert a1 = new Alert(AlertType.CONFIRMATION, "Are you sure you want DELETE the song?",ButtonType.YES,ButtonType.NO);
		a1.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		a1.showAndWait();
		if(a1.getResult() == ButtonType.NO)return;
		songListView.getItems().remove(indexToShow);
		songArrList.remove(indexToShow);
		if(songArrList.size() > indexToShow+1) {
			showSongDetails(indexToShow);
			songListView.getSelectionModel().select(indexToShow);
		}
		if(songArrList.size() == 0) {
			clearAllTextFields();
			return;
		}
		showSongDetails(songArrList.size()-1);
		songListView.getSelectionModel().select(songArrList.size()-1);
    	
    }
    
    @FXML
    void editSongInList(ActionEvent event) {
    	Button temp = (Button)event.getSource();
    	
    	int indexToShow = songListView.getSelectionModel().getSelectedIndex();
    	Song songInList = songArrList.get(indexToShow);
    	Song songToEdit = createObject();
    	
    	if(temp == confirmEditButton) {
    		if(changesToTextFields(songInList)) {
    			Alert a1 = new Alert(AlertType.CONFIRMATION, "Are you sure you want to make the following changes?",ButtonType.YES,ButtonType.NO);
        		a1.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
    			a1.showAndWait();
    			if(a1.getResult() == ButtonType.NO) return;
    			songListView.getItems().remove(indexToShow);
    			songArrList.remove(indexToShow);
    			addToArrList(songToEdit);
    			indexToShow = songListView.getItems().indexOf(songToEdit.toString());
    		}
    	}else if(temp == cancelEditButton) {
    		if(changesToTextFields(songInList)){
    			Alert a1 = new Alert(AlertType.CONFIRMATION, "Are you sure you don't want to save the following changes?",ButtonType.YES,ButtonType.NO);
        		a1.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
    			a1.showAndWait();
    			if(a1.getResult() == ButtonType.NO) return;
    		}
    	
    	}
    
    	showSongDetails(indexToShow);
		editableTextFields(false);
		visibilityOfButtonsForEdit(false);
    }
    
    @FXML
    void addSongToList(ActionEvent event) {
    	Button temp = (Button)event.getSource();
    	
    	if(temp == confirmAddButton) {
    		Song songToAdd = createObject();
    		if(songToAdd == null) return;
    		addToArrList(songToAdd);
    	}else if( temp == cancelAddButton) {
    		if(songListView.getSelectionModel().isEmpty()) {
    			songListView.getSelectionModel().select(0);
        		editableTextFields(false);
    		}else {
    			songListView.getSelectionModel().select(0);
    			editableTextFields(false);
    			showSongDetails(0);
    		}
    	}
    	visibilityOfButtonsForAdd(false);
    }
    
    public void addToArrList(Song songToAdd) {
    	if(songArrList.size() == 0) {
    		songArrList.add(songToAdd);
    		songListView.getItems().add(0, songToAdd.toString());
    		songListView.getSelectionModel().select(0);
    		editableTextFields(false);
			showSongDetails(0);
			return;
    	}
    	for(int i = 0; i <  songArrList.size(); i++ ) {
    		
    		if(songArrList.get(i).getSongName().equalsIgnoreCase(songToAdd.getSongName())) {
    			
    			if(songArrList.get(i).getArtistName().equalsIgnoreCase(songToAdd.getArtistName())) {
    				Alert a1 = new Alert(AlertType.NONE,  "This song already exists.",ButtonType.CLOSE);
    				a1.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
    				a1.showAndWait();
    				songListView.getSelectionModel().select(0);
    				editableTextFields(false);
    				showSongDetails(0);
    				return;
    			}
    
    		}
    	}
    	songArrList.add(songToAdd);
    	Collections.sort(songArrList,Song.songNameComp);
		assistAddToArrList(songToAdd);
		return;
    }
    
    public void assistAddToArrList(Song songToAdd) {
    	for(int index = 0; index < songArrList.size(); index++) {
    		if(songArrList.get(index).getSongName().equalsIgnoreCase(songToAdd.getSongName()) && songArrList.get(index).getArtistName().equalsIgnoreCase(songToAdd.getArtistName()) ) {
    			songListView.getItems().add(index, songToAdd.toString());
    			songListView.getSelectionModel().select(index);
    			editableTextFields(false);
    			showSongDetails(index);
    			return;
    		}
    	}
    }
    
    public Song createObject() {
    	if(!checkForConstraints()) return null;
    	
    	if(!albumYearTextField.getText().trim().isEmpty()) {
    		if(!isInteger(albumYearTextField.getText().trim())) {
    			Alert a1 = new Alert(AlertType.WARNING,  "Please enter appropriate year.",ButtonType.OK);
    			a1.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
    			a1.showAndWait();
    			setTextAfterError();
    			return null;
    		}else if(Integer.parseInt(albumYearTextField.getText().trim())<=0) {
    			Alert a1 = new Alert(AlertType.WARNING,  "Please enter appropriate year.",ButtonType.OK);
    			a1.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
    			a1.showAndWait();
    			setTextAfterError();
    			return null;
    		}
    	}
    	
    	Song temp = new Song(songNameTextField.getText().trim(),albumNameTextField.getText().trim(),
    						artistNameTextField.getText().trim(),albumYearTextField.getText().trim());
    	return temp;
    }
    
    
    
    public boolean isInteger( String input ) { 
        try {
            Integer.parseInt( input );
            return true; 
        }
        catch( Exception e ) { 
            return false; 
        }
    } 
    
    public boolean checkForConstraints() {
    	if(songNameTextField.getText().trim().isEmpty() || artistNameTextField.getText().trim().isEmpty()) {
			Alert a1 = new Alert(AlertType.WARNING,  "Missing Name or Artist of the Song.",ButtonType.CLOSE);
			a1.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			a1.showAndWait();
			setTextAfterError();
			return false;
		}
    	return true;
    }

    public boolean changesToTextFields(Song songToEdit) {
    	if(!songNameTextField.getText().trim().equalsIgnoreCase(songToEdit.getSongName())||
    			!albumNameTextField.getText().trim().equalsIgnoreCase(songToEdit.getAlbumName())||
    			!artistNameTextField.getText().trim().equalsIgnoreCase(songToEdit.getArtistName())||
    			!albumYearTextField.getText().trim().equalsIgnoreCase(songToEdit.getAlbumYear())) {
    		return true;
    	}
    	return false;
    }    
    
    public void editableTextFields(boolean command) {
    	if(command) {
    		songNameTextField.setEditable(true);
    		artistNameTextField.setEditable(true);
    		albumNameTextField.setEditable(true);
    		albumYearTextField.setEditable(true);
    	}else{
    		songNameTextField.setEditable(false);
    		artistNameTextField.setEditable(false);
    		albumNameTextField.setEditable(false);
    		albumYearTextField.setEditable(false);
    	}
		
    }
    
    public void showSongDetails(int num) {
    	songNameTextField.setText(songArrList.get(num).getSongName());
    	artistNameTextField.setText(songArrList.get(num).getArtistName());
    	albumNameTextField.setText(songArrList.get(num).getAlbumName());
    	albumYearTextField.setText(songArrList.get(num).getAlbumYear());
    }
    
    public void setTextAfterError() {
    	songNameTextField.setText(songNameTextField.getText());
    	artistNameTextField.setText(artistNameTextField.getText());
    	albumNameTextField.setText(albumNameTextField.getText());
    	albumYearTextField.setText(albumYearTextField.getText());
    }
    
    public void setPromptText() {
    	clearAllTextFields();
	    songNameTextField.setPromptText("Enter the Song name.");
		artistNameTextField.setPromptText("Enter the Artist name.");
		albumNameTextField.setPromptText("Enter the Album name.(Optional)");
		albumYearTextField.setPromptText("Enter the Album year.(Optional)");
    }
    
    public void clearAllTextFields() {
    	songNameTextField.clear();
    	artistNameTextField.clear();
    	albumNameTextField.clear();
    	albumYearTextField.clear();
    }
    
    public void visibilityOfButtonsForAdd(boolean command) {
    	if(command) {
    		confirmAddButton.setVisible(true);
    		cancelAddButton.setVisible(true);
    	}else{
    		confirmAddButton.setVisible(false);
    		cancelAddButton.setVisible(false);
    	}
    }
    
    public void visibilityOfButtonsForEdit(boolean command) {
	    if(command) {
			confirmEditButton.setVisible(true);
			cancelEditButton.setVisible(true);
		}else{
			confirmEditButton.setVisible(false);
			cancelEditButton.setVisible(false);
		}
    }
    
    public boolean checkFileStatus() {
    	File f = new File("songDBMS.txt");
		boolean exists = f.exists();
		return exists;
    }
    
    public static void saveSongLibraryToFile() {
    	try {
    		FileWriter user = new FileWriter("songDBMS.txt");
    		for(int i = 0; i < songArrList.size(); i++) {
    			user.write(songArrList.get(i).getSongName() + ":" + songArrList.get(i).getAlbumName( ) + ":" + songArrList.get(i).getArtistName() + ":" + songArrList.get(i).getAlbumYear() +"\n");
    			
    		}
    		user.close();
    	}catch (IOException e) {
    		e.printStackTrace();
    	}
    }
    
    public void loadSongLibraryToProgram() {
    	try {
			String line;

			FileReader file = new FileReader("songDBMS.txt");
			BufferedReader bufferedReader = new BufferedReader(file);

			while ((line = bufferedReader.readLine()) != null ) {
				String[] temp = line.split(":",5);
 
				if(temp.length == 2) {
					Song tempSong = new Song(temp[0],"" ,temp[2] , "");
					songArrList.add(tempSong);
					songListView.getItems().add(tempSong.toString());
				}

				if(temp.length >= 3) {
					Song tempSong = new Song(temp[0], temp[1], temp[2], temp[3]);
					songArrList.add(tempSong);
					songListView.getItems().add(tempSong.toString());
				}	
			}
			bufferedReader.close();
			file.close();
			if(!songArrList.isEmpty()) {
				songListView.getSelectionModel().select(0);
				showSongDetails(0);
			}

		}
		catch(IOException i) {
			i.printStackTrace();
		}
    }
    
}

